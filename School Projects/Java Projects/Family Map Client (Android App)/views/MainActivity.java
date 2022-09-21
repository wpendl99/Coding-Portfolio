package com.wkp23.familymapclient.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.wkp23.familymapclient.R;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);
        if(fragment == null){
            fragment = createFirstFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.fragmentFrameLayout, fragment)
                    .commit();
        } else {
            if(fragment instanceof LoginFragment){
                ((LoginFragment) fragment).registerListener(this);
            }
        }
    }

    private Fragment createFirstFragment() {
        LoginFragment fragment = new LoginFragment();
        fragment.registerListener(this);
        return fragment;
    }

    @Override
    public void notifySuccessLogin(String fullName){
        Toast.makeText(this, getString(R.string.successfulLoginToast, fullName), Toast.LENGTH_SHORT).show();

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapsFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentFrameLayout, fragment)
                .commit();
    }

    @Override
    public void notifySuccessRegister(String fullName){
        Toast.makeText(this, getString(R.string.successfulRegisterToast, fullName), Toast.LENGTH_SHORT).show();

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapsFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentFrameLayout, fragment)
                .commit();
    }


    @Override
    public void notifyErrorLogin(){
        Toast.makeText(this, R.string.errorLoginToast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyErrorRegister(){
        Toast.makeText(this, R.string.errorRegisterToast, Toast.LENGTH_SHORT).show();
    }
}