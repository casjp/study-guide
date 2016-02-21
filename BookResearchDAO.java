package com.bucs.bupc.art.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bucs.bupc.art.database.SQLiteDatabaseHandler;
import com.bucs.bupc.art.entity.BookResearch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John Paul Cas on 1/31/2016.
 * @author John Paul Cas
 * @since January 2016
 *
 * connect to <i> John Paul Cas </i> on facebook
 * <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 *
 */
public class BookResearchDAO {

    private Context context;
    private SQLiteDatabase database;
    private SQLiteDatabaseHandler sqliteDbHandler;

    private String EQUALS = "=";

    public BookResearchDAO(Context context) {
        this.context = context;
        sqliteDbHandler = new SQLiteDatabaseHandler(context);
    }

    /**
     * Get all BookResearch item from database
     *
     * @return BookResearch list object
     * @throws SQLException
     */
    public List<BookResearch> findAll() throws SQLException {
        openDatabase();
        Cursor cursor = database.query(BookResearch.TABLE_NAME,
                BookResearch.DB_COLUMNS, null, null, null, null, BookResearch.RESEARCH_ID + " ASC");
        List<BookResearch> bookResearchList = convertCursorToList(cursor);
        closeDatabase();

        return bookResearchList;
    }

    /**
     * Get all the list of Book Research that equal to year specified
     * @param year the unique id of Book Research
     * @return List of Book Research
     * @throws SQLException
     */
    public List<BookResearch> findAllByCopyrightYear(int year) throws SQLException {
        openDatabase();
        Cursor cursor = database.query(BookResearch.TABLE_NAME, BookResearch.DB_COLUMNS,
                BookResearch.COPYRIGHT_YEAR + EQUALS + year, null, null, null, null);
        List<BookResearch> bookResearchList = convertCursorToList(cursor);
        closeDatabase();

        return bookResearchList;
    }

    /**
     * Permanently delete item into the database
     *
     * @param item the book research item to be deleted into the database
     * @return true if successful else false if an error occur while deleting item
     * @throws SQLException
     */
    public boolean delete(BookResearch item) throws SQLException {
        openDatabase();
        boolean result = database.delete(BookResearch.TABLE_NAME,
                BookResearch.RESEARCH_ID + EQUALS + item.getResearchId(), null) > 0 ? true : false;
        closeDatabase();

        return result;
    }

    /**
     *
     * @param bookResearch object to be inserted into the database
     * @return -1 if fail, else successful
     * @throws SQLException
     */
    public long insert(BookResearch bookResearch) throws SQLException {
        openDatabase();
        long result = database.insert(BookResearch.TABLE_NAME, null, getContentValues(bookResearch));
        closeDatabase();
        return result;
    }

    /**
     * Convert the cursor object to list object
     *
     * @param cursor the cursor to be converted to list
     * @return BookResearch List
     */
    public List<BookResearch> convertCursorToList(Cursor cursor) {
        List<BookResearch> bookResearchList = new ArrayList<BookResearch>();
        if(cursor.moveToFirst()) {
            do {
                bookResearchList.add(new BookResearch(cursor));
            } while (cursor.moveToNext());
        }

        return bookResearchList;
    }

    /**
     * Set the data Object content values
     *
     * @param item the BookResearch Object item
     * @return ContentValues
     */
    public ContentValues getContentValues(BookResearch item) {
        ContentValues values = new ContentValues();
        values.put(BookResearch.RESEARCH_TITLE, item.getResearchTitle());
        values.put(BookResearch.DEPARTMENT, item.getDepartment());
        values.put(BookResearch.PDF_FILE_NAME, item.getPdfFileName());
        values.put(BookResearch.DEPARTMENT, item.getDepartment());
        values.put(BookResearch.COPYRIGHT_YEAR, item.getCopyrightYear());
        values.put(BookResearch.RESEARCH_SUMMARY, item.getResearchSummary());
        values.put(BookResearch.STATUS, item.getStatus());

        return values;
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
