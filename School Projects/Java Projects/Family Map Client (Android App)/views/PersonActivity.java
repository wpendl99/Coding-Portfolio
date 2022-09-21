package com.wkp23.familymapclient.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wkp23.familymapclient.DataCache;
import com.wkp23.familymapclient.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    public static String PERSON_KEY = "Person_Key";
    private Person person;
    private ArrayList<Event> lifeEvents;
    private Map<String, Person> family; // = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();
        String personID = intent.getStringExtra(PERSON_KEY);
        person = DataCache.getPersonFromMap(personID);

        // Set the Person Detail Contents
        TextView firstNameTextView = findViewById(R.id.DetailFirstName);
        firstNameTextView.setText(person.getFirstName());

        TextView lastNameTextView = findViewById(R.id.DetailLastName);
        lastNameTextView.setText(person.getLastName());

        TextView genderTextView = findViewById(R.id.DetailGender);
        if(person.getGender().equals("f")){
            genderTextView.setText(getString(R.string.femaleGenderLabel));
        } else if (person.getGender().equals("m")) {
            genderTextView.setText(getString(R.string.maleGenderLabel));
        } else {
            throw new IllegalArgumentException("Unrecognized Gender: " + person.getGender());
        }


        // Set the DropDown Contents
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        lifeEvents = DataCache.getOrderedPersonEvents(person.getPersonID());
        family = DataCache.getPersonFamily(person);

        expandableListView.setAdapter(new ExpandableListAdapter(person, lifeEvents, family));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int LIFE_EVENTS_GROUP_POSITION = 0;
        private static final int FAMILY_GROUP_POSITION = 1;

        private final Person person;
        private final ArrayList<Event> lifeEvents;
        private final ArrayList<Pair<String, Person>> family = new ArrayList<>();


        public ExpandableListAdapter(Person person, ArrayList<Event> lifeEvents, Map<String, Person> family) {
            this.person = person;
            this.lifeEvents = lifeEvents;
            family.forEach((key, value) -> {
                this.family.add(new Pair<>(key, value));
            });
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return lifeEvents.size();
                case FAMILY_GROUP_POSITION:
                    return family.size();
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position");
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return getString(R.string.lifeEventsGroupTitle);
                case FAMILY_GROUP_POSITION:
                    return getString(R.string.familyGroupTitle);
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return lifeEvents.get(childPosition);
                case FAMILY_GROUP_POSITION:
                    return family.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.detail_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.detailTitle);

            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    titleView.setText(R.string.lifeEventsGroupTitle);
                    break;
                case FAMILY_GROUP_POSITION:
                    titleView.setText(R.string.familyGroupTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.detail_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    return itemView;
                case FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.detail_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    return itemView;
            }

            return null;
        }

        private void initializeEventView(View eventItemView, final int childPosition){
            ImageView detailItemIcon = eventItemView.findViewById(R.id.DetailItemIcon);
            detailItemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_pin));

            TextView detailItemTitle = eventItemView.findViewById(R.id.DetailItemTitle);
            Event event = lifeEvents.get(childPosition);
            detailItemTitle.setText(getString(R.string.detailTitleString, event.getEventType(), event.getCity(), event.getCountry(), String.valueOf(event.getYear())));

            TextView detailItemSubtitle = eventItemView.findViewById(R.id.DetailItemSubtitle);
            detailItemSubtitle.setText(getString(R.string.detailNameString, person.getFirstName(), person.getLastName()));

            eventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), EventActivity.class);
                    intent.putExtra(MapsFragment.EVENT_KEY, event.getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializePersonView(View personItemView, final int childPosition){
            ImageView detailItemIcon = personItemView.findViewById(R.id.DetailItemIcon);
            Person relative = family.get(childPosition).second;
            if(relative.getGender().equals("f")) {
                detailItemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_female));
            } else if (relative.getGender().equals("m")){
                detailItemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_male));
            } else {
                throw new IllegalArgumentException("Unrecognized gender: " + relative.getGender());
            }

            TextView detailItemTitle = personItemView.findViewById(R.id.DetailItemTitle);
            detailItemTitle.setText(getString(R.string.detailNameString, relative.getFirstName(), relative.getLastName()));

            TextView detailItemSubtitle = personItemView.findViewById(R.id.DetailItemSubtitle);
            detailItemSubtitle.setText(family.get(childPosition).first);

            personItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), PersonActivity.class);
                    intent.putExtra(PERSON_KEY, relative.getPersonID());
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }


}