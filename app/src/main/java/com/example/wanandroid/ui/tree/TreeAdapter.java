package com.example.wanandroid.ui.tree;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.data.bean.TreeBean;

import java.util.ArrayList;
import java.util.List;

public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.ViewHolder> {

    private List<TreeBean> mData = new ArrayList<>();

    public void setNewData(List<TreeBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tree, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TreeBean treeBean = mData.get(position);

        // 设置一级分类名
        holder.tvTitle.setText(treeBean.getName());

        // 拼接二级分类名 (例如: "基础知识   Activity   Fragment")
        StringBuilder childrenBuilder = new StringBuilder();
        if (treeBean.getChildren() != null) {
            for (TreeBean child : treeBean.getChildren()) {
                childrenBuilder.append(child.getName()).append("   ");
            }
        }
        holder.tvChildren.setText(childrenBuilder.toString());
        // 2. 在 onBindViewHolder 中设置点击
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(treeBean);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvChildren;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvChildren = itemView.findViewById(R.id.tv_children);
        }
    }
    // 1. 定义接口
    public interface OnItemClickListener {
        void onItemClick(TreeBean treeBean);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener; }

}
