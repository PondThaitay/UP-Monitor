package com.cm_smarthome.myservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by AdminPond on 18/4/2558.
 */
public class Sqlite extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mydatabase";

    // Table Name
    private static final String TABLE_NAME = "them";

    public Sqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(MemberID INTEGER PRIMARY KEY," +
                " Name TEXT(100)," +
                " Status TEXT(2)," +
                " Flag TEXT(2));");

        Log.d("CREATE TABLE", "Create Table Successfully.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    // Insert Data
    public long InsertData() {
        // TODO Auto-generated method stub

        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("MemberID", "1");
            Val.put("Name", "them");
            Val.put("Status", "0");
            Val.put("Flag", "1");

            long rows = db.insert(TABLE_NAME, null, Val);

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    // Select Data
    public String[] SelectData(String strMemberID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            Cursor cursor = db.query(TABLE_NAME, new String[]{"*"}, "MemberID=?",
                    new String[]{String.valueOf(strMemberID)}, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    // Update Data
    public long UpdateData(String strMemberID, String strName, String strStatus) {
        // TODO Auto-generated method stub

        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("Name", strName);
            Val.put("Status", strStatus);

            long rows = db.update(TABLE_NAME, Val, " MemberID = ?",
                    new String[]{String.valueOf(strMemberID)});

            db.close();
            return rows; // return rows updated.

        } catch (Exception e) {
            return -1;
        }
    }

    // Update Data(Status)
    public long UpdateData(String strMemberID, String strStatus) {
        // TODO Auto-generated method stub

        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("Status", strStatus);

            long rows = db.update(TABLE_NAME, Val, " MemberID = ?",
                    new String[]{String.valueOf(strMemberID)});

            db.close();
            return rows; // return rows updated.

        } catch (Exception e) {
            return -1;
        }
    }

    // Update Data(Status)
    public long UpdateDataFlag(String strMemberID, String strFlag) {
        // TODO Auto-generated method stub

        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("Flag", strFlag);

            long rows = db.update(TABLE_NAME, Val, " MemberID = ?",
                    new String[]{String.valueOf(strMemberID)});

            db.close();
            return rows; // return rows updated.

        } catch (Exception e) {
            return -1;
        }
    }
}
