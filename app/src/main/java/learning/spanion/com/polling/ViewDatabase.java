package learning.spanion.com.polling;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Ankit Kumar on 7/15/2017.
 */

public class ViewDatabase extends Activity{
    Button userdb,votedb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.viewdatabase);

        userdb = (Button) findViewById(R.id.btUserDB);
        votedb = (Button) findViewById(R.id.btVoteDB);

        votedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showVoteReults = new Intent(ViewDatabase.this, ViewVoteResults.class);
                startActivity(showVoteReults);
                overridePendingTransition( R.anim.activity_up, R.anim.activity_down );
            }
        });
        userdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showUsers = new Intent(ViewDatabase.this, ViewUsers.class);
                startActivity(showUsers);
                overridePendingTransition( R.anim.activity_up, R.anim.activity_down );
            }
        });
    }



    @Override
    public void onBackPressed() {
        signoutAlert();
    }
    private void signoutAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent launchFrontPage = new Intent("learning.spanion.com.polling.FRONTPAGE");
                launchFrontPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(launchFrontPage);
                Toast.makeText(ViewDatabase.this, "Log Out Successful!", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("No", null);
        alert.setMessage("Do you want to logout?");
        alert.setTitle(R.string.app_name);
        alert.show();
    }
}
