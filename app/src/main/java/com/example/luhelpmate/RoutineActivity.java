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

            if (i == 57 && (j.equals("A") || j.equals("a"))) {
                String s = "1st Semester, Batch : " + i + ", Section: " + j;
                headerR.setText(s);

                objects.add(new Object("", "SUNDAY", "", ""));
                objects.add(new Object("11:00 AM-11:50 AM", "ENG-1111", "JTE", "*"));
                objects.add(new Object("12:00 PM-12:50 PM", "ENG-1111", "JTE", "*"));


                objects.add(new Object("", "WEDNESDAY", "", "*"));
                objects.add(new Object("09:00 AM-09:50 AM", "ENG-1111", "JTE", "*"));
                objects.add(new Object("11:00 AM-11:50 AM", "CSE-1112", "MJR", "G-3"));
                objects.add(new Object("12:00 PM-12:50 PM", "CSE-1112", "MJR", "G-3"));
                objects.add(new Object("", "BREAK", "", "*"));

                objects.add(new Object("", "THURSDAY", "", ""));
                objects.add(new Object("02:00 PM-02:50 M", "CEE-2110", "MJR", "GL"));
                objects.add(new Object("03:00 PM-03:50 PM", "CEE-2110", "MJR", "GL"));
                objects.add(new Object("04:00 PM-04:50 PM", "CEE-2110", "MJR", "GL"));

            } else if (i == 57 && (j.equals("B") || j.equals("b"))) {
                String s = "1st Semester, Batch : " + i + ", Section: " + j;
                headerR.setText(s);

                objects.add(new Object("", "MONDAY", "", ""));
                objects.add(new Object("09:00 AM-09:50 AM", "CEE-2110", "MJR", "GL"));
                objects.add(new Object("10:00 AM-10:50 AM", "CEE-2110", "MJR", "GL"));
                objects.add(new Object("11:00 AM-11:50 AM", "CEE-2110", "MJR", "GL"));
                objects.add(new Object("12:00 PM-12:50 PM", "MAT-1111", "KJH", "*"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "ENG-1111", "ANM", "*"));
                objects.add(new Object("03:00 PM-03:50 PM", "ENG-1111", "ANM", "*"));


                objects.add(new Object("", "WEDNESDAY", "", ""));
                objects.add(new Object("10:00 AM-10:50 AM", "ENG-1111", "ANM", "*"));
                objects.add(new Object("11:00 AM-11:50 AM", "CSE-1111", "PRB", "R-302"));
                objects.add(new Object("12:00 PM-12:50 PM", "CSE-1111", "PRB", "R-302"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "CSE-1112", "AHQ", "ACL-2"));
                objects.add(new Object("03:00 PM-03:50 PM", "CSE-1112", "AHQ", "ACL-2"));
                objects.add(new Object("04:00 PM-04:50 PM", "ART-1111", "TCJ", "*"));


                objects.add(new Object("", "THURSDAY", "", ""));
                objects.add(new Object("11:00 AM-11:50 AM", "MAT-1111", "KJH", "*"));
                objects.add(new Object("12:00 PM-12:50 PM", "MAT-1111", "KJH", "*"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));

                objects.add(new Object("02:00 PM-02:50 PM", "ART-1111", "TCJ", "*"));
                objects.add(new Object("03:00 PM-03:50 AM", "ART-1111", "TCJ", "*"));

            } else if (i == 56 && j.equals("")) {
                String s = "2nd Semester, Batch : " + i;
                headerR.setText(s);

                objects.add(new Object("", "SUNDAY", "", ""));
                objects.add(new Object("10:00 AM-10:50 AM", "CSE-1213", "AHQ", "R-304"));
                objects.add(new Object("11:00 AM-11:50 AM", "CSE-1214", "AHQ", "ACL-2"));
                objects.add(new Object("12:00 PM-12:50 PM", "CSE-1214", "AHQ", "ACL-2"));


                objects.add(new Object("", "TUESDAY", "", ""));
                objects.add(new Object("09:00 AM-09:50 AM", "MAT-1213", "KJH", "*"));
                objects.add(new Object("10:00 AM-10:50 AM", "MAT-1213", "KJH", "*"));
                objects.add(new Object("11:00 AM-11:50 AM", "CSE-1213", "AHQ", "R-304"));
                objects.add(new Object("12:00 PM-12:50 PM", "CSE-1213", "AHQ", "R-304"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));

                objects.add(new Object("02:00 PM-02:50 PM", "ART-2213", "TCL", "*"));
                objects.add(new Object("03:00 PM-03:50 PM", "CSE-1215", "JIM", "R-302"));


                objects.add(new Object("", "THURSDAY", "", ""));
                objects.add(new Object("11:00 AM-11:50 AM", "ART-2213", "TCL", "*"));
                objects.add(new Object("12:00 PM-12:50 PM", "ART-2213", "TCL", "*"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));

                objects.add(new Object("02:00 PM-02:50 PM", "CSE-1215", "JIM", "G-3"));
                objects.add(new Object("03:00 PM-03:50 PM", "CSE-1215", "JIM", "G-3"));
                objects.add(new Object("04:00 PM-04:50 PM", "MAT-1213", "KHJ", "R-309"));

            } else if (i == 54 || i == 55 && j.equals("")) {
                String s = "3rd+4thSemester, Batch : " + i;
                headerR.setText(s);

                objects.add(new Object("", "SUNDAY", "", ""));
                objects.add(new Object("09:00 AM-09:50 AM", "ART-1311", "HLB", "R-306"));
                objects.add(new Object("10:00 AM-10:50 AM", "ART-1311", "HLB", "R-309"));


                objects.add(new Object("", "MONDAY", "", ""));
                objects.add(new Object("11:00 AM-11:50 AM", "ENG-1311", "ANM", "*"));
                objects.add(new Object("12:00 PM-12:50 PM", "ENG-1311", "ANM", "*"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "MAT-1315", "SAB", "*"));
                objects.add(new Object("03:00 PM-03:50 PM", "MAT-1315", "SAB", "*"));


                objects.add(new Object("", "TUESDAY", "", ""));
                objects.add(new Object("10:00 AM-10:50 AM", "ART-1311", "HLB", "*"));
                objects.add(new Object("11:00 AM-11:50 AM", "ENG-1311", "ANM", "*"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));

                objects.add(new Object("02:00 PM-02:50 PM", "MAT-1315", "SAB", "R-304"));

            } else if (i == 50 && (j.equals("D") || j.equals("d"))) {
                String s = "8th Semester, Batch : " + i + ", Section:" + j;
                headerR.setText(s);


                objects.add(new Object("", "SUNDAY", "", ""));
                objects.add(new Object("10:00 AM-10:50 AM", "ART-1111", "CTS", "*"));
                objects.add(new Object("11:00 AM-11:50 AM", "ART-1111", "CTS", "*"));
                objects.add(new Object("12:00 PM-12:50 PM", "CSE-3211", "MSR", "R-304"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "EEE-2318", "AHQ", "ECL"));
                objects.add(new Object("03:00 PM-03:50 PM", "EEE-2318", "AHQ", "ECL"));
                objects.add(new Object("04:00 PM-04:50 PM", "EEE-2318", "AHQ", "ECL"));

                objects.add(new Object("", "", "", ""));

                objects.add(new Object("", "TUESDAY", "", ""));
                objects.add(new Object("09:00 AM-09:50 AM", "CSE-3317", "JIM", "ACL-1"));
                objects.add(new Object("10:00 AM-10:50 AM", "CSE-3317", "JIM", "ACL-1"));
                objects.add(new Object("11:00 AM-11:50 AM", "ART-1111", "CTS", "*"));
                objects.add(new Object("12:00 PM-12:50 PM", "EEE-2317", "AFZ", "G-2"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "CSE-3212", "SRK", "GL"));
                objects.add(new Object("03:00 PM-03:50 PM", "CSE-3212", "SRK", "GL"));

                objects.add(new Object("", "", "", ""));

                objects.add(new Object("", "THURSDAY", "", ""));
                objects.add(new Object("09:00 AM-09:50 AM", "CSE-3211-2317", "MSR", "R-302"));
                objects.add(new Object("10:00 AM-10:50 AM", "CSE-3317", "JIM", "G-3"));
                objects.add(new Object("11:00 AM-11:50 AM", "CSE-3317", "JIM", "G-3"));
                objects.add(new Object("12:00 PM-12:50 PM", "CSE-3317", "JIM", "G-3"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "EEE-2317", "AFZ", "R-302"));
                objects.add(new Object("04:00 PM-04:50 PM", "EEE-2317", "AFZ", "R-302"));

            } else if (i == 50 && (j.equals("E") || j.equals("e"))) {
                String s = "8th Semester, Batch : " + i + ", Section:" + j;
                headerR.setText(s);


                objects.add(new Object("", "MONDAY", "", ""));
                objects.add(new Object("10:00 AM-10:50 AM", "ART-1111", "CTS", "*"));
                objects.add(new Object("11:00 AM-11:50 AM", "CSE-3318", "JIM", "GL"));
                objects.add(new Object("12:00 PM-12:50 PM", "CSE-3318", "JIM", "GL"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "CSE-3317", "DEB", "R-302"));
                objects.add(new Object("03:00 PM-03:50 PM", "CSE-3317", "DEB", "R-302"));
                objects.add(new Object("04:00 PM-04:50 PM", "CSE-3317", "DEB", "R-302"));

                objects.add(new Object("", "", "", ""));

                objects.add(new Object("", "TUESDAY", "", ""));
                objects.add(new Object("10:00 AM-10:50 AM", "CSE-3211", "MSR", "*"));
                objects.add(new Object("11:00 AM-11:50 AM", "CSE-3211", "MSR", "GL"));
                objects.add(new Object("12:00 PM-12:50 PM", "CSE-3211", "MSR", "GL"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "EEE-2318", "AHQ", "ECL"));
                objects.add(new Object("03:00 PM-03:50 PM", "EEE-2318", "AHQ", "ECL"));
                objects.add(new Object("04:00 PM-04:50 PM", "EEE-2318", "AHQ", "ECL"));

                objects.add(new Object("", "", "", ""));

                objects.add(new Object("", "WEDNESDAY", "", ""));
                objects.add(new Object("09:00 AM-09:50 AM", "CSE-3211-2317", "MSR", "ACL-1"));
                objects.add(new Object("10:00 AM-10:50 AM", "EEE-2317", "AHQ", "R-304"));
                objects.add(new Object("11:00 AM-11:50 AM", "EEE-2317", "AHQ", "R-304"));
                objects.add(new Object("12:00 PM-12:50 PM", "EEE-2317", "AHQ", "R-304"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "ART-1111", "CTS", "*"));
                objects.add(new Object("04:00 PM-04:50 PM", "ART-1111", "CTS", "*"));

            } else if (i == 50 && (j.equals("F") || j.equals("f"))) {
                String s = "8th Semester, Batch : " + i + ", Section:" + j;
                headerR.setText(s);


                objects.add(new Object("", "MONDAY", "", ""));
                objects.add(new Object("09:00 AM-09:50 AM", "CSE-3212", "MSR", "ACL-1"));
                objects.add(new Object("10:00 AM-10:50 AM", "CSE-3212", "MSR", "ACL-1"));
                objects.add(new Object("11:00 AM-11:50 AM", "ART-1111", "CTS", "*"));
                objects.add(new Object("12:00 PM-12:50 PM", "ART-1111", "CTS", "*"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "EEE-2318", "AHQ", "ECL"));
                objects.add(new Object("03:00 PM-03:50 PM", "EEE-2318", "AHQ", "ECL"));

                objects.add(new Object("", "", "", ""));

                objects.add(new Object("", "WEDNESDAY", "", ""));
                objects.add(new Object("10:00 AM-10:50 AM", "CSE-3317", "PRB", "GL"));
                objects.add(new Object("11:00 AM-11:50 AM", "CSE-3317", "PRB", "GL"));
                objects.add(new Object("12:00 PM-12:50 PM", "CSE-3317", "PRB", "GL"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "CSE-3317", "PRB", "ACL-1"));
                objects.add(new Object("03:00 PM-03:50 PM", "CSE-3317", "PRB", "ACL-1"));

                objects.add(new Object("", "", "", ""));

                objects.add(new Object("", "THURSDAY", "", ""));
                objects.add(new Object("10:00 AM-10:50 AM", "EEE-2317", "AHQ", "R-302"));
                objects.add(new Object("11:00 AM-11:50 AM", "EEE-2317", "AHQ", "R-302"));
                objects.add(new Object("12:00 PM-12:50 PM", "EEE-2317", "AHQ", "R-302"));
                objects.add(new Object("01:00 PM-01:50 PM", "BREAK", "", ""));
                objects.add(new Object("02:00 PM-02:50 PM", "CSE-3211", "MSR", "R-304"));
                objects.add(new Object("03:00 PM-03:50 PM", "CSE-3211", "MSR", "R-304"));
                objects.add(new Object("04:00 PM-04:50 PM", "ART-1111", "CTS", "R-306"));

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