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
import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewActivity extends AppCompatActivity {

    private PhotoView imageView;
    private TextView textView;
    private String imageUrl, title;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        imageView = findViewById(R.id.nImage);
        textView = findViewById(R.id.nTitle);
        progressBar = findViewById(R.id.progressBar);

        imageUrl = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");
        textView.setText(title);

        Glide.with(this).load(imageUrl).into(imageView);
        progressBar.setVisibility(View.GONE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download, menu);
        MenuItem download = menu.findItem(R.id.download);

        download.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(imageUrl));
            startActivity(intent);
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }

}