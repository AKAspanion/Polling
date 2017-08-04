package learning.spanion.com.polling;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ankit Kumar on 7/17/2017.
 */

public class ViewVoteResults extends Activity {
    LoginDatabase logindb;
    PartyDatabase partydb;
    LinearLayout linearlayout;
    int delay=200;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.viewvoteresults);

        partydb = new PartyDatabase(this);
        logindb = new LoginDatabase(this);

        linearlayout = (LinearLayout) findViewById(R.id.llContainer);
        linearlayout.setOrientation(LinearLayout.VERTICAL);
        viewResults();
    }
    public void viewResults(){
        int peopleCount=0;
        logindb.open();
        Cursor cLogin = logindb.executeQuery("SELECT * FROM userinfo");
        peopleCount = cLogin.getCount();
        logindb.close();
        partydb.open();
        Cursor cParty = partydb.executeQuery("SELECT * FROM partyinform");
        String partyName, votes;
        int count=0,voteCount=0;
        if(cParty.getCount()==0){
            Toast.makeText(ViewVoteResults.this, "No Data Found!", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cParty.moveToNext()){
                count++;
                partyName=cParty.getString(0);
                votes=cParty.getString(1);
                voteCount = Integer.parseInt(votes)+voteCount;
                showVotes(partyName,votes,count);
            }
            count++;
            showVotes("Total No of votes",Integer.toString(voteCount),count);
            count++;
            showVotes("Total No of people",Integer.toString(peopleCount),count);
        }
        partydb.close();
    }

    public void showVotes(String name, String votes, final int count){
        LinearLayout.LayoutParams lpForInnerLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if(count==8||count==9){
            lpForInnerLayout.setMargins(20,30,20,10);
        }
        else{
            lpForInnerLayout.setMargins(20,10,20,10);
        }
        final CardView cardView = new CardView(this);
        cardView.setRadius(3);
        cardView.setLayoutParams(lpForInnerLayout);

        linearlayout.addView(cardView);
        final LinearLayout innerLinearLayout = new LinearLayout(this);
        innerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerLinearLayout.setLayoutParams(lpForInnerLayout);

        if(count==8||count==9){
            innerLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        else{
            innerLinearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        cardView.addView(innerLinearLayout);

        LinearLayout.LayoutParams lpForTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lpForTextView.setMargins(10,10,10,10);
        final TextView textView = new TextView(this);
        final TextView textView1 = new TextView(this);
        if(count==8||count==9){
            textView.setText(name);
            textView.setPadding(20,20,30,20);
            textView1.setPadding(20,20,30,20);
            textView.setTextSize(18);
            textView1.setTextSize(18);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            textView1.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else {
            textView.setText(getFullName(name));
            textView.setPadding(30,10,30,10);
            textView1.setPadding(30,10,30,10);
            textView.setTextSize(15);
            textView1.setTextSize(15);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            textView1.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        textView.setLayoutParams(lpForTextView);
        textView1.setText(votes);
        textView1.setLayoutParams(lpForTextView);
        textView1.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        innerLinearLayout.addView(textView);
        innerLinearLayout.addView(textView1);
        Animation animation;
        if(count==8||count==9){
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        }
        else {
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_off_reverse);
        }
        cardView.startAnimation(animation);
        cardView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation;
                if(count==8 || count==9){
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                }
                else{
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_reverse);
                }
                cardView.startAnimation(animation);
            }
        }, delay*count);
    }
    public String getFullName(String s){
        switch (s){
            case "BSP": return  "Bahujan Samaj Party";
            case "BJP": return  "Bharatiya Janata Party";
            case "CPI": return  "Communist Party of India";
            case "INC": return  "Indian National Congress";
            case "AITC": return  "All India Trinamool Congress";
            case "NCP": return  "Nationalist Congress Party";
            case "NOTA": return  "None of the above";
        }
        return "";
    }
}
