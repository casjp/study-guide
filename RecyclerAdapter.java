package com.bucs.bupc.art.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bucs.bupc.art.R;
import com.bucs.bupc.art.model.YearResearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by John Paul Cas on 2/3/2016.
 *
 * @author John Paul Cas
 * @since January 2016
 *
 * connect to <i> John Paul Cas </> on facebook
 * <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 *
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.YearBookHolder> {

    private LayoutInflater inflater;
    List<YearResearch> data = Collections.emptyList();

    public RecyclerAdapter(Context context, List<YearResearch> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public YearBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_book_research_row, parent, false);
        YearBookHolder holder = new YearBookHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(YearBookHolder holder, int position) {
        YearResearch yearResearchItem = data.get(position);
        holder.yearTitle.setText(yearResearchItem.getYearNameDesc() + " File");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setFilter(List<YearResearch> items) {
        data = new ArrayList<>();
        data.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * This class will handle the item view to make a better performance
     */
    class YearBookHolder extends RecyclerView.ViewHolder {
        TextView yearTitle;
        public YearBookHolder(View itemView) {
            super(itemView);
            yearTitle = (TextView) itemView.findViewById(R.id.tv_year);
        }

    }
}
