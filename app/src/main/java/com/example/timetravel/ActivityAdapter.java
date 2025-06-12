package com.example.timetravel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private final List<String> activities;
    private final List<String> selected = new ArrayList<>();

    public ActivityAdapter(List<String> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        final String activity = activities.get(position);
        holder.name.setText(activity);
        holder.checkBox.setChecked(selected.contains(activity));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    if (!selected.contains(activity)) selected.add(activity);
                } else {
                    selected.remove(activity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public List<String> getSelectedActivities() {
        return new ArrayList<>(selected);
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CheckBox checkBox;
        ActivityViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.activityName);
            checkBox = itemView.findViewById(R.id.activityCheck);
        }
    }
}
