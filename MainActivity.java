package com.example.tiger.mob_tracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
// battery measurer function

    public AlarmManager alarmManager;
    public PendingIntent pendingIntent;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            int level = i.getIntExtra("level", 0);
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar);
            pb.setProgress(level);
            TextView tv = (TextView) findViewById(R.id.textfield);
            // Integer.toString(level) is the numerical value of the battery
            tv.setText("Battery Level: " + Integer.toString(level) + "%");
        }

    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //battery stuff
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

       // this.unregisterReceiver(mBatInfoReceiver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Starts MobTrakerService
        startService(new Intent(getBaseContext(), MobTrackerService.class));


            /////////////ALARM
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            //we make pendingintnet which can be updated in the future
             pendingIntent = PendingIntent.getBroadcast(this, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            //RTC_WAKEUP make alive even phone is in sleep. when it will be trigered
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (60 * 1000), pendingIntent);





        //we need to call sharePreferences settings in order it to
        // loaded
        loadPreferences();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

/*
    public void onUserLeaveHint(){
        super.onUserLeaveHint();
        //canceling timer
//       alarmManager.cancel(pendingIntent);
//        pendingIntent.cancel();

        Toast.makeText(this, "sreen_refresh", Toast.LENGTH_LONG).show();
        //resetting timer

        //pendingIntent = PendingIntent.getBroadcast(this, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (60 * 1000), pendingIntent);

    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedId = item.getItemId();

        switch(selectedId)
        {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, Settings.class));
                Toast.makeText(this, "settings", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, about_app.class));
                Toast.makeText(this, "about", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_search:
                Toast.makeText(this, "search", Toast.LENGTH_LONG).show();
                break;

            case R.id.action_share:
                Toast.makeText(this, "search", Toast.LENGTH_LONG).show();
                Intent i=new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject test");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "MobTracker www.mobTracker.org");
                startActivity(Intent.createChooser(i,"Share via"));
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    //makes instance of preferences called settings sets it to showSettings (the key value in the listpref)
    public void loadPreferences()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        settings.getBoolean("is_first", true);
        TextView ShowSettings = (TextView) findViewById(R.id.showSettings);
        boolean silentmode = settings.getBoolean("override_preference", Boolean.parseBoolean(""));
        ShowSettings.setText((settings.getString("email_address", "")) +  " -- " + (settings.getString("phone_number", "")) + " -- " +
                (settings.getString("timer_preference", "")) + "  --  "  + silentmode  + "++++" + (settings.getBoolean("is_first", Boolean.parseBoolean(""))));
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadPreferences();
    }

    ////////////////////////////////////////////////////////////////
    //START IT FROM HERE



}
