package com.test_project.interview_quiz.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test_project.interview_quiz.Bean.InputBean;

import java.util.ArrayList;
import com.test_project.interview_quiz.R;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.Holder> {

    private ArrayList<InputBean> arrayList = new ArrayList<>();
    View view;

    public FollowerAdapter(ArrayList<InputBean> arrayList) { //click
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_recycleview, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.txt_login.setText(arrayList.get(position).getloging_follower());
        Glide.with(view.getContext())
                .load(arrayList.get(position).gettxt_avatar_follower())
                .into(holder.img_avatar);

        if(arrayList.get(position).getdouble_follow()){
            holder.img_double_follow.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView txt_login;
        ImageView img_avatar;
        ImageView img_double_follow;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txt_login = itemView.findViewById(R.id.txt_login);
            img_avatar = itemView.findViewById(R.id.img_avatar);
            img_double_follow = itemView.findViewById(R.id.img_double_follow);
        }
    }


}

