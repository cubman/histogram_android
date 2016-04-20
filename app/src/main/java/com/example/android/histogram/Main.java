package com.example.android.histogram;

import android.app.Activity;
import android.content.*;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;


public class Main extends AppCompatActivity {
    private EditText getEditText;
    private LinearLayout getLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public void onClick(View v) {
        Intent intent = new Intent(Main.this,
                histogram_page.class);

        TextView quantityTextView = (TextView) findViewById(R.id.edit_text_main);

        intent.putExtra("user_name", quantityTextView.getText().toString());

        startActivityForResult(intent, 0);
    }


    public void ExitClick(View v) {
        System.exit(0);
    }
}
