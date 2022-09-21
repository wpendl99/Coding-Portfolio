package com.wkp23.familymapclient.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.wkp23.familymapclient.DataCache;
import com.wkp23.familymapclient.HttpClient;
import com.wkp23.familymapclient.R;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.AuthToken;
import model.Person;
import model.User;
import request.EventRequest;
import request.LoginRequest;
import request.PersonRequest;
import request.RegisterRequest;
import result.ErrorResult;
import result.EventResult;
import result.EventsResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;
import result.Result;


public class LoginFragment extends Fragment {

    private static final String LOG_TAG = "LoginFragment";
    private static final String NAME_KEY = "fullNameKey";

    private Listener listener;
    private String serverHost;
    private String serverPort;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;

    public interface Listener {
        void notifySuccessLogin(String fullName);
        void notifySuccessRegister(String fullName);
        void notifyErrorLogin();
        void notifyErrorRegister();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Set the serverHost TextEdit
        EditText serverHostEdit = view.findViewById(R.id.serverHostField);
        serverHostEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                serverHost = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                buttonToggler(view);
            }
        });

        // Set the serverPort TextEdit
        EditText serverPortEdit = view.findViewById(R.id.serverPortField);
        serverPortEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                serverPort = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                buttonToggler(view);
            }
        });

        // Set the username TextEdit
        EditText usernameEdit = view.findViewById(R.id.usernameField);
        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                buttonToggler(view);
            }
        });

        // Set the password TextEdit
        EditText passwordEdit = view.findViewById(R.id.passwordField);
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                buttonToggler(view);
            }
        });

        // Set the firstName TextEdit
        EditText firstNameEdit = view.findViewById(R.id.firstNameField);
        firstNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                firstName = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                buttonToggler(view);
            }
        });

        // Set the lastName TextEdit
        EditText lastNameEdit = view.findViewById(R.id.lastNameField);
        lastNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lastName = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                buttonToggler(view);
            }
        });

        // Set the email TextEdit
        EditText emailEdit = view.findViewById(R.id.emailAddressField);
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                buttonToggler(view);
            }
        });

        // Set the gender RadioButtons
        RadioGroup genderGroup = view.findViewById(R.id.genderSelection);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch(id){
                    case R.id.maleSelected:
                        gender = "m";
                        break;
                    case R.id.femaleSelected:
                        gender = "f";
                        break;
                }
                buttonToggler(view);
            }
        });

        // Set the Login Button
        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "ServerHost: " + serverHost);
                Log.d(LOG_TAG, "ServerPort: " + serverPort);
                Log.d(LOG_TAG, "Username: " + username);
                Log.d(LOG_TAG, "Password: " + password);

                LoginRequest loginRequest = new LoginRequest(username, password);
                DataCache.setServerHost(serverHost);
                DataCache.setServerPort(serverPort);

                if(listener != null) {
                    try {
                        Handler uiThreadMessageHandler = new Handler() {
                            @Override
                            public void handleMessage(Message message) {
                                Bundle bundle = message.getData();

                                if (bundle.isEmpty()){
                                    listener.notifyErrorLogin();
                                } else {
                                    listener.notifySuccessLogin(bundle.getString(NAME_KEY));
                                }


                            }
                        };

                        LoginTask loginTask = new LoginTask(uiThreadMessageHandler, loginRequest);

                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(loginTask);

                    } catch (Exception e){
                        Log.e(LOG_TAG, e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });

        // Set the Register Button
        // Set the Login Button
        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "ServerHost: " + serverHost);
                Log.d(LOG_TAG, "ServerPort: " + serverPort);
                Log.d(LOG_TAG, "Username: " + username);
                Log.d(LOG_TAG, "Password: " + password);
                Log.d(LOG_TAG, "FirstName: " + firstName);
                Log.d(LOG_TAG, "LastName: " + lastName);
                Log.d(LOG_TAG, "Email: " + email);
                Log.d(LOG_TAG, "Gender: " + gender);

                RegisterRequest registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
                DataCache.setServerHost(serverHost);
                DataCache.setServerPort(serverPort);

                if(listener != null) {
                    try {
                        Handler uiThreadMessageHandler = new Handler() {
                            @Override
                            public void handleMessage(Message message) {
                                Bundle bundle = message.getData();

                                if (bundle.isEmpty()){
                                    listener.notifyErrorRegister();
                                } else {
                                    listener.notifySuccessRegister(bundle.getString(NAME_KEY));
                                }


                            }
                        };

                        RegisterTask registerTask = new RegisterTask(uiThreadMessageHandler, registerRequest);

                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(registerTask);

                    } catch (Exception e){
                        Log.e(LOG_TAG, e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }

    private void buttonToggler(View view){
        if(serverHost != null && !serverHost.isEmpty() && serverPort != null && !serverPort.isEmpty()){
            // Enable or Disable Login Button Depending on what EditText's are filled in
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()){
                view.findViewById(R.id.loginButton).setEnabled(true);

                // Enable or Disable Register Button Depending on what EditText's are filled in
                view.findViewById(R.id.registerButton).setEnabled(firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty() && email != null && !email.isEmpty() && gender != null && !gender.isEmpty());
            } else {
                view.findViewById(R.id.loginButton).setEnabled(false);
            }
        }
    }

    private static class LoginTask implements Runnable {

        private final Handler messageHandler;
        private final LoginRequest request;

        public LoginTask(Handler messageHandler, LoginRequest request){
            this.messageHandler = messageHandler;
            this.request = request;
        }

        @Override
        public void run() {
            HttpClient httpClient = new HttpClient();
            Result result;
            try {
                URL url = new URL("http://" + DataCache.getServerHost() + ":" + DataCache.getServerPort() + "/user/login");
                result = httpClient.loginUser(url, request);
            } catch (Exception e){
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
                return;
            }
            Log.d(LOG_TAG, "Result Received from httpClient.loginUser");

            if(result instanceof LoginResult){
                AuthToken authToken = new AuthToken(((LoginResult) result).getAuthToken(), ((LoginResult) result).getUsername());
                DataCache.setAuthToken(authToken);
                DataCache.setPersonID(((LoginResult) result).getPersonID());

                try {
                    GetDataTask getDataTask = new GetDataTask(messageHandler, authToken);
                    getDataTask.run();

                } catch (Exception e){
                    Log.e(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                }
            } else {
                sendError();
            }
        }

        private void sendMessage(String fullName){
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putString(NAME_KEY, fullName);
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

        private void sendError(){
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

    }

    private static class RegisterTask implements Runnable {

        private final Handler messageHandler;
        private final RegisterRequest request;

        public RegisterTask(Handler messageHandler, RegisterRequest request){
            this.messageHandler = messageHandler;
            this.request = request;
        }

        @Override
        public void run() {
            HttpClient httpClient = new HttpClient();
            Result result;
            try {
                URL url = new URL("http://" + DataCache.getServerHost() + ":" + DataCache.getServerPort() + "/user/register");
                result = httpClient.registerUser(url, request);
            } catch (Exception e){
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
                return;
            }
            Log.d(LOG_TAG, "Result Received from httpClient.registerUser");

            if(result instanceof RegisterResult){
                AuthToken authToken = new AuthToken(((RegisterResult) result).getAuthToken(), ((RegisterResult) result).getUsername());
                DataCache.setAuthToken(authToken);
                DataCache.setPersonID(((RegisterResult) result).getPersonID());

                try {
                    GetDataTask getDataTask = new GetDataTask(messageHandler, authToken);
                    getDataTask.run();

                } catch (Exception e){
                    Log.e(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                }
            } else {
                sendError();
            }
        }

        private void sendMessage(String fullName){
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putString(NAME_KEY, fullName);
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

        private void sendError(){
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

    }

    private static class GetDataTask implements Runnable {

        private final Handler messageHandler;
        private final AuthToken authToken;

        public GetDataTask(Handler messageHandler, AuthToken authToken) {
            this.messageHandler = messageHandler;
            this.authToken = authToken;
        }

        @Override
        public void run() {
            Log.d(LOG_TAG, "Starting getDataTask");
            HttpClient httpClient = new HttpClient();

            // Get Person Data
            PersonRequest pRequest = new PersonRequest(authToken);
            Result result;
            try {
                result = httpClient.userPeople(new URL("http://" + DataCache.getServerHost() + ":" + DataCache.getServerPort() + "/person"), pRequest);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
                return;
            }
            Log.d(LOG_TAG, "Result Received from httpClient.userPeople");

            // Check that Result is valid, then add to AllPeople list
            if (result instanceof PersonResult) {
                DataCache.setAllPeople(Arrays.asList(((PersonResult) result).getData()));
            } else {
                sendError();
            }

            // Get Event Data
            EventRequest eRequest = new EventRequest(authToken);
            try {
                result = httpClient.userEvents(new URL("http://" + DataCache.getServerHost() + ":" + DataCache.getServerPort() + "/event"), eRequest);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
                return;
            }
            Log.d(LOG_TAG, "Result Received from httpClient.userEvents");

            // Check that Result is valid, then add to AllEvents list
            if (result instanceof EventsResult) {
                DataCache.setAllEvents(Arrays.asList(((EventsResult) result).getData()));
            } else {
                sendError();
            }

            sendMessage();
        }

        private void sendMessage() {
            Message message = Message.obtain();
            Person person = DataCache.getPersonFromMap(DataCache.getPersonID());
            String fullName = person.getFirstName() + " " + person.getLastName();

            Bundle messageBundle = new Bundle();
            messageBundle.putString(NAME_KEY, fullName);
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

        private void sendError() {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }
    }
}