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
import com.campusrecruitmentsystem.databinding.ItemCompanyBinding;
import com.campusrecruitmentsystem.pojo.CompanyDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ManageCompanyAdapter extends RecyclerView.Adapter<ManageCompanyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CompanyDetails> companyDetailsArrayList = new ArrayList<>();
    private OnClickListener onClickListener;

    public ManageCompanyAdapter(Context context,
                                ArrayList<CompanyDetails> companyDetailsArrayList,
                                OnClickListener onClickListener) {
        this.context = context;
        this.companyDetailsArrayList = companyDetailsArrayList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCompanyBinding.bind(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_company, parent, false)), onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ManageCompanyAdapter.ViewHolder holder, int position) {
        ItemCompanyBinding binding = holder.binding;
        CompanyDetails company = companyDetailsArrayList.get(position);

        binding.companyName.setText(company.getFullName());
        binding.locationValue.setText(company.getLocation());
        binding.contactNoValue.setText(company.getContactNo());
        binding.emailIdValue.setText(company.getEmailId());

        if(company.getBlocked()) binding.blockButton.setText("UNBLOCK");

        binding.blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkConnected(context)) {
                    if(binding.blockButton.getText().toString().equals("BLOCK")) {
                        binding.blockButton.setText("UNBLOCK");
                        companyDetailsArrayList.get(position).setBlocked(true);
                        onClickListener.onBlockStatusChanged(position, true);
                    } else {
                        binding.blockButton.setText("BLOCK");
                        companyDetailsArrayList.get(position).setBlocked(false);
                        onClickListener.onBlockStatusChanged(position, false);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyDetailsArrayList.size();
    }

    public void setNotifyDatasetChanged(ArrayList<CompanyDetails> companyDetailsArrayList){
        this.companyDetailsArrayList = companyDetailsArrayList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ItemCompanyBinding binding;
        private OnClickListener onClickListener;

        public ViewHolder(@NonNull @NotNull ItemCompanyBinding itemView, OnClickListener onClickListener) {
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
        void onBlockStatusChanged(int position, boolean isBlocked);
    }
}
