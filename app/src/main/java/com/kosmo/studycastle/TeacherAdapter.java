package com.kosmo.studycastle;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TeacherAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Bitmap> teaimagebit;
    private ArrayList<String> teaName;
    private ArrayList<String> teaIntro;
    private ArrayList<String> subject;

    public TeacherAdapter(Context context, ArrayList<Bitmap> teaimagebit, ArrayList<String> teaName, ArrayList<String> teaIntro, ArrayList<String> subject) {
        this.context = context;
        this.teaimagebit = teaimagebit;
        this.teaName = teaName;
        this.teaIntro = teaIntro;
        this.subject = subject;
    }

    @Override
    public int getCount() {
        return teaName.size();
    }

    @Override
    public Object getItem(int position) {
        return teaName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("강사정보 갯수 : ",Integer.toString(teaName.size()));
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.teacher_intro,null);
        }
        //위젯 가져와서 이미지&텍스트 삽입
        ImageView tea_image = (ImageView)convertView.findViewById(R.id.tea_image);
        tea_image.setImageBitmap(teaimagebit.get(position));
        TextView tea_name = (TextView)convertView.findViewById(R.id.tea_name);
        tea_name.setText(teaName.get(position)+" - "+subject.get(position));
        TextView tea_intro = (TextView)convertView.findViewById(R.id.tea_intro);
        tea_intro.setText(teaIntro.get(position));

        return convertView;
    }
}
