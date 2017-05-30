package com.twowater.customizeview.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twowater.customizeview.R;

import java.util.List;

public class MainRecycleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MainRecycleAdapter(List<String> lsit) {
        super(R.layout.item_main_recyclerview, lsit);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.button, item)
                .addOnClickListener(R.id.button);
    }

}
