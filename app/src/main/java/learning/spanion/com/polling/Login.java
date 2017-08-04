package learning.spanion.com.polling;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Ankit Kumar on 7/12/2017.
 */

public class Login extends Activity {

    ImageView visibility;
    LoginDatabase logindb;
    Button login;
    EditText password, uniqueid;
    int flag=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        logindb = new LoginDatabase(this);

        login = (Button) findViewById(R.id.btLogin);
        password = (EditText) findViewById(R.id.etPassword);
        uniqueid = (EditText) findViewById(R.id.etUserName);
        visibility = (ImageView) findViewById(R.id.visibility);

        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0){
                    flag=1;
                    password.setTransformationMethod(null);
                    visibility.setImageResource(R.drawable.ic_visibility_on);
                }
                else {
                    flag=0;
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    visibility.setImageResource(R.drawable.ic_visibility_off);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int flag=1;
                String id= uniqueid.getText().toString();
                String pass=password.getText().toString();
                if(id.isEmpty() || pass.isEmpty()){
                    Toast.makeText(Login.this, "Don't leave it blank!", Toast.LENGTH_SHORT).show();
                }
                else if(id.equals("admin") && pass.equals("admin")){
                    Intent launchDatabase = new Intent(Login.this, ViewDatabase.class);
                    launchDatabase.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(launchDatabase);
                    overridePendingTransition( R.anim.activity_up, R.anim.activity_down);
                }
                else{
                    logindb.open();
                    Cursor c=logindb.executeQuery("SELECT * FROM userinfo WHERE UNIQUEID='"+id+"' AND PASS='"+pass+"'");
                    String dbUser=null;
                    String dbPass=null;
                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                        dbUser=c.getString(0).toString();
                        dbPass=c.getString(1).toString();
                        if(id.equals(dbUser) && pass.equals(dbPass)){
                            Toast.makeText(Login.this, "Log in successful!", Toast.LENGTH_SHORT).show();
                            Intent launchPolling = new Intent(Login.this, Polling.class);
                            launchPolling.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            launchPolling.putExtra("id", dbUser);
                            startActivity(launchPolling);
                            overridePendingTransition( R.anim.activity_up_2, R.anim.activity_down_2);
                            flag=0;
                            break;
                        }
                    }
                    if(flag==1){
                        Toast.makeText(getApplicationContext(),"Unique Id and password do not match!",Toast.LENGTH_SHORT).show();
                    }
                    logindb.close();
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
