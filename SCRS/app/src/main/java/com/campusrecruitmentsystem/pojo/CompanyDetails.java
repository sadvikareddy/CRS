package com.campusrecruitmentsystem.pojo;

public class CompanyDetails {

    private String Uid;
    private String fullName;
    private String emailId;
    private String contactNo;
    private String location;
    private Boolean isBlocked;

    public CompanyDetails(String uid, String fullName, String emailId, String contactNo, String location, Boolean isBlocked) {
        Uid = uid;
        this.fullName = fullName;
        this.emailId = emailId;
        this.contactNo = contactNo;
        this.location = location;
        this.isBlocked = isBlocked;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }
}
