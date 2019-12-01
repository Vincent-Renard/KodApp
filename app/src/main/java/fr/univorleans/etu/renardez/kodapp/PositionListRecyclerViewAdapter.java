package fr.univorleans.etu.renardez.kodapp;

import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemView.setBackgroundColor(
                (position % 2 == 0)
                ? Color.WHITE
                : Color.LTGRAY
        );
        final PositionUser pos = positions.get(position);
        holder.labelPos.setText(pos.getLabel());
        holder.detailsPos.setText(pos.getDetails());
        holder.datePos.setText(DateFormat.format(
            DateFormat.getBestDateTimePattern(Locale.getDefault(), "ddMMyyyy"),
            pos.getDate()
        ));
        holder.timePos.setText(DateFormat.format(
            DateFormat.getBestDateTimePattern(Locale.getDefault(), "HHmmss"),
            pos.getDate()
        ));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return positions.size();
    }

    public void deleteItem(int position) {
        positions.remove(position);
        notifyDataSetChanged();
        listener.onItemClick(-1, null);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView labelPos;
        public TextView detailsPos;
        public TextView datePos;
        public TextView timePos;

        public ViewHolder(View itemView) {
            super(itemView);
            labelPos = itemView.findViewById(R.id.item_position_list_label);
            detailsPos = itemView.findViewById(R.id.item_position_list_details);
            datePos = itemView.findViewById(R.id.item_position_list_date_date);
            timePos = itemView.findViewById(R.id.item_position_list_date_time);
        }
    }
}
