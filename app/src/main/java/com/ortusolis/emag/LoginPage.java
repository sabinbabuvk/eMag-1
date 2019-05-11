package com.ortusolis.emag;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginPage extends AppCompatActivity {
    private static final String url ="jdbc:mysql://13.233.81.45:3306/e_mag";
    private  static final String user = "ortusolis";
    private static final String pass = "Ortusolis@123";
    private Button Login;
    private EditText emailid;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        emailid = (EditText) findViewById(R.id.btnemailid);
        Login = (Button) findViewById(R.id.btnLogin);
        Login.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                String getText=emailid.getText().toString();
                String Expn =

                        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"

                                +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"

                                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."

                                +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"

                                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"

                                +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
                if (getText.matches(Expn) && getText.length() > 0)

                {
                    LoginOn(emailid.getText().toString());
                  //  textviewMessage.setText("valid email");
                    Toast.makeText(getApplicationContext(),
                            " valid email",
                            Toast.LENGTH_SHORT).show();
                }

                else

                {
                    Toast.makeText(getApplicationContext(),
                            " invalid email",
                            Toast.LENGTH_SHORT).show();
                  //  textviewMessage.setText("invalid email");

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}


        }));

    }

    public void LoginOn(String emailid){
        try{
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,user,pass);

            String result = "Database connected successfully";

            Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT EmailId FROM subscribers_details where EmailId='"+emailid+"'");
            if(rs.next()){
                Log.e("FLAG",rs.getString(1));
                Toast.makeText(getApplicationContext(),"Offical member "+emailid+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Log.e("FLAG", emailid.toString());
                st.executeUpdate("INSERT INTO subscribers_details (SubType,SubTitle,FirstName,MiddleName,LastName,EmailId,PhoneNum1,PhoneNum2,Address,State,City,PinCode,Password,SubStartDate,SubEndDate) VALUES ('','','','','','" + emailid + "','3256898745','2132564587','','','','235689','','0000-00-00','0000-00-00');");
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

