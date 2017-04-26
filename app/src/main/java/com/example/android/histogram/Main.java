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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        getEditText = (EditText)findViewById(R.id.edit_text_main);
    }


  /*  private String get_user_name() {
        String user_name = new String();
        return user_name;
    }*/
// получаем имя пользователя
    private String GetUserName() {
        String UserName;

        if ((UserName = getEditText.getText().toString()).matches(""))
            UserName = getString(R.string.incognito);

        return UserName;
    }
// для старта построения гистограммы
    public void onClickHistogram(View v) {
        Intent intent = new Intent(Main.this,
                HistogramPage.class);

        intent.putExtra("user_name", GetUserName());

        startActivity(intent);
    }
// при запуске о программе
    public void onClickAboutProgram(View v) {
        Intent intent = new Intent(Main.this,
                AboutProgram.class);

        intent.putExtra("user_name", GetUserName());

        startActivity(intent);
    }

    // выход
    public void ExitClick(View v) {
        System.exit(0);
    }
}
