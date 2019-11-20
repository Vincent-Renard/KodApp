package fr.univorleans.etu.renardez.kodapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

public class PositionListRecyclerViewAdapter extends RecyclerView.Adapter<PositionListRecyclerViewAdapter.ViewHolder> {
    private final List<PositionUser> positions;

    public PositionListRecyclerViewAdapter(List<PositionUser> positions) {
        this.positions = positions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_position_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(positions.get(position).getDetails());
    }

    @Override
    public int getItemCount() {
        return positions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_position_list_text);
        }
    }
}
