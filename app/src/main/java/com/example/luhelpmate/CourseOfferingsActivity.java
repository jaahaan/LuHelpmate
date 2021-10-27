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
                objects.add(new Object("CSE-1213", "Computer Programming", "3", "CSE-1111"));
                objects.add(new Object("CSE-1214", "Computer Programming Sessional", "1", "CSE-1112"));
                objects.add(new Object("CSE-1215", "Discrete Mathematics", "3", "-"));
                objects.add(new Object("ART-1311", "Introduction to Sociology", "3", "-"));
                objects.add(new Object("MAT-1213", "Linear Algebra & Complex Analysis", "3", "MAT-1111"));
                objects.add(new Object("", "Total Credit", "13", ""));
            } else if (i == 56) {
                String s = f + "\n3rd Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("ENG-1311", "English Writing And Listening ", "3", "EEE-1111"));
                objects.add(new Object("ART-1311", "Introduction to Sociology  ", "3", "-"));
                objects.add(new Object("CSE-1315", "Data Structures ", "3", "CSE-1213"));
                objects.add(new Object("CSE-1316", "Data Structures Sessional", "1", "CSE-1214"));
                objects.add(new Object("MAT-1315", "Differential Equations and Fourier Analysis ", "3", "MAT-1213"));
                objects.add(new Object("", "Total Credit", "13", ""));
            } else if (i == 55) {
                String s = f + "\n4th Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("CSE-2117", "Computer Algorithm and Complexity", "3", "CSE-1315"));
                objects.add(new Object("CSE-2118", "Computer Algorithm and Complexity Sessional", "1", "CSE-1316"));
                objects.add(new Object("CHE-2311", "Chemistry  ", "2", "-"));
                objects.add(new Object("CHE-2312", "Chemistry Sessional", "1", "-"));
                objects.add(new Object("ACC-2111", "Principles of Accounting", "3", "-"));
                objects.add(new Object("MAT--2111", "Co-Ordinate Geometry & Vector Analysis", "3", "MAT-1315"));
                objects.add(new Object("EEE-1112", "Electrical Circuit I Sessional", "1", "-"));
                objects.add(new Object("", "Total Credit", "14", ""));
            } else if (i == 54) {
                String s = f + "\n5th Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("CHE-2311", "Chemistry ", "2", "-"));
                objects.add(new Object("CHE-2312", "Chemistry Sessional", "1", "-"));
                objects.add(new Object("MAT-2111", "Co-Ordinate Geometry and Vector Analysis", "3", "-"));
                objects.add(new Object("CSE-2313", "Object Oriented Programming", "3", "CSE-1213"));
                objects.add(new Object("CSE-2314", "Object Oriented Programming Sessional", "1", "CSE-1214"));
                objects.add(new Object("EEE-1112", "Electrical Circuit I Sessional", "1", "-"));
                objects.add(new Object("ACC-2111", "Principles of Accounting", "3", "-"));
                objects.add(new Object("", "Total Credit", "14", ""));
            } else if (i == 53) {
                String s = f + "\n6th Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("CHE-3317", "Java Programming ", "3", "CSE-2213"));
                objects.add(new Object("CHE-3318", "Java Programming Sessional", "1", "CSE-2214"));
                objects.add(new Object("EEE-2317", "Digital Electronics", "3", "EEE-1215"));
                objects.add(new Object("EEE-2318", "Digital Electronics Sessional ", "1.5", "EEE-1216"));
                objects.add(new Object("CSE-2319", "Database Management System ", "3", "-"));
                objects.add(new Object("CSE-2320", "Database Management System Sessional", "1", "-"));
                objects.add(new Object("MAT-2213", "Probability and Statistics ", "3", "MAT-2111"));
                objects.add(new Object("", "Total Credit", "15.5", ""));
            } else if (i == 52) {
                String s = f + "\n7th Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("CSE-3227", "Theory of Computation ", "3", "-"));
                objects.add(new Object("CSE-3115", "Computer Networks", "3", "-"));
                objects.add(new Object("CSE-3116", "Computer Networks Sessional ", "1", "-"));
                objects.add(new Object("EEE-1215", "Electronics", "3", "-"));
                objects.add(new Object("EEE-1216", "Electronics Sessional", "1", "EEE-1111"));
                objects.add(new Object("CSE-2320", "Database Management System Sessional", "1", "EEE-1112"));
                objects.add(new Object("ART-2213", "Professional Ethics ", "3", "-"));
                objects.add(new Object("", "Total Credit", "14", ""));
            } else if (i == 51) {
                String s = f + "\n8th Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("EEE-1215", "Electronics", "3", "EEE-1111"));
                objects.add(new Object("EEE-1216", "Electronics Sessional", "1", "EEE-1112"));
                objects.add(new Object("CSE-3211", "Operating System", "3", "-"));
                objects.add(new Object("CSE-3212", "Operating System Sessional ", "1", "-"));
                objects.add(new Object("ART-2213", "Professional Ethics", "3", "-"));
                objects.add(new Object("CSE-3317", "Java Programming ", "3", "CSE-2213"));
                objects.add(new Object("CSE-3318", "Java Programming Sessional ", "1", "CSE-2214"));
                objects.add(new Object("", "Total Credit", "14", ""));
            } else if (i == 50) {
                String s = f + "\n9th Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("EEE-3211", "Microprocessor,Assembly Language and Computer Interfacing", "3", "EEE-2317"));
                objects.add(new Object("EEE-3212", "Microprocessor,Assembly Language and Computer Interfacing Sessional", "1", "EEE-2318"));
                objects.add(new Object("EEE-1216", "Electronics Sessional ", "1", "EEE-1112"));
                objects.add(new Object("CSE-3315", "Compiler Design and Construction", "3", "CSE-3227"));
                objects.add(new Object("CSE-3316", "Compiler Design and Construction Sessional ", "1", "-"));
                objects.add(new Object("CSE-4111", "Management Information System", "3", "-"));
                objects.add(new Object("CSE-3300", "Project-I", "1", "CSE-3317"));
                objects.add(new Object("", "Total Credit", "13", ""));
            } else if (i == 49) {
                String s = f + "\n10th Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("CSE-3213", "Digital Signal Processing", "3", "-"));
                objects.add(new Object("CSE-3214", "Digital Signal Processing Sessional", "1", "-"));
                objects.add(new Object("CSE-4119", "Artificial Intelligence  ", "3", "-"));
                objects.add(new Object("CSE-4113", "Computer Graphics ", "3", "CSE-1213"));
                objects.add(new Object("CSE-4114", "Computer Graphics Sessional ", "1", "CSE-1214"));
                objects.add(new Object("", "Total Credit", "11", ""));
            } else if (i == 48) {
                String s = f + "\n11th Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("CSE-4315", "Computer Security & Cryptography", "3", "-"));
                objects.add(new Object("CSE-4800", "Project/Thesis(part 1)", "1", "CSE-3300"));
                objects.add(new Object("EEE-4127", "VLSI I", "2", "EEE-3211"));
                objects.add(new Object("EEE-4128", "VLSI I  Sessional ", "1", "EEE-3212"));
                objects.add(new Object("CSE-4211", "Web Technologies", "3", "CSE-2319"));
                objects.add(new Object("CSE-4212", "Web Technologies  Sessional ", "1", "CSE-2320"));
                objects.add(new Object("", "Total Credit", "11", ""));
            } else if (i == 47) {
                String s = f + "\n12th Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("CSE-4801", "Project/Thesis (Part 2)", "3", "CSE-4800"));
                objects.add(new Object("", "Total Credit", "3", ""));
            } else if (i == 14) {
                String s = f + "\n10th Semester, Batch : " + i;
                headerC.setText(s);
                objects.add(new Object("Course Code", "Course Title", "Credit", "Prerequisite"));
                objects.add(new Object("CSE-4317", "Human Computer Interaction", "3", "-"));
                objects.add(new Object("CSE-4223", "Neural Network & Fuzzy Logic", "3", "-"));
                objects.add(new Object("CSE-4315", " Computer Security & Cryptography", "3", "-"));
                objects.add(new Object("CSE-4801", "Project/Thesis", "4", "-"));
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