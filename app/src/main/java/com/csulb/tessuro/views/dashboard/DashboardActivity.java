package com.csulb.tessuro.views.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.UserModel;
import com.csulb.tessuro.views.dashboard.help.HelpFragment;
import com.csulb.tessuro.views.dashboard.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private String TAG = DashboardActivity.class.getSimpleName();
    private static final String USER_SHARED_PREF = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserModel userModel = new UserModel(getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE));
        String fullname = userModel.getFullname();
        String email = userModel.getEmail();
        String role = userModel.getRole();
        String created = userModel.getCreated();
        Log.i(TAG, "onCreate: " + fullname + " "  + email + " " + role + " " + created);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  new HelpFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_help);
//        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_account_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Closes drawer first before leaving activity.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) { // check if open
            drawer.closeDrawer(GravityCompat.START);    // close drawer
        } else {
            super.onBackPressed();  // drawer not open, continue as normal
        }
    }
}
