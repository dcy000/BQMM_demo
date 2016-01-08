package com.siyanhui.bqmm_demo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.melink.bqmmsdk.bean.Emoji;
import com.melink.bqmmsdk.sdk.BQMMSdk;
import com.melink.bqmmsdk.ui.keyboard.Bqmm_Keyboard;
import com.melink.bqmmsdk.widget.Bqmm_Editview;
import com.melink.bqmmsdk.widget.Bqmm_SendButton;

import java.util.List;


public class MultipleModeActivity extends FragmentActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener{
    private Bqmm_Editview mEditView;
    private Bqmm_Keyboard mKeyboard;
    private BQMMSdk bqmmSdk;
    private CheckBox handleCb;
    private InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_layout);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initBQSDK();
    }

    private void initView() {
        handleCb = (CheckBox) findViewById(R.id.cb_handle);
        handleCb.setOnCheckedChangeListener(this);
        mKeyboard = (Bqmm_Keyboard) findViewById(R.id.bqmm_keyboard);
        mEditView = (Bqmm_Editview) findViewById(R.id.et_input);
        mEditView.setOnClickListener(this);
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
                MultipleModel item = new MultipleModel();
                item.emojis = list;
                Intent result = new Intent();
                result.putExtra("data", item);
                setResult(100, result);
                finish();
            }

            @Override
            public void onSendFace(Emoji emoji) {

            }
        });
    }

    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.showSoftInput(mEditView, InputMethodManager.SHOW_IMPLICIT);
        }
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
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked){
            mKeyboard.setVisibility(View.VISIBLE);
            hideKeyboard();
        } else {
            mKeyboard.setVisibility(View.GONE);
            showKeyboard();
        }
    }
}
