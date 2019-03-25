package com.kosmo.studycastle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        SharedPreferences pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
        String name = pref.getString("name","로그인해주세요");
        login = (TextView)findViewById(R.id.login);
        login.setText(name);

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
