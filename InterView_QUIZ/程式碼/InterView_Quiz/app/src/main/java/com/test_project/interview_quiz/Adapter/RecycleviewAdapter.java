package com.test_project.interview_quiz.Adapter;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test_project.interview_quiz.Bean.InputBean;
import com.test_project.interview_quiz.R;


import java.util.ArrayList;

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.Holder> {

    private OnClickListener listener; //listener

    private ArrayList<InputBean> arrayList = new ArrayList<>();

    private SharedPreferences share_prefer = null;

    View view;


    public RecycleviewAdapter(ArrayList<InputBean> arrayList, OnClickListener listener, SharedPreferences sharedPreferences) { //click
        this.arrayList = arrayList;
        this.listener = listener;
        this.share_prefer = sharedPreferences;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycleview, parent, false);

        return new Holder(view);
    }

    public InputBean getlogin(int pos) {
        return arrayList.get(pos);
    } //click


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.login.setText(arrayList.get(position).getlogin());

        Glide.with(view.getContext())
                .load(arrayList.get(position).gettxt_avatar())
                .into(holder.img_avatar);

        //第一次進入時判斷有無被加入最愛
        holder.chx_star.setChecked(arrayList.get(position).getfavor_status());

        //監聽recycleview的頭像&姓名按鈕，點下去進入分頁
        holder.con_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position, getlogin(position));
                }
            }
        });

        //監聽recycleview中的星號按鈕  ((用onclick listener才不會錯亂
        holder.chx_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String User_List = share_prefer.getString("User_List", "");
                String login = arrayList.get(position).getlogin();

                Boolean check = ((CheckBox) v).isChecked();
                holder.chx_star.setChecked(check);
                arrayList.get(position).setfavor_status(check);

                if(check){
                    share_prefer.edit().putString("User_List", User_List + login).apply();
                }
                else{
                    share_prefer.edit().putString("User_List", User_List.replace(login, "")).apply();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView login; //ID
        ImageView img_avatar; //頭貼
        CheckBox chx_star; //星號按鈕
        ConstraintLayout con_layout; //左半邊layout

        public Holder(@NonNull View itemView) {
            super(itemView);
            login = itemView.findViewById(R.id.txt_login);
            img_avatar = itemView.findViewById(R.id.img_avatar);
            chx_star = itemView.findViewById(R.id.chx1);
            con_layout = itemView.findViewById(R.id.conslayout);
        }
    }

    public interface OnClickListener { //click
        void onClick(int pos, InputBean bin); //click
    }

}
