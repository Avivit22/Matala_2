package com.example.timetravel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrioritizeAdapter extends RecyclerView.Adapter<PrioritizeAdapter.PrioritizeViewHolder> {

    private final List<String> activities;
    private final Map<String, Integer> priorities = new HashMap<>();

    public PrioritizeAdapter(List<String> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public PrioritizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prioritize, parent, false);
        return new PrioritizeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PrioritizeViewHolder holder, int position) {
        final String act = activities.get(position);
        holder.name.setText(act);
        int prio = priorities.containsKey(act) ? priorities.get(act) : -1;
        if (prio != -1) {
            ((RadioButton) holder.group.getChildAt(prio-1)).setChecked(true);
        } else {
            holder.group.clearCheck();
        }
        holder.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View rb = group.findViewById(checkedId);
                int idx = group.indexOfChild(rb) + 1;
                priorities.put(act, idx);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public List<String> getPrioritized() {
        List<String> res = new ArrayList<>(activities);
        res.sort((a,b) -> {
            int pa = priorities.getOrDefault(a, 4);
            int pb = priorities.getOrDefault(b, 4);
            return Integer.compare(pa, pb);
        });
        return res.subList(0, Math.min(3, res.size()));
    }

    static class PrioritizeViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RadioGroup group;
        PrioritizeViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.prioName);
            group = itemView.findViewById(R.id.prioGroup);
        }
    }
}
