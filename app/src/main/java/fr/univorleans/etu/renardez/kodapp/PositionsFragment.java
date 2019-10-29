package fr.univorleans.etu.renardez.kodapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

public class PositionsFragment extends Fragment {
    private List<PositionUser> positionUserList;
    private OnListFragmentInteractionListener positionUserListeListener;
    private PositionUserListRecyclerViewAdapter pulRecyclerViewAdapter;

    public PositionsFragment() {
        this.positionUserList = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        //View view = inflater.inflate(R.layout.pu_liste_frag, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (positionUserList == null) {
                positionUserList = new ArrayList<>();
            }
            if (pulRecyclerViewAdapter == null)
                pulRecyclerViewAdapter = new PositionUserListRecyclerViewAdapter(positionUserList, positionUserListeListener);
            recyclerView.setAdapter(pulRecyclerViewAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            positionUserListeListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(this.getClass().getName(), "OnDetach");
        positionUserListeListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(PositionUser pos);
    }
}
