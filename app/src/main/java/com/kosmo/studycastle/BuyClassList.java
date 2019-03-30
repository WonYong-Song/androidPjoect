package com.kosmo.studycastle;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BuyClassList extends LinearLayout {

    TextView teaname, pay, class_date, class_time, class_name;

    public BuyClassList(Context context) {
        super(context);

        //레이아웃Xml파일을 가져와서 전개
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.buy_class_list,this,true);

        //위젯가져오기
        teaname = (TextView)findViewById(R.id.teaname);
        pay = (TextView)findViewById(R.id.pay);
        class_date = (TextView)findViewById(R.id.class_date);
        class_time = (TextView)findViewById(R.id.class_time);
        class_name = (TextView)findViewById(R.id.class_name);
    }

    public void setTeaname(String s){teaname.setText(s);}
    public void setPay(String s){pay.setText(s);}
    public void setClass_date(String s){class_date.setText(s);}
    public void setClass_time(String s){class_time.setText(s);}
    public void setClass_name(String s){class_name.setText(s);}
}
