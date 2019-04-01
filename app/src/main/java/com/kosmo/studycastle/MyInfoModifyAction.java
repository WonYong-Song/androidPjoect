package com.kosmo.studycastle;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

public class MyInfoModifyAction extends AppCompatActivity {

    EditText editText;
    Button button;
    BoomMenuButton bmb;
    Intent intent2;
    //상단 그라데이션
    ImageView frontActivityBackground = null;
    ImageView uzb = null;
    AnimationDrawable frameAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_modify_action);

        //상단 그라데이션 처리
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

        //위젯받기
        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);

        //메인 액티비티에서 전달한 부가데이터 읽어오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int result = (bundle.getInt("result")==0) ? 0 : bundle.getInt("result");
        Log.i("Intent",Integer.toString(result));

        String resultText;
        if(result==1){
            resultText = "정상적으로 변경되었습니다.";
        }
        else{
            resultText = "정상적으로 처리되지 않았습니다.";
        }
        editText.setText(resultText);

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(),MyPage.class);
                startActivity(intent1);
            }
        });

    }
}
