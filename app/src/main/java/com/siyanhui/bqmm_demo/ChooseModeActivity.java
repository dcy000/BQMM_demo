package com.siyanhui.bqmm_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseModeActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode_layout);
        findViewById(R.id.btn_single).setOnClickListener(this);
        findViewById(R.id.btn_multiple).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_single:
                startActivity(new Intent(this, SingleModeActivity.class));
                break;
            case R.id.btn_multiple:
                startActivity(new Intent(this, MultipleShowActivity.class));
                break;
        }
    }
}
