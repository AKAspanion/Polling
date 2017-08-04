package learning.spanion.com.polling;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

/**
 * Created by Ankit Kumar on 7/12/2017.
 */

public class FrontPage extends Activity{

    VideoView myVideoView;
    Button login, signup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.frontpage);
        login = (Button) findViewById(R.id.btLogin);
        signup = (Button) findViewById(R.id.btSignUp);
        startVideo();

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launchLogin = new Intent("learning.spanion.com.polling.LOGIN");
                startActivity(launchLogin);
                overridePendingTransition( R.anim.activity_up_2, R.anim.activity_down_2);
            }
        });
        signup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent launchSignUp = new Intent("learning.spanion.com.polling.SIGNUP");
                startActivity(launchSignUp);
                overridePendingTransition( R.anim.activity_up_2, R.anim.activity_down_2);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        startVideo();
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.setNegativeButton("No", null);
        alert.setMessage("Do you want to exit?");
        alert.setTitle(R.string.app_name);
        alert.show();
    }

    void startVideo(){
        myVideoView = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.back);
        myVideoView.setVideoURI(uri);
        myVideoView.start();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }
}
