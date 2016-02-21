package com.bucs.bupc.art.adapter;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bucs.bupc.art.R;
import com.bucs.bupc.art.constant.ApplicationConstant;
import com.bucs.bupc.art.dao.DownloadHistoryDAO;
import com.bucs.bupc.art.entity.DownloadHistory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by John Paul Cas on 2/8/2016.
 *
 * @author John Paul Cas
 *
 * connect to <i> John Paul Cas </i> on facebook
 * <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 *
 */
public class InboxAdapter extends BaseAdapter {

    private Context context;
    private List<DownloadHistory> list;
    private DownloadHistoryDAO downloadHistoryDAO;

    public InboxAdapter(Context context, List<DownloadHistory> list) {
        this.list = list;
        this.context = context;
        downloadHistoryDAO = new DownloadHistoryDAO(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        InboxViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_inbox_row, parent, false);
            holder = new InboxViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (InboxViewHolder) row.getTag();
        }
        final DownloadHistory downloadHistory = list.get(position);
        holder.inboxTitle.setText(downloadHistory.getResearchTitle());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + ApplicationConstant.FOLDER_NAME + "/" + downloadHistory.getPdfUriLocation());
                Uri path = Uri.fromFile(pdfFile);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    context.startActivity(pdfIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No Application to view the pdf file", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(Environment.getExternalStorageDirectory() + "/" + ApplicationConstant.FOLDER_NAME, downloadHistory.getPdfUriLocation(), downloadHistory.getId(), downloadHistory.getResearchTitle());
            }
        });

        return row;
    }

    /**
     *
     * @param path
     * @param fileName
     * @param id
     */
    private boolean deleteFile(String path, String fileName, int id, String researchTitle) {
        final String filePath = path;
        final int fileId = id;
        final boolean deleteResult = false;
        final String fileFileName = fileName;
        final String fileResearchTitle = researchTitle;
            buildDeleteConfirmationDialog("Alert", "Are you sure you want to delete " + researchTitle + " pdf file?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                try {
                                    boolean res = new File(filePath + "/" + fileFileName).delete();
                                    if (res) {
                                        Toast.makeText(context, "File successfully deleted", Toast.LENGTH_LONG).show();
                                        boolean result = downloadHistoryDAO.delete(fileId);
                                        if (result) {
                                            List<DownloadHistory> list = downloadHistoryDAO.findAll();
                                            notifyDataChange(list);
                                        } else {
                                            Toast.makeText(context, "Error occured while deleting the file", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "Sorry error occured with deleting of file", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(context, "Unable to delete file dir", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                /* DO NOTHING */
                            }
                            dialog.dismiss();
                        }
                    });
        return false;

    }

    public void notifyDataChange(List<DownloadHistory> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * create notification delete dialog
     *
     * @param title dialog title
     * @param message dialog message
     * @param listener dialog listener
     */
    public void buildDeleteConfirmationDialog(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.cancel, listener);
        builder.setPositiveButton(R.string.delete, listener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class InboxViewHolder {
        TextView inboxTitle;
        ImageView view;
        View delete;
        public InboxViewHolder(View row) {
            inboxTitle = (TextView) row.findViewById(R.id.inbox_title);
            view = (ImageView) row.findViewById(R.id.inbox_view);
            delete = row.findViewById(R.id.inbox_delete);
        }
    }
}
