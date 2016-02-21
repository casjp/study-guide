package com.bucs.bupc.art.entity;

import android.database.Cursor;

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
public class DownloadHistory {

    public static final String TABLE_NAME = "history";

    public static final String ID = "id";
    public static final String RESEARCH_TITLE = "research_title";
    public static final String DATE_DOWNLOADED = "date_download";
    public static final String PDF_URI_LOCATION = "pdf_location";

    private int id;
    private String researchTitle;
    private String dateDownload;
    private String pdfUriLocation;

    public DownloadHistory() { }

    /**
     * Database column model
     */
    public static final String DB_COLUMNS[] = {
            ID, RESEARCH_TITLE, DATE_DOWNLOADED, PDF_URI_LOCATION
    };

    /**
     * Set download history object
     *
     * @param cursor the item from db to be set
     */
    public DownloadHistory(Cursor cursor) {
        setId(cursor.getInt(cursor.getColumnIndex(ID)));
        setResearchTitle(cursor.getString(cursor.getColumnIndex(RESEARCH_TITLE)));
        setDateDownload(cursor.getString(cursor.getColumnIndex(DATE_DOWNLOADED)));
        setPdfUriLocation(cursor.getString(cursor.getColumnIndex(PDF_URI_LOCATION)));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResearchTitle() {
        return researchTitle;
    }

    public void setResearchTitle(String researchTitle) {
        this.researchTitle = researchTitle;
    }

    public String getDateDownload() {
        return dateDownload;
    }

    public void setDateDownload(String dateDownload) {
        this.dateDownload = dateDownload;
    }

    public String getPdfUriLocation() {
        return pdfUriLocation;
    }

    public void setPdfUriLocation(String pdfUriLocation) {
        this.pdfUriLocation = pdfUriLocation;
    }


    /**
     * @return DownloadHistory table structure
     */
    public static final String getTableStructure() {
        return "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RESEARCH_TITLE + " TEXT, "
                + DATE_DOWNLOADED + " VARCHAR(255), "
                + PDF_URI_LOCATION + " TEXT)";
    }
}

