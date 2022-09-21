package service;

import DataAccess.EventDao;
import DataAccess.PersonDao;
import DataAccess.UserDao;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import rescources.Database;
import result.ErrorResult;
import result.LoadResult;
import result.Result;

public class LoadService {
    public LoadService() {
    }

    /**
     * Clears all data from the database (just like the /clear API)
     * Loads the user, person, and event data from the request body into the database.
     * @param request LoadRequest request
     * @return Result result
     */
    public Result load(LoadRequest request) {
        Database db = new Database();
        try {
            System.out.println("Computing Load Request");

            db.openConnection();

            // Clear Data initialize counters
            new ClearService().clear();

            // Load Users
            for(User user: request.getUsers()){
                new UserDao(db.getConnection()).registerUser(user);
            }

            // Load Persons
            for(Person person: request.getPersons()){
                new PersonDao(db.getConnection()).fillPerson(person);
            }

            // Load Events
            for(Event event: request.getEvents()){
                new EventDao(db.getConnection()).insert(event);
            }

            //Generate message
            String message = "Successfully added " + request.getUsers().length + " users, " + request.getPersons().length + " persons, and " + request.getEvents().length + "events to the database.";

            // Record and Return
            LoadResult result = new LoadResult(message, true);
            db.closeConnection(true);
            System.out.println("Loading successfully completed");
            System.out.println(message);
            return result;
        // If there was an error, Record and Return
        } catch (Exception e){
            db.closeConnection(false);
            e.printStackTrace();
            return new ErrorResult(e.getMessage(), false);
        }
    }
}
