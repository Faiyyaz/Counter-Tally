package com.khatri.tasbeehcount;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private EditText sc;
    private Button count, reset, exit;
    private int counter, s;
    private String set;
    private ToggleButton toggle,vibtoggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        exit = (Button) findViewById(R.id.exit);
        count = (Button) findViewById(R.id.count);
        reset = (Button) findViewById(R.id.reset);
        sc = (EditText) findViewById(R.id.setcount);
        toggle=(ToggleButton)findViewById(R.id.toggle);
        vibtoggle=(ToggleButton)findViewById(R.id.vibtoggle);

        loadSharedPreferences();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instructions");
        builder.setMessage("In app there are two options to count \n 1. By using Count Button. \n 2. By using Volume Up Button")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);

        set = sc.getText().toString();
        s = Integer.valueOf(set);

        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(toggle.isChecked()) {
                    mp.start();
                }

                if(vibtoggle.isChecked()){
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(50);
                }

                SharedPreferences countSettings = getSharedPreferences("count", 0);
                int counter = countSettings.getInt("counts", 0);
                counter++;
                final SharedPreferences.Editor edit = countSettings.edit();
                edit.putInt("counts", counter);
                edit.commit();

                if (counter == Integer.parseInt(sc.getText().toString())) {
                    setCount();
                }

                if (counter %100 == 0){
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(2000);
                }

                tv.setText(String.valueOf(counter));
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences countSettings = getSharedPreferences("count", 0);
                final SharedPreferences.Editor edit = countSettings.edit();
                edit.putInt("counts", 0);
                edit.commit();
                sc.setText("0");
                tv.setText(String.valueOf(counter = 0));
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });

        toggle.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences soundPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=soundPreferences.edit();
                editor.putBoolean("toggle",toggle.isChecked());
                editor.commit();
            }
        });

        vibtoggle.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences vibPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=vibPreferences.edit();
                editor.putBoolean("vibtoggle",vibtoggle.isChecked());
                editor.commit();
            }
        });
    }

    private void loadSharedPreferences(){
        SharedPreferences soundPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        toggle.setChecked(soundPreferences.getBoolean("toggle",false));
        SharedPreferences vibPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        vibtoggle.setChecked(vibPreferences.getBoolean("vibtoggle",false));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {

            final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);

            if (toggle.isChecked())
                mp.start();

            if(vibtoggle.isChecked()){
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(50);
            }

            SharedPreferences countSettings = getSharedPreferences("count", 0);
            int counter = countSettings.getInt("counts", 0);
            counter++;
            final SharedPreferences.Editor edit = countSettings.edit();
            edit.putInt("counts", counter);
            edit.commit();

            if (counter == Integer.parseInt(sc.getText().toString())) {
                setCount();
            }

            if (counter %100 == 0){
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(2000);
            }

            tv.setText(String.valueOf(counter));
        }
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences countSettings = getSharedPreferences("count", 0);
        final SharedPreferences.Editor edit = countSettings.edit();
        edit.putInt("counts", 0);
        edit.commit();
        sc.setText("0");
        tv.setText(String.valueOf(counter = 0));
    }

    public void setCount() {

            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(1000);
            Toast.makeText(getApplicationContext(), "You have reached your target", Toast.LENGTH_LONG).show();
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.dismiss();
                            sc.setText("0");
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            exit();
                            sc.setText("0");
                            SharedPreferences countSettings = getSharedPreferences("count", 0);
                            final SharedPreferences.Editor edit = countSettings.edit();
                            edit.putInt("counts", 0);
                            edit.commit();
                            tv.setText(String.valueOf(counter = 0));
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to pray more?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    public void exit(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        System.exit(0);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to Exit?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item:
                Intent about=new Intent(MainActivity.this,About.class);
              startActivity(about);
        }
        return true;
    }
}


