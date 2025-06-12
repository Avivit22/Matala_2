package com.example.timetravel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PeriodAdapter extends RecyclerView.Adapter<PeriodAdapter.PeriodViewHolder> {

    private final List<PeriodItem> periods;
    private PeriodItem selected;

    public PeriodAdapter(List<PeriodItem> periods) {
        this.periods = periods;
    }

    @NonNull
    @Override
    public PeriodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_period, parent, false);
        return new PeriodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PeriodViewHolder holder, int position) {
        final PeriodItem item = periods.get(position);
        holder.title.setText(item.getTitle());
        holder.image.setImageResource(item.getImageRes());
        holder.radio.setChecked(item == selected);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(item);
            }
        });
        holder.radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(item);
            }
        });
    }

    private void setSelected(PeriodItem item) {
        if (selected != null) selected.setSelected(false);
        item.setSelected(true);
        selected = item;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }

    public PeriodItem getSelectedPeriod() {
        return selected;
    }

    static class PeriodViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        RadioButton radio;
        PeriodViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.periodImage);
            title = itemView.findViewById(R.id.periodTitle);
            radio = itemView.findViewById(R.id.periodRadio);
        }
    }
}
