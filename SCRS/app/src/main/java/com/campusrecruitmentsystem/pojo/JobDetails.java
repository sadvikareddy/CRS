package com.campusrecruitmentsystem.pojo;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class JobDetails implements Serializable {

    public String companyUid;
    public String companyName;
    public int batch;
    public String jobDescription;
    public String companyMarket;
    public String employmentType;
    public String jobRole;
    public String jobLocation;
    public String hiringProcess;
    public String probationPeriod;
    public int salaryDuringProbation;
    public int salaryAfterProbation;
    public int bond;
    public int workingDays;
    public String message;
    public int twelfthPercentage;
    public int graduationPercentage;
    public int backlogs;

    public JobDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public JobDetails(String companyUid, String companyName, int batch, String jobDescription,
                      String companyMarket, String employmentType, String jobRole,
                      String jobLocation, String hiringProcess, String probationPeriod,
                      int salaryDuringProbation, int salaryAfterProbation, int bond,
                      int workingDays, String message, int twelfthPercentage,
                      int graduationPercentage, int backlogs) {
        this.companyUid = companyUid;
        this.companyName = companyName;
        this.batch = batch;
        this.jobDescription = jobDescription;
        this.companyMarket = companyMarket;
        this.employmentType = employmentType;
        this.jobRole = jobRole;
        this.jobLocation = jobLocation;
        this.hiringProcess = hiringProcess;
        this.probationPeriod = probationPeriod;
        this.salaryDuringProbation = salaryDuringProbation;
        this.salaryAfterProbation = salaryAfterProbation;
        this.bond = bond;
        this.workingDays = workingDays;
        this.message = message;
        this.twelfthPercentage = twelfthPercentage;
        this.graduationPercentage = graduationPercentage;
        this.backlogs = backlogs;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyUid() {
        return companyUid;
    }

    public int getBatch() {
        return batch;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getCompanyMarket() {
        return companyMarket;
    }

    public String getJobRole() {
        return jobRole;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public String getHiringProcess() {
        return hiringProcess;
    }

    public String getProbationPeriod() {
        return probationPeriod;
    }

    public int getSalaryDuringProbation() {
        return salaryDuringProbation;
    }

    public int getSalaryAfterProbation() {
        return salaryAfterProbation;
    }

    public int getBond() {
        return bond;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public String getMessage() {
        return message;
    }

    public int getTwelfthPercentage() {
        return twelfthPercentage;
    }

    public int getGraduationPercentage() {
        return graduationPercentage;
    }

    public int getBacklogs() {
        return backlogs;
    }
}
