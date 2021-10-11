package com.example.luhelpmate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class RoutineActivity extends AppCompatActivity implements View.OnClickListener {


    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_listview);
        button = findViewById(R.id.fetchR);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText batch = findViewById(R.id.batchR);
        String name = batch.getText().toString();
        int i = Integer.parseInt(name);

        EditText section = findViewById(R.id.sectionR);
        String j = section.getText().toString();

        TextView headerR = findViewById(R.id.headerR);

        String f = "Fall-2021";
        ArrayList<Object> objects = new ArrayList<>();

        if (i == 58 && (j.equals("A") || j.equals("a"))) {
            String s = f + "\n1st Semester, Batch : " + i + ", Section: " + j;
            headerR.setText(s);

            objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
            objects.add(new Object("ENG-1111", "English Reading and Speaking", "3", "-"));
            objects.add(new Object("MAT-1111", "Differential and Integral Calculus", "3", "-"));
            objects.add(new Object("CSE-1111", "Introduction To Computers", "2", "-"));
            objects.add(new Object("CSE-1112", "Introduction To Computers Sessional", "1", "-"));
            objects.add(new Object("CHE-2311", "Chemistry", "2", "-"));
            objects.add(new Object("CHE-2312", "Chemistry Laboratory", "1", "-"));
            objects.add(new Object("CEE-2110", "Engineering Drawing", "1.5", "-"));
            objects.add(new Object("", "Total Credit", "13.5", ""));
        }
        else if (i == 57) {
            String s = f + "\n1st Semester, Batch : " + i;
            headerR.setText(s);

            objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
            objects.add(new Object("ENG-1311", "English Writing and Listening", "3", "ENG-1111"));
            objects.add(new Object("MAT-1213", "Linear Algebra & Complex Analysis", "3", "-"));
            objects.add(new Object("CSE-1213", "Computer Programming", "3", "-"));
            objects.add(new Object("CSE-1214", "Computer Programming Sessional", "1", "-"));
            objects.add(new Object("CSE-1215", "Discrete Mathematics", "3", "-"));
            objects.add(new Object("", "Total Credit", "13", ""));
        }
        else {
            Toast.makeText(this, getString(R.string.toast1), Toast.LENGTH_SHORT).show();
        }

        ListView routine = findViewById(R.id.list);

        final RoutineAdapter adapter = new RoutineAdapter(this, objects);

        routine.setAdapter(adapter);

    }

}