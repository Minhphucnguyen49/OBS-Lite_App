package com.hciws22.obslite.todo;

import android.content.Context;
import android.util.Log;
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
import com.google.android.material.slider.Slider.OnChangeListener;
import com.hciws22.obslite.R;
import com.hciws22.obslite.db.SqLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleRecViewAdapter extends RecyclerView.Adapter<ModuleRecViewAdapter.ViewHolder> {

    private List<Todo> modules = new ArrayList<>();//from Appointment Table
    private List<Todo> extraInfo = new ArrayList<>();//from Extra Table
    private Context contextToShowImage;
    private TodoDbService todoDbService;

    public ModuleRecViewAdapter(Context contextToShowImage) {
        this.contextToShowImage = contextToShowImage;
    }
    public ModuleRecViewAdapter(Context contextToShowImage, SqLiteHelper sqLiteHelper) {
        this.contextToShowImage = contextToShowImage;
        this.todoDbService = new TodoDbService(sqLiteHelper);
    }

    /**
     * bring them back in TodoController
     * @return
     */
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


    public String shortenName(String fullName){
        //shorten name to initials
        String LabNumber = "";
        String actualName = fullName;
        String shortName = "";

        //keep lab-number if exists
        if(fullName.contains("#")) {
            LabNumber = fullName.substring(fullName.length() - 3);
            actualName = fullName.substring(0, fullName.length() - 3);
        }
        String moduleInitials = "";
        if(actualName.contains(" ")) {
            //mehr als ein Wort
            for (String s : actualName.split(" ")) {
                moduleInitials += s.charAt(0);
            }
            if(fullName.contains("#")) {
                shortName=moduleInitials+" "+LabNumber;
            }else{
                shortName=moduleInitials;
            }
            return shortName;
        }

        if(fullName.contains("#")) {
            shortName =actualName+=LabNumber;
        }

        return shortName;
    }

    public String adaptDate(String originDate){
        //rearange date --> DD.MM.YYYY
        String[] words = originDate.split(" ");
        String[] date = words[1].split("\\.");
        String newDate =  words[0]+" "+date[2]+"."+date[1]+"."+date[0];

        return newDate;
    }

    public String checkNameLength(String fullName){
        //check if name has to be shortened
        if(fullName.length()>20){
            return shortenName(fullName);
        }

        return fullName;
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

        String cardPercentage = "";

        //Positioning slider with the right card.
        for(int i=0;i<extraInfo.size();i++) {
            if (Objects.equals(modules.get(position).getName(), extraInfo.get(i).getName())){
                cardPercentage = extraInfo.get(i).getPercentage();
            }
        }

        final String cardName = checkNameLength(modules.get(position).getName()) + "\n" ;
        final String cardDate = adaptDate(modules.get(position).getDate())+ "\n";

        holder.moduleInfor.setText(cardName);
        holder.moduleDate.setText(cardDate);
        holder.moduleProgress.setText("Progress " + cardPercentage);

        /**
         * ExpandedLayout
         */
        holder.slider.addOnChangeListener((slider, value, fromUser) -> {
            //TODO: Change the colour displayed on each Module Card correspondingly with the value of slider
            value = slider.getValue();
            Integer valueInt = Math.round(value);

           // the progress changes will be displayed while sliding
            holder.moduleProgress.setText("Progress " + Integer.toString(valueInt) + " %");
        });

        holder.time_location.setText(modules.get(position).getTime() + " \t " + modules.get(position).getLocation());

        setVisibilityView(holder, position);
        holder.downArrow.setOnClickListener(view -> {
            holder.downArrow.animate().rotation(holder.downArrow.getRotation()-180).start();
        });


    }

    private void setVisibilityView(@NonNull ViewHolder holder, int position) {
        if (modules.get(position).isExpanded()){
            TransitionManager.beginDelayedTransition(holder.module, new AutoTransition());
            holder.expandedRelLayout.setVisibility(View.VISIBLE);

        }else{
            TransitionManager.beginDelayedTransition(holder.module, new AutoTransition());
            holder.expandedRelLayout.setVisibility(View.GONE);
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
        private CardView module;//name of CardView Material
        private TextView moduleInfor;
        private TextView moduleDate;
        private TextView moduleProgress;
        private ImageView downArrow, upArrow;
        private RelativeLayout collapsedRelLayout;
        private RelativeLayout expandedRelLayout;
        private TextView time_location;
        private Slider slider;

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

            collapsedRelLayout.setOnClickListener(view -> {
                Todo module = modules.get(getAdapterPosition());
                module.setExpanded( !module.isExpanded() );//toggle
                notifyItemChanged(getAdapterPosition());//no need to notify all like notifyDataChanged()
            });

            /*
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
             */

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
