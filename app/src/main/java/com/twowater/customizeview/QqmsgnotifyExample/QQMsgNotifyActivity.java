package com.twowater.customizeview.QqmsgnotifyExample;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.twowater.customizeview.R;
import com.twowater.qqmsgnotify.GooViewListener;

public class QQMsgNotifyActivity extends AppCompatActivity {

    private TextView mTvPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_msg_notify);
        initView();
    }

    private void initView() {
        mTvPoint = (TextView) findViewById(R.id.point_conversation);
        mTvPoint.setText("10");
        mTvPoint.setTag(10);
        GooViewListener listener = new GooViewListener(this, mTvPoint) {
            @Override
            public void onDisappear(PointF mDragCenter) {
                super.onDisappear(mDragCenter);
                Toast.makeText(QQMsgNotifyActivity.this, "消失了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReset(boolean isOutOfRange) {
                super.onReset(isOutOfRange);
                Toast.makeText(QQMsgNotifyActivity.this, "重置了", Toast.LENGTH_SHORT).show();
            }
        };
        mTvPoint.setOnTouchListener(listener);
    }

}
