package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.R;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewActivity extends AppCompatActivity {

    private PhotoView imageView;
    private TextView textView;
    private String image, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_notice);

        imageView = findViewById(R.id.nImage);
        textView = findViewById(R.id.nTitle);

        image = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");
        textView.setText(title);

        Glide.with(this).load(image).into(imageView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_share, menu);
        MenuItem download = menu.findItem(R.id.download);
        MenuItem share = menu.findItem(R.id.share);

        download.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(image));
                startActivity(intent);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}