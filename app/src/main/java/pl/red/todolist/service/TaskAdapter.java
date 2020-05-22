package pl.red.todolist.service;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.red.todolist.R;
import pl.red.todolist.model.TaskListFacade;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final TaskListFacade tasks;
    private Intent intent;


    public TaskAdapter(TaskListFacade tasks, Intent intent) {
        this.tasks = tasks;
        this.intent = intent;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int position) {
        taskViewHolder.setBackground(position);
        taskViewHolder.title.setText(tasks.getTitleTask(position));
        taskViewHolder.deadline.setText(tasks.getDeadlineDateTask(position));
        taskViewHolder.priority.setImageResource(tasks.getPriorityColorTask(position));
        taskViewHolder.finished.setChecked(tasks.getStatusTask(position));
    }

    @Override
    public int getItemCount() {
        return tasks.count();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView deadline;
        ImageView priority;
        CheckBox finished;

        TaskViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title);
            deadline = itemView.findViewById(R.id.deadline);
            priority = itemView.findViewById(R.id.priority);
            finished = itemView.findViewById(R.id.finished);

            ((View) itemView.findViewById(R.id.delete).getParent()).setOnClickListener(
                    v -> {
                        intent.putExtra("position", getAdapterPosition());
                    }
            );

            finished.setOnCheckedChangeListener((buttonView, isChecked) -> tasks.setStatusTask(getAdapterPosition(), isChecked));
        }

        void setBackground(int position) {
            if (position % 2 == 0) {
                itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                itemView.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public void notifyChanged() {
        notifyDataSetChanged();
    }

    public void notifyDeleted(int pos) {
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, tasks.count() + 1);
    }
}