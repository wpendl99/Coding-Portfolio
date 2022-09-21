package service;

import DataAccess.PersonDao;
import model.AuthToken;
import model.Person;
import request.PersonRequest;
import rescources.Database;
import result.ErrorResult;
import result.PersonResult;
import result.Result;

import java.util.ArrayList;
import java.util.Objects;

public class PersonService {
    /**
     * Returns ALL family members of the current user. The current user is determined by the provided authtoken.
     * @param personRequest PersonRequest request
     * @return Result result
     */
    public Result persons(PersonRequest personRequest) {
        // Get the AuthToken
        AuthToken authToken = personRequest.getAuthToken();
        Database db = new Database();
        try {
            System.out.println("Computing Persons Request");

            db.openConnection();
            Person[] data;

            try {
                // Process request for every Person associated to that User
                ArrayList<Person> tmp = new PersonDao(db.getConnection()).getPersons(authToken.getUsername());
                data = (Person[]) tmp.toArray(new Person[tmp.size()]);
            //  If there isn't a person associated to the User, record and return
            } catch (Exception e){
                e.printStackTrace();
                throw new Exception("Nobody belongs to this person");
            }

            // Record and return all the Persons associated to the User
            PersonResult result = new PersonResult(data, true);
            db.closeConnection(true);
            System.out.println("Person Request successfully completed");
            return result;
        // If there was an error, record and return
        } catch (Exception e){
            db.closeConnection(false);
            e.printStackTrace();
            return new ErrorResult(e.getMessage(), false);
        }
    }

    /**
     * Returns the single Person object with the specified ID (if the person is associated with the current user). The current user is determined by the provided authtoken.
     * @param personRequest PersonRequest request
     * @return Result result
     */
    public Result person(PersonRequest personRequest) {
        // Get the AuthToken and the personID
        AuthToken authToken = personRequest.getAuthToken();
        String s = personRequest.getPersonID();
        Database db = new Database();
        try {
            System.out.println("Computing Person Request");

            db.openConnection();
            Person person;
            try {
                // Process request for personID
                person = new PersonDao(db.getConnection()).getPerson(s);
            // If there isn't a person associateed to that username, record and return
            } catch (Exception e){
                e.printStackTrace();
                throw new Exception("Error - Requested Person does not exist");
            }
            // If the Person isn't associated to the username, deny access, record and return
            if(!Objects.equals(person.getAssociatedUsername(), authToken.getUsername())){
                throw new Exception("Error - Requested Person does not belong to this user");
            }
            // Record and return Person
            PersonResult result = new PersonResult(person, true);
            db.closeConnection(true);
            System.out.println("Person Request successfully completed");
            return result;
        // If there was an error, record and return
        } catch (Exception e){
            db.closeConnection(false);
            e.printStackTrace();
            return new ErrorResult(e.getMessage(), false);
        }
    }
}
