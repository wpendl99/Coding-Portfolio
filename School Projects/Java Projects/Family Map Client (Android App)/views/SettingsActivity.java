package com.wkp23.familymapclient.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.wkp23.familymapclient.DataCache;
import com.wkp23.familymapclient.R;
import com.wkp23.familymapclient.SettingsItem;

import java.util.ArrayList;
import java.util.Collection;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RecyclerView recyclerView = findViewById(R.id.SettingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SettingsActivity.this));

        ArrayList<SettingsItem> settings = DataCache.getSettings();

        SettingsAdapter adapter = new SettingsAdapter(settings);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onPause()  {
        super.onPause();
//        DataCache.updateSettings();
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

    private class SettingsAdapter extends RecyclerView.Adapter<SettingsViewHolder> {
        private final ArrayList<SettingsItem> settings;

        private SettingsAdapter(ArrayList<SettingsItem> settings) {
            this.settings = settings;
        }

        @NonNull
        @Override
        public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            view = getLayoutInflater().inflate(R.layout.settings_item, parent, false);

            return new SettingsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
            holder.bind(settings.get(position));
        }

        @Override
        public int getItemCount() {
            return settings.size();
        }
    }

    private class SettingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final TextView description;
        private final Switch toggle;

        private SettingsItem setting;

        SettingsViewHolder(View view){
            super(view);

            itemView.setOnClickListener(this);

            name = itemView.findViewById(R.id.SettingItemName);
            description = itemView.findViewById(R.id.SettingItemDescription);
            toggle = itemView.findViewById(R.id.SettingItemToggle);

            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                    setting.setStatus(status);
                    DataCache.setSetting(setting.getName(), status);
                }
            });
        }

        private void bind(SettingsItem setting){
            this.setting = setting;
            name.setText(setting.getName());
            description.setText(setting.getDescription());
            if(!setting.isToggleable()){
                toggle.setVisibility(View.GONE);
            } else {
                toggle.setChecked(setting.getStatus());
            }
        }

        @Override
        public void onClick(View view) {
            if (setting.getName().equals("Logout")) {
                DataCache.theBigOne();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }


    }
}