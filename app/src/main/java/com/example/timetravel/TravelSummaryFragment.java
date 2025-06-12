package com.example.timetravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TravelSummaryFragment extends Fragment {

    private static final String ARG_TIME = "time";
    private static final String ARG_ACTS = "acts";
    private static final String ARG_PERIOD = "period";

    public static TravelSummaryFragment newInstance(long time, List<String> acts, PeriodItem period) {
        TravelSummaryFragment f = new TravelSummaryFragment();
        Bundle b = new Bundle();
        b.putLong(ARG_TIME, time);
        b.putStringArrayList(ARG_ACTS, new java.util.ArrayList<>(acts));
        if (period != null)
            b.putString(ARG_PERIOD, period.getTitle());
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_travel_summary, container, false);
        TextView summaryText = v.findViewById(R.id.summaryText);
        Button shareBtn = v.findViewById(R.id.btnShare);
        Button resetBtn = v.findViewById(R.id.btnReset);

        long time = getArguments().getLong(ARG_TIME);
        List<String> acts = getArguments().getStringArrayList(ARG_ACTS);
        String period = getArguments().getString(ARG_PERIOD, "");

        DatabaseHelper db = new DatabaseHelper(requireContext());
        db.insertHistory(time, acts, period);

        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date d = new Date(time);
        String text = "תאריך: " + dfDate.format(d) + "\n" +
                "שעה: " + dfTime.format(d) + "\n" +
                "פעילויות: " + acts.toString() + "\n" +
                "תקופה: " + period;
        summaryText.setText(text);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send = new Intent(Intent.ACTION_SEND);
                send.setType("text/plain");
                send.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(send, "שיתוף"));
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSharedPreferences("travel_prefs", 0).edit().clear().apply();
                requireActivity().finish();
            }
        });

        return v;
    }
}
