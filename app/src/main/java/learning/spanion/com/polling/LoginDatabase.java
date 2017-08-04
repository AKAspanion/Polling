package learning.spanion.com.polling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by Ankit Kumar on 7/13/2017.
 */

public class LoginDatabase {
    public static final String DATABASE_NAME = "logindba";
    private static final int DATABASE_VERSION = 1;

    private DataBaseHelper myHelper;
    private final Context myContext;
    private SQLiteDatabase myDataBase;

    private class DataBaseHelper extends SQLiteOpenHelper{

        public DataBaseHelper(Context context){
            super(context,DATABASE_NAME ,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS userinfo(UNIQUEID VARCHAR(30) PRIMARY KEY," +
                    "PASS VARCHAR(30) NOT NULL," +
                    "FIRSTNAME VARCHAR(30) NOT NULL," +
                    "LASTNAME VARCHAR(30) NOT NULL," +
                    "ADDRESS VARCHAR(30) NOT NULL," +
                    "EMAIL VARCHAR(30) NOT NULL," +
                    "PHONE VARCHAR(30) NOT NULL," +
                    "VOTE VARCHAR(30) NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }
    public LoginDatabase(Context c){
        myContext=c;
    }
    public LoginDatabase open() {
        myHelper = new DataBaseHelper(myContext);
        myDataBase = myHelper.getWritableDatabase();
        return this;
    }

    public boolean updateLogin(String id) {
        ContentValues cv = new ContentValues();
        cv.put("vote", "yes");
        return myDataBase.update("userinfo", cv, "uniqueid="+ id, null) > 0;
    }

    public  boolean updateData(String id, String firstname, String lastname, String address, String email, String phone){
        ContentValues cv = new ContentValues();
        cv.put("firstname", firstname);
        cv.put("lastname", lastname);
        cv.put("address", address);
        cv.put("email", email);
        cv.put("phone", phone);
        return myDataBase.update("userinfo", cv, "uniqueid="+ id, null) > 0;
    }

    public void close(){
        myHelper.close();
    }

    public void executeNonQuery(String query) {
        myDataBase.execSQL(query);
    }


    public Cursor executeQuery(String query) {
        Cursor c = myDataBase.rawQuery(query, null);
        return c;
    }
    public static boolean doesDatabaseExist(Context context, String dbname){
        File dbFile = context.getDatabasePath(dbname);
        return dbFile.exists();
    }
}
