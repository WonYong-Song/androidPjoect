package com.kosmo.studycastle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AcademyList extends AppCompatActivity {

    //전역변수
    Spinner searchColumn;
    EditText searchContents;
    Button searchButton;
    ListView acaList;
    ProgressDialog dialog;//대기시 프로그레스창

    TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_list);

        //위젯 얻어오기
        textResult = (TextView)findViewById(R.id.result);
        searchColumn = (Spinner)findViewById(R.id.search_column);
        searchContents = (EditText)findViewById(R.id.search_contents);
        searchButton = (Button)findViewById(R.id.search_button);



        //메인 액티비티에서 전달한 부가데이터 읽어오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String search_column = bundle.getString("search_column");
        String search_contents = bundle.getString("search_contents");
        String button_name = bundle.getString("button_name");


        //ProgressDialog객체생성(서버 응답 대기용)
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("학원정보 리스트 가져오기");
        dialog.setMessage("서버로부터 응답을 기다리고있습니다.");

        new AsyncHttpRequest().execute("http://192.168.0.18:8080/FinallyProject/AppAcaList.jsp"
                ,"search_column="+search_column
                ,"search_contents="+search_contents
                ,"button_name="+button_name
        );
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
                out.write("&".getBytes());
                out.write(params[2].getBytes());
                out.write("&".getBytes());
                out.write(params[3].getBytes());
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


            try{
                Log.i("KOSMO",sBuffer.toString());
                //JSONArray파싱하기
                JSONArray jsonArray = new JSONArray(sBuffer.toString());

                sBuffer.setLength(0);//sBuffer초기화
                for(int i=0 ; i<jsonArray.length() ; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    sBuffer.append("아이디 : "+jsonObject.getString("id"));
                    sBuffer.append(",패스워드 : "+jsonObject.getString("pass"));
                    sBuffer.append(",이름 : "+jsonObject.getString("name"));
                    sBuffer.append(",가입날짜 : "+jsonObject.getString("regidate"));
                    sBuffer.append("\r\n");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //진행대화창 닫기
            dialog.dismiss();

            //서버의 응답데이터 파싱후 텍스트뷰에 출력
            textResult.setText(s);
        }
    }

    //커스텀 어뎁터
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*AcademyView view = new AcademyView(getApplicationContext());
            view.setAddress();
            view.setCategory();
            view.setScore();
            view.setImage();*/
            return /*view*/  null;
        }
    }
}
