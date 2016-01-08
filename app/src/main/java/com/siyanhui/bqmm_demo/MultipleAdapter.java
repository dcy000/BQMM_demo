package com.siyanhui.bqmm_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MultipleAdapter extends BaseAdapter{
    private ArrayList<MultipleModel> dataList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MultipleAdapter(Context context, ArrayList<MultipleModel> dataList){
        mInflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder mHolder;
        if(view == null){
            view = mInflater.inflate(R.layout.item_multiple_layout, null);
            mHolder = new Holder();
            mHolder.contentTv = (TextView) view.findViewById(R.id.tv_item_content);
            view.setTag(mHolder);
        } else {
            mHolder = (Holder) view.getTag();
        }
        Utils.showTextInfo(mContext, mHolder.contentTv, dataList.get(i).emojis);
        return view;
    }

    private class Holder{
        public TextView contentTv;
    }
}
