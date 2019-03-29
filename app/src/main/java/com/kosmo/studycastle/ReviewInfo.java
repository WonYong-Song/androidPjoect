package com.kosmo.studycastle;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewInfo extends LinearLayout {

    RatingBar score;
    TextView id,scorestr,content;

    public ReviewInfo(Context context) {
        super(context);

        //레이아웃Xml파일을 가져와서 전개
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.review_info,this,true);

        score = (RatingBar)findViewById(R.id.score);
        id = (TextView)findViewById(R.id.id);
        scorestr = (TextView)findViewById(R.id.scorestr);
        content = (TextView)findViewById(R.id.content);

    }

    public void setId(String s){id.setText(s);}
    public void setScorestr(String s){scorestr.setText(s+"/5");}
    public void setContent(String s){content.setText(s);}
    public void setScore(String s){score.setRating(Float.parseFloat(s));}

}
