package com.ortusolis.emag;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MagazineActivity extends Activity {
    private Button Home;
    private Button Magazine;
    private Button Share;
    private Button Feedback;
    ListView lv_pdf;
    DownloadManager downloadManager;
    private static final String url ="jdbc:mysql://13.233.81.45:3306/e_mag";
    private  static final String user = "ortusolis";
    private static final String pass = "Ortusolis@123";
    public static ArrayList<File> fileList = new ArrayList<>();
    PDFAdapter obj_adapter;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    File dir;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine);
        this.setTitle("Magazine");
        Feedback = (Button) findViewById(R.id.feedback);
        Feedback.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MagazineActivity.this, FeedbackActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Home = (Button) findViewById(R.id.home);
        Home.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MagazineActivity.this, MainActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Share = (Button) findViewById(R.id.share);
        Share.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MagazineActivity.this, ShareActivity.class);
                startActivity(intent);
       //         finish();
            }
        }));
        prepareListData();
        init();
    }
    private void init() {
        lv_pdf = (ListView) findViewById(R.id.lv_pdf);
        dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.ortusolis.emag/files/Ananya_pdf_files/");
        Log.e("FLAG",   dir.toString());
        fn_permission();
        lv_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Magazine2Activity.class);
                intent.putExtra("position", i);
                startActivity(intent);

                Log.e("FLAG", i + "");
            }
        });
    }

    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

              //  fileList.add(listFile[i]);
                if (listFile[i].isDirectory()) {
                    getfile(listFile[i]);

                } else {

                    boolean booleanpdf = false;
                   if (listFile[i].getName().endsWith(".pdf")) {

                        for (int j = 0; j < fileList.size(); j++) {
                            if (fileList.get(j).getName().equals(listFile[i].getName())) {
                                booleanpdf = true;
                            } else {

                            }
                        }

                        if (booleanpdf) {
                            booleanpdf = false;
                        } else {
                            fileList.add(listFile[i]);
                        }
                   }
                }
            }
        }
        return fileList;
    }
    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MagazineActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MagazineActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;

            getfile(dir);

            obj_adapter = new PDFAdapter(getApplicationContext(), fileList);
            lv_pdf.setAdapter(obj_adapter);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;
                getfile(dir);

                obj_adapter = new PDFAdapter(getApplicationContext(), fileList);
                lv_pdf.setAdapter(obj_adapter);

            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        try{
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,user,pass);

            String result = "Database connected successfully";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM pdf_details ");
            try {
                while (rs.next()) {
                    String fileUrl = rs.getString(7);
                    String filename = rs.getString(6);
                    Log.e("FLAG", fileUrl);
                    File file = new File(Environment.getExternalStorageDirectory()+"/Android/data/com.ortusolis.emag/files/Ananya_pdf_files/"+filename);


                    if( file != null && file.exists()){
                        Toast.makeText(getApplicationContext(),
                        " Magazine is Ready",
                        Toast.LENGTH_SHORT).show();
                        Log.e("FLAG", file.toString());
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                " Magazine is on the way",
                                Toast.LENGTH_SHORT).show();
                        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse("http://13.233.81.45/"+fileUrl);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle(filename);
                        request.setNotificationVisibility(1);
                        request.allowScanningByMediaScanner();
                        request.setMimeType("application/pdf");
                        request.setDestinationInExternalFilesDir(this, "/Ananya_pdf_files", filename);
                      //  request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        long reference = downloadManager.enqueue(request);
                    }
                }
            } finally {
                rs.close();
            }
            Log.e("FLAG", rs.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
