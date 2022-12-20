package com.xclusive.x_note.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.xclusive.x_note.R;
import com.xclusive.x_note.model.ColorModel;

import java.util.List;
import java.util.PrimitiveIterator;

import static com.xclusive.x_note.MainActivity.colordialog;
import static com.xclusive.x_note.MainActivity.editlayout;
import static com.xclusive.x_note.MainActivity.color;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    List<ColorModel>colorModelList;

    public ColorAdapter(List<ColorModel> colorModelList) {
        this.colorModelList = colorModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cardView.setCardBackgroundColor(Color.parseColor(colorModelList.get(position).getColor()));

        holder.itemView.setOnClickListener(V->{
            editlayout.setBackgroundColor(Color.parseColor(colorModelList.get(position).getColor()));
            color = colorModelList.get(position).getColor();
            colordialog.dismiss();
        });
    }


    @Override
    public int getItemCount() {
        return colorModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            MaterialCardView        cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.colorview);
        }
    }
}
