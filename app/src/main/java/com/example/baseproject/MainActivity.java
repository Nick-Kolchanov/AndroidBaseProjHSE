package com.example.baseproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View studentButton = findViewById(R.id.students_timetable_button);
        View teacherButton = findViewById(R.id.teachers_timetable_button);
        View settingsButton = findViewById(R.id.settings_button);


        studentButton.setOnClickListener(view -> ShowStudent());
        teacherButton.setOnClickListener(view -> ShowTeacher());
        settingsButton.setOnClickListener(view -> ShowSettings());
    }

    private void ShowStudent() {
        Intent intent = new Intent(this, StudentActivity.class);
        startActivity(intent);
    }

    private void ShowTeacher() {
        Intent intent = new Intent(this, TeacherActivity.class);
        startActivity(intent);
    }

    private void ShowSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}