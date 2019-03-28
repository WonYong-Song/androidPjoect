package com.kosmo.studycastle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class MembersJoin extends AppCompatActivity {

    ProgressDialog dialog;//대기시 프로그레스창
    EditText id, pass, passcheck, name2, emailid, emaildomain;
    CheckBox music, exercise, art, kor, eng, math, etc, checkbox1, checkbox2;
    TextView detail1, detail2, idresult, passresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_join);

        //위젯가져오기
        id = (EditText)findViewById(R.id.id);
        pass = (EditText)findViewById(R.id.pass);
        passcheck = (EditText)findViewById(R.id.passcheck);
        name2 = (EditText)findViewById(R.id.name2);
        emailid = (EditText)findViewById(R.id.emailid);
        emaildomain = (EditText)findViewById(R.id.emaildomain);
        music = (CheckBox)findViewById(R.id.music);
        exercise = (CheckBox)findViewById(R.id.exercise);
        art = (CheckBox)findViewById(R.id.art);
        kor = (CheckBox)findViewById(R.id.kor);
        eng = (CheckBox)findViewById(R.id.eng);
        math = (CheckBox)findViewById(R.id.math);
        etc = (CheckBox)findViewById(R.id.etc);
        checkbox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox)findViewById(R.id.checkbox2);
        detail1 = (TextView)findViewById(R.id.detail1);
        detail2 = (TextView)findViewById(R.id.detail2);
        idresult = (TextView)findViewById(R.id.idresult);
        passresult = (TextView)findViewById(R.id.passresult);

        //필수글짜 빨간색으로 처리
        Spannable span1 = (Spannable)checkbox1.getText();
        Spannable span2 = (Spannable)checkbox2.getText();
        span1.setSpan(new ForegroundColorSpan(Color.RED),6,10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span2.setSpan(new ForegroundColorSpan(Color.RED),14,18,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //스피너 부착
        final Spinner domain = (Spinner)findViewById(R.id.domain);
        ArrayAdapter domainlist = ArrayAdapter.createFromResource(this,R.array.email_domain,android.R.layout.simple_spinner_dropdown_item);
        domain.setAdapter(domainlist);
        //스피너 이벤트
        domain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    emaildomain.setText("");
                    emaildomain.setFocusable(false);
                }
                else if(position==5){
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

        detail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBasic = new AlertDialog.Builder(v.getContext());

                alertBasic.setCancelable(false);
                alertBasic.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("이용약관")
                        .setMessage(R.string.detail1)

                        /*
                            setPositiveButton("버튼텍스트",리스너)
                                : 확인버튼 설정(취소버튼도 동일함)
                         */
                        .setPositiveButton("확인버튼", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        detail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBasic = new AlertDialog.Builder(v.getContext());

                alertBasic.setCancelable(false);
                alertBasic.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("이용약관")
                        /*.setMessage(R.string.detail2)*/

                        /*
                            setPositiveButton("버튼텍스트",리스너)
                                : 확인버튼 설정(취소버튼도 동일함)
                         */
                        .setPositiveButton("확인버튼", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String idstr = id.getText().toString();

                if(Pattern.matches("^[0-9][a-zA-Z0-9]{0,29}$",idstr)){
                    idresult.setText("첫 글자는 숫자일 수 없습니다.");
                }
                else if(idstr.length()<8){
                    idresult.setText("아이디는 8자이상이어야 합니다.");
                }
                else if(!Pattern.matches("^[a-zA-Z0-9]*$",idstr)){
                    idresult.setText("영문/숫자만 사용할 수 있습니다.");
                }
                else{
                    //통신하여 사용가능여부 확인
                    new AsyncHttpRequest().execute("http://192.168.0.24:8080/FinallyProject/catle/AppLoginIDCheck.do"
                            ,"id="+idstr
                    );

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passstr = pass.getText().toString();
                if(Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",passstr)){
                    passresult.setText("사용가능한 비밀번호 입니다.");
                }
                else{
                    passresult.setText("비밀번호는 숫자와 영문자 조합으로 8~12자리를 사용해야 합니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passstr = pass.getText().toString();
                String passcheckstr = passcheck.getText().toString();
                if(passstr.equals(passcheckstr)){
                    passresult.setText("일치하는 비밀번호입니다.");
                }
                else{
                    passresult.setText("일치하지 않습니다..");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //ProgressDialog객체생성(서버 응답 대기용)
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("학원정보 리스트 가져오기");
        dialog.setMessage("서버로부터 응답을 기다리고있습니다.");
    }//onCreate

    class AsyncHttpRequest extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            Log.i("resultNumber",Integer.toString(resultNumber));
            if(resultNumber==1){
                idresult.setText("사용중인 아이디입니다.");
            }
            else{
                idresult.setText("사용할 수 있는 아이디입니다.");
            }
        }

    }

}
