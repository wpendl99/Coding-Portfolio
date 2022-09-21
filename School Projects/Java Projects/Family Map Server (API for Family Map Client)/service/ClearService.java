package service;

import DataAccess.AuthTokenDao;
import DataAccess.EventDao;
import DataAccess.PersonDao;
import DataAccess.UserDao;
import rescources.Database;
import result.ClearResult;
import result.ErrorResult;
import result.Result;

public class ClearService {
    public ClearService(){
    }

    /**
     * Deletes ALL data from the database, including user, authtoken, person, and event data
     * @return Result result
     */
    public Result clear() {
        Database db = new Database();
        try {
            System.out.println("Computing Clear Request");

            db.openConnection();
            // Creates new instances of every Dao, and clears them all
            new AuthTokenDao(db.getConnection()).clearAuthTokens();
            new EventDao(db.getConnection()).clear();
            new PersonDao(db.getConnection()).clearPersons();
            new UserDao(db.getConnection()).clearUsers();

            // Record and return results
            ClearResult result = new ClearResult("Clear succeeded", true);
            db.closeConnection(true);
            System.out.println("Clearing of Database successfully completed");
            return result;
        // If error, record and return
        } catch (Exception e){
            db.closeConnection(false);
            e.printStackTrace();
            return new ErrorResult(e.getMessage(), false);
        }
    }
}
