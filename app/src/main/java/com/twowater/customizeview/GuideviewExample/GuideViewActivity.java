package com.twowater.customizeview.GuideviewExample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.twowater.customizeview.R;
import com.twowater.guideview.SpringIndicator;

import java.util.ArrayList;

public class GuideViewActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SpringIndicator springIndicator;
    private String[] titles = {"1", "2", "3", "4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_view);
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        springIndicator = (SpringIndicator) findViewById(R.id.indicator);
        ArrayList fragList = new ArrayList<Fragment>();
        fragList.add(new MyFragment(R.drawable.girl1));
        fragList.add(new MyFragment(R.drawable.girl2));
        fragList.add(new MyFragment(R.drawable.girl3));
        fragList.add(new MyFragment(R.drawable.girl4));
        ArrayList list = new ArrayList<String>();
        for (String str : titles) {
            list.add(str);
        }
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager(), fragList, list);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        springIndicator.setViewPager(viewPager);

    }

}
