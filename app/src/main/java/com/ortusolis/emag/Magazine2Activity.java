package com.ortusolis.emag;

import android.content.Intent;
import android.support.annotation.Nullable;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

public class Magazine2Activity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    private Button Home;
    private Button Magazine;
    private Button Share;
    private Button Feedback;
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG="Magazine2Activity";
    int position=-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine2);
        this.setTitle("Magazine2");

        Feedback = (Button) findViewById(R.id.feedback);
        Feedback.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Magazine2Activity.this, FeedbackActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Home = (Button) findViewById(R.id.home);
        Home.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Magazine2Activity.this, MainActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Share = (Button) findViewById(R.id.share);
        Share.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Magazine2Activity.this, ShareActivity.class);
                startActivity(intent);
                //         finish();
            }
        }));
        init();
    }
    private void init(){
        pdfView= (PDFView)findViewById(R.id.pdfView);
        position = getIntent().getIntExtra("position",-1);
        displayFromSdcard();
        linkuse();

    }

    private void displayFromSdcard() {
        pdfFileName = MagazineActivity.fileList.get(position).getName();

        pdfView.fromFile(MagazineActivity.fileList.get(position))
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(true)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
        pdfView.useBestQuality(true);
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }
    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public String linkuse(){
        return pdfFileName;
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
}
