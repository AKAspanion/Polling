package learning.spanion.com.polling;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Ankit Kumar on 7/12/2017.
 */

public class Polling extends Activity {
    LoginDatabase logindb;
    TextView tvId,tvName,tvEmail,tvPhone,tvIsCasted,tvAdd;
    Button logout,vote,update;
    String id, isVoteCasted="no";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.polling);

        logindb = new LoginDatabase(this);

        tvId=(TextView) findViewById(R.id.tvId);
        tvName=(TextView) findViewById(R.id.tvName);
        tvEmail=(TextView) findViewById(R.id.tvEmail);
        tvPhone=(TextView) findViewById(R.id.tvPhone);
        tvIsCasted=(TextView) findViewById(R.id.tvIsCasted);
        tvAdd=(TextView) findViewById(R.id.tvAdd);
        logout = (Button) findViewById(R.id.btLogOut);
        vote = (Button) findViewById(R.id.btCaste);
        update = (Button) findViewById(R.id.btUpdate);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        tvId.setText("ID: "+id);

        saveId();
        fillTVs();

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchCasteVote = new Intent(Polling.this, CasteVote.class);
                launchCasteVote.putExtra("id",id);
                startActivity(launchCasteVote);
                overridePendingTransition( R.anim.activity_up, R.anim.activity_down);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchUpdate = new Intent(Polling.this, Update.class);
                launchUpdate.putExtra("id",id);
                startActivity(launchUpdate);
                overridePendingTransition( R.anim.activity_up, R.anim.activity_down);
            }
        });
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                signOutAlert();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillTVs();
    }

    @Override
    public void onBackPressed() {
        signOutAlert();
    }

    private void signOutAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent launchFrontPage = new Intent("learning.spanion.com.polling.FRONTPAGE");
                launchFrontPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(launchFrontPage);
                clearID();
                Toast.makeText(Polling.this, "Log Out Successful!", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.setMessage("Do you want to logout?");
        alert.setTitle(R.string.app_name);
        alert.show();
    }
    public void fillTVs(){
        logindb.open();
        Cursor c=logindb.executeQuery("SELECT * FROM userinfo WHERE UNIQUEID='"+id+"'");
        String dbUser=null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            dbUser=c.getString(0);
            if(id.equals(dbUser)){
                tvName.setText("Welcome, "+c.getString(2)+" "+c.getString(3));
                tvAdd.setText("Address: "+c.getString(4)+"");
                tvEmail.setText("Email: "+c.getString(5)+"");
                tvPhone.setText("Phone: "+c.getString(6)+"");
                isVoteCasted = c.getString(7);
                break;
            }
        }
        c.close();
        logindb.close();

        if(isVoteCasted.equals("no")) {
            tvIsCasted.setText("You have not casted your vote.\nPlease tap the button below to Vote.");
        }
        else{
            tvIsCasted.setText("Thanks for voting!\nYou are helping build a better future.");
            vote.setVisibility(View.GONE);
        }
    }
    public void saveId(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", id);
        editor.apply();
    }
    public void clearID(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", "-_-");
        editor.apply();
    }
}
