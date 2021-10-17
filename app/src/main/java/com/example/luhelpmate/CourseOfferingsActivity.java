package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CourseOfferingsActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list_view);
        Button button = findViewById(R.id.fetchC);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        TextView headerC = findViewById(R.id.headerC);
        String f = "Fall-2021";
        ArrayList<Object> objects = new ArrayList<>();
        try {
            //For user input
            EditText editText = findViewById(R.id.batchC);
            String name = editText.getText().toString();
            int i = Integer.parseInt(name);

            if (i == 58) {
                String s = f + "\n1st Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("ENG-1111", "English Reading and Speaking", "3", "-"));
                objects.add(new Object("MAT-1111", "Differential and Integral Calculus", "3", "-"));
                objects.add(new Object("CSE-1111", "Introduction To Computers", "2", "-"));
                objects.add(new Object("CSE-1112", "Introduction To Computers Sessional", "1", "-"));
                objects.add(new Object("ART-1111", "Bangladesh Studies", "3", "-"));
                objects.add(new Object("CEE-2110", "Engineering Drawing", "1.5", "-"));
                objects.add(new Object("", "Total Credit", "13.5", ""));
            } else if (i == 57) {
                String s = f + "\n2nd Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("CSE-1213", "Computer Programming", "3", "-"));
                objects.add(new Object("CSE-1214", "Computer Programming Sessional", "1", "-"));
                objects.add(new Object("CSE-1215", "Discrete Mathematics", "3", "-"));
                objects.add(new Object("ART-1311", "Introduction to Sociology", "3", "-"));
                objects.add(new Object("MAT-1213", "Linear Algebra & Complex Analysis", "3", "-"));
                objects.add(new Object("", "Total Credit", "13", ""));
            } else {
                headerC.setText("");
                Toast.makeText(this, getString(R.string.toast1), Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            headerC.setText("");
            Toast.makeText(this, getString(R.string.toast2), Toast.LENGTH_SHORT).show();

        }
        ListView course = findViewById(R.id.list);

        final CourseAdapter adapter = new CourseAdapter(this, objects);

        course.setAdapter(adapter);

    }
}