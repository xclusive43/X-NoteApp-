package com.xclusive.x_note.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.xclusive.x_note.MainActivity;
import com.xclusive.x_note.R;
import com.xclusive.x_note.model.Model_notes;
import java.util.ArrayList;


public class Adapter_notes extends RecyclerView.Adapter<Adapter_notes.ViewHolder> {
    private   ArrayList<Model_notes> model_notesList;
    private final Context context;
    public Adapter_notes(ArrayList<Model_notes> model_notesList, Context context) {
        this.model_notesList = model_notesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snoteitem_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(model_notesList.get(position).getTitle());
        holder.date.setText(model_notesList.get(position).getDates());
        holder.data.setText(model_notesList.get(position).getNotes());
        holder.id.setText(model_notesList.get(position).getId());

        holder.cardview_l.setCardBackgroundColor(Color.parseColor(model_notesList.get(position).getColor()));

        holder.itemView.setOnClickListener(V -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("TASK", 1);
            intent.putExtra("TITLE", model_notesList.get(position).getTitle());
            intent.putExtra("SUBTITLE", model_notesList.get(position).getSubtitle());
            intent.putExtra("NOTE", model_notesList.get(position).getNotes());
            intent.putExtra("DATE", model_notesList.get(position).getDates());
            intent.putExtra("COLOR", model_notesList.get(position).getColor());
            intent.putExtra("ID", model_notesList.get(position).getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return model_notesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, data, date, id;
        public CardView cardview_l;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_L);
            data = itemView.findViewById(R.id.notes_l);
            date = itemView.findViewById(R.id.date_l);
            id = itemView.findViewById(R.id.Idd);
            cardview_l = itemView.findViewById(R.id.cardview_l);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updatelist(ArrayList<Model_notes> newlist) {
        if (newlist.isEmpty()) {
            return;
        }
        model_notesList = new ArrayList<>();
        model_notesList.addAll(newlist);
        notifyDataSetChanged();
    }
}
