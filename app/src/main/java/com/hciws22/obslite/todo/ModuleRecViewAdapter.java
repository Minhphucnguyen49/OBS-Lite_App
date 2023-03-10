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
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;
import com.google.android.material.slider.Slider;
import com.hciws22.obslite.R;
import com.hciws22.obslite.db.SqLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class ModuleRecViewAdapter extends RecyclerView.Adapter<ModuleRecViewAdapter.ViewHolder> {

    private List<Todo> modules = new ArrayList<>();//from Appointment Table
    private List<Todo> extraInfo = new ArrayList<>();//from Extra Table
    private final Context contextToShowImage;
    private TodoDbService todoDbService;

    public ModuleRecViewAdapter(Context contextToShowImage) {
        this.contextToShowImage = contextToShowImage;
    }
    public ModuleRecViewAdapter(Context contextToShowImage, SqLiteHelper sqLiteHelper) {
        this.contextToShowImage = contextToShowImage;
        this.todoDbService = new TodoDbService(sqLiteHelper);
    }

    public List<Todo> getExtraInfo(){
        return todoDbService.selectExtra();
    }
    public List<Todo> getToDo(){
        return todoDbService.selectAllWeek();
    }

    public List<Todo> getAllWeek(){
        return todoDbService.selectAllWeek();
    }
    public List<Todo> getCurrentWeek() {
        return todoDbService.selectCurrentWeek();
    }
    public List<Todo> getExams(){return todoDbService.selectExams();}



    public String adaptDate(String originDate){
        //rearange date --> DD.MM.YYYY
        String[] words = originDate.split(" ");
        String[] date = words[1].split("\\.");
        return words[0]+" "+date[2]+"."+date[1]+"."+date[0];

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

        //Positioning slider with the right card.
        // If code of module (e.g. 300456) is also available in local database,
        // there will be no problem with changing en <-> de and keeping slider's value (Eg. 300456 #2)
        for (int i = 0; i < extraInfo.size(); i++) {
            String percentage = extraInfo.get(i).getPercentage();
            String name = modules.get(position).getName();
            if ( name.equals(extraInfo.get(i).getName()) && !percentage.isEmpty() ){
                //read from database (Extra Table) and show value in slider
                setSlider(holder, Float.parseFloat(percentage));
            }

        }

        final String cardName = modules.get(position).getName() + "\n" ;
        final String cardDate = adaptDate(modules.get(position).getDate())+ "\n";
        String cardProgress = "Progress " + modules.get(position).getPercentage() + "%";


        holder.moduleInfor.setText(cardName);
        holder.moduleDate.setText(cardDate);
        if(modules.get(position).getPercentage().equals("")){
            String initProgress = "Progress 0%";
            holder.moduleProgress.setText(initProgress);
        } else {
            holder.moduleProgress.setText(cardProgress);
        }


        /**
         * ExpandedLayout
         */
        holder.slider.addOnChangeListener((slider, value, fromUser) -> {
            Integer valueInt = Math.round(value);
            // the progress changes will be displayed while sliding
            String progress = "Progress " + Integer.toString(valueInt) + " %";
            holder.moduleProgress.setText(progress);
            //save slider's value in modules to show it in collapsed Layout. (Problem 2 fixed)
            modules.get(position).setPercentage(Integer.toString(valueInt));
        });

        String timeLocation = modules.get(position).getTime() + " \t " + modules.get(position).getLocation();
        holder.time_location.setText(timeLocation);

        setVisibilityView(holder, position);

    }

    private void setSlider(@NonNull ViewHolder holder, float value) {
        //value = slider.getValue();
        Integer valueInt = Math.round(value);
        //Slider will display its value instead of being set back in 0 (Problem 1 fixed)
        holder.slider.setValue(valueInt);
    }

    private void setVisibilityView(@NonNull ViewHolder holder, int position) {
        if (modules.get(position).isExpanded()){
            TransitionManager.beginDelayedTransition(holder.module, new AutoTransition());
            holder.expandedRelLayout.setVisibility(View.VISIBLE);
            holder.downArrow.setVisibility(View.GONE);
            holder.upArrow.setVisibility(View.VISIBLE);
        }else{
            TransitionManager.beginDelayedTransition(holder.module, new AutoTransition());
            holder.expandedRelLayout.setVisibility(View.GONE);
            holder.upArrow.setVisibility(View.GONE);
            holder.downArrow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public void setModules(List<Todo> modules, List<Todo> extraInfo) {
        this.modules = modules;
        this.extraInfo = extraInfo;
        /*
        indicates that the data set has changed
        and the RecyclerView should update the displayed views to reflect the new data.
         */
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final CardView module;//name of CardView Material
        private final TextView moduleInfor;
        private final TextView moduleDate;
        private final TextView moduleProgress;
        private final ImageView downArrow, upArrow;
        private final RelativeLayout collapsedRelLayout;
        private final RelativeLayout expandedRelLayout;
        private final TextView time_location;
        private final Slider slider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            module = itemView.findViewById(R.id.module);
            moduleInfor = itemView.findViewById(R.id.module_infor);
            moduleDate = itemView.findViewById(R.id.module_time);
            moduleProgress = itemView.findViewById(R.id.module_progress);
            downArrow = itemView.findViewById(R.id.btnDownArrow);
            upArrow = itemView.findViewById(R.id.btnUpArrow);
            expandedRelLayout = itemView.findViewById(R.id.expandedRelLayout);
            collapsedRelLayout = itemView.findViewById(R.id.collapsedRellayout);
            time_location = itemView.findViewById(R.id.time_location);
            slider = itemView.findViewById(R.id.slider);

            /*
            collapsedRelLayout.setOnClickListener(view -> {
                Todo module = modules.get(getAdapterPosition());
                module.setExpanded( !module.isExpanded() );//toggle
                notifyItemChanged( getAdapterPosition() );//no need to notify all like notifyDataChanged()
            });

             */


            downArrow.setOnClickListener(view -> {
                Todo module = modules.get(getAdapterPosition());
                module.setExpanded( !module.isExpanded() );//toggle
                notifyItemChanged(getAdapterPosition());//no need to notify all like notifyDataChanged()
            });

            upArrow.setOnClickListener(view -> {
                Todo module = modules.get(getAdapterPosition());
                module.setExpanded( !module.isExpanded() );//toggle
                notifyItemChanged(getAdapterPosition());
            });


            // save data in Extra Table
            slider.addOnChangeListener((slider, value, fromUser) -> {
                Todo module = modules.get(getAdapterPosition());
                Integer valueInt = Math.round(value);
                module.setPercentage(Integer.toString(valueInt));
                todoDbService.updateExtra(module);
            });

        }
    }
}
