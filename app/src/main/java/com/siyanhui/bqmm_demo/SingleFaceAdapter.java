package com.siyanhui.bqmm_demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melink.baseframe.utils.DensityUtils;
import com.melink.baseframe.utils.StringUtils;
import com.melink.bqmmsdk.bean.Emoji;
import com.melink.bqmmsdk.sdk.BQMMSdk;
import com.melink.bqmmsdk.ui.store.EmojiDetail;
import com.thirdparty.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SingleFaceAdapter extends BaseAdapter{
    private ArrayList<SingleFaceModel> dataList;
    private LayoutInflater mInflater;
    private Context mContext;
    private BQMMSdk mBqmmSdk;

    public SingleFaceAdapter(Context mContext, BQMMSdk bqmmSdk, ArrayList<SingleFaceModel> data){
        this.dataList = data;
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mBqmmSdk = bqmmSdk;
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
        final Holder mHolder;
        if(view == null){
            view = mInflater.inflate(R.layout.item_single_face_layout, null);
            mHolder = new Holder();
            mHolder.contentTv = (TextView) view.findViewById(R.id.tv_item_content);
            mHolder.faceIv = (ImageView) view.findViewById(R.id.iv_item_emoji);
            view.setTag(mHolder);
        } else {
            mHolder = (Holder) view.getTag();
        }
        SingleFaceModel itemModel = dataList.get(i);
        Utils.showTextInfo(mContext, mHolder.contentTv, itemModel.content);
        if(itemModel.face != null){
            Glide.with(mContext).load(R.drawable.default_image).override(DensityUtils.dip2px(mContext, 50),
                    DensityUtils.dip2px(mContext, 50)).placeholder(R.drawable.default_image).into(mHolder.faceIv);
            mBqmmSdk.fetchEmojiByCode(mContext, itemModel.face.getEmoCode(), new BQMMSdk.IFetchEmojiByCodeCallback(){
                @Override
                public void onSuccess(final Emoji emoji) {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            // gif则按照gif展示，否则展示图片
                            if(emoji.getPathofImage() == null || emoji.getPathofImage().equals("")){//读网络上的
                                Glide.with(mContext).load(StringUtils.decodestr(emoji.getMainImage())).override(
                                        DensityUtils.dip2px(mContext, 50), DensityUtils.dip2px(mContext, 50)).placeholder(R.drawable.default_image).into(mHolder.faceIv);
                            }else{
                                Glide.with(mContext).load(emoji.getPathofImage()).override(
                                        DensityUtils.dip2px(mContext, 50), DensityUtils.dip2px(mContext, 50)).placeholder(R.drawable.default_image).into(mHolder.faceIv);
                            }
                            //添加点击事件，跳转只详情预览
                            mHolder.faceIv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(mContext, EmojiDetail.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Emoji_Detail", emoji);
                                    it.putExtras(bundle);
                                    mContext.startActivity(it);
                                }
                            });
                        }
                    });
                }
                @Override
                public void onError(Throwable arg0) {
                }
            });
        }
        return view;
    }

    private class Holder{
        public TextView contentTv;
        public ImageView faceIv;
    }
}
