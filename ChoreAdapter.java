package com.example.test7;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChoreAdapter extends RecyclerView.Adapter<ChoreAdapter.ChoreViewHolder> {
    private List<Chore> chores;
    private SimpleDateFormat dateFormat;
    private OnChoreDeleteListener deleteListener;
    private OnChoreCompletionToggleListener completionToggleListener;

    public interface OnChoreDeleteListener {
        void onChoreDelete(Chore chore);
    }

    public interface OnChoreCompletionToggleListener {
        void onChoreCompletionToggle(Chore chore);
    }

    public ChoreAdapter(OnChoreDeleteListener deleteListener, OnChoreCompletionToggleListener completionToggleListener) {
        chores = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        this.deleteListener = deleteListener;
        this.completionToggleListener = completionToggleListener;
    }

    @NonNull
    @Override
    public ChoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chore_item, parent, false);
        return new ChoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoreViewHolder holder, int position) {
        Chore chore = chores.get(position);
        holder.choreNameTextView.setText(chore.getName());
        if (chore instanceof ShortTermChore) {
            holder.deadlineTextView.setText("Deadline: " + dateFormat.format(((ShortTermChore) chore).getDeadline()));
        } else if (chore instanceof LongTermChore) {
            holder.deadlineTextView.setText("Deadline: " + dateFormat.format(((LongTermChore) chore).getDeadline()));
        }
        holder.progressBar.setProgress(chore.getProgress());
        holder.completedCheckBox.setChecked(chore.isCompleted());
        holder.incomeTextView.setText(String.format("$%.2f", chore.getIncome()));

        holder.itemView.setOnLongClickListener(v -> {
            deleteListener.onChoreDelete(chore);
            return true;
        });

        holder.completedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            completionToggleListener.onChoreCompletionToggle(chore);
        });
    }

    @Override
    public int getItemCount() {
        return chores.size();
    }

    public void setChores(List<Chore> chores) {
        this.chores = chores;
        notifyDataSetChanged();
    }

    static class ChoreViewHolder extends RecyclerView.ViewHolder {
        TextView choreNameTextView;
        TextView deadlineTextView;
        ProgressBar progressBar;
        CheckBox completedCheckBox;
        TextView incomeTextView;

        ChoreViewHolder(View itemView) {
            super(itemView);
            choreNameTextView = itemView.findViewById(R.id.choreNameTextView);
            deadlineTextView = itemView.findViewById(R.id.deadlineTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
            completedCheckBox = itemView.findViewById(R.id.completedCheckBox);
            incomeTextView = itemView.findViewById(R.id.incomeTextView);
        }
    }
}
