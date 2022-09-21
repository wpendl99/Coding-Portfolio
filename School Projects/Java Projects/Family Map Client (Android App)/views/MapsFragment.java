package com.wkp23.familymapclient.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.wkp23.familymapclient.DataCache;
import com.wkp23.familymapclient.R;

import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Person;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String LOG_TAG = "MapsFragment";
    public static final String EVENT_KEY = "thisEvent";
    float googleBlue = BitmapDescriptorFactory.HUE_BLUE;
    float googleRed = BitmapDescriptorFactory.HUE_ROSE;
    float googleGreen = BitmapDescriptorFactory.HUE_CYAN;
    GoogleMap map;
    Event thisEvent;
    ArrayList<Polyline> lines = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @Override
        public void onMapReady(GoogleMap map) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_maps, container,false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DataCache.updateSettings();

        Iconify.with(new FontAwesomeModule());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        Intent intent = getActivity().getIntent();
        if(intent.hasExtra(EVENT_KEY)){
            String eventID = intent.getStringExtra(EVENT_KEY);
            thisEvent = DataCache.getEventFromMap(eventID);
        } else {
            thisEvent = DataCache.getSelectedEvent();
        }


        if (thisEvent == null || !DataCache.getActiveEvents().contains(thisEvent)) {
            ImageView imageView = (ImageView) view.findViewById(R.id.eventDetailsIcon);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_pin));
        }

        LinearLayout detailsView = view.findViewById(R.id.eventDetails);
        detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thisEvent != null) {
                    Intent intent = new Intent(getContext(), PersonActivity.class);
                    intent.putExtra(PersonActivity.PERSON_KEY, thisEvent.getPersonID());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        if (getActivity() instanceof MainActivity) {
            inflater.inflate(R.menu.main_menu, menu);

            MenuItem searchMenuItem = menu.findItem(R.id.searchMenuItem);
            searchMenuItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_search)
                    .colorRes(R.color.white)
                    .actionBarSize());

            MenuItem settingsMenuItem = menu.findItem(R.id.settingsMenuItem);
            settingsMenuItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_gear)
                    .colorRes(R.color.white)
                    .actionBarSize());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.searchMenuItem:
                Intent searchIntent = new Intent(getContext(), SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.settingsMenuItem:
                Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private float getEventColor(String type){

        return DataCache.getEventTypeColor(type);

//        switch (type){
//            case "Birth":
//                return googleBlue;
//            case "Marriage":
//                return googleGreen;
//            case "Death":
//                return googleRed;
//        }
//
//        return googleRed;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.map = map;
        List<Event> list = DataCache.getActiveEvents();

        if(thisEvent != null && list.contains(thisEvent)) {
            updateEventDetails(thisEvent);
            updateMapLines(thisEvent);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(thisEvent.getLatitude(), thisEvent.getLongitude()), 2));
        }

        map.getUiSettings().setZoomControlsEnabled(true);

        list.forEach((event) -> {
            LatLng pos = new LatLng(event.getLatitude(), event.getLongitude());

            Float color = getEventColor(event.getEventType());

            Marker marker = map.addMarker(new MarkerOptions().position(pos).title(event.getEventID()).icon(BitmapDescriptorFactory.defaultMarker(color)));
            marker.setTag(event);
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker){
                Event tmp = (Event) marker.getTag();
                updateEventDetails(tmp);
                updateMapLines(tmp);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(tmp.getLatitude(), tmp.getLongitude()), 2));
                return true;
            }
        });
    }

    private void updateEventDetails(Event event){
        View view = getView();
        Person person = DataCache.getPersonFromMap(event.getPersonID());
        DataCache.setSelectedEvent(event);
        thisEvent = event;

        switch (person.getGender()){
            case "m":
                ((ImageView) view.findViewById(R.id.eventDetailsIcon)).setImageDrawable(getResources().getDrawable(R.drawable.ic_male));
                break;
            case "f":
                ((ImageView) view.findViewById(R.id.eventDetailsIcon)).setImageDrawable(getResources().getDrawable(R.drawable.ic_female));
                break;
        }

        ((TextView) view.findViewById(R.id.eventDetailsName)).setText(getString(R.string.personFullName, person.getFirstName(), person.getLastName()));
        ((TextView) view.findViewById(R.id.eventDetailsDescription)).setText(getString(R.string.eventDescription, event.getEventType(), event.getCity(), event.getCountry(), String.valueOf(event.getYear())));
        ((TextView) view.findViewById(R.id.eventDetailsDescription)).setVisibility(View.VISIBLE);
    }

    private void updateMapLines(Event event){
        clearMapLines();

        if(DataCache.isSetting("Life Story Lines")){
            // Draw the Story Lines
            drawMapLine(DataCache.getOrderedPersonEvents(event.getPersonID()), R.color.red, 10.0F);
        }
        if(DataCache.isSetting("Spouse Lines")){
            drawSpouseLine(event);
        }
        if(DataCache.isSetting("Family Tree Lines")){
            drawFamilyLine(event, 10.0F);
        }
    }

    private void drawMapLine(ArrayList<Event> events, int color, float width){
        ArrayList<LatLng> points = new ArrayList<>();
        Log.d(LOG_TAG, events.toString());
        events.forEach((event) -> {
            points.add(new LatLng(event.getLatitude(), event.getLongitude()));
        });

        PolylineOptions options = new PolylineOptions()
                .color(getResources().getColor(color))
                .width(width);

        points.forEach(options::add);

        Polyline line = map.addPolyline(options);
        lines.add(line);
    }

    private void drawSpouseLine(Event event) {
        ArrayList<Event> events = new ArrayList<>();

        events.add(event);

        Person person = DataCache.getPersonFromMap(event.getPersonID());
        Person spouse = DataCache.getPersonFromMap(person.getSpouseID());

        if(spouse != null) {
            events.add(DataCache.getPersonFirstEvent(spouse.getPersonID()));
        }

        while(events.remove(null));

        drawMapLine(events, R.color.yellow, 10.0f);
    }

    private void drawFamilyLine(Event event, float width) {
        ArrayList<Event> events = new ArrayList<>();
        Person person = DataCache.getPersonFromMap(event.getPersonID());

        // Add mother's Birth and draw their Parents Events
        if(person.getMotherID() != null) {
            drawFamilyLineHelper(person.getMotherID(), (width * .66f));
            events.add(DataCache.getPersonFirstEvent(person.getMotherID()));
        }
        // Add Persons Events
        events.add(event);
        // Add father's Birth and draw their Parents Events
        if(person.getFatherID() != null) {
            drawFamilyLineHelper(person.getFatherID(), (width * .66f));
            events.add(DataCache.getPersonFirstEvent(person.getFatherID()));
        }

        while(events.remove(null));

        drawMapLine(events, R.color.blue, width);
    }

    private void drawFamilyLineHelper(String personID, float width) {
        ArrayList<Event> events = new ArrayList<>();
        Person person = DataCache.getPersonFromMap(personID);

        // Add mother's Birth and draw their Parents Events
        if(person.getMotherID() != null) {
            drawFamilyLineHelper(person.getMotherID(), (width * .66f));
            events.add(DataCache.getPersonFirstEvent(person.getMotherID()));
        }
        // Add Persons Events
        events.add(DataCache.getPersonEvent(personID, "birth"));
        // Add father's Birth and draw their Parents Events
        if(person.getFatherID() != null) {
            drawFamilyLineHelper(person.getFatherID(), (width * .66f));
            events.add(DataCache.getPersonFirstEvent(person.getFatherID()));
        }

        while(events.remove(null));

        drawMapLine(events, R.color.blue, width);
    }

    private void clearMapLines(){
        lines.forEach(Polyline::remove);
        lines.clear();
    }
}