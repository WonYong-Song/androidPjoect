package com.kosmo.studycastle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AcademyList extends AppCompatActivity {

    //전역변수
    Spinner searchColumn;
    EditText searchContents;
    Button searchButton;
    ListView acaList;
    ProgressDialog dialog;//대기시 프로그레스창
    BoomMenuButton bmb;
    Intent intent2;

    //전역변수 인텐트

    //결과를 답을 배열 생성
    ArrayList<String> idx = new ArrayList<String>();
    ArrayList<String> aca_name = new ArrayList<String>();
    ArrayList<String> address = new ArrayList<String>();
    ArrayList<String> detail_address = new ArrayList<String>();
    ArrayList<String> category = new ArrayList<String>();
    ArrayList<String> score = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();
    ArrayList<String> image_uu = new ArrayList<String>();
    ArrayList<Bitmap> imagebit = new ArrayList<Bitmap>();

    //이미지 처리진행할 bitmap변수
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_list);

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

        //스피너 부착
        final Spinner searchSpinner = (Spinner)findViewById(R.id.search_column);
        ArrayAdapter searchAdapter = ArrayAdapter.createFromResource(this,R.array.search_column,android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(searchAdapter);

        //위젯 얻어오기
        searchContents = (EditText)findViewById(R.id.search_contents);
        searchButton = (Button)findViewById(R.id.search_button);



        //메인 액티비티에서 전달한 부가데이터 읽어오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String search_column = (bundle.getString("search_column"))==null ? "" : bundle.getString("search_column");
        String search_contents = (bundle.getString("search_contents"))==null ? "" : bundle.getString("search_contents");
        final String button_name = (bundle.getString("button_name"))==null ? "" : bundle.getString("button_name");
        //인텐트를 넘어온 값을 확인하기
        Log.i("getIntent","search_column : "+search_column+", search_contents : "+search_contents+", button_name : "+button_name);

        //ProgressDialog객체생성(서버 응답 대기용)
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("학원정보 리스트 가져오기");
        dialog.setMessage("서버로부터 응답을 기다리고있습니다.");

        new AsyncHttpRequest().execute("http://172.30.1.22:8080/FinallyProject/catle/AppAcaList.do"
                ,"search_column="+search_column
                ,"search_contents="+search_contents
                ,"button_name="+button_name
        );

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(),AcademyList.class);
                intent1.putExtra("button_name",button_name);
                intent1.putExtra("search_column",searchColumn.getSelectedItem().toString());
                intent1.putExtra("search_contents",searchContents.getText().toString());

                startActivity(intent1);
            }
        });


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


            //JSONArray파싱하기
            try{
                Log.i("KOSMO",sBuffer.toString());

                JSONArray jsonArray = new JSONArray(sBuffer.toString());

                sBuffer.setLength(0);//sBuffer초기화
                for(int i=0 ; i<jsonArray.length() ; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    idx.add(jsonObject.getString("idx"));
                    aca_name.add(jsonObject.getString("acaName"));
                    address.add(jsonObject.getString("address"));
                    detail_address.add(jsonObject.getString("detailAddress"));
                    category.add(jsonObject.getString("category"));
                    image.add(jsonObject.getString("acaIntroPhoto"));
                    image_uu.add(jsonObject.getString("acaIntroPhotoUU"));
                    score.add(jsonObject.getString("score"));

                    sBuffer.append("고유번호 : "+jsonObject.getString("idx"));
                    sBuffer.append(",학원명 : "+jsonObject.getString("acaName"));
                    sBuffer.append(",주소 : "+jsonObject.getString("address"));
                    sBuffer.append(",상세주소 : "+jsonObject.getString("detailAddress"));
                    sBuffer.append(",카테고리 : "+jsonObject.getString("category"));
                    sBuffer.append(",학원사진 : "+jsonObject.getString("acaIntroPhoto"));
                    sBuffer.append(",평점 : "+jsonObject.getString("score"));
                    sBuffer.append("\r\n");

                    Thread mthread = new Thread(){
                        @Override
                        public void run() {
                            try{
                                /*
                                    url_str에는 이미지가 저장되어있는 웹주소를 넣고
                                    jsobObject.getString()으로 파일의 이름을 가져와 같이 붙여서
                                    new URL()의 괄호 안에 넣어 가져온다.
                                 */
                                String url_str = "http://blogfiles.naver.net/20141219_45/weppy22_1418971309581t1gPa_JPEG/%B9%CC%C5%B0%B8%B6%BF%EC%BD%BA5.jpg";
                                URL url = new URL(url_str);

                                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                                conn.setDoInput(true);
                                conn.connect();

                                InputStream is = conn.getInputStream();
                                bitmap = BitmapFactory.decodeStream(is);
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }

                        }
                    };

                    mthread.start();

                    try{
                        mthread.join();

                        imagebit.add(bitmap);
                    }
                    catch (Exception e){
                        e.printStackTrace();
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

            //커스텀 뷰
            MyAdapter adapter = new MyAdapter();
            acaList = (ListView)findViewById(R.id.aca_list);
            acaList.setAdapter(adapter);

            //배열 선택시 상세페이지로 이동
            acaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //이동할 곳의 인텐트 작업중
                    Intent intent = new Intent(view.getContext(),AcademyDetailView.class);
                    //해당 리스트의 고유번호를 인텐트에 담아 넘김
                    intent.putExtra("idx",idx.get(position));

                    startActivity(intent);
                }
            });
        }
    }

    //커스텀 어뎁터
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return aca_name.size();
        }

        @Override
        public Object getItem(int position) {
            return aca_name.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AcademyView view = new AcademyView(getApplicationContext());
            view.setAddress(address.get(position)+detail_address.get(position));
            view.setCategory(category.get(position));
            view.setName(aca_name.get(position));
            view.setScore(score.get(position));
            view.setImage(imagebit.get(position));
            return view;
        }
    }
}
