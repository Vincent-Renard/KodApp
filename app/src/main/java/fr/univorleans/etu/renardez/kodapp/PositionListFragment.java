package fr.univorleans.etu.renardez.kodapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.univorleans.etu.renardez.kodapp.db.Frigo;
import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

public class PositionListFragment extends Fragment {
    private Frigo base;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_position_list, container, false);

        recyclerView = view.findViewById(R.id.position_list_recycler_view);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        base = Frigo.getInstance(view.getContext());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                adapter = new PositionListRecyclerViewAdapter(base.positionUserDao().getAllPU(), new OnPositionListItemClickListener() {
                    @Override
                    public void onItemClick(PositionUser position) {
                        Toast.makeText(view.getContext(), position.getDetails(), Toast.LENGTH_LONG).show();
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }

    public interface OnPositionListItemClickListener {
        void onItemClick(PositionUser position);
    }
}
