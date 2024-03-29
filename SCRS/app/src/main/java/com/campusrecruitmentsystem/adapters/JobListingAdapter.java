package com.campusrecruitmentsystem.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campusrecruitmentsystem.R;
import com.campusrecruitmentsystem.databinding.ItemJobListingBinding;
import com.campusrecruitmentsystem.pojo.JobDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class JobListingAdapter extends RecyclerView.Adapter<JobListingAdapter.ViewHolder> {

    private ArrayList<JobDetails> jobs = new ArrayList<>();
    private Context context;
    private OnJobClickLitener onJobClickLitener;
    private int accountType = 1;

    public JobListingAdapter(Context context, ArrayList<JobDetails> jobs, OnJobClickLitener onJobClickLitener) {
        this.context = context;
        this.jobs = jobs;
        this.onJobClickLitener = onJobClickLitener;
        accountType = PreferenceManager.getDefaultSharedPreferences(context).getInt("accountType",1);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemJobListingBinding.bind(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_listing, parent, false)),onJobClickLitener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull JobListingAdapter.ViewHolder holder, int position) {
        JobDetails job = jobs.get(position);
        if(accountType == 1){
            // STUDENT
            holder.binding.companyName.setText(job.getCompanyName());
            holder.binding.jobRole.setText(job.getJobRole());
        } else if(accountType == 2){
            // COMPANY
            holder.binding.companyName.setText(job.getJobRole());
            holder.binding.jobRole.setText(job.getJobLocation());
        }

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJobClickLitener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void setNotifyDatasetChanged(ArrayList<JobDetails> jobs){
        this.jobs = jobs;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ItemJobListingBinding binding;
        private OnJobClickLitener onJobClickLitener;
        public ViewHolder(@NonNull @NotNull ItemJobListingBinding itemView, OnJobClickLitener onJobClickLitener) {
            super(itemView.getRoot());
            binding = itemView;
            this.onJobClickLitener = onJobClickLitener;
        }
    }

    public interface OnJobClickLitener{
        void onClick(int position);
    }
}
