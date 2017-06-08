package com.twowater.customizeview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.twowater.customizeview.BeziercurvesExample.BezierCurveActivity;
import com.twowater.customizeview.GuideviewExample.GuideViewActivity;
import com.twowater.customizeview.QqmsgnotifyExample.QQMsgNotifyActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] arry = {"展示贝塞尔曲线", "QQ消息提示小红点", "ViewPage滑动引导动画"};
    private ArrayList<String> list = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private MainRecycleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (String str : arry) {
            list.add(str);
        }
        mAdapter = new MainRecycleAdapter(list);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent;
                switch (list.get(position)) {
                    case "展示贝塞尔曲线":
                        intent = new Intent(MainActivity.this, BezierCurveActivity.class);
                        startActivity(intent);
                        break;
                    case "QQ消息提示小红点":
                        intent = new Intent(MainActivity.this, QQMsgNotifyActivity.class);
                        startActivity(intent);
                        break;
                    case "ViewPage滑动引导动画":
                        intent = new Intent(MainActivity.this, GuideViewActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
    }
}
