package service;

import DataAccess.AuthTokenDao;
import DataAccess.UserDao;
import model.AuthToken;
import model.User;
import request.FillRequest;
import request.RegisterRequest;
import rescources.Database;
import result.ErrorResult;
import result.RegisterResult;
import result.Result;

import java.util.UUID;

public class RegisterService {
    public RegisterService() {
    }

    /**
     * Creates a new user account (user row in the database)
     * Generates 4 generations of ancestor data for the new user (just like the /fill endpoint if called with a generations value of 4 and this new user’s username as parameters)
     * Logs the user in
     * @param request RegisterRequest request
     * @return Result Result
     */
    public Result register(RegisterRequest request) {
        Database db = new Database();
        try {
            System.out.println("Computing Register Request");

            db.openConnection();
            // Generate new user from request
            User user = new User(request.getUsername(), request.getPassword(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getGender(), generateRandomUUID());
            new UserDao(db.getConnection()).registerUser(user);
            // Generate new AuthToken from request
            AuthToken authToken = new AuthToken(generateRandomUUID(), request.getUsername());
            new AuthTokenDao(db.getConnection()).registerAuthToken(authToken);
            // generate result using new AuthToken and new User
            RegisterResult result = new RegisterResult(authToken.getAuthtoken(), user.getUsername(), user.getPersonID(), true);
            //
            db.closeConnection(true);

            // Add FillService Stuff for 4 generations
            new FillService().fill(new FillRequest(user.getUsername(), 4));

            System.out.println("Registration of " + user.getUsername() + " successfully completed");
            return result;
        // If there was an error, record it and return it
        } catch (Exception e){
            db.closeConnection(false);
            e.printStackTrace();
            return new ErrorResult(e.getMessage(), false);
        }
    }

    /**
     * Generates Random UUID String 8 chars long
     * @return UUID String Unique UUID
     */
    private String generateRandomUUID(){
        return UUID.randomUUID().toString().substring(0,8);
    }
}
