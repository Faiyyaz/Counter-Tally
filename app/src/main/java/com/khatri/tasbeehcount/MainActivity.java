package com.khatri.tasbeehcount;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private TextView tv, tv1;
    private EditText sc;
    private Button count, reset, target, exit;
    private int counter, s;
    private String set;
    private ToggleButton toggle,vibtoggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        tv1 = (TextView) findViewById(R.id.tv1);
        exit = (Button) findViewById(R.id.exit);
        count = (Button) findViewById(R.id.count);
        reset = (Button) findViewById(R.id.reset);
        sc = (EditText) findViewById(R.id.setcount);
        target = (Button) findViewById(R.id.set);
        toggle=(ToggleButton)findViewById(R.id.toggle);
        vibtoggle=(ToggleButton)findViewById(R.id.vibtoggle);

        toggle.setChecked(false);
        vibtoggle.setChecked(false);

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
                SharedPreferences countSettings = getSharedPreferences("count", 0);
                int counter = countSettings.getInt("counts", 0);
                counter++;
                final SharedPreferences.Editor edit = countSettings.edit();
                edit.putInt("counts", counter);
                edit.commit();

                if (counter == Integer.parseInt(sc.getText().toString())) {
                    setCount();
                }

                tv.setText(String.valueOf(counter));
            }
        });

        vibtoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean vibon=((ToggleButton)view).isChecked();
                if(vibon){
                    count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mp.start();
                            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibe.vibrate(50);
                            SharedPreferences countSettings = getSharedPreferences("count", 0);
                            int counter = countSettings.getInt("counts", 0);
                            counter++;
                            final SharedPreferences.Editor edit = countSettings.edit();
                            edit.putInt("counts", counter);
                            edit.commit();

                            if (counter == Integer.parseInt(sc.getText().toString())) {
                                setCount();
                            }

                            if (counter % 100 == 0) {
                                vibe.vibrate(200);
                            }

                            tv.setText(String.valueOf(counter));
                        }
                    });

                }
                else{
                    count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mp.start();
                            SharedPreferences countSettings = getSharedPreferences("count", 0);
                            int counter = countSettings.getInt("counts", 0);
                            counter++;
                            final SharedPreferences.Editor edit = countSettings.edit();
                            edit.putInt("counts", counter);
                            edit.commit();

                            if (counter == Integer.parseInt(sc.getText().toString())) {
                                setCount();
                            }
                            tv.setText(String.valueOf(counter));
                        }
                    });
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences countSettings = getSharedPreferences("count", 0);
                final SharedPreferences.Editor edit = countSettings.edit();
                edit.putInt("counts", 0);
                edit.commit();
                tv.setText(String.valueOf(counter = 0));
            }
        });
        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv1.setText(sc.getText().toString());
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean on=((ToggleButton)view).isChecked();
                if(on){
                    count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mp.start();
                            SharedPreferences countSettings = getSharedPreferences("count", 0);
                            int counter = countSettings.getInt("counts", 0);
                            counter++;
                            final SharedPreferences.Editor edit = countSettings.edit();
                            edit.putInt("counts", counter);
                            edit.commit();

                            if (counter == Integer.parseInt(sc.getText().toString())) {
                                setCount();
                            }

                            tv.setText(String.valueOf(counter));
                        }
                    });
                }
                else{
                        count.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences countSettings = getSharedPreferences("count", 0);
                                int counter = countSettings.getInt("counts", 0);
                                counter++;
                                final SharedPreferences.Editor edit = countSettings.edit();
                                edit.putInt("counts", counter);
                                edit.commit();

                                if (counter == Integer.parseInt(sc.getText().toString())) {
                                    setCount();
                                }

                                tv.setText(String.valueOf(counter));
                            }
                        });
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            SharedPreferences countSettings = getSharedPreferences("count", 0);
            int counter = countSettings.getInt("counts", 0);
            counter++;
            final SharedPreferences.Editor edit = countSettings.edit();
            edit.putInt("counts", counter);
            edit.commit();

            if (counter == Integer.parseInt(sc.getText().toString())) {
                setCount();
            }

            tv.setText(String.valueOf(counter));
        }
        return true;
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
                            count.setClickable(true);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            SharedPreferences countSettings = getSharedPreferences("count", 0);
                            final SharedPreferences.Editor edit = countSettings.edit();
                            edit.putInt("counts", 0);
                            edit.commit();
                            tv.setText(String.valueOf(counter = 0));
                            count.setClickable(false);
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to pray more?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }


