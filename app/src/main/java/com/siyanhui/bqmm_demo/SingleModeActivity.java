package com.siyanhui.bqmm_demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.melink.baseframe.utils.DensityUtils;
import com.melink.baseframe.utils.StringUtils;
import com.melink.bqmmsdk.bean.Emoji;
import com.melink.bqmmsdk.sdk.BQMMSdk;
import com.melink.bqmmsdk.ui.keyboard.Bqmm_Keyboard;
import com.melink.bqmmsdk.ui.store.EmojiDetail;
import com.melink.bqmmsdk.widget.Bqmm_Editview;
import com.melink.bqmmsdk.widget.Bqmm_SendButton;
import com.thirdparty.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SingleModeActivity extends FragmentActivity implements View.OnClickListener{
    private Bqmm_Editview mEditView;
    private Bqmm_Keyboard mKeyboard;
    private BQMMSdk bqmmSdk;
    private InputMethodManager manager;
    private ArrayList<SingleFaceModel> dataList = new ArrayList<>();
    private SingleFaceAdapter mAdapter;
    private ImageView mPreviewIv;
    private Emoji currentEmoji;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_layout);
        mContext = this;
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initBQSDK();
        mAdapter = new SingleFaceAdapter(this, bqmmSdk, dataList);
        ((ListView)findViewById(R.id.lv_content)).setAdapter(mAdapter);
    }

    private void initView() {
        mKeyboard = (Bqmm_Keyboard) findViewById(R.id.bqmm_keyboard);
        mEditView = (Bqmm_Editview) findViewById(R.id.et_input);
        mEditView.setOnClickListener(this);
        mPreviewIv = (ImageView) findViewById(R.id.iv_preview);
        mPreviewIv.setOnClickListener(this);
    }

    private void initBQSDK(){
        bqmmSdk = BQMMSdk.getInstance();
        bqmmSdk.setSendButton((Bqmm_SendButton) findViewById(R.id.btn_send));
        bqmmSdk.setKeyboard(mKeyboard);
        bqmmSdk.setEditView(mEditView);
        bqmmSdk.load();
        bqmmSdk.setBqmmSendMsgListener(new Bqmm_SendButton.IBqmmSendMessageListener() {
            @Override
            public void onSendEmoji(List<Object> list) {
                SingleFaceModel item = new SingleFaceModel();
                item.content = list;
                item.face = currentEmoji;
                dataList.add(item);
                mAdapter.notifyDataSetChanged();
                currentEmoji = null;
                mPreviewIv.setImageResource(R.drawable.default_image);
            }

            @Override
            public void onSendFace(Emoji emoji) {
                handleFaceClick(emoji);
            }
        });
    }

    private void handleFaceClick(Emoji emoji){
        bqmmSdk.fetchEmojiByCode(this, emoji.getEmoCode(), new BQMMSdk.IFetchEmojiByCodeCallback(){
            @Override
            public void onSuccess(final Emoji emoji) {
                currentEmoji = emoji;
                SingleModeActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (emoji.getPathofImage() == null || emoji.getPathofImage().equals("")) {
                            Glide.with(mContext).load(StringUtils.decodestr(emoji.getMainImage())).override(
                                    DensityUtils.dip2px(mContext, 50), DensityUtils.dip2px(mContext, 50)).placeholder(R.drawable.default_image).into(mPreviewIv);
                        } else {
                            Glide.with(mContext).load(emoji.getPathofImage()).override(
                                    DensityUtils.dip2px(mContext, 50), DensityUtils.dip2px(mContext, 50)).placeholder(R.drawable.default_image).into(mPreviewIv);
                        }
                    }
                });
            }
            @Override
            public void onError(Throwable arg0) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bqmmSdk.destory();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_input:
                mKeyboard.setVisibility(View.GONE);
                break;
            case R.id.iv_preview:
                hideKeyboard();
                mKeyboard.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
