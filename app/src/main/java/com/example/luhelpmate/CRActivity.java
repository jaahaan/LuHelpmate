package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class CRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        ArrayList<Object> objects = new ArrayList<>();

        objects.add(new Object("57th\n(B)", "Sheikh Tashfeah Tabassum", "ID: 2122020063", "Phone No: 01705240332"));
        objects.add(new Object("57th\n(B)", "Ishmam Rahman", "ID: 20220200054", "Phone No: 01872100120"));
        objects.add(new Object("57th\n(A)", "Sadia Abu", "ID: 2122020023", "Phone No: 01609374079"));
        objects.add(new Object("57th\n(A)", "Syed Abu Raiyan Sarhad", "ID: 2122020035", "Phone No: 01768305939 "));
        objects.add(new Object("56th", "Aminoor Rahim", "ID: 2112020027", "Phone No: 01301954563"));
        objects.add(new Object("55th", "Mohammad Rakibul Islam", "ID: 2032020002", "Phone No: 01732133168"));
        objects.add(new Object("55th", "Ashraf Ahmed", "ID: 2032020001", "Phone No: 01749290616"));
        objects.add(new Object("54th", "Ahnaf Rfid", "ID: 2022020002", "Phone No: 01680004770"));
        objects.add(new Object("53rd\n(H)", "Sandip Chakraborty", "ID: 2012020365", "Phone No:0120264931"));
        objects.add(new Object("53rd\n(H)", "Md. Mijanur Rahman Chowdhury", "ID: 2012020351", "Phone No: 01772299407"));
        objects.add(new Object("53rd\n(G)", "Sajid Abdullah", "ID: 2012020308", "Phone No: 01722100839"));
        objects.add(new Object("53rd\n(G)", "Mizanur Rahman Bijoy", "ID: 2012020323", "Phone No: 01786935388"));
        objects.add(new Object("53rd\n(F)", "Sujoy Chandra Das", "ID: 2012020269", "Phone No:01738980447"));
        objects.add(new Object("53rd\n(F)", "Touhid Hasan Badhon", "ID: 2012020296", "Phone No: 01302910575"));
        objects.add(new Object("53rd\n(E)", "Md. Akter Hosen", "ID: 2012020234", "Phone No: 0179862713"));
        objects.add(new Object("53rd\n(D)", "Abu Shalehin MD. Rayat", "ID: 2012020162", "Phone No: 01625750124"));
        objects.add(new Object("53rd\n(C)", "Mahdi Hasan Hira", "ID: 2012020106", "Phone No: 01772757936"));
        objects.add(new Object("53rd\n(C)", "Tanjid Islam", "ID: 2012020147", "Phone No: 01753222156"));
        objects.add(new Object("53rd\n(B)", "Afia Sultana Promi", "ID:2012020082", "Phone No: 01789353791"));
        objects.add(new Object("53rd\n(A)", "Rafid Al Raiyan", "ID: 2012020024", "Phone No: 01799482872"));
        objects.add(new Object("52nd", "Md. Shahinur Rahman", "ID: 1932020044", "Phone No: 01646884879"));
        objects.add(new Object("51st", "Abdullah", "ID: 1922020016", "Phone No: 01733720541"));
        objects.add(new Object("50th\n(F)", "Priyanka Rani Deb", "ID: 1912020256", "Phone No: 01742026583"));
        objects.add(new Object("50th\n(E)", "Tamim Iqbal Chowdhury", "ID: 1912020183", "Phone No: 01646705394"));
        objects.add(new Object("50th\n(E)", "Shamima Chowdhury Nadia", "ID: 1912020224", "Phone No: 01831755286"));
        objects.add(new Object("50th\n(D)", "Jalal Udddin Chowdhury ", "ID: 1912020138", "Phone No: 01646705394"));
        objects.add(new Object("50th\n(D)", "Anisa Jahan", "ID: 1912020171", "Phone No: 01746465648"));
        objects.add(new Object("50th\n(C)", "Naeem Hasan Chowdhury", "ID: 1912020107", "Phone No: 01779875412"));
        objects.add(new Object("50th\n(B)", "Nakib Islam Chowdhury", "ID: 1912020056", "Phone No: 01307664678"));
        objects.add(new Object("50th\n(A)", "Riyan Ahmed", "ID: 1912020037", "Phone No: 0191271560"));
        objects.add(new Object("50th\n(A)", "Taslima Sultana Juli", "ID: 1912020007", "Phone No: 01995961841"));
        objects.add(new Object("49th", "Sanzida Parvin Shorna", "ID: 1832020015", "Phone No: 01757089101"));
        objects.add(new Object("49th", "Maruf Ahmed", "ID: 1832020018", "Phone No: 01767068737"));
        objects.add(new Object("48th", "Al Ekram Hussain", "ID: 1822020019", "Phone No: 01717925670"));
        objects.add(new Object("47th\n(E+F)", "Kamrul Hasan Chowdhury", "ID: 1812020181", "Phone No: 01734181970"));
        objects.add(new Object("47th\n(E+F)", "Akash Chanda Tushar", "ID: 1812020192", "Phone No: 01776774988"));
        objects.add(new Object("47th\n(D)", "Showhan Ibn Saif Anik ", "ID: 1812020156", "Phone No: 01798413657"));
        objects.add(new Object("47th\n(D)", "Tanzil Ebad Chowdhury ", "ID: 1812020179", "Phone No: 01746545650"));
        objects.add(new Object("47th\n(C)", "A.S.M Wasim ", "ID: 1812020119", "Phone No: 01756376101"));
        objects.add(new Object("47th\n(C)", "Md. Mahfujur Rahman", "ID: 1812020131", "Phone No:01716670801"));
        objects.add(new Object("47th\n(B)", "Tanvir Hossain", "ID: 1812020075", "Phone No: 01820946080"));
        objects.add(new Object("47th\n(B)", "Tamim Ahmed ", "ID: 1812020049", "Phone No: 01632645928"));
        objects.add(new Object("47th\n(A)", "Md.Kamrul Alom", "ID: 1812020030", "Phone No: 01792771413"));
        objects.add(new Object("46th", "MD.Mahmudul Hasan Rahat", "ID: 1732020016", "Phone No: 01680100979"));
        objects.add(new Object("46th", "Md. Mehedi Hasan Suhag", "ID: 1732020014", "Phone No: 01759608905"));
        objects.add(new Object("45th", "Md. Shahidul Islam Shaikot", "ID: 1722020022", "Phone No: 01715236701"));
        objects.add(new Object("44th\n(E+E)", "Tahmid Hossain", "ID: 1712020184", "Phone No: 01688565557"));
        objects.add(new Object("44th\n(D)", "Rafiqur Rahman", "ID: 1712020176", "Phone No: 01740789951"));
        objects.add(new Object("44th\n(C)", "Chowdhury Fabiha Hayat", "ID: 1712020122", "Phone No: 01731390184"));
        objects.add(new Object("44th\n(C)", "Md. Tawsif Ul Hye Chowdhury", "ID: 1712020094", "Phone No: 01775286080"));
        objects.add(new Object("44th\n(B)", "Mustak Ahmed Rashel", "ID: 1712020067", "Phone No: 01716249806"));
        objects.add(new Object("44th\n(B)", "S.M. Musharaf Hussain Sadi", "ID: 1712020087", "Phone No: 01764754903"));
        objects.add(new Object("44th\n(A)", "Md. Nazmul huda", "ID: 1712020035", "Phone No: 01823561991"));
        objects.add(new Object("43rd", "Mohammed Abdul Mohsin", "ID: 1632020002", "Phone No: 01683793358"));
        objects.add(new Object("42nd", "Masrura Jehin", "ID: 1622020031", "Phone No: 01990689705"));
        objects.add(new Object("42nd", "Shuvo Sarker", "ID: 1622020028", "Phone No: 01929329721"));

        ListView crListView = findViewById(R.id.list);

        final CrAdapter adapter = new CrAdapter(this, objects);

        crListView.setAdapter(adapter);


    }
}