package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        ArrayList<Object> objects = new ArrayList<>();

        objects.add(new Object("26 Oct\n2021", "Transaction charges for Bkash/Online Bank payment of University fees", "https://www.lus.ac.bd/notice/regarding-transaction-charges-while-paying-university-fee-through-bkash-online-bank/"));
        objects.add(new Object("26 Oct\n2021", "Regarding Course Registration (Fall-2021)", "https://www.lus.ac.bd/notice/regarding-course-registration-fall-2021/"));
        objects.add(new Object("19 Oct\n2021", "Notice regarding resume of full operation in the campus", "https://drive.google.com/file/d/1O6m-B-v70REK--T9K5pziD1tyB2Ansde/view?usp=sharing"));
        objects.add(new Object("19 Oct\n2021", "Regarding International Conference on 4h Industrial Revolution and Beyond", ""));
        objects.add(new Object("18 Oct\n2021", "Notice for Makeup of In-course Evaluation of Summer 2021", "https://drive.google.com/file/d/1o67oYb3-b6NP2dBKWB-MpEb2mofIaaGF/view?usp=sharing"));
        objects.add(new Object("17 Oct\n2021", "LU will remain closed on 20 October 2021", "https://drive.google.com/file/d/1KoF-Fz1DSH95szmhtK_yjfocUE8hAQ_9/view?usp=sharing"));
        objects.add(new Object("17 Oct\n2021", "Regarding vaccination program", "https://drive.google.com/file/d/1ObI3eLOonakpp9YmdvIFwHPNZzl4a30O/view?usp=sharing"));
        objects.add(new Object("12 Oct\n2021", "Regarding Vaccine Shots", "https://drive.google.com/file/d/1a1yFUcrAQ45hjFp1C9t72UU1GWa7Gx4r/view?usp=sharing"));
        objects.add(new Object("11 Oct\n2021", "LU will remain closed during 13-15 October 2021", "https://drive.google.com/file/d/1CXgu4Nh9Hd8IE7XUZ4NmNqCc-45cd2Du/view?usp=sharing"));
        objects.add(new Object("5 Oct\n2021", "Notice regarding resume classes in the campus", "https://drive.google.com/file/d/1Q1kexW9QQpPxmLuTluBT4uGb8HTebb90/view?usp=sharing"));
        objects.add(new Object("5 Oct\n2021", "LU will remain closed on 6 October 2021", "https://drive.google.com/file/d/1NkfAxcMNajWlMzB81_yDKzdLEM0Hy6Vd/view?usp=sharing"));


        ListView noticeListView = findViewById(R.id.list);

        final NoticeAdapter adapter = new NoticeAdapter(this, objects);

        noticeListView.setAdapter(adapter);
        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current notice that was clicked on
                Object currentNotice = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri noticeUri = Uri.parse(currentNotice.getnUrl());

                // Create a new intent to view the notice URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, noticeUri);

                try {
                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(NoticeActivity.this, getString(R.string.toast3), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}