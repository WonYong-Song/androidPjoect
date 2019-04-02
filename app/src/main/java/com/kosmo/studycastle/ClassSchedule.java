package com.kosmo.studycastle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ImageView;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClassSchedule extends AppCompatActivity {

    ProgressDialog dialog;//대기시 프로그레스창
    BoomMenuButton bmb;
    Intent intent2;

    //상단 그라데이션
    ImageView frontActivityBackground = null;
    ImageView uzb = null;
    AnimationDrawable frameAnimation;

    TextView montime, monname, tuetime, tuename, wedtime, wedname, thutime, thuname,
            fritime, friname, sattime, satname, suntime, sunname, monclassname, tueclassname,
            wedclassname, thuclassname, friclassname, satclassname, sunclassname;
    String montimestr = "";
    String monnamestr = "";
    String tuetimestr = "";
    String tuenamestr = "";
    String wedtimestr = "";
    String wednamestr = "";
    String thutimestr = "";
    String thunamestr = "";
    String fritimestr = "";
    String frinamestr = "";
    String sattimestr = "";
    String satnamestr = "";
    String suntimestr = "";
    String sunnamestr = "";
    String monclassnamestr = "";
    String tueclassnamestr = "";
    String wedclassnamestr = "";
    String thuclassnamestr = "";
    String friclassnamestr = "";
    String satclassnamestr = "";
    String sunclassnamestr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);

        frontActivityBackground = (ImageView)findViewById(R.id.frontActivityBackground);
        frontActivityBackground.setBackgroundResource(R.drawable.transition);

        frameAnimation = (AnimationDrawable) frontActivityBackground.getBackground();
        frameAnimation .setEnterFadeDuration(1000);
        frameAnimation .setExitFadeDuration(1000);


        frontActivityBackground.postDelayed(new Runnable() {
            public void run() {
                frameAnimation.start();
            }
        }, 200);

        SharedPreferences pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
        String idstr = pref.getString("id","");

        montime = (TextView)findViewById(R.id.montime);
        monname = (TextView)findViewById(R.id.monname);
        tuetime = (TextView)findViewById(R.id.tuetime);
        tuename = (TextView)findViewById(R.id.tuename);
        wedtime = (TextView)findViewById(R.id.wedtime);
        wedname = (TextView)findViewById(R.id.wedname);
        thutime = (TextView)findViewById(R.id.thutime);
        thuname = (TextView)findViewById(R.id.thuname);
        fritime = (TextView)findViewById(R.id.fritime);
        friname = (TextView)findViewById(R.id.friname);
        sattime = (TextView)findViewById(R.id.sattime);
        satname = (TextView)findViewById(R.id.satname);
        suntime = (TextView)findViewById(R.id.suntime);
        sunname = (TextView)findViewById(R.id.sunname);
        monclassname = (TextView)findViewById(R.id.monclassname);
        tueclassname = (TextView)findViewById(R.id.tueclassname);
        wedclassname = (TextView)findViewById(R.id.wedclassname);
        thuclassname = (TextView)findViewById(R.id.thuclassname);
        friclassname = (TextView)findViewById(R.id.friclassname);
        satclassname = (TextView)findViewById(R.id.satclassname);
        sunclassname = (TextView)findViewById(R.id.sunclassname);

        //붐메뉴적용
        bmb = (BoomMenuButton)findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_1);
        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            /*
            bmb.addBuilder(new SimpleCircleButton.Builder()
                    .normalImageRes(R.drawable.study_castle));
            */
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder();
            if(i==0){
                builder.normalImageRes(R.drawable.my)
                        .normalText("마이페이지")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                intent2 = new Intent(getApplicationContext(),MyPage.class);

                                startActivity(intent2);
                            }
                        });
            }
            else {
                builder.normalImageRes(R.drawable.icon_home)
                        .normalText("홈으로")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                intent2 = new Intent(getApplicationContext(),MainActivity.class);

                                startActivity(intent2);
                            }
                        });
            }

            bmb.addBuilder(builder);

        }

        //ProgressDialog객체생성(서버 응답 대기용)
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("학원정보 리스트 가져오기");
        dialog.setMessage("서버로부터 응답을 기다리고있습니다.");

        String map = "/catle/AppClassSchedule.do";
        String url;
        url = getString(R.string.http);
        new AsyncHttpRequest().execute(url+map
                ,"id="+idstr
        );

    }//onCreate

    class AsyncHttpRequest extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //서버로 요청하는 시점에 프로그레스 대화창을 띄워준다.
            if(!dialog.isShowing()){
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            //파라미터확인용
            for(String s : params){
                Log.i("AsycnTask Class","파라미터:"+s);
            }

            //서버의 응답데이터를 저장할 변수(디버깅용)
            StringBuffer sBuffer = new StringBuffer();

            try{
                //요청주소로 URL객체 생성
                URL url = new URL(params[0]);
                //위 참조변수로 URL(웹주소)연결
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                //전송방식은 POST로 설정한다.(디폴트는 GET 방식)
                connection.setRequestMethod("POST");
                //OutputStream으로 파라미터를 전달하겠다는 설정
                connection.setDoOutput(true);

                /*
                요청 파라미터를 OutputStream으로 조립후 전달한다.
                -하라미터는 뭐리스트링 형태로 지정한다.
                -한국어를 전송하는 경우에는 URLEncode를 해야한다.
                -아래와 같이 처리하면 Request Body에 데이터를 담을 수 있다.
                 */
                OutputStream out = connection.getOutputStream();
                out.write(params[1].getBytes());
                out.flush();
                out.close();

                /*
                getResponseCode()를 호출하면 서버로 요청이 전달된다.
                 */
                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    //서버로부터 응답이 온 경우..
                    //응답데이터를 StringBuffer변수에 저장한다.
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
                    String responseData;
                    while ((responseData=reader.readLine()) !=null){
                        sBuffer.append(responseData+"\n\r");
                    }
                    reader.close();
                }
                else{
                    //서버로부터 응답이 없는 경우
                    Log.i("AsyncTask Class","HTTP_OK 안됨");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            //JSONObject 파싱하기
            try{
                Log.i("KOSMO",sBuffer.toString());

                JSONObject jsonObject = new JSONObject(sBuffer.toString());

                sBuffer.setLength(0);//sBuffer초기화

                //월요일 정보 가져오기
                JSONArray array1 = jsonObject.getJSONArray("월요일");
                for(int i=0 ; i<array1.length() ; i++){
                    JSONObject object = array1.getJSONObject(i);
                    if(i==array1.length()-1){
                        montimestr  += object.getString("starttime")+"~"+object.getString("endtime");
                        if(object.getString("classname").length()>15){
                            monclassnamestr  += object.getString("classname").substring(0,12)+"...";
                        }
                        else{
                            monclassnamestr  += object.getString("classname");
                        }

                        if(object.getString("acaname").length()>7){
                            monnamestr += object.getString("acaname").substring(0,5)+"...";
                        }
                        else{
                            monnamestr  += object.getString("acaname");
                        }

                    }
                    else{
                        montimestr  += object.getString("starttime")+"~"+object.getString("endtime")+"\n";
                        if(object.getString("acaname").length()>7){
                            monnamestr += object.getString("acaname").substring(0,5)+"..."+"\n";
                        }
                        else{
                            monnamestr += object.getString("acaname")+"\n";
                        }

                        if(object.getString("classname").length()>15){
                            monclassnamestr  += object.getString("classname").substring(0,12)+"..."+"\n";
                        }
                        else{
                            monclassnamestr  += object.getString("classname")+"\n";
                        }
                    }

                }
                //화요일 정보 가져오기
                JSONArray array2 = jsonObject.getJSONArray("화요일");
                for(int i=0 ; i<array2.length() ; i++){
                    JSONObject object = array2.getJSONObject(i);
                    if(i==array2.length()-1){
                        tuetimestr += object.getString("starttime")+"~"+object.getString("endtime");

                        if(object.getString("acaname").length()>7){
                            tuenamestr += object.getString("acaname").substring(0,5)+"...";
                        }
                        else{
                            tuenamestr += object.getString("acaname");
                        }
                        if(object.getString("classname").length()>15){
                            tueclassnamestr  += object.getString("classname").substring(0,12)+"...";
                        }
                        else{
                            tueclassnamestr  += object.getString("classname");
                        }
                    }
                    else{
                        tuetimestr  += object.getString("starttime")+"~"+object.getString("endtime")+"\n";
                        if(object.getString("acaname").length()>7){
                            tuenamestr += object.getString("acaname").substring(0,5)+"..."+"\n";
                        }
                        else{
                            tuenamestr += object.getString("acaname")+"\n";
                        }

                        if(object.getString("classname").length()>15){
                            tueclassnamestr  += object.getString("classname").substring(0,12)+"..."+"\n";
                        }
                        else{
                            tueclassnamestr  += object.getString("classname")+"\n";
                        }
                    }

                }
                //수요일 정보 가져오기
                JSONArray array3 = jsonObject.getJSONArray("수요일");
                for(int i=0 ; i<array3.length() ; i++){
                    JSONObject object = array3.getJSONObject(i);
                    if(i==array3.length()-1){
                        wedtimestr  += object.getString("starttime")+"~"+object.getString("endtime");

                        if(object.getString("acaname").length()>7){
                            wednamestr += object.getString("acaname").substring(0,5)+"...";
                        }
                        else{
                            wednamestr += object.getString("acaname");
                        }
                        if(object.getString("classname").length()>15){
                            wedclassnamestr  += object.getString("classname").substring(0,12)+"...";
                        }
                        else{
                            wedclassnamestr  += object.getString("classname");
                        }
                    }
                    else{
                        wedtimestr  += object.getString("starttime")+"~"+object.getString("endtime")+"\n";
                        if(object.getString("acaname").length()>7){
                            wednamestr += object.getString("acaname").substring(0,5)+"..."+"\n";
                        }
                        else{
                            wednamestr += object.getString("acaname")+"\n";
                        }

                        if(object.getString("classname").length()>15){
                            wedclassnamestr  += object.getString("classname").substring(0,12)+"..."+"\n";
                        }
                        else{
                            wedclassnamestr  += object.getString("classname")+"\n";
                        }
                    }

                }
                //목요일 정보 가져오기
                JSONArray array4 = jsonObject.getJSONArray("목요일");
                for(int i=0 ; i<array4.length() ; i++){
                    JSONObject object = array4.getJSONObject(i);
                    if(i==array4.length()-1){
                        thutimestr  += object.getString("starttime")+"~"+object.getString("endtime");
                        if(object.getString("acaname").length()>7){
                            thunamestr += object.getString("acaname").substring(0,5)+"...";
                        }
                        else{
                            thunamestr += object.getString("acaname");
                        }
                        if(object.getString("classname").length()>15){
                            thuclassnamestr  += object.getString("classname").substring(0,12)+"...";
                        }
                        else{
                            thuclassnamestr  += object.getString("classname");
                        }
                    }
                    else{
                        thutimestr  += object.getString("starttime")+"~"+object.getString("endtime")+"\n";
                        if(object.getString("acaname").length()>7){
                            thunamestr += object.getString("acaname").substring(0,5)+"..."+"\n";
                        }
                        else{
                            thunamestr += object.getString("acaname")+"\n";
                        }

                        if(object.getString("classname").length()>15){
                            thuclassnamestr  += object.getString("classname").substring(0,12)+"..."+"\n";
                        }
                        else{
                            thuclassnamestr  += object.getString("classname")+"\n";
                        }
                    }

                }
                //금요일 정보 가져오기
                JSONArray array5 = jsonObject.getJSONArray("금요일");
                for(int i=0 ; i<array5.length() ; i++){
                    JSONObject object = array5.getJSONObject(i);
                    if(i==array5.length()-1) {
                        fritimestr += object.getString("starttime") + "~" + object.getString("endtime");
                        if (object.getString("acaname").length() > 7) {
                            frinamestr += object.getString("acaname").substring(0, 5) + "...";
                        } else {
                            frinamestr += object.getString("acaname");
                        }
                        if (object.getString("classname").length() > 15) {
                            friclassnamestr += object.getString("classname").substring(0, 12) + "...";
                        } else {
                            friclassnamestr += object.getString("classname");
                        }
                    }
                    else{
                        fritimestr  += object.getString("starttime")+"~"+object.getString("endtime")+"\n";
                        if(object.getString("acaname").length()>7){
                            frinamestr += object.getString("acaname").substring(0,5)+"..."+"\n";
                        }
                        else{
                            frinamestr += object.getString("acaname")+"\n";
                        }

                        if(object.getString("classname").length()>15){
                            friclassnamestr  += object.getString("classname").substring(0,12)+"..."+"\n";
                        }
                        else{
                            friclassnamestr  += object.getString("classname")+"\n";
                        }
                    }

                }
                //토요일 정보 가져오기
                JSONArray array6 = jsonObject.getJSONArray("토요일");
                for(int i=0 ; i<array6.length() ; i++){
                    JSONObject object = array6.getJSONObject(i);
                    if(i==array6.length()-1){
                        sattimestr  += object.getString("starttime")+"~"+object.getString("endtime");
                        if (object.getString("acaname").length() > 7) {
                            satnamestr += object.getString("acaname").substring(0, 5) + "...";
                        } else {
                            satnamestr += object.getString("acaname");
                        }
                        if (object.getString("classname").length() > 15) {
                            satclassnamestr += object.getString("classname").substring(0, 12) + "...";
                        } else {
                            satclassnamestr += object.getString("classname");
                        }
                    }
                    else{
                        sattimestr  += object.getString("starttime")+"~"+object.getString("endtime")+"\n";
                        if(object.getString("acaname").length()>7){
                            satnamestr += object.getString("acaname").substring(0,5)+"..."+"\n";
                        }
                        else{
                            satnamestr += object.getString("acaname")+"\n";
                        }

                        if(object.getString("classname").length()>15){
                            satclassnamestr  += object.getString("classname").substring(0,12)+"..."+"\n";
                        }
                        else{
                            satclassnamestr  += object.getString("classname")+"\n";
                        }
                    }

                }
                //일요일 정보 가져오기
                JSONArray array7 = jsonObject.getJSONArray("일요일");
                for(int i=0 ; i<array7.length() ; i++){
                    JSONObject object = array7.getJSONObject(i);
                    if(i==array7.length()-1){
                        suntimestr  += object.getString("starttime")+"~"+object.getString("endtime");
                        if (object.getString("acaname").length() > 7) {
                            sunnamestr += object.getString("acaname").substring(0, 5) + "...";
                        } else {
                            sunnamestr += object.getString("acaname");
                        }
                        if (object.getString("classname").length() > 15) {
                            sunclassnamestr += object.getString("classname").substring(0, 12) + "...";
                        } else {
                            sunclassnamestr += object.getString("classname");
                        }
                    }
                    else{
                        suntimestr  += object.getString("starttime")+"~"+object.getString("endtime")+"\n";
                        if(object.getString("acaname").length()>7){
                            sunnamestr += object.getString("acaname").substring(0,5)+"..."+"\n";
                        }
                        else{
                            sunnamestr += object.getString("acaname")+"\n";
                        }

                        if(object.getString("classname").length()>15){
                            sunclassnamestr  += object.getString("classname").substring(0,12)+"..."+"\n";
                        }
                        else{
                            sunclassnamestr  += object.getString("classname")+"\n";
                        }
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return sBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //진행대화창 닫기
            dialog.dismiss();

            montime.setText(montimestr);
            monname.setText(monnamestr);
            tuetime.setText(tuetimestr);
            tuename.setText(tuenamestr);
            wedtime.setText(wedtimestr);
            wedname.setText(wednamestr);
            thutime.setText(thutimestr);
            thuname.setText(thunamestr);
            fritime.setText(fritimestr);
            friname.setText(frinamestr);
            sattime.setText(sattimestr);
            satname.setText(satnamestr);
            suntime.setText(suntimestr);
            sunname.setText(sunnamestr);
            monclassname.setText(monclassnamestr);
            tueclassname.setText(tueclassnamestr);
            wedclassname.setText(wedclassnamestr);
            thuclassname.setText(thuclassnamestr);
            friclassname.setText(friclassnamestr);
            satclassname.setText(satclassnamestr);
            sunclassname.setText(sunclassnamestr);
        }

    }
}
