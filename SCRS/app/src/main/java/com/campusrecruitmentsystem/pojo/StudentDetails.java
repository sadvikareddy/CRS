package com.campusrecruitmentsystem.pojo;

public class StudentDetails {

    private String Uid;
    private String FullName;
    private String EmailId;
    private String ContactNo;
    private String RollNo;
    private String ProfileSummary;
    private String KeySkills;
    private Long TenthPercentage;
    private String TenthBoard;
    private Long TwelfthPercentage;
    private String TwelfthBoard;
    private Long GraduationPercentage;
    private String GraduationStream;

    public StudentDetails(String uid, String fullName, String emailId, String contactNo, String rollNo) {
        Uid = uid;
        FullName = fullName;
        EmailId = emailId;
        ContactNo = contactNo;
        RollNo = rollNo;
    }

    public StudentDetails(String uid, String fullName, String emailId, String contactNo,
                          String rollNo, String profileSummary, String keySkills,
                          Long tenthPercentage, String tenthBoard, Long twelfthPercentage,
                          String twelfthBoard, Long graduationPercentage, String graduationStream) {
        Uid = uid;
        FullName = fullName;
        EmailId = emailId;
        ContactNo = contactNo;
        RollNo = rollNo;
        ProfileSummary = profileSummary;
        KeySkills = keySkills;
        TenthPercentage = tenthPercentage;
        TenthBoard = tenthBoard;
        TwelfthPercentage = twelfthPercentage;
        TwelfthBoard = twelfthBoard;
        GraduationPercentage = graduationPercentage;
        GraduationStream = graduationStream;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getRollNo() {
        return RollNo;
    }

    public void setRollNo(String rollNo) {
        RollNo = rollNo;
    }

    public String getProfileSummary() {
        return ProfileSummary;
    }

    public void setProfileSummary(String profileSummary) {
        ProfileSummary = profileSummary;
    }

    public String getKeySkills() {
        return KeySkills;
    }

    public void setKeySkills(String keySkills) {
        KeySkills = keySkills;
    }

    public Long getTenthPercentage() {
        return TenthPercentage;
    }

    public void setTenthPercentage(Long tenthPercentage) {
        TenthPercentage = tenthPercentage;
    }

    public String getTenthBoard() {
        return TenthBoard;
    }

    public void setTenthBoard(String tenthBoard) {
        TenthBoard = tenthBoard;
    }

    public Long getTwelfthPercentage() {
        return TwelfthPercentage;
    }

    public void setTwelfthPercentage(Long twelfthPercentage) {
        TwelfthPercentage = twelfthPercentage;
    }

    public String getTwelfthBoard() {
        return TwelfthBoard;
    }

    public void setTwelfthBoard(String twelfthBoard) {
        TwelfthBoard = twelfthBoard;
    }

    public Long getGraduationPercentage() {
        return GraduationPercentage;
    }

    public void setGraduationPercentage(Long graduationPercentage) {
        GraduationPercentage = graduationPercentage;
    }

    public String getGraduationStream() {
        return GraduationStream;
    }

    public void setGraduationStream(String graduationStream) {
        GraduationStream = graduationStream;
    }
}
