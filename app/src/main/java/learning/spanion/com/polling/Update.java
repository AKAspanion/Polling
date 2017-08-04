package learning.spanion.com.polling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Ankit Kumar on 04-08-2017.
 */

public class Update extends Activity {

    EditText  firstName, lastName, address, email, phone;
    String id;
    Button confirm;
    LoginDatabase logindb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.update);

        firstName = (EditText) findViewById(R.id.etFirstNameU);
        lastName = (EditText) findViewById(R.id.etLastNameU);
        address = (EditText) findViewById(R.id.etAddLine1U);
        email = (EditText) findViewById(R.id.etEmailU);
        phone = (EditText) findViewById(R.id.etPhoneU);

        confirm = (Button) findViewById(R.id.btConfirm);

        logindb = new LoginDatabase(this);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstName.getText().toString().isEmpty()||lastName.getText().toString().isEmpty()||
                        address.getText().toString().isEmpty()||email.getText().toString().isEmpty()||
                        phone.getText().toString().isEmpty()){
                    Toast.makeText(Update.this, "You can't leave anything empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(checkForEmail(email.getText().toString())){
                        if(checkPhoneNo(phone.getText().toString())) {
                            logindb.open();
                            if(logindb.updateData(id, firstName.getText().toString(),lastName.getText().toString(),
                                    address.getText().toString(),email.getText().toString(),phone.getText().toString())){
                                Toast.makeText(Update.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                                Intent launchPolling = new Intent(Update.this, Polling.class);
                                launchPolling.putExtra("id",id);
                                launchPolling.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(launchPolling);
                                overridePendingTransition( R.anim.activity_up, R.anim.activity_down);
                            }
                            else {
                                Toast.makeText(Update.this, "Error Updating!", Toast.LENGTH_SHORT).show();
                            }
                            logindb.close();

                        }
                        else{
                            Toast.makeText(Update.this, "Phone no should be 10 digit long!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Update.this, "Please enter valid email id!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
    private boolean checkForEmail(String email){
        if(email.contains("@")&& email.contains("."))
            return true;
        return false;
    }
    private boolean checkPhoneNo(String no){
        if(no.length()==10){
            return true;
        }
        return false;
    }
}
