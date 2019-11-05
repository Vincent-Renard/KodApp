package fr.univorleans.etu.renardez.kodapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

class PositionUserListRecyclerViewAdapter extends RecyclerView.Adapter<PositionUserListRecyclerViewAdapter.ViewHolder> {

    private final List<PositionUser> losPos;
    private final PositionsFragment.OnListFragmentInteractionListener elListener;

    PositionUserListRecyclerViewAdapter(List<PositionUser> losPos, PositionsFragment.OnListFragmentInteractionListener elListener) {
        this.losPos = losPos;
        this.elListener = elListener;
    }

    @NonNull
    @Override
    public PositionUserListRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PositionUserListRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return losPos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //  public final TextView mIdView;
        public final TextView mContentView;
        public PositionUser mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            // mIdView = (TextView) view.findViewById(R.id.id_item);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
