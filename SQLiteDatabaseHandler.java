package com.bucs.bupc.art.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bucs.bupc.art.entity.BookResearch;
import com.bucs.bupc.art.entity.DownloadHistory;

/**
 * Created by John Paul Cas on 1/31/2016.
 *
 * @author John Paul Cas
 * @since January 2016
 *
 * connect to <i> John Paul Cas </i> on facebook <br />
 * <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 *
 */

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "researchtool";
    public static final int DATABASE_VERSION = 1;

    /**
     * Instantiate SQLiteDatabaseHandler setting
     * @param context
     */
    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BookResearch.getTableStructure());
        db.execSQL(DownloadHistory.getTableStructure());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BookResearch.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DownloadHistory.TABLE_NAME);

        onCreate(db);
    }
}
