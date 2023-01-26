package com.hciws22.obslite.today;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hciws22.obslite.R;

import java.util.ArrayList;
import java.util.List;

public class LectureRecViewAdapter extends RecyclerView.Adapter<LectureRecViewAdapter.ViewHolder> {

    public List<Today> getModules() {
        return modules;
    }

    private List<Today> modules = new ArrayList<>();
    private Context contextToShowImage;

    public LectureRecViewAdapter(Context contextToShowImage) {
        this.contextToShowImage = contextToShowImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_today, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /**
         * Module information displayed on CardViews
         */

        String time = modules.get(position).getTime() + " " ;
        String name = "\n" + modules.get(position).getModuleType() +": "+ modules.get(position).getName();
        String location = "\n" + modules.get(position).getLocation();
        holder.moduleInfor.setText(time + name + location);

        String type = modules.get(position).getModuleType();
        if( type.contains("V") || type.contains("L")){
            holder.moduleToday.setCardBackgroundColor(ContextCompat.getColor(contextToShowImage,R.color.colorLecture));
        }
        if(type.contains("P") || type.contains("Ü") || type.contains("E")){
            holder.moduleToday.setCardBackgroundColor(ContextCompat.getColor(contextToShowImage,R.color.colorLab));
        }



    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public void setModules(List<Today> modules) {
        this.modules = modules;
        /*
        indicates that the data set has changed
        and the RecyclerView should update the displayed views to reflect the new data.
         */
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView moduleToday;//name of CardView Material
        private TextView moduleInfor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.moduleToday = itemView.findViewById(R.id.module_today);
            this.moduleInfor = itemView.findViewById(R.id.module_infor_today);
        }
    }
}
