package com.bucs.bupc.art.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bucs.bupc.art.database.SQLiteDatabaseHandler;
import com.bucs.bupc.art.entity.DownloadHistory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
public class DownloadHistoryDAO {

    private Context context;
    private SQLiteDatabase database;
    private SQLiteDatabaseHandler sqliteDbHandler;

    private String EQUALS = "=";

    public DownloadHistoryDAO(Context context) {
        this.context = context;
        sqliteDbHandler = new SQLiteDatabaseHandler(context);
    }

    /**
     *
     * @return Download History List
     * @throws SQLException
     */
    public List<DownloadHistory> findAll() throws SQLException {
        openDatabase();
        Cursor cursor = database.query(DownloadHistory.TABLE_NAME,
                DownloadHistory.DB_COLUMNS, null, null, null, null, DownloadHistory.ID + " ASC");
        List<DownloadHistory> downloadHistoryList = convertCursorToList(cursor);
        closeDatabase();

        return downloadHistoryList;
    }

    /**
     *
     * @param id the id of item to be find
     * @return List of DownloadHistory
     * @throws SQLException
     */
    public List<DownloadHistory> findAllById(int id) throws SQLException {
        openDatabase();
        Cursor cursor = database.query(DownloadHistory.TABLE_NAME, DownloadHistory.DB_COLUMNS,
                DownloadHistory.ID + EQUALS + id, null, null, null, null);
        List<DownloadHistory> downloadHistoryList = convertCursorToList(cursor);
        closeDatabase();

        return downloadHistoryList;
    }

    /**
     *
     * @param item item DownloadHistoryItem to be deleted
     * @return true if successful otherwise false if error occurred
     * @throws SQLException
     */
    public boolean delete(DownloadHistory item) throws SQLException {
        openDatabase();
        boolean result = database.delete(DownloadHistory.TABLE_NAME,
                DownloadHistory.ID + EQUALS + item.getId(), null) > 0 ? true : false;
        closeDatabase();

        return result;
    }

    /**
     *
     * @param item the item ID to be deleted
     * @return boolean
     * @throws SQLException
     */
    public boolean delete(int item) throws SQLException {
        openDatabase();
        boolean result = database.delete(DownloadHistory.TABLE_NAME,
                DownloadHistory.ID + EQUALS + item, null) > 0 ? true : false;
        closeDatabase();

        return result;
    }

    /**
     *
     * @param downloadHistory the object item to be inserted
     * @return -1 if fail otherwise item successfully inserted to database
     * @throws SQLException
     */
    public long insert(DownloadHistory downloadHistory) throws SQLException {
        openDatabase();
        long result = database.insert(DownloadHistory.TABLE_NAME, null, getContentValues(downloadHistory));
        closeDatabase();
        return result;
    }

    public ContentValues getContentValues(DownloadHistory item) {
        ContentValues values = new ContentValues();
        values.put(DownloadHistory.RESEARCH_TITLE, item.getResearchTitle());
        values.put(DownloadHistory.DATE_DOWNLOADED, item.getDateDownload());
        values.put(DownloadHistory.PDF_URI_LOCATION, item.getPdfUriLocation());

        return values;
    }

    public List<DownloadHistory> convertCursorToList(Cursor cursor) {
        List<DownloadHistory> bookResearchList = new ArrayList<DownloadHistory>();
        if(cursor.moveToFirst()) {
            do {
                bookResearchList.add(new DownloadHistory(cursor));
            } while (cursor.moveToNext());
        }

        return bookResearchList;
    }

    /**
     * Open database connection for CRUD operation
     * @throws SQLException
     */
    private void openDatabase() throws SQLException {
        database = sqliteDbHandler.getWritableDatabase();
    }

    /**
     * Close database connection
     */
    private void closeDatabase() {
        sqliteDbHandler.close();
    }

}
