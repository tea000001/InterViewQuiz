package com.test_project.interview_quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test_project.interview_quiz.Helper.ProgressDialogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "Wei_SecondActivity";

    String login = "";
    String bio = "";
    String name = "";
    String location = "";
    String blog = "";
    String avatar_url = "";

    ImageView img_avatar;
    TextView txt_name;
    TextView txt_login;
    TextView txt_location;
    TextView txt_blog;
    TextView txt_bio;

    Button btn_star;

    Drawable white_star;
    Drawable yellow_star;

    private SharedPreferences share_prefer = null;
    ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        login = getIntent().getExtras().getString("login");

        progressDialogHelper = new ProgressDialogHelper(this);
        progressDialogHelper.showProgressDialog("Loading user....", false);
        initUI();
    }

    private void initUI() {

        share_prefer = getSharedPreferences("favor_user", MODE_PRIVATE);

        img_avatar = findViewById(R.id.img_avatar);
        txt_name = findViewById(R.id.txt_name);
        txt_login = findViewById(R.id.txt_login);
        txt_location = findViewById(R.id.txt_location);
        txt_bio = findViewById(R.id.txt_bio);

        Button btn_back = findViewById(R.id.btn_back); //關閉按鍵 (返回)
        btn_back.setOnClickListener(this);

        btn_star = findViewById(R.id.btn_star); //最愛使用者開關
        btn_star.setOnClickListener(this);

        Button btn_follow = findViewById(R.id.btn_follow); //顯示追隨者
        btn_follow.setOnClickListener(this);

        TextView txt_follow = findViewById(R.id.txt_follow); //顯示追隨者
        txt_follow.setOnClickListener(this);

        txt_blog = findViewById(R.id.txt_blog);
        txt_blog.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //增加下底線
        txt_blog.setOnClickListener(this); //blog超連結

        white_star = AppCompatResources.getDrawable(this, R.drawable.baseline_star_border_black_24dp);
        yellow_star = AppCompatResources.getDrawable(this, R.drawable.baseline_star_border_yellow_24dp);

        call_detail_API(login);
    }

    //利用gitHub login(ID) 抓取 detail
    private void call_detail_API(String login) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(30, TimeUnit.SECONDS);    //限制寫入秒數
        builder.readTimeout(30, TimeUnit.SECONDS);     //限制讀取秒數
        builder.connectTimeout(30, TimeUnit.SECONDS);  //限制讀取秒數
        OkHttpClient client = builder.build();

        Request request = new Request.Builder()
                .url("https://api.github.com/users/" + login) //獲取list
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(body);
                    avatar_url = jsonObject.getString("avatar_url");
                    bio = jsonObject.getString("bio");
                    name = jsonObject.getString("name");
                    location = jsonObject.getString("location");
                    blog = jsonObject.getString("blog");

                    showUI();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //讀取API後，顯示畫面
    private void showUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(SecondActivity.this)
                        .load(avatar_url)
                        .into(img_avatar);

                if (location.equals("null")) {
                    location = "User not configured";
                }
                if (!bio.equals("null")) {
                    txt_bio.setText(bio);
                }

                txt_name.setText(name);
                txt_login.setText(login);
                txt_location.setText(location);
                txt_blog.setText(blog);
                checkFavorite(login);
                progressDialogHelper.closeProgressDialog();
            }

        });
    }


    //查詢是否為最愛用戶
    private void checkFavorite(String login) {
        if (share_prefer.getString("User_List", "").contains(login)) {
            btn_star.setBackground(yellow_star);
        } else {
            btn_star.setBackground(white_star);
        }
    }

    //註冊全部的onclick事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:  //關閉按鍵
                finish();
                break;

            case R.id.txt_blog:  //點選blog超連結
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(blog));
                startActivity(intent);
                break;

            case R.id.btn_star: //最愛使用者

                Resources resources = view.getContext().getResources();
                Drawable white_star = resources.getDrawable(R.drawable.baseline_star_border_black_24dp);
                Drawable yellow_star = resources.getDrawable(R.drawable.baseline_star_border_yellow_24dp);

                String User_List = share_prefer.getString("User_List", "");

                if (User_List.contains(login)) //該使用者已為最愛 點選 變為取消設最愛
                {
                    share_prefer.edit().putString("User_List", User_List.replace(login, "")).apply();
                    btn_star.setBackground(white_star);
                } else {                       //該使用者非最愛 點選 設最愛
                    share_prefer.edit().putString("User_List", User_List + login).apply();
                    btn_star.setBackground(yellow_star);
                }
                break;

            case R.id.btn_follow: //顯示追隨者
            case R.id.txt_follow:
                Intent intent2 = new Intent();
                intent2.setClass(SecondActivity.this, FollowActivity.class);
                intent2.putExtra("login", login);
                startActivity(intent2);
                break;

        }
    }


}