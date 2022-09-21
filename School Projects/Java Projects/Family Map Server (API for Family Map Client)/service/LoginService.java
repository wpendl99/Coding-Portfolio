package service;

import DataAccess.AuthTokenDao;
import DataAccess.UserDao;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import rescources.Database;
import result.ErrorResult;
import result.LoginResult;
import result.Result;

import java.util.Objects;
import java.util.UUID;

public class LoginService {
    public LoginService(){
    }

    /**
     * Logs the user in
     * @param request LoginRequest request
     * @return Result result
     */
    public Result login(LoginRequest request){
        Database db = new Database();
        try {
            System.out.println("Computing Login Request");
            db.openConnection();
            // Gets the User Object from Database
            User user;
            user = new UserDao(db.getConnection()).loginUser(request.getUsername());

            // If a User was found
            if (user != null) {
                // If the password is correct
                if (Objects.equals(user.getPassword(), request.getPassword())) {
                    // Generate a new AuthToken from User
                    AuthToken authToken = new AuthToken(generateRandomUUID(), request.getUsername());
                    new AuthTokenDao(db.getConnection()).registerAuthToken(authToken);
                    // Record results and return them
                    LoginResult result = new LoginResult(authToken.getAuthtoken(), user.getUsername(), user.getPersonID(), true);
                    db.closeConnection(true);
                    System.out.println("Login of " + user.getUsername() + " successfully completed");
                    return result;
                // If the Password was incorrect
                } else {
                    db.closeConnection(false);
                    System.out.println("Password incorrect");
                    return new ErrorResult("Error - Password Incorrect", false);
                }
            // If the user was not found
            } else {
                db.closeConnection(false);
                System.out.println("User Doesn't exist");
                return new ErrorResult("Error - User doesn't exist", false);
            }
        // If there was an error
        } catch (Exception e){
            db.closeConnection(false);
            e.printStackTrace();
            return new ErrorResult(e.getMessage(), false);
        }
    }

    /**
     * Generates Random UUID String 8 chars long
     * @return UUID
     */
    private String generateRandomUUID(){
        return UUID.randomUUID().toString().substring(0,8);
    }
}
