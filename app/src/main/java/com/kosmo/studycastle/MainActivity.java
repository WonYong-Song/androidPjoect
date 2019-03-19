package com.kosmo.studycastle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //스피너 부착
        Spinner searchSpinner = (Spinner)findViewById(R.id.search_column);
        ArrayAdapter searchAdapter = ArrayAdapter.createFromResource(this,R.array.search_column,android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(searchAdapter);
    }
}
