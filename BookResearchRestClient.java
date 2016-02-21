package com.bucs.bupc.art.rest;

import android.content.Context;

import com.bucs.bupc.art.dao.BookResearchDAO;
import com.bucs.bupc.art.entity.BookResearch;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.SQLException;
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
 */
public class BookResearchRestClient {

    private RestClient restClient = null;
    private BookResearchDAO bookResearchDAO;
    private int insertCounter = 0;

    /**
     *
     * @param context application context
     */
    public BookResearchRestClient(Context context) {
        bookResearchDAO = new BookResearchDAO(context);
    }

    /**
     *
     * @param url where the json data will be get
     */
    public void execute(String url) {
        String stream = null;

        restClient = new RestClient();
        stream = restClient.getHttpDataRequest(url);

        if(stream != null) {
            try {
                JSONArray jsonArray = new JSONArray(stream);

                List<BookResearch> list = bookResearchDAO.findAll();
                if(list.size() > 0) {
                    // If item list is greater than zero delete the item in the list
                    for(BookResearch item : list) {
                        bookResearchDAO.delete(item);
                    }
                }

                for(int i = 0; i < jsonArray.length(); i++) {
                    bookResearchDAO.insert(new BookResearch(jsonArray.getJSONObject(i)));
                    insertCounter++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This counter will determine if how many data was inserted in the counter otherwise it will return 0
     * @return number of item inserted
     */
    public int getInsertCounter() {
        return insertCounter;
    }

}
