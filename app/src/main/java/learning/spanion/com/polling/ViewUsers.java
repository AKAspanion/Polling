package learning.spanion.com.polling;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ankit Kumar on 7/17/2017.
 */

public class ViewUsers extends Activity {
    LoginDatabase logindb;
    LinearLayout linearlayout;
    int delay=400;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.viewusers);

        linearlayout = (LinearLayout) findViewById(R.id.llContainer);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        logindb = new LoginDatabase(this);

        viewUsers();
    }

    private void viewUsers() {
        logindb.open();
        int count=0;
        StringBuffer buffer = new StringBuffer();
        Cursor cLogin = logindb.executeQuery("SELECT * FROM userinfo");
        if(cLogin.getCount()==0){
            showUsers("No Users Found", count+1);
        }
        else{
            while(cLogin.moveToNext()){
                count++;
                buffer.append("ID: \t"+ cLogin.getString(0)+
                        "\nName: \t"+ cLogin.getString(2)+ " " + cLogin.getString(3)+
                        "\nAddress: \t"+ cLogin.getString(4)+
                        "\nEmail: \t"+ cLogin.getString(5)+
                        "\nPhone: \t"+ cLogin.getString(6)+
                        "\nVoted: \t"+ cLogin.getString(7));
                showUsers(buffer.toString(), count);
                buffer.delete(0, buffer.length());
            }
            showUsers("**No More Users**", ++count);
        }
    }

    private void showUsers(String content, int count) {
        LinearLayout.LayoutParams lpForInnerLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpForInnerLayout.setMargins(30,15,30,15);

        final CardView cardView = new CardView(this);
        cardView.setRadius(3);
        cardView.setLayoutParams(lpForInnerLayout);
        linearlayout.addView(cardView);

        final LinearLayout innerLinearLayout = new LinearLayout(this);
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout.setLayoutParams(lpForInnerLayout);
        innerLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        cardView.addView(innerLinearLayout);

        LinearLayout.LayoutParams lpForTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lpForTextView.setMargins(10,10,10,10);

        final TextView textView = new TextView(this);
        textView.setText(content);
        textView.setPadding(30,30,30,30);
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        innerLinearLayout.addView(textView);

        Animation animation;
        if(count%2==0){
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_off);
            cardView.startAnimation(animation);
            cardView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation;
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
                    cardView.startAnimation(animation);
                }
            }, delay*count);
        }
        else {
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_off_reverse);
            cardView.startAnimation(animation);
            cardView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation;
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_reverse);
                    cardView.startAnimation(animation);
                }
            }, delay*count);
        }


    }
}
