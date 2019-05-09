package com.ortusolis.emag;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {
    private Button Home;
    private Button Magazine;
    private Button Share;
    private Button Feedback;
    ListView listView;
    TextView textView;
    String[] listItem;
    ArrayList<File> fileListShare = new ArrayList<>();
    File dir;
    String[] listItemshare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        this.setTitle("Share");
        Magazine2Activity inst2 = new Magazine2Activity();
        String link1= inst2.linkuse();
        MagazineActivity inst = new MagazineActivity();


        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.ortusolis.emag/files/Ananya_pdf_files/");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.ortusolis.emag/files/Ananya_pdf_files";

        File f = new File(path);
        File file[] = f.listFiles();
        int index=0;
        for(int i=0;i<file.length;i++){
            Log.e("Rest file success", file[i].getName());
          listItemshare[index] =file[i].getName();
            Log.e("Rest file2  success", listItemshare[index]);
        }
 //       menuArray = getResources().getStringArray(R.);



        listView=(ListView)findViewById(R.id.listViewShare);
       // listItem = getResources().getStringArray( );
        fileListShare=inst.getfile(dir);





        final ArrayAdapter<String> adaptershare = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItemshare);
        listView.setAdapter(adaptershare);










        // sendMessage("whats_up");
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
     //   whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        startActivity(Intent.createChooser(whatsappIntent, "share to:"));

        Magazine = (Button) findViewById(R.id.magazine);
        Magazine.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareActivity.this, MagazineActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Home = (Button) findViewById(R.id.home);
        Home.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareActivity.this, MainActivity.class);
                startActivity(intent);
//                finish();
            }
        }));
        Feedback = (Button) findViewById(R.id.feedback);
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
