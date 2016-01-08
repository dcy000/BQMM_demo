package com.siyanhui.bqmm_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MultipleShowActivity extends Activity implements View.OnClickListener{
    private ArrayList<MultipleModel> dataList = new ArrayList<>();
    private MultipleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_show_layout);
        findViewById(R.id.btn_send).setOnClickListener(this);
        mAdapter = new MultipleAdapter(this, dataList);
        ((ListView)findViewById(R.id.lv_show)).setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                startActivityForResult(new Intent(this, MultipleModeActivity.class), 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 100:
                if(data == null){
                    return;
                }
                MultipleModel itemModel = (MultipleModel)data.getSerializableExtra("data");
                dataList.add(itemModel);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
