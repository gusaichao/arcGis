package com.gsc.arcgis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gisinfo.android.core.base.util.ToastUtils;

public class KeepDataActivity extends AppCompatActivity implements View.OnClickListener {

    private DataBean dataBean;
    private DataBean infoBean;
    private EditText ed_name;
    private EditText ed_remake;
    private Button btn_submit;
    private Button btn_delete;
    private DaoUtils instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_data);
        ed_name = findViewById(R.id.ed_name);
        ed_remake = findViewById(R.id.ed_remake);
        btn_submit = findViewById(R.id.btn_submit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_submit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        Intent intent = getIntent();
        dataBean = (DataBean) intent.getSerializableExtra("data");
        infoBean = (DataBean) intent.getSerializableExtra("info");

        if (dataBean != null) {
            ed_name.setText(dataBean.getName());
            ed_remake.setText(dataBean.getRemark());
            btn_delete.setVisibility(View.VISIBLE);
        }
        instance = DaoUtils.getInstance(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (dataBean != null) {
                    instance.update(dataBean.getId() + "",
                            ed_name.getText().toString().trim(),
                            dataBean.getType(),
                            dataBean.getImage(),
                            dataBean.getPoint(),
                            ed_remake.getText().toString().trim());
                } else {
                    instance.add(ed_name.getText().toString().trim(),
                            infoBean.getType(),
                            "图片",
                            infoBean.getPoint(),
                            ed_remake.getText().toString().trim());
                }
                ToastUtils.showShortToast(this,"提交成功");
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btn_delete:
                instance.delete(dataBean.getId() + "");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}