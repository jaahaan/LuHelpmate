package com.example.luhelpmate.CourseList;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditCourseList extends AppCompatActivity {

    private EditText cCode, cTitle, cCredit, cPrerequisite;
    private Button add;
    private DatabaseReference reference, dbRef;
    private String code, title, credit, prerequisite;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course_list);

        reference = FirebaseDatabase.getInstance().getReference().child("Course List");

        pd = new ProgressDialog(this);

        title = getIntent().getStringExtra("title");
        code = getIntent().getStringExtra("code");
        credit = getIntent().getStringExtra("credit");
        prerequisite = getIntent().getStringExtra("prerequisite");

        cCode = findViewById(R.id.codeC);
        cTitle = findViewById(R.id.titleC);
        cCredit = findViewById(R.id.creditC);
        cPrerequisite = findViewById(R.id.prerequisiteC);
        add = findViewById(R.id.addC);

        cTitle.setText(title);
        cCode.setText(code);
        cCredit.setText(credit);
        cPrerequisite.setText(prerequisite);
        add.setOnClickListener(v -> checkValidation());
    }

    private void checkValidation() {
        code = cCode.getText().toString();
        title = cTitle.getText().toString();
        credit = cCredit.getText().toString();
        prerequisite = cPrerequisite.getText().toString();
        if (code.isEmpty()) {
            cCode.setError("Enter Course Code");
            cCode.requestFocus();
            return;
        } else if (title.isEmpty()) {
            cTitle.setError("Enter Course Title");
            cTitle.requestFocus();
            return;
        } else if (credit.isEmpty()) {
            cCredit.setError("Enter Course Credit");
            cCredit.requestFocus();
            return;
        } else {
            if (prerequisite.isEmpty()) {
                prerequisite ="-";
            }
            pd.setMessage("Updating...");
            pd.show();
            uploadData();
        }
    }

    private void uploadData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("code", code);
        map.put("credit", credit);
        map.put("prerequisite", prerequisite);
        String uniqueKey = getIntent().getStringExtra("key");
        map.put("key", uniqueKey);

        reference.child(uniqueKey).setValue(map).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, CourseList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}