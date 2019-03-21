package com.kosmo.studycastle;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AcademyView extends LinearLayout {

    //전역변수
    ImageView aca_image;
    TextView category, aca_name, score, address;

    public AcademyView(Context context) {
        super(context);

        //레이아웃 xml파일을 가져와서 전개
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_academy_view,this,true);

        //위젯 가져오기
        aca_image = (ImageView)findViewById(R.id.aca_image);
        category = (TextView)findViewById(R.id.category);
        score = (TextView)findViewById(R.id.score);
        address = (TextView)findViewById(R.id.address);
        aca_name = (TextView)findViewById(R.id.aca_name);
    }

    //카테고리를 텍스트뷰에 설정
    public void setCategory(String category){this.category.setText(category);}

    //이름을 텍스트뷰에 설정
    public void setName(String name){aca_name.setText(name);}

    //평점을 텍스트뷰에 설정
    public void setScore(String score){this.score.setText(score);}

    //주소를 텍스트뷰에 설정
    public void setAddress(String address){this.address.setText(address);}

    //사진 이미지를 이미지뷰에 설정
    public void setImage(Bitmap bitmap){aca_image.setImageBitmap(bitmap);}
}
