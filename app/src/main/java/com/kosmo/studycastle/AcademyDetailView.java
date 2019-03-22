package com.kosmo.studycastle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.naver.maps.map.MapView;

public class AcademyDetailView extends AppCompatActivity {

    //네이버지도 이용을 위한 변수선언
    private MapView mapView;

    //젼역변수
    ImageView aca_image;
    TextView category,aca_name,telephone,address,introduce;
    GridView teacher_intro;
    ListView class_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_detail_view);

        //네이버지도 구현 부분
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        //이전 엑티비이에서 전달한 부가 데이터 읽어오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String idx = bundle.getString("c");

        //AsyncTask를 이용한 서버 접속
        new AsyncHttpRequest().execute("http://192.168.0.24:8080/FinallyProject/catle/AppAcaDetail.do"
                ,"idx="+idx
        );
    }//onCreate

    class AsyncHttpRequest extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
