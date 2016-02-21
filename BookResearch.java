package com.bucs.bupc.art.entity;

import android.database.Cursor;

import com.bucs.bupc.art.constant.ApplicationConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by John Paul Cas on 1/30/2016.
 *
 * @author John Paul Cas
 * @since January 2016
 *
 * connect to <i> John Paul Cas </> on facebook <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 *
 */
public class BookResearch {

    public static final String TABLE_NAME = "research";

    public static final String RESEARCH_ID      = "id";
    public static final String RESEARCH_TITLE   = "research_title";
    public static final String DEPARTMENT       = "department";
    public static final String PDF_FILE_NAME    = "file";
    public static final String COPYRIGHT_YEAR   = "copyright_year";
    public static final String RESEARCH_SUMMARY = "research_summary";
    public static final String STATUS           = "status";

    private int researchId;
    private String researchTitle;
    private String department;
    private String pdfFileName;
    private int copyrightYear;
    private String researchSummary;
    private String status;

    private BookResearch() { }

    /**
     *  BookResearch columns entity
     */
    public static final String DB_COLUMNS[] = {
            RESEARCH_ID,
            RESEARCH_TITLE,
            DEPARTMENT,
            PDF_FILE_NAME,
            COPYRIGHT_YEAR,
            RESEARCH_SUMMARY,
            STATUS
    };

    /**
     *
     * @param jsonObject the json object that will be set to the model
     * @throws JSONException
     */
    public BookResearch(JSONObject jsonObject) throws JSONException {
        setResearchTitle(jsonObject.getString(RESEARCH_TITLE));
        setDepartment(jsonObject.getString(DEPARTMENT));
        setPdfFileName(jsonObject.getString(PDF_FILE_NAME));
        setCopyrightYear(Integer.parseInt(jsonObject.getString(COPYRIGHT_YEAR)));
        setResearchSummary(jsonObject.getString(RESEARCH_SUMMARY));
        setStatus(jsonObject.getString(STATUS));
    }

    /**
     * Set the cursor data that will get to the database
     * @param cursor
     */
    public BookResearch(Cursor cursor) {
        setResearchTitle(cursor.getString(cursor.getColumnIndex(RESEARCH_TITLE)));
        setDepartment(cursor.getString(cursor.getColumnIndex(DEPARTMENT)));
        setPdfFileName(cursor.getString(cursor.getColumnIndex(PDF_FILE_NAME)));
        setCopyrightYear(cursor.getInt(cursor.getColumnIndex(COPYRIGHT_YEAR)));
        setResearchSummary(cursor.getString(cursor.getColumnIndex(RESEARCH_SUMMARY)));
        setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
    }

    public int getResearchId() {
        return researchId;
    }

    public void setResearchId(int researchId) {
        this.researchId = researchId;
    }

    public String getResearchTitle() {
        return researchTitle;
    }

    public void setResearchTitle(String researchTitle) {
        this.researchTitle = researchTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public String getFullPdfUrl() {
        return ApplicationConstant.RESEARCH_PDF_URL + "/" + pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public int getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(int copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public String getResearchSummary() {
        return researchSummary;
    }

    public void setResearchSummary(String researchSummary) {
        this.researchSummary = researchSummary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get database structure for Research Table
     *
     * @return research table structure
     */
    public static final String getTableStructure() {
        return "CREATE TABLE " + TABLE_NAME + "("
                + RESEARCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RESEARCH_TITLE + " TEXT, "
                + DEPARTMENT + " TEXT, "
                + PDF_FILE_NAME + " VARCHAR(255), "
                + COPYRIGHT_YEAR + " INTEGER(5), "
                + RESEARCH_SUMMARY + " TEXT, "
                + STATUS + " VARCHAR(200))";
    }
}
