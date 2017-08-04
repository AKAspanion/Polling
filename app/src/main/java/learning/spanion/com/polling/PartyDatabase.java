package learning.spanion.com.polling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by Ankit Kumar on 7/14/2017.
 */

public class PartyDatabase {
    public static final String DATABASE_NAME = "party";
    private static final int DATABASE_VERSION = 1;

    private PartyDatabase.DataBaseHelper myHelper;
    private final Context myContext;
    private SQLiteDatabase myDataBase;
    private class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS partyinform(PARTYNAME VARCHAR(30) PRIMARY KEY,"+
                    "VOTECOUNT INTEGER NOT NULL);");
            db.execSQL("INSERT INTO partyinform VALUES('BSP',0)");
            db.execSQL("INSERT INTO partyinform VALUES('BJP',0)");
            db.execSQL("INSERT INTO partyinform VALUES('CPI',0)");
            db.execSQL("INSERT INTO partyinform VALUES('INC',0)");
            db.execSQL("INSERT INTO partyinform VALUES('NCP',0)");
            db.execSQL("INSERT INTO partyinform VALUES('AITC',0)");
            db.execSQL("INSERT INTO partyinform VALUES('NOTA',0)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }
    public PartyDatabase(Context context){
        myContext = context;
    }

    public void close(){
        myHelper.close();
    }


    public PartyDatabase open() {
        myHelper = new DataBaseHelper(myContext);
        myDataBase = myHelper.getWritableDatabase();
        return this;
    }

    public boolean updateParty(String name, int count) {
        ContentValues cv = new ContentValues();
        cv.put("votecount", count);
        return myDataBase.update("partyinform", cv, "partyname='"+name+"'", null) > 0;
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
