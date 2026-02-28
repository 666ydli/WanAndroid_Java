package com.example.wanandroid.ui.project;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.wanandroid.R;
import com.example.wanandroid.data.bean.Article;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private List<Article> mData = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(Article article);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener; }

    public void setNewData(List<Article> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<Article> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = mData.get(position);

        // 项目的标题和描述中有时会有 HTML 标签，使用 Html.fromHtml 去除标签
        holder.tvTitle.setText(Html.fromHtml(article.getTitle()));
        holder.tvDesc.setText(Html.fromHtml(article.getDesc()));
        holder.tvAuthor.setText(article.getAuthor());
        holder.tvDate.setText(article.getNiceDate());

        // 使用 Glide 加载网络图片，并设置圆角
        Glide.with(holder.itemView.getContext())
                .load(article.getEnvelopePic())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into(holder.ivCover);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(article);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle, tvDesc, tvAuthor, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
