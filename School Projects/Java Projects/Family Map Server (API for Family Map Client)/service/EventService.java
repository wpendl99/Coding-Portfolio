package service;

import DataAccess.EventDao;
import model.AuthToken;
import model.Event;
import request.EventRequest;
import rescources.Database;
import result.*;

import java.util.ArrayList;
import java.util.Objects;

public class EventService {
    /**
     * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
     * @param eventRequest EventRequest request
     * @return Result result
     */
    public Result events(EventRequest eventRequest) {
        // Grab AuthToken
        AuthToken authToken = eventRequest.getAuthToken();
        Database db = new Database();
        try {
            System.out.println("Computing Events Request");

            db.openConnection();
            Event[] data;
            try {
                // Process request for all Events for User
                ArrayList<Event> tmp = new EventDao(db.getConnection()).getEvents(authToken.getUsername());
                data = (Event[]) tmp.toArray(new Event[tmp.size()]);
            // If no Persons were found in the database
            } catch (Exception e){
                e.printStackTrace();
                throw new Exception("Nobody belongs to this person");
            }
            // Record and Return results
            EventsResult result = new EventsResult(data, true);
            db.closeConnection(true);
            System.out.println("Events Request successfully completed");
            return result;
        // If there was an error, record and return
        } catch (Exception e){
            db.closeConnection(false);
            e.printStackTrace();
            return new ErrorResult(e.getMessage(), false);
        }

    }

    /**
     * Returns the single Event object with the specified ID (if the event is associated with the current user). The current user is determined by the provided authtoken.
     * @param eventRequest EventRequest result
     * @return Result result
     */
    public Result event(EventRequest eventRequest) {
        // Get AuthToken and eventID
        AuthToken authToken = eventRequest.getAuthToken();
        String eventID = eventRequest.getEventID();
        Database db = new Database();
        try {
            System.out.println("Computing Event Request");

            db.openConnection();
            Event event;
            try {
                // Return All Family Previous Persons and get user
                event = new EventDao(db.getConnection()).find(eventID);
            // If no event was found, record and return
            } catch (Exception e){
                e.printStackTrace();
                throw new Exception("Error - Requested Event does not exist");
            }
            // If the event is not associated to the username, deny access, record and return
            if(!Objects.equals(event.getAssociatedUsername(), authToken.getUsername())){
                throw new Exception("Error - Requested Event does not belong to this user");
            }
            // Record and return the event
            EventResult result = new EventResult(event, true);
            db.closeConnection(true);
            System.out.println("Event Request successfully completed");
            return result;
        // If there was an error, record and return
        } catch (Exception e){
            db.closeConnection(false);
            e.printStackTrace();
            return new ErrorResult(e.getMessage(), false);
        }
    }
}
