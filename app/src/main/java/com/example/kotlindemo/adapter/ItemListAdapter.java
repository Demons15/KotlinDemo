// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.kotlindemo.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kotlindemo.ImageShowActivity;
import com.example.kotlindemo.R;
import com.example.kotlindemo.model.Item;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListAdapter extends RecyclerView.Adapter {
    List<Item> images;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new DebounceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final DebounceViewHolder debounceViewHolder = (DebounceViewHolder) holder;
        final Item image = images.get(position);
        Glide.with(holder.itemView.getContext()).load(image.imageUrl).into(debounceViewHolder.imageIv);
        debounceViewHolder.descriptionTv.setText(image.description);
        debounceViewHolder.imageArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(debounceViewHolder.imageArea.getContext(), ImageShowActivity.class);
                intent.putExtra("image", image.imageUrl);
                debounceViewHolder.imageArea.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public void setItems(List<Item> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    static class DebounceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_area)
        View imageArea;
        @BindView(R.id.imageIv)
        ImageView imageIv;
        @BindView(R.id.descriptionTv)
        TextView descriptionTv;

        public DebounceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
