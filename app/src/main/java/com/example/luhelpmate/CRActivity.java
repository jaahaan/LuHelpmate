package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;

public class CRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        ArrayList<Object> objects = new ArrayList<>();

        objects.add(new Object("44th\n(A)", "Md Nazmul huda", "ID: 1712020035", "Phone No: 01823561991"));
        objects.add(new Object("43rd", "Mohammed Abdul Mohsin", "ID: 1632020002", "Phone No: 01683793358"));
        objects.add(new Object("42nd", "Masrura Jehin", "ID: 1622020031", "Phone No: 01990689705"));
        objects.add(new Object("42nd", "Shuvo Sarker", "ID: 1622020028", "Phone No: 01929329721"));


        ListView crListView = findViewById(R.id.list);

        final CrAdapter adapter = new CrAdapter(this, objects);

        crListView.setAdapter(adapter);


    }
}