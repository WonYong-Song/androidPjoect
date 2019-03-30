package com.kosmo.studycastle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

public class MyPage extends AppCompatActivity {

    SharedPreferences.Editor editor;
    BoomMenuButton bmb;
    Intent intent2;
    TextView login, buy_list, schedule;
    ImageView my;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        final SharedPreferences pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
        String name = pref.getString("name","로그인해주세요");
        id = pref.getString("id","");
        //이미지 처리
        my = (ImageView)findViewById(R.id.my);
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getString("id","").equals("")){
                    //로그인되어있지 않을때
                    Intent intent = new Intent(v.getContext(),Login.class);
                    //로그인페이지로이동
                    startActivity(intent);
                }
                else{
                    //로그인되어있을때
                    //내정보보기로 이동
                    Intent intent = new Intent(v.getContext(),MyInfo.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            }
        });
        //로그인 텍스트처리
        login = (TextView)findViewById(R.id.login);
        login.setText(name);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getString("id","").equals("")){
                    //로그인되어있지 않을때
                    Intent intent = new Intent(v.getContext(),Login.class);
                    //로그인페이지로이동
                    startActivity(intent);
                }
                else{
                    //로그인되어있을때
                    //내정보보기로 이동
                    Intent intent = new Intent(v.getContext(),MyInfo.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            }
        });

        //등록한 수강목록처리
        buy_list = (TextView)findViewById(R.id.buy_list);
        buy_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getString("id","").equals("")){
                    //로그인되어있지 않을때
                    Intent intent = new Intent(v.getContext(),Login.class);
                    //로그인페이지로이동
                    startActivity(intent);
                }
                else{
                    //로그인되어있을때
                    Intent intent = new Intent(v.getContext(),BuyList.class);
                    //구매내역 페이지로 이동
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            }
        });

        //시간표 처리
        schedule = (TextView)findViewById(R.id.schedule);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getString("id","").equals("")){
                    //로그인되어있지 않을때
                    Intent intent = new Intent(v.getContext(),Login.class);
                    //로그인페이지로이동
                    startActivity(intent);
                }
                else{
                    //로그인되어있을때
                }
            }
        });


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
    }
}
