package com.test_project.interview_quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.test_project.interview_quiz.Adapter.FollowerAdapter;
import com.test_project.interview_quiz.Adapter.FollowingAdapter;
import com.test_project.interview_quiz.Bean.InputBean;
import com.test_project.interview_quiz.Helper.ProgressDialogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FollowActivity extends AppCompatActivity {

    String TAG = "Wei_FollowActivity";

    TabLayout tabLayout; //上方切換選單
    TextView txt_name; //gitHUB ID
    String login;

    ArrayList<String> following_list = new ArrayList<String>();
    ArrayList<String> follower_list = new ArrayList<String>();;
    String mfollower_list;

    int following_count; //計算有幾位following
    int follower_count; //計算有幾位follower

    //recycleview用
    InputBean bin_following = null;
    InputBean bin_follower = null;

    private RecyclerView recyclerView_following;
    private RecyclerView recyclerView_follower;

    ArrayList<InputBean> arrayList_following = new ArrayList<>();
    ArrayList<InputBean> arrayList_follower = new ArrayList<>();

    private FollowingAdapter followingAdapter;
    private FollowerAdapter followerAdapter;

    protected ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        login = getIntent().getExtras().getString("login");

        progressDialogHelper = new ProgressDialogHelper(this);
        progressDialogHelper.showProgressDialog("Loading following ..", false);

        initUI();
    }

    private void initUI() {

        following_list.clear();
        follower_list.clear();

        mfollower_list = "";

        txt_name = findViewById(R.id.txt_name); //github ID
        txt_name.setText(login);

        ConstraintLayout back_layout = findViewById(R.id.back_layout);
        back_layout.setOnClickListener(view -> {
            finish();
        });

        Button btn_back_arrow = findViewById(R.id.btn_back_arrow);
        btn_back_arrow.setOnClickListener(view -> {
            finish();
        });

        recyclerView_following = findViewById(R.id.recycleview2);
        recyclerView_following.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_follower = findViewById(R.id.recycleview3);
        recyclerView_follower.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_follower.setVisibility(View.GONE);

        followingAdapter = new FollowingAdapter(arrayList_following);
        followerAdapter = new FollowerAdapter(arrayList_follower);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    recyclerView_follower.setVisibility(View.GONE);
                    recyclerView_following.setVisibility(View.VISIBLE);
                } else {
                    recyclerView_following.setVisibility(View.GONE);
                    recyclerView_follower.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        call_following_API(login);
        call_follower_API(login);
    }

    //讀取following list
    private void call_following_API(String login) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(30, TimeUnit.SECONDS);    //限制寫入秒數
        builder.readTimeout(30, TimeUnit.SECONDS);     //限制讀取秒數
        builder.connectTimeout(30, TimeUnit.SECONDS);  //限制讀取秒數
        OkHttpClient client = builder.build();

        Request request = new Request.Builder()
                .url("https://api.github.com/users/" + login + "/following") //獲取following
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String login = "";
                String avatar_url = "";

                try {
                    JSONArray jsonArray = new JSONArray(body);
                    JSONObject jsonObject = null;
                    following_count = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        login = jsonObject.getString("login");
                        avatar_url = jsonObject.getString("avatar_url");

                        bin_following = new InputBean();
                        bin_following.setloging_following(login);
                        bin_following.settxt_avatar_following(avatar_url);
                        arrayList_following.add(bin_following);

                        following_list.add(login); //將該名稱加入arraylist中，用於之後檢查雙向追蹤

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //讀取follower list
    private void call_follower_API(String login) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(30, TimeUnit.SECONDS);    //限制寫入秒數
        builder.readTimeout(30, TimeUnit.SECONDS);     //限制讀取秒數
        builder.connectTimeout(30, TimeUnit.SECONDS);  //限制讀取秒數
        OkHttpClient client = builder.build();

        Request request = new Request.Builder()
                .url("https://api.github.com/users/" + login + "/followers") //獲取follower
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String login = "";
                String avatar_url = "";

                try {
                    JSONArray jsonArray = new JSONArray(body);
                    JSONObject jsonObject = null;
                    follower_count = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject = jsonArray.getJSONObject(i);
                        login = jsonObject.getString("login");
                        avatar_url = jsonObject.getString("avatar_url");

                        bin_follower = new InputBean();
                        bin_follower.setloging_follower(login);
                        bin_follower.settxt_avatar_follower(avatar_url);
                        arrayList_follower.add(bin_follower);

                        follower_list.add(login); //將該名稱加入arraylist中，用於之後檢查雙向追蹤

                    }
                    showRecycleview();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //顯示RecycleView
    private void showRecycleview() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //檢查雙向追蹤 S
                //檢查每個following名單有無在follower arraylist中
                for (int i = 0; i < arrayList_following.size(); i++) {
                    if(follower_list.contains(arrayList_following.get(i).getloging_following())) {
                        arrayList_following.get(i).setdouble_follow(true);
                    }
                }

                //檢查每個follower名單有無在following arraylist中
                for (int i = 0; i < arrayList_follower.size(); i++) {
                    if(following_list.contains(arrayList_follower.get(i).getloging_follower())) {
                        arrayList_follower.get(i).setdouble_follow(true);
                    }
                }
                //檢查雙向追蹤 E

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView_following.setAdapter(followingAdapter);
                        recyclerView_follower.setAdapter(followerAdapter);
                        tabLayout.addTab(tabLayout.newTab().setText("Following (" + following_count + ")"), 0);
                        tabLayout.addTab(tabLayout.newTab().setText("Follower (" + follower_count + ")"), 1);

                        progressDialogHelper.closeProgressDialog();
                    }
                }, 200);

            }

        });
    }


}