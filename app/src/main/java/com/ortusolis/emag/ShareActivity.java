package com.ortusolis.emag;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ShareActivity extends AppCompatActivity {
    private Button Home;
    private Button Magazine;
    private Button Share;
    private Button Feedback;
    private static final String url ="jdbc:mysql://13.233.81.45:3306/e_mag";
    private  static final String user = "ortusolis";
    private static final String pass = "Ortusolis@123";
    ListView listView;
    TextView textView;
    String[] listItem;
    ArrayList<File> fileListShare = new ArrayList<>();
    File dir;
    ArrayList<String> listItemshare = new ArrayList<>();
   // String[] listItemshare= new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        this.setTitle("Share");
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
    //    whatsappIntent.putExtra(Intent.EXTRA_TEXT," https://play.google.com/store/apps/details?id=com.ortusolis.emag");
        //   whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.ortusolis.emag The Beautiful Magazine For You Kannadiga , share");
        startActivity(Intent.createChooser(whatsappIntent, "share to:"));
     //   Magazine2Activity inst2 = new Magazine2Activity();
//        String link1= inst2.linkuse();
//        MagazineActivity inst = new MagazineActivity();


//      //  dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.ortusolis.emag/files/Ananya_pdf_files/");
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.ortusolis.emag/files/Ananya_pdf_files/";
//
//        File f = new File(path);
//        File file[] = f.listFiles();
//        int index=0;
//
//        for(int i=0;i<=file.length-1;i++){
//            Log.e("FLAG", file[i].getName());
//         listItemshare.add(file[i].getName()) ;
//         //   Log.e("Rest file2  success", listItemshare);
//        }
// //       menuArray = getResources().getStringArray(R.);
//
//
//
//        listView=(ListView)findViewById(R.id.listViewShare);
//        textView=(TextView)findViewById(R.id.textView);
//      //  fileListShare=inst.getfile(dir);
//
//
//
//
//
//        final ArrayAdapter<String> adaptershare = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, listItemshare);
//        listView.setAdapter(adaptershare);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                // TODO Auto-generated method stub
//                String value=adaptershare.getItem(position);
//                //db
//                try{
//                    StrictMode.ThreadPolicy policy =
//                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                    StrictMode.setThreadPolicy(policy);
//
//                    Class.forName("com.mysql.jdbc.Driver");
//                    Connection conn = DriverManager.getConnection(url,user,pass);
//
//                    String result = "Database connected successfully";
//
//                    Statement st = conn.createStatement();
//                    ResultSet rs = st.executeQuery("SELECT * FROM pdf_details Where File_Name='"+value+"'");
//                    try {
//                        while (rs.next()) {
//                            String fileUrl = rs.getString(7);
////                            String filename = rs.getString(6);
//                            Log.e("FLAG","" +fileUrl+"");
//                           // File file = new File(Environment.getExternalStorageDirectory()+"/Android/data/com.ortusolis.emag/files/Ananya_pdf_files/"+filename);
//                            String linkmag="http://13.233.81.45/"+fileUrl;
//                            String body = "<a href=\"" +linkmag+ "\">" +linkmag+ "</a>";
//                            Log.e("FLAG", body);
////                            Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
//                            // sendMessage("whats_up");
//                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
//                            whatsappIntent.setType("text/plain");
//                            whatsappIntent.putExtra(Intent.EXTRA_TEXT,linkmag);
//                            //   whatsappIntent.setPackage("com.whatsapp");
//                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The Beautiful Magazine For You Kannadiga , share");
//                            startActivity(Intent.createChooser(whatsappIntent, "share to:"));
//                        }
//                    } finally {
//                        rs.close();
//                    }
//                    Log.e("FLAG", rs.toString());
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }


                // call db
//
//
//            }
//        });

        Magazine = (Button) findViewById(R.id.magazineShare);
        Magazine.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareActivity.this, MagazineActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Home = (Button) findViewById(R.id.homeShare);
        Home.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareActivity.this, MainActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Feedback = (Button) findViewById(R.id.feedbackShare);
        Feedback.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareActivity.this, FeedbackActivity.class);
                startActivity(intent);
        //        finish();
            }
        }));
    }

}
