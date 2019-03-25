package com.kosmo.studycastle;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClassDetailInfo extends LinearLayout {

    TextView class_name, tea_name, class_term, class_time, class_pay, class_personnel;

    public ClassDetailInfo(Context context) {
        super(context);

        //레이아웃Xml파일을 가져와서 전개
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.class_info,this,true);

        //위젯가져오기
        class_name = (TextView)findViewById(R.id.class_name);
        tea_name = (TextView)findViewById(R.id.tea_name);
        class_term = (TextView)findViewById(R.id.class_term);
        class_time = (TextView)findViewById(R.id.class_time);
        class_pay = (TextView)findViewById(R.id.class_pay);
        class_personnel = (TextView)findViewById(R.id.class_personnel);

    }

    public void setClassName(String c){class_name.setText(c);}
    public void setTeaName(String c){tea_name.setText(c);}
    public void setClassTerm(String c){class_term.setText(c);}
    public void setClassTime(String c){class_time.setText(c);}
    public void setClassPay(String c){class_pay.setText(c);}
    public void setClassPersonnal(String c){class_personnel.setText(c);}

}
