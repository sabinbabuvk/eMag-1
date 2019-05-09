package com.ortusolis.emag;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import static com.ortusolis.emag.MagazineActivity.fileList;

public class MainActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    ListView mylistView;
    PDFView mypdfView;
    DownloadManager downloadManager;
    private Button Home;
    private Button Magazine;
    private Button Share;
    private Button Feedback;
    File dir;
    Integer pageNumber = 0;
    String pdfFileName;
    PDFAdapter obj_adapter;
    boolean boolean_permission;
    public static int REQUEST_PERMISSIONS = 1;
    public static ArrayList<File> fileList1 = new ArrayList<File>();
    private static final String url = "jdbc:mysql://13.233.81.45:3306/e_mag";
    private static final String user = "ortusolis";
    private static final String pass = "Ortusolis@123";
    long output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Home");
        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mypdfView = findViewById(R.id.pdfOpener);

        Magazine = findViewById(R.id.magazineHomeScreen);
        Magazine.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MagazineActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Share = (Button) findViewById(R.id.shareHomeScreen);
        Share.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShareActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Feedback = (Button) findViewById(R.id.feedbackHomeScreen);
        Feedback.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
                //          finish();
            }
        }));
                        fn_permission();
                        doInBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;

           // getfile(dir);

            obj_adapter = new PDFAdapter(getApplicationContext(), fileList1);
            // lv_pdf.setAdapter(obj_adapter);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;
               // getfile(dir);

                obj_adapter = new PDFAdapter(getApplicationContext(), fileList1);
                // lv_pdf.setAdapter(obj_adapter);

            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = mypdfView.getDocumentMeta();
        printBookmarksTree(mypdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e("MainActivity", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
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
            ResultSet rs = st.executeQuery("SELECT *\n" +
                    "FROM pdf_details\n" +
                    "ORDER BY \n" +
                    "PDF_Id desc limit 1;");
            try {
                while (rs.next()) {
                    String fileUrl = rs.getString(7);
                    String filename = rs.getString(6);
                    Log.e("Result set", fileUrl);
                    File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.ortusolis.emag/files/Ananya_pdf_files/" + filename);
                    if (file != null && file.exists()) {
                        fn_permission();
                        mypdfView.fromFile(file)
                                .defaultPage(pageNumber)
                                .enableSwipe(true)

                                .swipeHorizontal(true)
                                .onPageChange(this)
                                .enableAnnotationRendering(true)
                                .onLoad(this)
                                .scrollHandle(new DefaultScrollHandle(this))
                                .load();
                        mypdfView.useBestQuality(true);
                        Toast.makeText(getApplicationContext(),
                                " Magazine is Ready",
                                Toast.LENGTH_SHORT).show();
                        Log.e("Result file exist ", file.toString());
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
                        mypdfView.fromFile(file)
                                                .defaultPage(pageNumber)
                                                .enableSwipe(true)

                                                .swipeHorizontal(true)
                                                .onPageChange(this)
                                                .enableAnnotationRendering(true)
                                                .onLoad(this)
                                                .scrollHandle(new DefaultScrollHandle(this))
                                                .load();
                                        mypdfView.useBestQuality(true);
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


}
