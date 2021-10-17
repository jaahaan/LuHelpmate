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
        TextView se = findViewById(R.id.session);
        TextView lu = findViewById(R.id.lastUpdate);

        String session = "Fall-2021";
        String lastUpdated = "Last Updated: 10.10.2021";

        se.setText(session);
        lu.setText(lastUpdated);

    }

    @Override
    public void onClick(View v) {

        ArrayList<Object> objects = new ArrayList<>();
        TextView headerR = findViewById(R.id.headerR);
        try {
            EditText batch = findViewById(R.id.batchR);
            String name = batch.getText().toString();
            int i = Integer.parseInt(name);

            EditText section = findViewById(R.id.sectionR);
            String j = section.getText().toString();
            if (i == 58 && (j.equals("A") || j.equals("a"))) {
                String s = "1st Semester, Batch : " + i + ", Section: " + j;
                headerR.setText(s);

                objects.add(new Object("", "MONDAY", "", ""));
                objects.add(new Object("11.00 AM-11.50 AM", "EEE-1111", "JTE", "*"));
                objects.add(new Object("12.00 PM-12.50 PM", "EEE-1111", "JTE", "*"));
                objects.add(new Object("01.00 PM-01.50 PM", "Break", "", ""));

                objects.add(new Object("", "THURSDAY", "", ""));
                objects.add(new Object("11.00 AM-11.50 AM", "EEE-1111", "JTE", "*"));

            } else if (i == 57 && j.equals("")) {
                String s = "2st Semester, Batch : " + i;
                headerR.setText(s);

                objects.add(new Object("", "MONDAY", "", ""));
                objects.add(new Object("11.00 AM-11.50 AM", "EEE-1111", "JTE", "*"));
                objects.add(new Object("12.00 PM-12.50 PM", "EEE-1111", "JTE", "*"));
                objects.add(new Object("01.00 PM-01.50 PM", "Break", "", ""));

                objects.add(new Object("", "THURSDAY", "", ""));
                objects.add(new Object("11.00 AM-11.50 AM", "EEE-1111", "JTE", "*"));


            } else {
                headerR.setText("");
                Toast.makeText(this, getString(R.string.toast1), Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            headerR.setText("");
            Toast.makeText(this, getString(R.string.toast2), Toast.LENGTH_SHORT).show();

        }
        ListView routine = findViewById(R.id.list);

        final RoutineAdapter adapter = new RoutineAdapter(this, objects);

        routine.setAdapter(adapter);

    }

}