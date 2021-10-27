package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class FacultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        ArrayList<Object> objects = new ArrayList<>();

        objects.add(new Object("Shafkat Kibria (SKB)", "Assistant Professor & Head (Acting)", "Cell Phone: 01972601050", "E-mail: shafkat@lus.ac.bd", R.drawable.skb));
        objects.add(new Object("Prof. Dr. AS. Sikder (DEF)", "Professor", "Cell Phone: 01757584584", "E-mail: faruq_cse@lus.ac.bd", R.drawable.def));
        objects.add(new Object("Rumel M. S. Rahman Pir (RMS)", "Associate Professor", "Cell Phone: 01758228736", "E-mail: rumelpir@lus.ac.bd", R.drawable.rms));
        objects.add(new Object("Sabia Akter Bhuiyan (SAB)", "Assistant Professor", "Cell Phone: 01714506159", "E-mail: aktersabia@yahoo.com", R.drawable.sab));
        objects.add(new Object("Md. Ebrahim Hossain (EBH)", "Assistant Professor", "Cell Phone: 01733688612", "E-mail: ebrahim.cse@lus.ac.bd", R.drawable.ebh));
        objects.add(new Object("Rana M Luthfur Rahman Pir (RPL)", "Assistant Professor", "Cell Phone: 01763460076", "E-mail: ranapir_cse@lus.ac.bd", R.drawable.rpl));
        objects.add(new Object("Arafat Habib Quraishi (AHQ)", "Lecturer", "Cell Phone: 01914487146", "E-mail: arafat@lus.ac.bd", R.drawable.ahq));
        objects.add(new Object("Kazi Md. Jahid Hasan (KJH)", "Lecturer (Mathematics)", "Cell Phone: 01676480060", "E-mail: jahidce@lus.ac.bd", R.drawable.kjh));
        objects.add(new Object("Md. Saiful Ambia Chowdhury (MSA)", "Lecturer (On study leave)", "Cell Phone: 01718377269", "E-mail: sas2505@lus.ac.bd", R.drawable.msa));
        objects.add(new Object("Debojyoti Biswas (DEB)", "Lecturer (On study leave)", "Cell Phone: 01861888162", "E-mail: bishaldebojyoti@gmail.com", R.drawable.deb));
        objects.add(new Object("Mohammad Jaber Hossain (MJH)", "Lecturer (On study leave)", "Cell Phone: 01711091906", "E-mail: jhsarzil120@gmail.com", R.drawable.mjh));
        objects.add(new Object("Adil Ahmed Chowdhury (AAC)", "Lecturer", "Cell Phone: 01771852888", "E-mail: adil@lus.ac.bd", R.drawable.aac));
        objects.add(new Object("Md. Saidur Rahman Kohinoor (SRK)", "Lecturer", "Cell Phone: 01732477046", "E-mail: kohinoor_cse@lus.ac.bd", R.drawable.srk));
        objects.add(new Object("Syeda Tamanna Alam Monisha (STA)", "Lecturer", "Cell Phone: 01712953999", "E-mail: monisha_cse@lus.ac.bd", R.drawable.sta));
        objects.add(new Object("Mohammad Shoaib Rahman (MSR)", "Lecturer", "Cell Phone: 01717001333", "E-mail: shoaib_cse@lus.ac.bd", R.drawable.msr));
        objects.add(new Object("Prithwiraj Bhattacharjee (PRB)", "Lecturer", "Cell Phone: 01985240328", "E-mail: prithwiraj_cse@lus.ac.bd", R.drawable.prb));
        objects.add(new Object("Md. Arifuzzaman (AFZ)", "Lecturer", "Cell Phone: 01998740789", "E-mail: arif_cse@lus.ac.bd", R.drawable.afz));
        objects.add(new Object("Md. Jamaner Rahaman (MJR)", "Lecturer", "Cell Phone: 01310817573", "E-mail: jamaner_cse@lus.ac.bd", R.drawable.mjr));
        objects.add(new Object("Md. Jehadul Islam Mony (JIM)", "Cell Phone: 01686749128", "Lecturer", "E-mail: mony_cse@lus.ac.bd", R.drawable.jim));

        ListView facultyListView = findViewById(R.id.list);

        final FacultyAdapter adapter = new FacultyAdapter(this, objects);

        facultyListView.setAdapter(adapter);
    }
}