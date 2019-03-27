package com.kosmo.studycastle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyInfoModify extends AppCompatActivity {

    ProgressDialog dialog;//대기시 프로그레스창
    BoomMenuButton bmb;
    Intent intent2;
    TextView id;
    EditText pass, name2, emailid, emaildomain,mobile1,mobile2,mobile3,passcheck;
    CheckBox music, exercise, art, kor, eng, math, etc;

    String idstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_modify);

        //위젯가져오기
        id = (TextView)findViewById(R.id.id);
        pass = (EditText)findViewById(R.id.pass);
        name2 = (EditText)findViewById(R.id.name2);
        emailid = (EditText)findViewById(R.id.emailid);
        emaildomain = (EditText)findViewById(R.id.emaildomain);
        mobile1 = (EditText)findViewById(R.id.mobile1);
        mobile2 = (EditText)findViewById(R.id.mobile2);
        mobile3 = (EditText)findViewById(R.id.mobile3);
        passcheck = (EditText)findViewById(R.id.passcheck);
        music = (CheckBox)findViewById(R.id.music);
        exercise = (CheckBox)findViewById(R.id.exercise);
        art = (CheckBox)findViewById(R.id.art);
        kor = (CheckBox)findViewById(R.id.kor);
        eng = (CheckBox)findViewById(R.id.eng);
        math = (CheckBox)findViewById(R.id.math);
        etc = (CheckBox)findViewById(R.id.etc);


        //메인 액티비티에서 전달한 부가데이터 읽어오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        idstr = (bundle.getString("id"))==null ? "" : bundle.getString("id");
        Log.i("Intent",idstr);

        //스피너 부착
        final Spinner domain = (Spinner)findViewById(R.id.domain);
        ArrayAdapter domainlist = ArrayAdapter.createFromResource(this,R.array.email_domain,android.R.layout.simple_spinner_dropdown_item);
        domain.setAdapter(domainlist);
        //스피너 이벤트
        domain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==4){
                    emaildomain.setText("");
                    emaildomain.setFocusableInTouchMode(true);
                    emaildomain.requestFocus();
                }
                else{
                    emaildomain.setText(domain.getItemAtPosition(position).toString());
                    emaildomain.setFocusable(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //ProgressDialog객체생성(서버 응답 대기용)
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("학원정보 리스트 가져오기");
        dialog.setMessage("서버로부터 응답을 기다리고있습니다.");

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

        new AsyncHttpRequest().execute("http://192.168.0.24:8080/FinallyProject/catle/AppMyInfo.do"
                ,"id="+idstr
        );
    }//onCreate

    class AsyncHttpRequest extends AsyncTask<String,Void,String> {
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





            return sBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //진행대화창 닫기
            dialog.dismiss();

            //JSONArray파싱하기
            try{
                Log.i("KOSMO",s);
                //결과(문자를)를 JSONObject 객체로 변형
                JSONObject jsonObject = new JSONObject(s);

                name2.setText(jsonObject.getString("name"));
                id.setText(jsonObject.getString("id"));
                pass.setText(jsonObject.getString("pass"));
                passcheck.setText(jsonObject.getString("pass"));
                emailid.setText(jsonObject.getString("emailid"));
                emaildomain.setText(jsonObject.getString("emaildomain"));
                mobile1.setText(jsonObject.getString("mobile1"));
                mobile2.setText(jsonObject.getString("mobile2"));
                mobile3.setText(jsonObject.getString("mobile3"));
                Log.i("ModifyInfo","가져올때 : "+jsonObject.getString("interest"));
                String[] intereststr = jsonObject.getString("interest").split(",");
                for(int i=0; i<intereststr.length; i++){
                    if(music.getText().toString().equals(intereststr[i])){
                        music.setChecked(true);
                    }
                    else if(exercise.getText().toString().equals(intereststr[i])){
                        exercise.setChecked(true);
                    }
                    else if(art.getText().toString().equals(intereststr[i])){
                        art.setChecked(true);
                    }
                    else if(kor.getText().toString().equals(intereststr[i])){
                        kor.setChecked(true);
                    }
                    else if(eng.getText().toString().equals(intereststr[i])){
                        eng.setChecked(true);
                    }
                    else if(math.getText().toString().equals(intereststr[i])){
                        math.setChecked(true);
                    }
                    else if(etc.getText().toString().equals(intereststr[i])){
                        etc.setChecked(true);
                    }
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }


        }

    }
    //수정하기 버튼 클릭시 작동하는 메소드
    public void modifyAction(View v){
        //폼값체크 및 이상 없을 시 다음 액티비티로 이동
        if(pass.getText().toString().equals("")){
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            pass.requestFocus();
            return;
        }
        else if(passcheck.getText().toString().equals("")){
            Toast.makeText(this, "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show();
            passcheck.requestFocus();
            return;
        }
        else if(name2.getText().toString().equals("")){
            Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            name2.requestFocus();
            return;
        }
        else if(emailid.getText().toString().equals("")){
            Toast.makeText(this, "이메일아이디을 입력해주세요", Toast.LENGTH_SHORT).show();
            emailid.requestFocus();
            return;
        }
        else if(emaildomain.getText().toString().equals("")){
            Toast.makeText(this, "이메일의 도메인을 입력해주세요", Toast.LENGTH_SHORT).show();
            emaildomain.requestFocus();
            return;
        }
        else if(mobile1.getText().toString().equals("")||mobile1.getText().toString().length()<3){
            Toast.makeText(this, "휴대폰번호를 정상적으로 입력해주세요", Toast.LENGTH_SHORT).show();
            mobile1.requestFocus();
            return;
        }
        else if(mobile2.getText().toString().equals("")||mobile2.getText().toString().length()<4){
            Toast.makeText(this, "휴대폰번호를 정상적으로 입력해주세요", Toast.LENGTH_SHORT).show();
            mobile2.requestFocus();
            return;
        }
        else if(mobile3.getText().toString().equals("")||mobile2.getText().toString().length()<4){
            Toast.makeText(this, "휴대폰번호를 정상적으로 입력해주세요", Toast.LENGTH_SHORT).show();
            mobile3.requestFocus();
            return;
        }
        else if(!music.isChecked()&&!exercise.isChecked()&&!art.isChecked()&&!kor.isChecked()&&!eng.isChecked()&&!math.isChecked()&&!etc.isChecked()){
            Toast.makeText(this, "관심사를 체크해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!pass.getText().toString().equals(passcheck.getText().toString())){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            passcheck.setText("");
            passcheck.requestFocus();

        }
        else{
            //폼체크 완료 후 액티브 이동

            //체크박스의 text값을 저장하기위한 배열 생성
            JSONArray jsonArray = new JSONArray();
            if(exercise.isChecked()){
                jsonArray.put(exercise.getText().toString());
            }
            if(music.isChecked()){
                jsonArray.put(music.getText().toString());
            }
            if(art.isChecked()){
                jsonArray.put(art.getText().toString());
            }
            if(kor.isChecked()){
                jsonArray.put(kor.getText().toString());
            }
            if(eng.isChecked()){
                jsonArray.put(eng.getText().toString());
            }
            if(math.isChecked()){
                jsonArray.put(math.getText().toString());
            }
            if(etc.isChecked()){
                jsonArray.put(etc.getText().toString());
            }
            Log.i("ModifyInfo","jsonArray:"+jsonArray.toString());


            JSONObject object = new JSONObject();
            try{

                object.put("id",id.getText().toString());
                object.put("pass",pass.getText().toString());
                object.put("name",name2.getText().toString());
                object.put("emailid",emailid.getText().toString());
                object.put("emaildomain",emaildomain.getText().toString());
                object.put("mobile1",mobile1.getText().toString());
                object.put("mobile2",mobile2.getText().toString());
                object.put("mobile3",mobile3.getText().toString());
                object.put("interest",jsonArray.toString());
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            Log.i("ModifyInfo",object.toString());

            new AsyncHttpModify().execute("http://192.168.0.24:8080/FinallyProject/catle/AppMyInfoModifyAction.do"
                    ,"info="+object.toString()
            );

        }

    }

    class AsyncHttpModify extends AsyncTask<String,Void,String> {
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



            return sBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //진행대화창 닫기
            dialog.dismiss();

            int resultNumber = 0;
            try{
                JSONObject object = new JSONObject(s);
                resultNumber = object.getInt("result");
            }
            catch (Exception e){
                e.printStackTrace();
            }

            Intent intent = new Intent(getApplicationContext(),MyInfoModifyAction.class);

            intent.putExtra("result",resultNumber);
            startActivity(intent);


        }

    }
}
