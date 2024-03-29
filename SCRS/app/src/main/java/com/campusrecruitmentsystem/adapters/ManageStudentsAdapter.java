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

public class ManageStudentsAdapter extends RecyclerView.Adapter<ManageStudentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<StudentDetails> studentDetailsArrayList = new ArrayList<>();
    private ArrayList<Boolean> isBlockedList = new ArrayList<>();
    private OnClickListener onClickListener;

    public ManageStudentsAdapter(Context context,
                                 ArrayList<StudentDetails> studentDetailsArrayList,
                                 ArrayList<Boolean> isBlockedList, OnClickListener onClickListener) {
        this.context = context;
        this.studentDetailsArrayList = studentDetailsArrayList;
        this.isBlockedList = isBlockedList;
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
    public void onBindViewHolder(@NonNull @NotNull ManageStudentsAdapter.ViewHolder holder, int position) {
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

        if(isBlockedList.get(position))
            binding.blockButton.setText("UNBLOCK");

        binding.blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkConnected(context)) {
                    if(binding.blockButton.getText().toString().equals("BLOCK")) {
                        binding.blockButton.setText("UNBLOCK");
                        isBlockedList.set(position, true);
                        onClickListener.onBlockStatusChanged(position, true);
                    } else {
                        binding.blockButton.setText("BLOCK");
                        isBlockedList.set(position, false);
                        onClickListener.onBlockStatusChanged(position, false);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentDetailsArrayList.size();
    }

    public void setNotifyDatasetChanged(ArrayList<StudentDetails> studentDetailsArrayList, ArrayList<Boolean> isBlockedList){
        this.studentDetailsArrayList = studentDetailsArrayList;
        this.isBlockedList = isBlockedList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ItemAppliedStudentBinding binding;
        private OnClickListener onClickListener;

        public ViewHolder(@NonNull @NotNull ItemAppliedStudentBinding itemView, OnClickListener onClickListener) {
            super(itemView.getRoot());
            binding = itemView;
            this.onClickListener = onClickListener;

            binding.acceptButton.setVisibility(View.GONE);
            binding.rejectButton.setVisibility(View.GONE);
            binding.blockButton.setVisibility(View.VISIBLE);

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
        void onBlockStatusChanged(int position, boolean isBlocked);
    }
}
