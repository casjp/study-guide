package com.bucs.bupc.art.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bucs.bupc.art.R;
import com.bucs.bupc.art.constant.ApplicationConstant;
import com.bucs.bupc.art.dao.DownloadHistoryDAO;
import com.bucs.bupc.art.entity.BookResearch;
import com.bucs.bupc.art.entity.DownloadHistory;
import com.bucs.bupc.art.utils.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by John Paul Cas on 2/7/2016.
 *
 * @author John Paul Cas
 *
 * connect to <i> John Paul Cas </i> on facebook
 * <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 */
public class BookResearchAdapter extends BaseAdapter {

    private List<BookResearch> researchList;
    private Context context;

    public BookResearchAdapter(Context context, List<BookResearch> researchList) {
        this.context = context;
        this.researchList = researchList;
    }

    @Override
    public int getCount() {
        return researchList.size();
    }

    @Override
    public Object getItem(int position) {
        return researchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ItemViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.research_item_row, parent, false);
            holder = new ItemViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ItemViewHolder) row.getTag();
        }

        final BookResearch book = researchList.get(position);
        holder.researchTitle.setText(book.getResearchTitle());
        holder.department.setText(book.getDepartment());
        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFile(book, context).execute(book.getFullPdfUrl(), book.getPdfFileName());
            }
        });

        return row;
    }

    /**
     *
     * @param newList the new list base on the sorted item
     */
    public void setFilter(List<BookResearch> newList) {
        this.researchList = new ArrayList<>();
        this.researchList.addAll(newList);
        notifyDataSetChanged();
    }

    class ItemViewHolder {
        private TextView researchTitle;
        private TextView department;
        private ImageView downloadButton;

        public ItemViewHolder(View row) {
            researchTitle = (TextView) row.findViewById(R.id.research_title);
            department = (TextView) row.findViewById(R.id.research_department);
            downloadButton = (ImageView) row.findViewById(R.id.download_file);
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;
        private BookResearch book;
        private DownloadHistoryDAO downloadHistoryDAO;
        private DownloadHistory downloadHistory;
        private boolean result = false;

        public DownloadFile(BookResearch bookResearch, Context context) {
            book = bookResearch;
            downloadHistoryDAO = new DownloadHistoryDAO(context);
            downloadHistory = new DownloadHistory();
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Downloading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            String fileUrl = params[0];
            String fileNameDir = params[1];
            String extStorageDir = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDir, ApplicationConstant.FOLDER_NAME);
            folder.mkdir();

            File pdfFile = new File(folder, fileNameDir);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            result = FileDownloader.downloadFile(fileUrl, pdfFile);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result == true) {
                Toast.makeText(context, "You successfully downloaded the file", Toast.LENGTH_LONG).show();
                downloadHistory.setResearchTitle(book.getResearchTitle());
                downloadHistory.setPdfUriLocation(book.getPdfFileName());
                try {
                    long result = downloadHistoryDAO.insert(downloadHistory);
                    if (result > 0) {
//                        Toast.makeText(context, "You successfully inserted new data", Toast.LENGTH_LONG).show();
                    }
                } catch (SQLException e) {
                    /* DO NOTHING */
                }
            } else {
                Toast.makeText(context, "An error occur while downloading the PDF file, Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        }
    }
}
