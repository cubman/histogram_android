package com.example.android.histogram;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class histogram_page extends AppCompatActivity {

    private String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_histogram_page);

        user_name = getIntent().getStringExtra("user_name");

        TextView tw = (TextView) findViewById(R.id.textView2);
        tw.setText(getString(R.string.user_name, user_name));
    }

    public void back_to_menu(View v) {
        Intent questionIntent = new Intent(histogram_page.this,
                Main.class);

        System.exit(0);
        startActivity(questionIntent);

    }
}
