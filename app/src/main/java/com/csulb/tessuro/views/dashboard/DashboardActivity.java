package com.csulb.tessuro.views.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.csulb.tessuro.R;
import com.csulb.tessuro.models.UserModel;
import com.csulb.tessuro.utils.SystemUtils;
import com.csulb.tessuro.views.dashboard.about.AboutUsFragment;
import com.csulb.tessuro.views.dashboard.help.HelpFragment;
import com.csulb.tessuro.views.dashboard.home.HomeStudentFragment;
import com.csulb.tessuro.views.dashboard.home.HomeAdminFragment;
import com.csulb.tessuro.views.dashboard.profile.ProfileFragment;
import com.csulb.tessuro.views.dashboard.quiz.QuizHistoryFragment;
import com.csulb.tessuro.views.intro.WelcomeActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FirebaseAuth auth;

    // from nav_header xml
    private CircleImageView navImage_imageView;

    private String TAG = DashboardActivity.class.getSimpleName();
    private static final String USER_SHARED_PREF = "user";
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        auth = FirebaseAuth.getInstance();  // get firebase instance

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);       // apply the toolbar as an action bar

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);  // apply listener for when an item is selected

        // create the drawer
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navImage_imageView = findViewById(R.id.navImage_imageView);

        // get user information for drawer
        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = headerView.findViewById(R.id.navEmail_textView);
        TextView navRole = headerView.findViewById(R.id.navRole_textView);

        userModel = new UserModel(getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE));
        Log.i(TAG, "onCreate: email, role" + userModel.getEmail() + " " + userModel.getRole());

        try {
            // set the text views for the nav
            navEmail.setText(userModel.getEmail());
            navRole.setText(userModel.getRole());
        } catch (Exception e) {
            Log.e(TAG, "onCreate: setting email & role in drawer -> " + e.getMessage());
        }

        handleDrawerActions();

        if (savedInstanceState == null) {

            if (userModel.getRole().equals("Admin")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  new HomeAdminFragment()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  new HomeStudentFragment()).commit();
            }
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    public void handleDrawerActions() {
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.e(TAG, "onDrawerOpened: drawer opened");

                try {
                    SystemUtils systemUtils = new SystemUtils();
                    systemUtils.hideSoftKeyboard(DashboardActivity.this);
                } catch (Exception e) {
                    Log.e(TAG, "onDrawerOpened: the keyboard was not opened");
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                if (userModel.getRole().equals("Admin")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  new HomeAdminFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  new HomeStudentFragment()).commit();
                }
                break;
            case R.id.nav_account_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUsFragment()).commit();
                break;
            case R.id.nav_quiz_preferences:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QuizHistoryFragment()).commit();
                break;
            case R.id.nav_logout:
                handleLogout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void handleLogout() {
        auth.signOut();
        Intent intent = new Intent(DashboardActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();   // prevent user from coming back
    }

    /**
     * Closes drawer first before leaving activity.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) { // check if open
            drawer.closeDrawer(GravityCompat.START);    // close drawer
        }
        // do nothing else, button is disabled
    }
}
