package com.kosmo.studycastle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AcademyDetailView extends AppCompatActivity  {


    //전역변수 선언s
    Context context;

    //네이버지도 이용을 위한 변수선언
    private MapView mapView;

    //주소에서 위경도 값을 가져오기 위한 geocoder처리를 위한 변수
    Geocoder geocoder;
    String geoStr;
    String[] geoStrArray;
    String latitudeStr;
    String longitudeStr;
    double latitude;
    double longitude;

    //젼역변수
    ImageView aca_image,tea_image;
    TextView category,aca_name,telephone,address,introduce,tea_name,tea_intro;
    GridView teacher_intro;
    ListView class_info;
    ProgressDialog dialog; //대기시 프로그래스 창 사용을 위한 변수
    ScrollView mother_scroll;
    BoomMenuButton bmb;
    Intent intent2;

    //이미지 처리진행할 bitmap변수
    ArrayList<Bitmap> acaimagebit = new ArrayList<Bitmap>();//학원이미지
    ArrayList<Bitmap> teaimagebit = new ArrayList<Bitmap>();//선생님이미지
    Bitmap bitmap;

    //통신결과를 저장하기위한 변수선언s
    //학원정보
    String acaIntroPhoto = "";
    String acaIntroPhotoUU = "";
    String category_result = "";
    String acaname = "";
    String telephone1 = "";
    String telephone2 = "";
    String telephone3 = "";
    String address_result = "";
    String detailAddress = "";
    String introduce_result = "";

    //강사정보
    ArrayList<String> teaImage = new ArrayList<String>();
    ArrayList<String> teaImageUU = new ArrayList<String>();
    ArrayList<String> teaName = new ArrayList<String>();
    ArrayList<String> teaIntro = new ArrayList<String>();
    ArrayList<String> subject = new ArrayList<String>();

    //강의정보
    ArrayList<String> classTeaName = new ArrayList<String>();
    ArrayList<String> startDate = new ArrayList<String>();
    ArrayList<String> endDate = new ArrayList<String>();
    ArrayList<String> day = new ArrayList<String>();
    ArrayList<String> startTime = new ArrayList<String>();
    ArrayList<String> endTime = new ArrayList<String>();
    ArrayList<String> className = new ArrayList<String>();
    ArrayList<String> participants = new ArrayList<String>();
    ArrayList<String> pay = new ArrayList<String>();
    ArrayList<String> classMembers = new ArrayList<String>();
    //통신결과를 저장하기위한 변수선언e
    //전역변수 선언e



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_detail_view);

        context=this;
        geocoder = new Geocoder(this);
        mapView = findViewById(R.id.map_view);

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
        //이전 엑티비이에서 전달한 부가 데이터 읽어오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String idx = bundle.getString("idx");
        //전달받은 인텐트값 확인하기
        Log.i("getIntent","idx:"+idx);


        //ProgressDialog객체생성(서버 응답 대기용)
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("학원정보 리스트 가져오기");
        dialog.setMessage("서버로부터 응답을 기다리고있습니다.");

        //스크롤뷰 내부 뷰에서 스크롤 기능을 작동하기위한 코드
        mother_scroll = (ScrollView)findViewById(R.id.mother_scroll);
        teacher_intro = (GridView)findViewById(R.id.teacher_intro);
        class_info = (ListView)findViewById(R.id.class_info);

        teacher_intro.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    mother_scroll.requestDisallowInterceptTouchEvent(false);
                }
                else{
                    mother_scroll.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        class_info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    mother_scroll.requestDisallowInterceptTouchEvent(false);
                }
                else{
                    mother_scroll.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    mother_scroll.requestDisallowInterceptTouchEvent(false);
                }
                else{
                    mother_scroll.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });


        //AsyncTask를 이용한 서버 접속
        String map = "/catle/AppAcaDetail.do";
        String url;
        url = getString(R.string.http);
        new AsyncHttpRequest().execute(url+map
                ,"idx="+idx
        );

        //네이버지도 구현 부분
        mapView.onCreate(savedInstanceState);

    }//onCreate



    class AsyncHttpRequest extends AsyncTask<String,Void,String> implements OnMapReadyCallback{

        @UiThread
        @Override
        public void onMapReady(@NonNull NaverMap naverMap) {

            naverMap.setMapType(NaverMap.MapType.Basic);
            naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT,true);

            //마커 셋팅
            Marker marker = new Marker();
            marker.setWidth(Marker.SIZE_AUTO);
            marker.setHeight(Marker.SIZE_AUTO);
            marker.setPosition(new LatLng(latitude, longitude));
            marker.setMap(naverMap);

            //카메라 위치 지정
            naverMap.setCameraPosition(new CameraPosition(new LatLng(latitude,longitude),15));
            Log.d("지도에 들어가는 위경도 값",Double.toString(latitude)+","+Double.toString(longitude));
        }
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
            try{
                Log.i("KOSMO",sBuffer.toString());

                JSONObject jsonObject = new JSONObject(sBuffer.toString());

                sBuffer.setLength(0);//sBuffer초기화

                //학원정보 가져오기
                JSONArray result1 = jsonObject.getJSONArray("학원정보");
                //학원 정보는 하나뿐이기 때문에 0번 인덱스로 가져온다.
                JSONObject object1 = result1.getJSONObject(0);
                acaIntroPhoto = object1.getString("acaIntroPhoto");
                acaIntroPhotoUU= object1.getString("acaIntroPhotoUU");
                category_result = object1.getString("category");
                acaname = object1.getString("acaName");
                telephone1 = object1.getString("telephone1");
                telephone2 = object1.getString("telephone2");
                telephone3 = object1.getString("telephone3");
                address_result = object1.getString("address");
                detailAddress = object1.getString("detailAddress");
                introduce_result = object1.getString("introduce");

                //주소를 가지고 위경도 값 가져오기
                try{
                    geoStr = (geocoder.getFromLocationName(address_result,10)).get(0).toString();
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                Log.d("위경도 geocoder처리 : ",geoStr);
                geoStrArray = geoStr.split(",");
                latitudeStr = geoStrArray[10];
                longitudeStr = geoStrArray[12];
                Log.d("위도:",latitudeStr);
                Log.d("경도:",longitudeStr);
                latitude = Double.parseDouble(latitudeStr.split("=")[1]);
                longitude = Double.parseDouble(longitudeStr.split("=")[1]);
                Log.d("위경도 더플형으로 파싱 이후:",Double.toString(latitude)+","+Double.toString(longitude));


                //학원소개이미지
                final String[] acaPhotoArray = acaIntroPhoto.split(","); //여러장인 경우 구분자로 나눠서 이미지처리 진행을 위해 배열처리
                //이미지 배열만큼 반복작업진행
                Thread mthread1 = new Thread(){
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

                mthread1.start();

                try{
                    mthread1.join();

                    acaimagebit.add(bitmap);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                //강사정보 출력
                JSONArray result2 = jsonObject.getJSONArray("강사정보");
                for(int i=0; i<result2.length() ; i++){
                    JSONObject object2 = result2.getJSONObject(i);
                    teaImage.add(object2.getString("teaImage"));
                    teaImageUU.add(object2.getString("teaImageUU"));
                    teaName.add(object2.getString("teaName"));
                    teaIntro.add(object2.getString("teaIntro"));
                    subject.add(object2.getString("subject"));

                    //선생님 이미지
                    Thread mthread2 = new Thread(){
                        @Override
                        public void run() {
                            try{
                                /*
                                    url_str에는 이미지가 저장되어있는 웹주소를 넣고
                                    jsobObject.getString()으로 파일의 이름을 가져와 같이 붙여서
                                    new URL()의 괄호 안에 넣어 가져온다.
                                 */
                                String url_str = "https://previews.123rf.com/images/tigatelu/tigatelu1404/tigatelu140400198/27656695-%EA%B5%90%EC%88%98%EC%9D%98-%EB%A7%8C%ED%99%94-%EA%B5%90%EC%9C%A1.jpg";
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

                    mthread2.start();

                    try{
                        mthread2.join();

                        teaimagebit.add(bitmap);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                //강의정보 출력
                JSONArray result3 = jsonObject.getJSONArray("강의정보");
                for(int i=0; i<result3.length() ; i++){
                    JSONObject object3 = result3.getJSONObject(i);
                    classTeaName.add(object3.getString("classTeaName"));
                    startDate.add(object3.getString("startDate"));
                    endDate.add(object3.getString("endDate"));
                    day.add(object3.getString("day"));
                    startTime.add(object3.getString("startTime"));
                    endTime.add(object3.getString("endTime"));
                    className.add(object3.getString("className"));
                    participants.add(object3.getString("participants"));
                    pay.add(object3.getString("pay"));
                    classMembers.add(object3.getString("classMembers"));
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

            //뷰에 데이터 입력을 위한 위젯 가져오기
            aca_image = (ImageView)findViewById(R.id.aca_image);
            category = (TextView)findViewById(R.id.category);
            aca_name = (TextView)findViewById(R.id.aca_name);
            telephone = (TextView)findViewById(R.id.telephone);
            address = (TextView)findViewById(R.id.address);
            introduce = (TextView)findViewById(R.id.introduce);

            //위젯에 데이터 입력하기
            aca_image.setImageBitmap(acaimagebit.get(0));
            category.setText(category_result);
            aca_name.setText(acaname);
            telephone.setText(telephone1+"-"+telephone2+"-"+telephone3);
            address.setText(address_result+" "+detailAddress);
            introduce.setText(introduce_result);

            mapView.getMapAsync(this);

            //커스텀뷰1
            Log.i("커스텀뷰에 들어가기전 강사정보 갯수",Integer.toString(teaName.size()));
            TeacherAdapter teacherAdapter = new TeacherAdapter(context,teaimagebit,teaName,teaIntro,subject);

            teacher_intro.setAdapter(teacherAdapter);

            //커스텀뷰2
            ClassInfo classInfo = new ClassInfo();

            class_info.setAdapter(classInfo);


        }
    }

    //커스텀어뎁터2 - 강의정보
    class ClassInfo extends BaseAdapter{
        @Override
        public int getCount() {
            return className.size();
        }

        @Override
        public Object getItem(int position) {
            return className.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ClassDetailInfo view = new ClassDetailInfo(getApplicationContext());
            view.setClassName(className.get(position));
            view.setTeaName(classTeaName.get(position));
            view.setClassTerm(startDate.get(position)+"~"+endDate.get(position));
            view.setClassTime("매주 "+day.get(position)+" "+startTime.get(position)+"~"+endTime.get(position));
            view.setClassPay(pay.get(position)+"원");
            view.setClassPersonnal(classMembers.get(position)+"/"+participants.get(position)+" 명");

            return view;
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
