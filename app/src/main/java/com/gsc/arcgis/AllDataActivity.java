package com.gsc.arcgis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.gisinfo.android.lib.base.BaseActivity;
import com.gsc.arcgis.databinding.ActivityMainBinding;

import java.util.List;

public class AllDataActivity extends BaseActivity implements DataAdapter.ItemClick{

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private DaoUtils daoUtils;
    private List<DataBean> select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_data);
        recyclerView = findViewById(R.id.recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        dataAdapter = new DataAdapter(this);
        dataAdapter.setItemClick(this);
        recyclerView.setAdapter(dataAdapter);
        daoUtils = DaoUtils.getInstance(this);
        select = daoUtils.select();
        dataAdapter.setList(select);
    }

    @Override
    public void onItemClickListener(DataBean bean) {
        Intent intent = new Intent(this,KeepDataActivity.class);
        intent.putExtra("data",bean);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==100){
            select = daoUtils.select();
            dataAdapter.setList(select);
        }
    }
}