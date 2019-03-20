package com.kosmo.studycastle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    //전역변수
    Spinner search_column;
    EditText search_contents;
    Button all,entrance_examination,physical_education,etc,search_button;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //스피너 부착
        final Spinner searchSpinner = (Spinner)findViewById(R.id.search_column);
        ArrayAdapter searchAdapter = ArrayAdapter.createFromResource(this,R.array.search_column,android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(searchAdapter);

        all=(Button)findViewById(R.id.all);
        entrance_examination=(Button)findViewById(R.id.entrance_examination);
        physical_education=(Button)findViewById(R.id.physical_education);
        etc=(Button)findViewById(R.id.etc);
        search_contents=(EditText)findViewById(R.id.search_contents);
        search_button = (Button)findViewById(R.id.search_button);

        //전체선택시
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),AcademyList.class);
                intent.putExtra("search_column",searchSpinner.getSelectedItem().toString());
                intent.putExtra("search_contents",search_contents.getText().toString());
                intent.putExtra("button_name",all.getText().toString());

                startActivity(intent);
            }
        });

        //입시선택시
        entrance_examination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),AcademyList.class);
                intent.putExtra("search_column",searchSpinner.getSelectedItem().toString());
                intent.putExtra("search_contents",search_contents.getText().toString());
                intent.putExtra("button_name",entrance_examination.getText().toString());

                startActivity(intent);
            }
        });
        //예체능선택시
        physical_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),AcademyList.class);
                intent.putExtra("search_column",searchSpinner.getSelectedItem().toString());
                intent.putExtra("search_contents",search_contents.getText().toString());
                intent.putExtra("button_name",physical_education.getText().toString());

                startActivity(intent);
            }
        });
        //기타 선택시
        etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),AcademyList.class);
                intent.putExtra("search_column",searchSpinner.getSelectedItem().toString());
                intent.putExtra("search_contents",search_contents.getText().toString());
                intent.putExtra("button_name",etc.getText().toString());

                startActivity(intent);
            }
        });

        //검색버튼 선택시
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),AcademyList.class);
                intent.putExtra("search_column",searchSpinner.getSelectedItem().toString());
                intent.putExtra("search_contents",search_contents.getText().toString());
                intent.putExtra("button_name",search_button.getText().toString());

                startActivity(intent);
            }
        });
    }
}
