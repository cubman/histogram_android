package com.example.android.histogram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AboutProgram extends AppCompatActivity {
        String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_program);

        user_name = getIntent().getStringExtra("user_name");

        TextView tw = (TextView) findViewById(R.id.welcome_user_2);
        tw.setText(getString(R.string.About_program_enter, user_name));
    }

    // Кнопка "назад"
    public void BbackToMenu(View v) {
        finish();
        super.onBackPressed();
    }
}
