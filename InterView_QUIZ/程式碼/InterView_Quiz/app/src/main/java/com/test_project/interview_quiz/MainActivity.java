package com.test_project.interview_quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.test_project.interview_quiz.Adapter.RecycleviewAdapter;
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

public class MainActivity extends AppCompatActivity {

    ImageView img1;
    String TAG = "Wei_MainActivity";

    //recycleview用
    InputBean bin = null;
    private RecyclerView recyclerView;
    ArrayList<InputBean> arrayList = new ArrayList<>();

    private SharedPreferences share_prefer = null;

    int entry_count = 1;

    protected ProgressDialogHelper progressDialogHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialogHelper = new ProgressDialogHelper(this);

        initUI();
    }

    private void initUI() {

        share_prefer = getSharedPreferences("favor_user", MODE_PRIVATE);

        recyclerView = findViewById(R.id.recycleview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        img1 = findViewById(R.id.img1);

        callListAPI();

    }

    //獲取使用者列表 (list)
    private void callListAPI() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(30, TimeUnit.SECONDS);    //限制寫入秒數
        builder.readTimeout(30, TimeUnit.SECONDS);     //限制讀取秒數
        builder.connectTimeout(30, TimeUnit.SECONDS);  //限制讀取秒數
        OkHttpClient client = builder.build();

        Request request = new Request.Builder()
                .url("https://api.github.com/users") //獲取list
                .build();

        showProgressDialog("Loading user lists...");

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
                    arrayList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        showProgressDialog("Loading user lists...");

                        jsonObject = jsonArray.getJSONObject(i);
                        login = jsonObject.getString("login");
                        avatar_url = jsonObject.getString("avatar_url");

                        bin = new InputBean();
                        bin.setlogin(login);
                        bin.settxt_avatar(avatar_url);

                        if (share_prefer.getString("User_List", "").contains(login)) {
                            bin.setfavor_status(true);
                        }

                        arrayList.add(bin);

                    }

                    closeProgressDialog();
                    showRecycleView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //返回第一頁時重新刷新API (載入最愛)
    @Override
    protected void onResume() {
        entry_count++;
        if (entry_count > 2) {
            callListAPI();
        }
        super.onResume();
    }

    private void showRecycleView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new RecycleviewAdapter(arrayList, new RecycleviewAdapter.OnClickListener() {
                    @Override
                    public void onClick(int pos, InputBean bin) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, SecondActivity.class);
                        intent.putExtra("login", bin.getlogin());
                        startActivity(intent);
                    }
                }, share_prefer));
            }
        });
    }

    private void showProgressDialog(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.showProgressDialog(msg, false);
            }
        });
    }

    private void closeProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.closeProgressDialog();
            }
        });
    }

}