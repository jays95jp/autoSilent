package com.example.admin.autosilentmode.adaper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.autosilentmode.activity.TimeSelectActivity;
import com.example.admin.autosilentmode.databinding.AdapterTimeListItemBinding;
import com.example.admin.autosilentmode.model.TimeBean;
import com.example.admin.autosilentmode.utils.Utils;

import java.util.ArrayList;

public class TimeScheduleListAdapter extends RecyclerView.Adapter<TimeScheduleListAdapter.AlbumViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<TimeBean> timeBeanArrayList;
    private Context context;

    public TimeScheduleListAdapter(Context context, ArrayList<TimeBean> stringArrayList) {
        this.timeBeanArrayList = stringArrayList;
        this.context = context;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        AdapterTimeListItemBinding adapterTimeListItemBinding = AdapterTimeListItemBinding.inflate(layoutInflater, parent, false);
        return new AlbumViewHolder(adapterTimeListItemBinding);

    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        holder.bind(timeBeanArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return timeBeanArrayList.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        private AdapterTimeListItemBinding listItemAlbumBinding;

        AlbumViewHolder(AdapterTimeListItemBinding itemView) {
            super(itemView.getRoot());
            listItemAlbumBinding = itemView;
        }

        void bind(final TimeBean bean) {
            listItemAlbumBinding.switchTime.setText(bean.getName().trim());
            listItemAlbumBinding.switchTime.setChecked(Utils.isMatchTime(bean));
            listItemAlbumBinding.txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimeSelectActivity.newInstance(context, true,bean);
                }
            });


        }
    }


}
