package com.example.a1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private String myUserId, userMailId;
    DrawerLayout drawer;
    TextView textViewForUserMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myUserId = FirebaseAuth.getInstance().getUid();
        userMailId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        textViewForUserMail = navigationView.getHeaderView(0).findViewById(R.id.userMailId);
        Log.d(TAG, "onCreate: userMailId " + userMailId);
        textViewForUserMail.setText(userMailId);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    @Override
    protected void onUserLeaveHint() {
        Log.d(TAG, "onUserLeaveHint: Tree will die");
        if(HomeFragment.myTimerRunning) {
            HomeFragment.cancelTimer();
        }
        super.onUserLeaveHint();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            if(HomeFragment.myTimerRunning) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Your Tree will die if you quit");
                builder.setMessage("Do you really want to quit ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HomeFragment.cancelTimer();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_timeline:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_logout:

                    Log.d(TAG, "logOut: myUserId = " + myUserId);
                    FirebaseAuth.getInstance().signOut();
                    Log.d("tag", "SignedOut");
                    myUserId = null;
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                    finish();

                break;
            case R.id.nav_stats:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PieChartFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_whiteList:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppListFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}