package com.hciws22.obslite.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;
import com.google.android.material.slider.Slider;
import com.google.android.material.slider.Slider.OnChangeListener;
import com.hciws22.obslite.R;

import java.util.ArrayList;

public class ModuleRecViewAdapter extends RecyclerView.Adapter<ModuleRecViewAdapter.ViewHolder> {

    private ArrayList<Todo> modules = new ArrayList<>();
    private Context contextToShowImage;
    public static Integer sliderValue;

    public ModuleRecViewAdapter(Context contextToShowImage) {
        this.contextToShowImage = contextToShowImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //list_item_module is connected with todo.xml HERE
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_module, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /**
         * CollapsedLayout
         */
        //float percentage = holder.slider.getValue();
        //modules.get(position).setPercentage(Float.toString(percentage));

        final String upRow = modules.get(position).getName() + " " ;
        final String downRow = "\n" + modules.get(position).getDate();
        holder.moduleInfor.setText(upRow + downRow);

        holder.module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Navigate to MODULE Screen
            }
        });

        /**
         * ExpandedLayout
         */
        holder.slider.addOnChangeListener(new OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                //TODO: Change the colour displayed on each Module Card correspondingly with the value of slider
                value = slider.getValue();
                Integer valueInt = Math.round(value);
                sliderValue = valueInt;
                /**
                 * Problem when calling holder here:
                 * Only when the Slider is being moved, the percentage will be displayed.
                 * But when the card collapse, all data will be back to normal
                 *
                 * If I just setText here and not in CollapsedLayout,
                 * no data will be shown except for the
                 * default value PG2 P1 100% assigned in xml
                 */
                holder.moduleInfor.setText(upRow + Integer.toString(valueInt) + " %" + downRow);
            }

        });

        holder.time_location.setText(modules.get(position).getTime() + " \t " + modules.get(position).getLocation());

        if (modules.get(position).isExpanded()){
            TransitionManager.beginDelayedTransition(holder.module);
            holder.expandedRelLayout.setVisibility(View.VISIBLE);
            holder.downArrow.setVisibility(View.GONE);
        }else{
            TransitionManager.beginDelayedTransition(holder.module);
            holder.expandedRelLayout.setVisibility(View.GONE);
            holder.downArrow.setVisibility(View.VISIBLE);
            holder.upArrow.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public void setModules(ArrayList<Todo> modules) {
        this.modules = modules;
        /*
        indicates that the data set has changed
        and the RecyclerView should update the displayed views to reflect the new data.
         */
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView module;//name of CardView Material
        private TextView moduleInfor;
        private ImageView downArrow, upArrow;
        private RelativeLayout expandedRelLayout;
        private TextView time_location;
        private Slider slider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            module = itemView.findViewById(R.id.module);
            moduleInfor = itemView.findViewById(R.id.module_infor);
            downArrow = itemView.findViewById(R.id.btnDownArrow);
            upArrow = itemView.findViewById(R.id.btnUpArrow);
            expandedRelLayout = itemView.findViewById(R.id.expandedRelLayout);
            time_location = itemView.findViewById(R.id.time_location);
            slider = itemView.findViewById(R.id.slider);

            downArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Todo module = modules.get(getAdapterPosition());
                    module.setExpanded( !module.isExpanded() );//toggle
                    notifyItemChanged(getAdapterPosition());//no need to notify all like notifyDataChanged()
                }
            });

            upArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Todo module = modules.get(getAdapterPosition());
                    module.setExpanded( !module.isExpanded() );//toggle
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
