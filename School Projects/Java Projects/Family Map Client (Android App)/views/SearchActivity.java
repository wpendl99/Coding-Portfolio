package com.wkp23.familymapclient.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.wkp23.familymapclient.DataCache;
import com.wkp23.familymapclient.R;
import com.wkp23.familymapclient.SettingsItem;

import java.util.ArrayList;

import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private String searchString = null;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.SearchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        adapter = new SearchAdapter(DataCache.getPeopleContaining(searchString), DataCache.getEventsContaining(searchString));
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.SearchSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchString = newText;
                adapter.updateData(searchString);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
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

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

        private ArrayList<Person> people = new ArrayList<>();
        private ArrayList<Event> events = new ArrayList<>();

        public SearchAdapter(ArrayList<Person> people, ArrayList<Event> events) {
            this.people = people;
            this.events = events;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            view = getLayoutInflater().inflate(R.layout.detail_item, parent, false);

            return new SearchViewHolder(view);
        }

        public void updateData(String searchString) {
            people = DataCache.getPeopleContaining(searchString);
            events = DataCache.getEventsContaining(searchString);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < people.size()) {
                holder.bind(people.get(position));
            } else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView icon;
        private final TextView title;
        private final TextView subtitle;

        private Person person;
        private Event event;

        public SearchViewHolder(@NonNull View view) {
            super(view);

            itemView.setOnClickListener(this);

            icon = itemView.findViewById(R.id.DetailItemIcon);
            title = itemView.findViewById(R.id.DetailItemTitle);
            subtitle = itemView.findViewById((R.id.DetailItemSubtitle));
        }

        private void bind(Person person) {
            this.person = person;

            switch (person.getGender()){
                case "m":
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_male));
                    break;
                case "f":
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_female));
                    break;
            }

            title.setText(getString(R.string.personFullName, person.getFirstName(), person.getLastName()));
            subtitle.setText("");
        }

        private void bind(Event event) {
            this.event = event;
            Person tmp = DataCache.getPersonFromMap(event.getPersonID());

            icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_pin));
            title.setText(getString(R.string.detailTitleString, event.getEventType(), event.getCity(), event.getCountry(), String.valueOf(event.getYear())));
            subtitle.setText(getString(R.string.detailNameString, tmp.getFirstName(), tmp.getLastName()));

        }

        @Override
        public void onClick(View view) {
            if(person != null) {
                Intent intent = new Intent(getBaseContext(), PersonActivity.class);
                intent.putExtra(PersonActivity.PERSON_KEY, person.getPersonID());
                startActivity(intent);
            } else if (event != null) {
                Intent intent = new Intent(getBaseContext(), EventActivity.class);
                intent.putExtra(MapsFragment.EVENT_KEY, event.getEventID());
                startActivity(intent);
            } else {
                throw new IllegalArgumentException("Somehow you clicked on an illegal Search Item");
            }
        }
    }
}