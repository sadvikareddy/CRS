package com.campusrecruitmentsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campusrecruitmentsystem.R;
import com.campusrecruitmentsystem.Utils;
import com.campusrecruitmentsystem.databinding.ItemAppliedStudentBinding;
import com.campusrecruitmentsystem.pojo.StudentDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AppliedStudentsAdapter extends RecyclerView.Adapter<AppliedStudentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<StudentDetails> studentDetailsArrayList = new ArrayList<>();
    private OnClickListener onClickListener;

    public AppliedStudentsAdapter(Context context, ArrayList<StudentDetails> studentDetailsArrayList, OnClickListener onClickListener) {
        this.context = context;
        this.studentDetailsArrayList = studentDetailsArrayList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemAppliedStudentBinding.bind(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_applied_student, parent, false)), onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AppliedStudentsAdapter.ViewHolder holder, int position) {
        ItemAppliedStudentBinding binding = holder.binding;
        StudentDetails student = studentDetailsArrayList.get(position);
        binding.studentName.setText(student.getFullName());
        binding.studentRollNo.setText(student.getRollNo());

        binding.contactNoValue.setText(student.getContactNo());
        binding.profileSummaryValue.setText(student.getProfileSummary());
        binding.keySkillsValue.setText(student.getKeySkills());
        binding.tenthPercentageValue.setText(String.valueOf(student.getTenthPercentage())+"%");
        binding.tenthBoardValue.setText(student.getTenthBoard());
        binding.twelfthPercentageValue.setText(String.valueOf(student.getTwelfthPercentage())+"%");
        binding.twelfthBoardValue.setText(student.getTwelfthBoard());
        binding.graduationPercentageValue.setText(String.valueOf(student.getGraduationPercentage())+"%");
        binding.graduationStreamValue.setText(student.getGraduationStream());

        binding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkConnected(context)) {
                    binding.rejectButton.setVisibility(View.GONE);
                    binding.acceptButton.setEnabled(false);
                    binding.acceptButton.setText("ACCEPTED");
                    onClickListener.onJobApplicationStatusListener(position, true);
                }
            }
        });
        binding.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkConnected(context)) {
                    binding.rejectButton.setVisibility(View.GONE);
                    binding.acceptButton.setEnabled(false);
                    binding.acceptButton.setText("REJECTED");
                    onClickListener.onJobApplicationStatusListener(position, false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentDetailsArrayList.size();
    }

    public void setNotifyDatasetChanged(ArrayList<StudentDetails> studentDetailsArrayList){
        this.studentDetailsArrayList = studentDetailsArrayList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ItemAppliedStudentBinding binding;
        private OnClickListener onClickListener;

        public ViewHolder(@NonNull @NotNull ItemAppliedStudentBinding itemView, OnClickListener onClickListener) {
            super(itemView.getRoot());
            binding = itemView;
            this.onClickListener = onClickListener;

            binding.headerSection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(binding.detailsSection.getVisibility() == View.GONE){
                        binding.arrow.setScaleY(-1);
                        binding.detailsSection.setVisibility(View.VISIBLE);
                    } else {
                        binding.arrow.setScaleY(1);
                        binding.detailsSection.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public interface OnClickListener{
        void onJobApplicationStatusListener(int position, boolean isAccepted);
    }
}
