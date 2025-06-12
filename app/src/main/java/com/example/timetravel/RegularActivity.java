package com.example.timetravel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegularActivity extends AppCompatActivity implements PrioritizeDialogFragment.PrioritizeListener {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private RecyclerView activitiesRecycler;
    private RecyclerView periodRecycler;
    private ActivityAdapter activityAdapter;
    private PeriodAdapter periodAdapter;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular);

        prefs = getSharedPreferences("travel_prefs", MODE_PRIVATE);

        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        activitiesRecycler = findViewById(R.id.activitiesRecycler);
        activitiesRecycler.setLayoutManager(new LinearLayoutManager(this));
        activityAdapter = new ActivityAdapter(getActivities());
        activitiesRecycler.setAdapter(activityAdapter);

        periodRecycler = findViewById(R.id.periodRecycler);
        periodRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        periodAdapter = new PeriodAdapter(getPeriods());
        periodRecycler.setAdapter(periodAdapter);

        Button btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirm();
            }
        });

        loadSavedDateTime();
    }

    private void loadSavedDateTime() {
        long time = prefs.getLong("datetime", -1);
        if (time != -1) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            datePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            timePicker.setHour(c.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(c.get(Calendar.MINUTE));
        }
    }

    private void saveDateTime(long time) {
        prefs.edit().putLong("datetime", time).apply();
    }

    private List<String> getActivities() {
        List<String> data = new ArrayList<>();
        data.add("הכרזת העצמאות");
        data.add("הקמת המדינה");
        data.add("המהפכה הצרפתית");
        data.add("נפילת החומה");
        return data;
    }

    private List<PeriodItem> getPeriods() {
        List<PeriodItem> data = new ArrayList<>();
        data.add(new PeriodItem(R.drawable.ancient_egypt, "מצרים העתיקה"));
        data.add(new PeriodItem(R.drawable.middle_ages, "אירופה בימי הביניים"));
        data.add(new PeriodItem(R.drawable.renaissance, "הרנסנס"));
        data.add(new PeriodItem(R.drawable.modern, "העת החדשה"));
        return data;
    }

    private void onConfirm() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, datePicker.getYear());
        c.set(Calendar.MONTH, datePicker.getMonth());
        c.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        c.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        c.set(Calendar.MINUTE, timePicker.getMinute());

        saveDateTime(c.getTimeInMillis());

        final List<String> selectedActivities = activityAdapter.getSelectedActivities();
        final PeriodItem selectedPeriod = periodAdapter.getSelectedPeriod();

        if (selectedActivities.size() > 1) {
            DialogFragment dialog = PrioritizeDialogFragment.newInstance(selectedActivities);
            dialog.show(getSupportFragmentManager(), "prio");
            return;
        }

        showConfirmationDialog(selectedActivities, selectedPeriod, c);
    }

    @Override
    public void onPrioritiesChosen(List<String> prioritized) {
        PeriodItem period = periodAdapter.getSelectedPeriod();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, datePicker.getYear());
        c.set(Calendar.MONTH, datePicker.getMonth());
        c.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        c.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        c.set(Calendar.MINUTE, timePicker.getMinute());
        showConfirmationDialog(prioritized, period, c);
    }

    private void showConfirmationDialog(final List<String> activities, final PeriodItem period, final Calendar c) {
        StringBuilder msg = new StringBuilder();
        msg.append("תאריך: ").append(c.get(Calendar.DAY_OF_MONTH)).append("/")
                .append(c.get(Calendar.MONTH)+1).append("/").append(c.get(Calendar.YEAR)).append("\n");
        msg.append("שעה: ").append(String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))).append("\n");
        msg.append("פעילויות: ").append(activities.toString()).append("\n");
        if (period != null) {
            msg.append("תקופה: ").append(period.getTitle());
        }

        new AlertDialog.Builder(this)
                .setTitle("אישור מסע בזמן")
                .setMessage(msg.toString())
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openSummaryFragment(c, activities, period);
                    }
                })
                .setNegativeButton("ביטול", null)
                .show();
    }

    private void openSummaryFragment(Calendar c, List<String> activities, PeriodItem period) {
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, TravelSummaryFragment.newInstance(c.getTimeInMillis(), activities, period))
                .addToBackStack(null)
                .commit();
    }
}
