package com.example.timetravel;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PrioritizeDialogFragment extends DialogFragment {

    public interface PrioritizeListener {
        void onPrioritiesChosen(List<String> prioritized);
    }

    private static final String ARG_ACTIVITIES = "acts";
    private List<String> activities;
    private PrioritizeAdapter adapter;

    public static PrioritizeDialogFragment newInstance(ArrayList<String> acts) {
        PrioritizeDialogFragment f = new PrioritizeDialogFragment();
        Bundle b = new Bundle();
        b.putStringArrayList(ARG_ACTIVITIES, acts);
        f.setArguments(b);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        activities = getArguments().getStringArrayList(ARG_ACTIVITIES);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_prioritize, null);
        RecyclerView recycler = v.findViewById(R.id.prioritizeRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PrioritizeAdapter(activities);
        recycler.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("סדר פעילויות מועדפות");
        builder.setView(v);
        builder.setPositiveButton("אישור", null);
        builder.setNegativeButton("ביטול", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button b = dialog.getButton(Dialog.BUTTON_POSITIVE);
            b.setOnClickListener(view -> {
                if (getParentFragment() instanceof PrioritizeListener) {
                    ((PrioritizeListener) getParentFragment()).onPrioritiesChosen(adapter.getPrioritized());
                } else if (getActivity() instanceof PrioritizeListener) {
                    ((PrioritizeListener) getActivity()).onPrioritiesChosen(adapter.getPrioritized());
                }
                dismiss();
            });
        });
        return dialog;
    }
}
