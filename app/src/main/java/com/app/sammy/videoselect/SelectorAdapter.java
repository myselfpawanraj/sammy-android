package com.app.sammy.videoselect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sammy.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;

public class SelectorAdapter extends RecyclerView.Adapter< SelectorAdapter.ViewHolder > {
    List< SelectorModel > modelList;
    Context context;
    Contract contract;

    public SelectorAdapter(Context context, List< SelectorModel > modelList) {
        this.context = context;
        this.modelList = modelList;
        this.contract = (Contract) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelectorModel model = modelList.get(position);
        Glide
                .with(context)
                .load(model.getImageUrl() + " ")
                .placeholder(R.drawable.music_disk)
                .into(holder.imageView);

        holder.title.setText(model.getTitle());
        holder.main_card.setOnClickListener(v -> contract.uploadData(model.url));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView main_card;
        ImageView imageView;
        TextView title;
        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView8);
            title = view.findViewById(R.id.textView14);
            main_card = view.findViewById(R.id.main_card);
            ButterKnife.bind(view);
        }
    }
}
