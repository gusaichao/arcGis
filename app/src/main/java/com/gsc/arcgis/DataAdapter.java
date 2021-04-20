package com.gsc.arcgis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyVh> {
    private Context context;
    private List<DataBean> list;

    public DataAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_data, parent, false);
        return new MyVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVh holder, int position) {
        holder.tv_name.setText("名称：" + list.get(position).getName());
        holder.tv_remake.setText("图片：" + list.get(position).getImage());
        holder.tv_type.setText("类型：" + list.get(position).getType() + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemClickListener(list.get(position));
            }
        });
    }

    private ItemClick itemClick;

    public interface ItemClick {
        void onItemClickListener(DataBean bean);
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyVh extends RecyclerView.ViewHolder {

        private final TextView tv_name, tv_remake, tv_type;

        public MyVh(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_remake = itemView.findViewById(R.id.tv_remake);
            tv_type = itemView.findViewById(R.id.tv_type);
        }
    }
}
