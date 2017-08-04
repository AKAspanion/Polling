package learning.spanion.com.polling;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Ankit Kumar on 7/14/2017.
 */

public class CasteVote extends Activity{
    LoginDatabase logindb;
    PartyDatabase partydb;
    RadioGroup rg;
    RadioButton rb;
    String id;
    String name=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.castevote);
        rg=(RadioGroup) findViewById(R.id.rgroup);

        logindb = new LoginDatabase(this);
        partydb = new PartyDatabase(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
    }
    public void rbclick(View v){
        int radioID=rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(radioID);

        switch (rb.getText().toString()){
            case "Bahujan Samaj Party":
                name="BSP";
                break;
            case "Bharatiya Janata Party":
                name="BJP";
                break;
            case "Communist Party of India":
                name="CPI";
                break;
            case "Indian National Congress":
                name="INC";
                break;
            case "Nationalist Congress Party":
                name="NCP";
                break;
            case "All India Trinamool Congress":
                name="AITC";
                break;
            case "None of the above":
                name="NOTA";
                break;
            default:
                name="NOTA";
                break;
        }
        showAlertBox();
    }
    public void showAlertBox(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDatabase();
            }
        });
        alert.setNegativeButton("No", null);
        alert.setMessage("Do you want to vote this party?");
        alert.setTitle(R.string.app_name);
        alert.show();
    }
    public void updateDatabase(){


        partydb.open();
        Cursor c=partydb.executeQuery("SELECT * FROM partyinform WHERE PARTYNAME='"+name+"'");
        String dbname=null;
        int count=0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            dbname = c.getString(0).toString();
            if(dbname.equals(name)){
                count=c.getInt(1);
                count++;
                break;
            }
        }
        c.close();
        partydb.updateParty(name,count);
        logindb.open();
        logindb.updateLogin(id);

        logindb.close();
        partydb.close();

        Toast.makeText(CasteVote.this, "Voting Successful!", Toast.LENGTH_SHORT).show();
        Intent launchPolling = new Intent(CasteVote.this, Polling.class);
        launchPolling.putExtra("id",id);
        launchPolling.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(launchPolling);

    }
}
