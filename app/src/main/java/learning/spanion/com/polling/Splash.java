package learning.spanion.com.polling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Ankit Kumar on 7/11/2017.
 */

public class Splash extends Activity{

    SharedPreferences sharedPreferences;
    String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = sharedPreferences.getString("uid", "_");

        Thread splashTimer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if(id.equals("_") || id.equals("-_-")) {
                        Intent openLoginPage = new Intent("learning.spanion.com.polling.FRONTPAGE");
                        startActivity(openLoginPage);
                    }
                    else{
                        Intent openPolling = new Intent("learning.spanion.com.polling.POLLING");
                        openPolling.putExtra("id", id);
                        startActivity(openPolling);
                        overridePendingTransition( R.anim.activity_up_2, R.anim.activity_down_2);
                    }
                }
            }
        };
        splashTimer.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
