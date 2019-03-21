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
    Button all,entrance_examination,physical_education,etc;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        all=(Button)findViewById(R.id.all);
        entrance_examination=(Button)findViewById(R.id.entrance_examination);
        physical_education=(Button)findViewById(R.id.physical_education);
        etc=(Button)findViewById(R.id.etc);

        //전체선택시
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),AcademyList.class);
                intent.putExtra("button_name",all.getText().toString());

                startActivity(intent);
            }
        });

        //입시선택시
        entrance_examination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),AcademyList.class);
                intent.putExtra("button_name",entrance_examination.getText().toString());

                startActivity(intent);
            }
        });
        //예체능선택시
        physical_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),AcademyList.class);
                intent.putExtra("button_name",physical_education.getText().toString());

                startActivity(intent);
            }
        });
        //기타 선택시
        etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),AcademyList.class);
                intent.putExtra("button_name",etc.getText().toString());

                startActivity(intent);
            }
        });

    }
}
