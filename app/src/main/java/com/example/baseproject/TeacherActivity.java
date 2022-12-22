package com.example.baseproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends BaseActivity implements TimeListener {

    TextView time;
    TextView status;
    TextView subject;
    TextView cabinet;
    TextView corpus;
    TextView teacher;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        spinner = findViewById(R.id.groupList);

        List<Group> groups = new ArrayList<>();
        InitGroups(groups);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItem, int selectedItemPos, long selectedId) {
                Object item = adapter.getItem(selectedItemPos);
                Log.d(TAG, "selected item: " + item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        time = findViewById(R.id.time);

        status = findViewById(R.id.status);
        subject = findViewById(R.id.subject);
        cabinet = findViewById(R.id.cabinet);
        corpus = findViewById(R.id.corpus);
        teacher = findViewById(R.id.teacher);

        View scheduleDayButton = findViewById(R.id.timetable_for_day_button);
        scheduleDayButton.setOnClickListener(v -> showSchedule(ScheduleType.DAY));
        View scheduleWeekButton = findViewById(R.id.timetable_for_week_button);
        scheduleWeekButton.setOnClickListener(v -> showSchedule(ScheduleType.WEEK));

        InitTime();
        InitData();
    }

    private void InitTime() {
        addListener(this);
        initTime();
    }

    private void showSchedule(ScheduleType type) {
        Object selectedItem = spinner.getSelectedItem();
        if ((selectedItem instanceof Group)){
            showScheduleImpl(ScheduleMode.TEACHER, type, (Group) selectedItem);
        }
    }

    private void showScheduleImpl(ScheduleMode mode, ScheduleType type, Group group) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra(ScheduleActivity.ARG_ID, group.getId());
        intent.putExtra(ScheduleActivity.ARG_NAME, group.getName());
        intent.putExtra(ScheduleActivity.ARG_TYPE, type);
        intent.putExtra(ScheduleActivity.ARG_MODE, mode);
        startActivity(intent);
    }

    private void InitGroups(List<Group> groups) {
        groups.add(new Group(1, "Викентьева Ольга Леонидовна"));
        groups.add(new Group(2, "Кузнецов Денис Борисович"));
    }

    private void InitData() {
        status.setText(R.string.teacher_has_no_lessons);
        subject.setText(R.string.subject_label);
        cabinet.setText(R.string.cabinet_label);
        corpus.setText(R.string.corpus_label);
        teacher.setText(R.string.teacher_label);
    }

    @Override
    public void showTime(String formattedDate) {
        time.setText(formattedDate);
    }
}