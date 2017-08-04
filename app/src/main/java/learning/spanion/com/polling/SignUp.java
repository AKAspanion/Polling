package learning.spanion.com.polling;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Ankit Kumar on 7/12/2017.
 */

public class SignUp extends Activity {
    LoginDatabase logindb;
    ImageView visi1, visi2;
    Button signup;
    int flag1=0, flag2=0;
    CardView c1,c2,c3,c4,c5,c6,c7,c8;
    EditText id,pass1, pass2, firstName, lastName, address, email, phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup);
        logindb = new LoginDatabase(this);
        signup = (Button) findViewById(R.id.btSignUp);
        visi1 = (ImageView) findViewById(R.id.visibility1);
        visi2 = (ImageView) findViewById(R.id.visibility2);

        id = (EditText) findViewById(R.id.etId);
        pass1 = (EditText) findViewById(R.id.etPass1);
        pass2 = (EditText) findViewById(R.id.etPass2);
        firstName = (EditText) findViewById(R.id.etFirstName);
        lastName = (EditText) findViewById(R.id.etLastName);
        address = (EditText) findViewById(R.id.etAddLine1);
        email = (EditText) findViewById(R.id.etEmail);
        phone = (EditText) findViewById(R.id.etPhone);
        c1 = (CardView) findViewById(R.id.card_view);
        c2 = (CardView) findViewById(R.id.card_view_2_a);
        c3 = (CardView) findViewById(R.id.card_view_2_b);
        c4 = (CardView) findViewById(R.id.card_view_3_a);
        c5 = (CardView) findViewById(R.id.card_view_3_b);
        c6 = (CardView) findViewById(R.id.card_view_4);
        c7 = (CardView) findViewById(R.id.card_view_5);
        c8 = (CardView) findViewById(R.id.card_view_6);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_off);
        Animation buttonAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
        c1.startAnimation(animation);
        c2.startAnimation(animation);
        c3.startAnimation(animation);
        c4.startAnimation(animation);
        c5.startAnimation(animation);
        c6.startAnimation(animation);
        c7.startAnimation(animation);
        c8.startAnimation(animation);
        signup.startAnimation(buttonAnimation);

        View[] views = new View[]{c1,c2,c3,c4,c5,c6,c7,c8};
        showTextViews(views,signup);

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(id.getText().toString().isEmpty()||pass1.getText().toString().isEmpty()||pass2.getText().toString().isEmpty()||
                        firstName.getText().toString().isEmpty()||lastName.getText().toString().isEmpty()||
                        address.getText().toString().isEmpty()||email.getText().toString().isEmpty()||
                        phone.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this, "You can't leave anything empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(checkForEmail(email.getText().toString())){
                        if(checkPhoneNo(phone.getText().toString())) {
                            if (pass1.getText().toString().equals(pass2.getText().toString())) {
                                logindb.open();
                                Cursor c = logindb.executeQuery("SELECT * FROM userinfo WHERE UNIQUEID='" + id.getText().toString() + "'");
                                if (!c.moveToFirst()) {
                                    logindb.executeNonQuery("INSERT INTO userinfo VALUES('" +
                                            id.getText().toString() + "','" +
                                            pass1.getText().toString() + "','" +
                                            firstName.getText().toString() + "','" +
                                            lastName.getText().toString() + "','" +
                                            address.getText().toString() + "','" +
                                            email.getText().toString() + "','" +
                                            phone.getText().toString() + "','" +
                                            "no" + "')"
                                    );
                                    if (logindb.doesDatabaseExist(getApplicationContext(), "logindba")) {
                                        Toast.makeText(SignUp.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                                        Intent launchPolling = new Intent(SignUp.this, Polling.class);
                                        launchPolling.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        launchPolling.putExtra("id", id.getText().toString());
                                        startActivity(launchPolling);
                                        overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                                    }
                                    c.close();
                                } else {
                                    Toast.makeText(SignUp.this, "ID already Exists!", Toast.LENGTH_SHORT).show();
                                }
                                logindb.close();
                            } else {
                                pass1.requestFocus();
                                Toast.makeText(SignUp.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            phone.requestFocus();
                            Toast.makeText(SignUp.this, "Phone no should be 10 digit long!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        email.requestFocus();
                        Toast.makeText(SignUp.this, "Please enter valid email id!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        visi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag1==0){
                    flag1=1;
                    pass1.setTransformationMethod(null);
                    visi1.setImageResource(R.drawable.ic_visibility_on);
                }
                else {
                    flag1=0;
                    pass1.setTransformationMethod(new PasswordTransformationMethod());
                    visi1.setImageResource(R.drawable.ic_visibility_off);
                }
            }
        });
        visi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag2==0){
                    flag2=1;
                    pass2.setTransformationMethod(null);
                    visi2.setImageResource(R.drawable.ic_visibility_on);
                }
                else {
                    flag2=0;
                    pass2.setTransformationMethod(new PasswordTransformationMethod());
                    visi2.setImageResource(R.drawable.ic_visibility_off);
                }
            }
        });
    }

    private void showTextViews(View[] views, View button) {
        int delayBetween = 200;
        for(int i=1;i<views.length+1;i++){
            final View view = views[i-1];
            int delay=i*delayBetween;
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
                    view.startAnimation(animation);
                }
            }, delay);
        }
        final View view2=button;
        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animationButton = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                view2.startAnimation(animationButton);
            }
        }, delayBetween*10);
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
