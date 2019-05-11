package com.ortusolis.emag;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MagazineActivity extends Activity {
    private Button Home;
    private Button Share;
    private Button Feedback;
    private static final String url = "jdbc:mysql://13.233.81.45:3306/e_mag";
    private static final String user = "ortusolis";
    private static final String pass = "Ortusolis@123";
    public static ArrayList<String> dename = new ArrayList<>();
    ListView listView;
    TextView textView;

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
        listView = (ListView) findViewById(R.id.lv_pdf);
        textView = (TextView) findViewById(R.id.textView);

        dename.clear();

        try {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);

            String result = "Database connected successfully";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT File_Name FROM pdf_details ");
            try {
                while (rs.next()) {
                    Log.e("Flag db ", rs.getString(1));
                    dename.add(rs.getString(1));
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
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, dename);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                Intent newActivity2 = new Intent(MagazineActivity.this, Magazine2Activity.class);
                newActivity2.putExtra("fileName", value);
                startActivity(newActivity2);
            }
        });
    }
}


