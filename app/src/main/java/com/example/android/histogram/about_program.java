package com.example.android.histogram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class about_program extends AppCompatActivity {
        String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_program);

        user_name = getIntent().getStringExtra("user_name");

        TextView tw = (TextView) findViewById(R.id.textView_about_program_1);
        tw.setText(getString(R.string.About_program_enter, user_name));
    }

    // Кнопка "назад"
    public void back_to_menu(View v) {
        finish();
        super.onBackPressed();
    }
}
