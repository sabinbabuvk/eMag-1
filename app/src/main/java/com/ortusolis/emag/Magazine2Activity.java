package com.ortusolis.emag;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.ortusolis.emag.MainActivity.REQUEST_PERMISSIONS;
import static com.ortusolis.emag.MainActivity.fileList1;

public class Magazine2Activity extends AppCompatActivity  {
    private static final String url = "jdbc:mysql://13.233.81.45:3306/e_mag";
    private static final String user = "ortusolis";
    private static final String pass = "Ortusolis@123";
    private Button Home;
    private Button Magazine;
    private Button Share;
    private Button Feedback;
    DownloadManager downloadManager;
    PDFView pdfViewmag;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG="Magazine2Activity";
    int position=-1;
    boolean boolean_permission;

    String value;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine2);
        this.setTitle("Magazine2");
        pdfViewmag = findViewById(R.id.pdfView);
        Feedback = (Button) findViewById(R.id.feedback);
        Feedback.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Magazine2Activity.this, FeedbackActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Magazine = (Button) findViewById(R.id.magazine);
        Magazine.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Magazine2Activity.this, MagazineActivity.class);
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
//        init();
        Intent intent1 = getIntent();
         value = intent1.getExtras().getString("fileName");
        fn_permission();
        doInBackground();
    }
    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Magazine2Activity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(Magazine2Activity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
        }
    }

    protected Boolean doInBackground() {
        boolean flag = true;
        boolean downloading = true;

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Ananya_pdf_files");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        try {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);

            String result = "Database connected successfully";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT URL,File_Name FROM pdf_details Where File_Name='"+value+"'");
            try {
                while (rs.next()) {
                    String fileUrl = rs.getString(1);
                   String filename = rs.getString(2);
                    Log.e("FLAG", fileUrl);
                    File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.ortusolis.emag/files/Ananya_pdf_files/" + filename);
                    if (file != null && file.exists()) {
                        fn_permission();
                        pdfViewmag.fromFile(file)
                                .defaultPage(pageNumber)
                                .enableSwipe(true)

                                .swipeHorizontal(true)
                                // .onPageChange(this)
                                .enableAnnotationRendering(true)
                                //  .onLoad(this)
                                .scrollHandle(new DefaultScrollHandle(this))
                                .load();
                        pdfViewmag.useBestQuality(true);
                        Toast.makeText(getApplicationContext(),
                                " Magazine is Ready",
                                Toast.LENGTH_SHORT).show();
                        Log.e("FLAG", file.toString());
                    } else {
                        doInBackground1(file, fileUrl, filename);

                    }
                }

            } finally {
                rs.close();
            }
            Log.e("Result set", rs.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    protected Boolean doInBackground1(File file,String fileUrl,String filename) {
        boolean flag = true;
        boolean downloading =true;
        try{
            DownloadManager mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Log.i ("FLAG","http://13.233.81.45/"+fileUrl);
            //  http://13.233.81.45/uploadPDF/Kalasinchana%20April%202019%20with%20cover%20pagesMagazine.pdf
            Uri uri = Uri.parse("http://13.233.81.45/"+fileUrl);
            // Uri uri = Uri.parse("http://13.233.81.45/" + fileUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(filename);
            request.setNotificationVisibility(1);
            request.allowScanningByMediaScanner();
            request.setMimeType("application/pdf");
            request.setDestinationInExternalFilesDir(this, "/Ananya_pdf_files", filename);
            //   request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            long reference = downloadManager.enqueue(request);
            DownloadManager.Query query = null;
            query = new DownloadManager.Query();
            Cursor c = null;
            if(query!=null) {
                query.setFilterByStatus(DownloadManager.STATUS_FAILED|DownloadManager.STATUS_PAUSED|DownloadManager.STATUS_SUCCESSFUL|DownloadManager.STATUS_RUNNING|DownloadManager.STATUS_PENDING);
            } else {
                return flag;
            }

            while (downloading) {
                c = mManager.query(query);
                if(c.moveToFirst()) {
                    Log.i ("FLAG","Downloading");
                    int status =c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                    if (status==DownloadManager.STATUS_SUCCESSFUL) {
                        Log.i ("FLAG","done");
                        pdfViewmag.fromFile(file)
                                .defaultPage(pageNumber)
                                .enableSwipe(true)

                                .swipeHorizontal(true)
                                // .onPageChange(this)
                                .enableAnnotationRendering(true)
                                //  .onLoad(this)
                                .scrollHandle(new DefaultScrollHandle(this))
                                .load();
                        pdfViewmag.useBestQuality(true);
                        downloading = false;
                        flag=true;
                        break;
                    }
                    if (status==DownloadManager.STATUS_FAILED) {
                        Log.i ("FLAG","Fail");
                        downloading = false;
                        flag=false;
                        break;
                    }
                }
            }

            return flag;
        }catch (Exception e) {
            flag = false;
            return flag;
        }
    }
//    private void init(){
//        pdfView= (PDFView)findViewById(R.id.pdfView);
//        position = getIntent().getIntExtra("position",-1);
//        displayFromSdcard();
//        linkuse();



//    private void displayFromSdcard() {
//        pdfFileName = MagazineActivity.fileList.get(position).getName();
//
//        pdfView.fromFile(MagazineActivity.fileList.get(position))
//                .defaultPage(pageNumber)
//                .enableSwipe(true)
//
//                .swipeHorizontal(true)
//               // .onPageChange(this)
//                .enableAnnotationRendering(true)
//               // .onLoad(this)
//                .scrollHandle(new DefaultScrollHandle(this))
//                .load();
//        pdfView.useBestQuality(true);
//    }
//    @Override
//    public void onPageChanged(int page, int pageCount) {
//        pageNumber = page;
//        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
//    }
//    @Override
//    public void loadComplete(int nbPages) {
//        PdfDocument.Meta meta = pdfView.getDocumentMeta();
//        printBookmarksTree(pdfView.getTableOfContents(), "-");
//
//    }

//    public String linkuse(){
//        return pdfFileName;
//    }

//    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
//        for (PdfDocument.Bookmark b : tree) {
//
//            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));
//
//            if (b.hasChildren()) {
//                printBookmarksTree(b.getChildren(), sep + "-");
//            }
//        }
//    }
}
