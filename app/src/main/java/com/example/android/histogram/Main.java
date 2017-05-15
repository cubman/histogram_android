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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Main extends AppCompatActivity {
    private EditText getEditText;
    private boolean was_saved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        getEditText = (EditText)findViewById(R.id.edit_text_main);
        if (savedInstanceState != null) {
            was_saved = savedInstanceState.getBoolean("save");
        } else {
            readFromFile(this);
        }
        if (was_saved)
            EnterField(false);
        else
            EnterField(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        //MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_histogram, menu);
        menu.add(0, 0, 0, R.string.changeName);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.println(1, "sad", String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case 0:
                was_saved = false;
                EnterField(true);
                return true;
        }
        return false;
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.hdd", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.hdd");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();

                String [] p = ret.split(" ");
                if (p.length == 3 && p[0].equals("name") && p[1].equals("=")) {
                    getEditText.setText(p[2]);
                    was_saved = true;
                } else
                    Log.d("str", ret);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void EnterField(boolean show) {
        LinearLayout lo = (LinearLayout) findViewById(R.id.Linear_layout_main_input);
        lo.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("save", was_saved);
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


    public void saveName(View v) {
        EnterField(false);
        was_saved = true;
        writeToFile("name = " + GetUserName(), this);
    }

    // выход
    public void ExitClick(View v) {
        System.exit(0);
    }
}
