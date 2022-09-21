package com.wkp23.familymapclient;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import model.AuthToken;
import model.Event;
import model.Person;

public class DataCache {

    private static final String LOG_TAG = "DataCache";

    // Variables For Server
    private static String serverHost;
    private static String serverPort;

    // Date Variables
    private static final DataCache instance = new DataCache();
    private static AuthToken authToken;
    private static List<Person> allPeople;
    private static List<Event> allEvents;

    // Helper Variables (i.e. used to make mappings better and faster)
    private static String personID;
    private static Event selectedEvent;
    private static HashMap<String, Person> personMap;
    private static HashMap<String, Event> eventMap;
    private static HashMap<String, HashMap<String, Event>> personEventsMap;
    private static ArrayList<String> eventTypes = new ArrayList<>();
    private static HashMap<String, Float> eventColors = new HashMap<>();
    private static final List<Event> activeEvents =  new ArrayList<>();
    private static final List<Event> fatherMaleEvents =  new ArrayList<>();
    private static final List<Event> fatherFemaleEvents =  new ArrayList<>();
    private static final List<Event> motherMaleEvents =  new ArrayList<>();
    private static final List<Event> motherFemaleEvents =  new ArrayList<>();
    private static final List<Float> colors = Arrays.asList(BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_ROSE, BitmapDescriptorFactory.HUE_CYAN, BitmapDescriptorFactory.HUE_MAGENTA, BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_YELLOW, BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_AZURE);

    // Setting Values
    private static final SettingsManager settingsManager = new SettingsManager();

    private DataCache(){
    }

    public static DataCache getInstance() {
        return instance;
    }

    public static String getServerHost() {
        return serverHost;
    }

    public static void setServerHost(String serverHost) {
        DataCache.serverHost = serverHost;
    }

    public static String getServerPort() {
        return serverPort;
    }

    public static void setServerPort(String serverPort) {
        DataCache.serverPort = serverPort;
    }

    public static AuthToken getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(AuthToken authToken) {
        DataCache.authToken = authToken;
    }

    public static List<Person> getAllPeople() {
        return allPeople;
    }

    public static void setAllPeople(List<Person> allPeople) {
        DataCache.allPeople = allPeople;

        personMap = new HashMap<>();
        allPeople.forEach((person) -> personMap.put(person.getPersonID(), person));
    }

    public static List<Event> getAllEvents() {
        return allEvents;
    }

    public static void setAllEvents(List<Event> allEvents) {
        DataCache.allEvents = allEvents;

        eventMap = new HashMap<>();
        allEvents.forEach((event) -> {
            // Put into Event Map
            eventMap.put(event.getEventID(), event);

            // check the type and put it in the respective lists
            if(!eventTypes.contains(event.getEventType())) {
                eventTypes.add(event.getEventType());
                eventColors.put(event.getEventType(), colors.get(eventTypes.size() % colors.size()));
            }
        });

        personEventsMap = new HashMap<>();
        for(Event event: allEvents){
            if(personEventsMap.containsKey(event.getPersonID())){
                HashMap<String, Event> map = personEventsMap.get(event.getPersonID());
                map.put(event.getEventType(), event);
                personEventsMap.put(event.getPersonID(), map);
            } else {
                HashMap<String, Event> map = new HashMap<>();
                map.put(event.getEventType(), event);
                personEventsMap.put(event.getPersonID(), map);
            }
        }

        setFilteredEvents();
    }
    
    private static void setFilteredEvents() {
        Person person = personMap.get(personID);
        Person mother = personMap.get(person.getMotherID());
        Person father = personMap.get(person.getFatherID());
        
        // Set the Mother's side
        setParentEvents(mother,  "mother");
        // Set the Father's side
        setParentEvents(father, "father");
    }
    
    private static void setParentEvents(Person person, String parent) {
        if(parent == "mother"){
            if (person.getGender().equals("f")){
                motherFemaleEvents.addAll(personEventsMap.get(person.getPersonID()).values());
            } else {
                motherMaleEvents.addAll(personEventsMap.get(person.getPersonID()).values());
            }
        } else {
            if (person.getGender().equals("f")){
                fatherFemaleEvents.addAll(personEventsMap.get(person.getPersonID()).values());
            } else {
                fatherMaleEvents.addAll(personEventsMap.get(person.getPersonID()).values());
            }
        }
        
        if (person.getMotherID() != null) {
            setParentEvents(personMap.get(person.getMotherID()), parent);
            setParentEvents(personMap.get(person.getFatherID()), parent);
        }
    }

    public static String getPersonID() {
        return personID;
    }

    public static void setPersonID(String personID) {
        DataCache.personID = personID;
    }

    public static Event getSelectedEvent() {
        return selectedEvent;
    }

    public static void setSelectedEvent(Event event) {
        DataCache.selectedEvent = event;
    }

    public static HashMap<String, Person> getPersonMap() {
        return personMap;
    }

    public static Person getPersonFromMap(String personID){
        return personMap.get(personID);
    }

    public static void setPersonMap(HashMap<String, Person> personMap) {
        DataCache.personMap = personMap;
    }

