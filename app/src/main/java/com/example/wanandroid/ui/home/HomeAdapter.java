package com.example.wanandroid.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wanandroid.R;
import com.example.wanandroid.data.bean.Article;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<Article> mData = new ArrayList<>();

    // 定义点击事件和收藏事件的接口
    public interface OnItemClickListener {
        void onItemClick(Article article);
        void onCollectClick(int position, int articleId, boolean isCollect);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setNewData(List<Article> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<Article> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public List<Article> getData() {
        return mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = mData.get(position);

        holder.tvTitle.setText(article.getTitle());
        holder.tvAuthor.setText(article.getAuthor()); // 或者是 article.getShowAuthor()
        holder.tvDate.setText(article.getNiceDate());

        // 绑定收藏状态前先清空监听，防止复用导致的错乱
        holder.ivCollect.setOnCheckedChangeListener(null);
        holder.ivCollect.setChecked(article.isCollect());

        // 整个 Item 的点击事件 (跳转详情)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(article);
                }
            }
        });

        // 收藏按钮的点击事件
        holder.ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    // 将期望的状态回调给 Activity 处理
                    listener.onCollectClick(position, article.getId(), holder.ivCollect.isChecked());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvDate;
        CheckBox ivCollect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivCollect = itemView.findViewById(R.id.iv_collect);
        }
    }
}
