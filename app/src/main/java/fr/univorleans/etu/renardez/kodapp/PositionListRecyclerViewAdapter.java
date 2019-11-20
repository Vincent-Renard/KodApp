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
    private final PositionListFragment.OnPositionListItemClickListener listener;

    public PositionListRecyclerViewAdapter(List<PositionUser> positions, PositionListFragment.OnPositionListItemClickListener listener) {
        this.positions = positions;
        this.listener = listener;
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
        final PositionUser pos = positions.get(position);
        holder.textView.setText(pos.getDetails());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(pos);
            }
        });
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
