package com.example.snaphelpertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SnapHelperAdapter extends RecyclerView.Adapter<SnapHelperAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<TestBean> mData = new ArrayList<>();

    public SnapHelperAdapter(Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        for (int i = 0; i < 20; i++){
            mData.add(new TestBean(i, "Item：" + i));
        }
    }

    @NonNull
    @Override
    public SnapHelperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_snap_helper, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestBean bean = mData.get(position);
        holder.tv.setText(bean.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}
