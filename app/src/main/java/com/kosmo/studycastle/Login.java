package com.kosmo.studycastle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {

    SharedPreferences.Editor editor;
    BoomMenuButton bmb;
    Intent intent2;
    EditText tvID, tvPwd;
    String sid, spwd;
    String id = "";
    String name = "";
    ProgressDialog dialog;//대기시 프로그레스창

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
        editor = pref.edit();

        //위잿가져오기
        tvID = findViewById(R.id.etID);
        tvPwd = findViewById(R.id.etPwd);

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
        dialog.setTitle("로그인 처리중");
        dialog.setMessage("서버로부터 응답을 기다리고있습니다.");
    }//onCreate

    //로그인 버튼을 클릭할때..
    public void btnLoginClicked(View view){
        //아이디와 비밀번호 입력 가져오기
        sid = tvID.getText().toString();
        spwd = tvPwd.getText().toString();

        String map = "/catle/AppLoginAction.do";
        String url;
        url = getString(R.string.http);
        new AsyncHttpRequest().execute(url+map
                ,"id="+sid
                ,"pass="+spwd
        );

    }

    //회원가입 버튼 클릭했을때..
    public void btnMemberJoin(View view){
        Intent intent = new Intent(view.getContext(),MembersJoin.class);
        startActivity(intent);
    }

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
                connection.setRequestMethod("GET");
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
                out.write("&".getBytes());
                out.write(params[2].getBytes());
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

            //JSONObject파싱하기
            try {
                Log.i("KOSMO", sBuffer.toString());
                //로그인의 결과가 있는 경우
                if(!sBuffer.toString().equals("")){
                    Log.d("ID,NAME","내부"+sBuffer.toString());
                    JSONObject jsonObject = new JSONObject(sBuffer.toString());
                    id = jsonObject.getString("id");
                    name = jsonObject.getString("name");
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

            Log.d("ID,NAME", "id:"+id+", name:"+name);
            if(!id.equals("")){
                editor.putString("id",id);
                editor.putString("name",name);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(),MyPage.class);
                startActivity(intent);
            }
            else{
                editor.clear();
                editor.commit();
                Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
