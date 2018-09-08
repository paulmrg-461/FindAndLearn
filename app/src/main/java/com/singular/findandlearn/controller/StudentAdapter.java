package com.singular.findandlearn.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.singular.findandlearn.R;
import com.singular.findandlearn.model.Student;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Paul Realpe on 24/03/2018.
 */

public class StudentAdapter extends BaseAdapter {

    List<Student> students;
    Context context;

    public StudentAdapter (List<Student> students, Context context){

        this.students = students;
        this.context = context;
    }
    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        Student student = students.get(position);
        return student;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v;
        if (view==null){
            v = View.inflate(context, R.layout.item_student, null);

        } else {
            v = view;
        }

        Student student = this.students.get(position);

        ImageView img = (ImageView) v.findViewById(R.id.img_student);
        TextView nickname = (TextView) v.findViewById(R.id.nickname_student);
        TextView location = (TextView) v.findViewById(R.id.location_student);
        TextView program = (TextView) v.findViewById(R.id.program_student);
        TextView points = (TextView) v.findViewById(R.id.points_student);

        //Picasso.with(context).load(module.getImg()).into(imageView);
        Picasso.with(context).load(student.getImg_student()).fit().into(img);
        nickname.setText(student.getNickname_student());
        location.setText(String.valueOf((student.getLatitude_student()))+" Estás mucho lo cerca");
        program.setText(student.getProgram_student());
        points.setText(String.valueOf((student.getPoints_student()))+" Puntos");
        return v;
    }
}
