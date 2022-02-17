package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity {
    private String url;
    private PDFView pdfView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        url = getIntent().getStringExtra("pdf");
        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar);

        loadFile(url);
    }

    private void loadFile(String url) {
        FileLoader.with(this)
                .load(url)
                .fromDirectory("pdf", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        File loadedFile = response.getBody();
                        progressBar.setVisibility(View.GONE);
                        pdfView.fromFile(loadedFile).password(null).defaultPage(0)
                                .enableAnnotationRendering(true)
                                .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                                //.enableSwipe(true).swipeHorizontal(false)
                                .enableDoubletap(true).spacing(5).load();
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error!!" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_share, menu);
        MenuItem download = menu.findItem(R.id.download);
        MenuItem share = menu.findItem(R.id.share);

        download.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }
}
