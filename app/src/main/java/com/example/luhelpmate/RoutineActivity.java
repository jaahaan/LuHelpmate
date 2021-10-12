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

            objects.add(new Object("", "Monday", "", ""));
            objects.add(new Object("11.00 AM-11.50 AM", "EEE-1111", "JTE", "*"));
            objects.add(new Object("12.00 PM-12.50 PM", "EEE-1111", "JTE", "*"));
            objects.add(new Object("01.00 PM-01.50 PM", "Break", "", ""));
            objects.add(new Object("01.00 PM-01.50 PM", "Break", "", ""));

            objects.add(new Object("", "Wed", "", ""));
        }
        else if (i == 57) {
            String s = f + "\n2st Semester, Batch : " + i;
            headerR.setText(s);

            
        }
        else {
            Toast.makeText(this, getString(R.string.toast1), Toast.LENGTH_SHORT).show();
        }

        ListView routine = findViewById(R.id.list);

        final RoutineAdapter adapter = new RoutineAdapter(this, objects);

        routine.setAdapter(adapter);

    }

}