    public static ArrayList<Person> getPeopleContaining(String string){
        ArrayList<Person> list = new ArrayList<>();

        if (string != null) {
            allPeople.forEach((person) -> {
                if (person.getFirstName().toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT)) || person.getLastName().toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT))) {
                    list.add(person);
                }
            });
        }

        return list;
    }

    public static Event getEventFromMap(String eventID) {
        Event event = eventMap.get(eventID);
        if (activeEvents.contains(event)){
            return event;
        }
        return null;
    }

    public static ArrayList<Event> getEventsContaining(String string){
        ArrayList<Event> list = new ArrayList<>();

        if (string != null) {
            activeEvents.forEach((event) -> {
                Person tmp = personMap.get(event.getPersonID());
                if (event.getCountry().toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT)) || event.getCity().toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT)) || event.getEventType().toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT)) || String.valueOf(event.getYear()).contains(string.toLowerCase(Locale.ROOT)) || tmp.getFirstName().toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT)) || tmp.getLastName().toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT))) {
                    list.add(event);
                }
            });
        }

        return list;
    }

    public static Map<String, Person> getPersonFamily(Person person) {
        if(allPeople.contains(person)) {
            Map<String, Person> family = new LinkedHashMap<>();

            if (personMap.get(person.getFatherID()) != null) {
                family.put("Father", personMap.get(person.getFatherID()));
            }
            if (personMap.get(person.getMotherID()) != null) {
                family.put("Mother", personMap.get(person.getMotherID()));
            }
            if (personMap.get(person.getSpouseID()) != null) {
                family.put("Spouse", personMap.get(person.getSpouseID()));
            }

            Person child = getPersonChild(person);
            if (child != null) {
                family.put("Child", child);
            }

            return family;
        }
        return null;
    }

    public static Person getPersonChild(Person parent) {
        String personID = parent.getPersonID();
        Person child = null;
        if (parent.getGender().equals("f")) {
            for (Person person : allPeople) {
                if (person.getMotherID() != null && person.getMotherID().equals(personID)) {
                    return person;
                }
            }
        } else if (parent.getGender().equals("m")) {
            for (Person person : allPeople) {
                if (person.getFatherID() != null && person.getFatherID().equals(personID)) {
                    return person;
                }
            }
        }
        return null;
    }

    public static Event getPersonEvent(String personID, String eventType) {
        Event event = personEventsMap.get(personID).get(eventType);
        if (activeEvents.contains(event)){
            return event;
        }
        return null;
    }

    public static ArrayList<Event> getOrderedPersonEvents(String personID){
        if(personMap.containsKey(personID)) {
            ArrayList<Event> events = new ArrayList<>();
            System.out.println(eventTypes);
            eventTypes.forEach((eventType) -> {
                    events.add(getPersonEvent(personID, eventType));
            });

            while (events.remove(null)) ;

            System.out.println(events);

            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event event1, Event event2) {
                    return event1.getYear() - event2.getYear();
                }
            });

            return events;
        }
        return null;
    }

    public static Event getPersonFirstEvent(String personID){
        if (getOrderedPersonEvents(personID) != null){
            return getOrderedPersonEvents(personID).get(0);
        }
        return null;
    }

    public static Float getEventTypeColor(String type){
        return eventColors.get(type);
    }

    public static List<Event> getActiveEvents() {
        return activeEvents;
    }

    public static List<Event> getFatherMaleEvents() {
        return fatherMaleEvents;
    }

    public static List<Event> getFatherFemaleEvents() {
        return fatherFemaleEvents;
    }

    public static List<Event> getMotherMaleEvents() {
        return motherMaleEvents;
    }

    public static List<Event> getMotherFemaleEvents() {
        return motherFemaleEvents;
    }

    public static ArrayList<SettingsItem> getSettings() {
        return new ArrayList<>(settingsManager.getData());
    }
    
    public static void updateSettings() {
        DataCache.activeEvents.clear();
        if(settingsManager.checkSetting("Father's Side")){
            if (settingsManager.checkSetting("Male Events")) {
                DataCache.activeEvents.addAll(fatherMaleEvents);
            }
            if (settingsManager.checkSetting("Female Events")) {
                DataCache.activeEvents.addAll(fatherFemaleEvents);
            }
        }
        if (settingsManager.checkSetting("Mother's Side")) {
            if (settingsManager.checkSetting("Male Events")) {
                DataCache.activeEvents.addAll(motherMaleEvents);
            }
            if (settingsManager.checkSetting("Female Events")) {
                DataCache.activeEvents.addAll(motherFemaleEvents);
            }
        }
        if (personMap.get(personID).getGender().equals("m") && settingsManager.checkSetting("Male Events")){
            DataCache.activeEvents.addAll(personEventsMap.get(personID).values());
        }
        if (personMap.get(personID).getGender().equals("f") && settingsManager.checkSetting("Female Events")){
            DataCache.activeEvents.addAll(personEventsMap.get(personID).values());
        }
        if (personMap.get(personID).getSpouseID() != null){
            if (personMap.get(personMap.get(personID).getSpouseID()).getGender().equals("m") && settingsManager.checkSetting("Male Events")){
                DataCache.activeEvents.addAll(personEventsMap.get(personMap.get(personID).getSpouseID()).values());
            }
            if (personMap.get(personMap.get(personID).getSpouseID()).getGender().equals("f") && settingsManager.checkSetting("Female Events")){
                DataCache.activeEvents.addAll(personEventsMap.get(personMap.get(personID).getSpouseID()).values());
            }
        }
    }

    public static void setSetting(String setting, Boolean status) {
        settingsManager.setSetting(setting, status);
    }

    public static boolean isSetting(String setting) {
        return settingsManager.checkSetting(setting);
    }

    public static void theBigOne(){
        personID = null;
        selectedEvent = null;
        personMap = null;
        eventMap = null;
        personEventsMap = null;
        eventTypes = new ArrayList<>();
        eventColors = new HashMap<>();
        activeEvents.clear();
        fatherMaleEvents.clear();
        fatherFemaleEvents.clear();
        motherMaleEvents.clear();
        motherFemaleEvents.clear();
    }
}
