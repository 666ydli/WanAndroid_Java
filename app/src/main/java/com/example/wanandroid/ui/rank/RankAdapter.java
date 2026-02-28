package com.example.wanandroid.ui.rank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.data.bean.CoinInfoBean;

import java.util.ArrayList;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<CoinInfoBean> mData = new ArrayList<>();

    public void setNewData(List<CoinInfoBean> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<CoinInfoBean> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinInfoBean bean = mData.get(position);
        holder.tvRank.setText(String.valueOf(position + 1)); // 简单起见，直接用列表索引+1作为排名
        // 或者使用服务端返回的 rank 字段: holder.tvRank.setText(bean.getRank());

        holder.tvUsername.setText(bean.getUsername());
        holder.tvCoin.setText(String.valueOf(bean.getCoinCount()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvUsername, tvCoin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvCoin = itemView.findViewById(R.id.tv_coin);
        }
    }
}
