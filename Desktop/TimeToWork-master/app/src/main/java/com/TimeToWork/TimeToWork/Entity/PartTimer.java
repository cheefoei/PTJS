package com.TimeToWork.TimeToWork.Entity;

public class PartTimer {
    private String jobseeker_id, jobseeker_name, jobseeker_dob, jobseeker_ic, jobseeker_address, jobseeker_phone_number, jobseeker_email,
            jobseeker_password, jobseeker_preferred_job, jobseeker_preferred_location, jobseeker_rating, jobseeker_experience;
    private char jobseeker_gender;
    private String jobseeker_img;
    private int salary, completed;

    public PartTimer() {
    }
    // RegisterPage and Change Password Page
    public PartTimer(String jobseeker_id, String jobseeker_name, char jobseeker_gender, String jobseeker_dob, String jobseeker_ic, String jobseeker_address, String jobseeker_phone_number, String jobseeker_email, String jobseeker_preferred_job, String jobseeker_preferred_location, String jobseeker_experience, String jobseeker_rating, String jobseeker_password, String jobseeker_img) {
        this.jobseeker_id = jobseeker_id;
        this.jobseeker_name = jobseeker_name;
        this.jobseeker_ic = jobseeker_ic;
        this.jobseeker_address = jobseeker_address;
        this.jobseeker_phone_number = jobseeker_phone_number;
        this.jobseeker_email = jobseeker_email;
        this.jobseeker_password = jobseeker_password;
        this.jobseeker_dob = jobseeker_dob;
        this.jobseeker_preferred_job = jobseeker_preferred_job;
        this.jobseeker_preferred_location = jobseeker_preferred_location;
        this.jobseeker_img = jobseeker_img;
        this.jobseeker_rating = jobseeker_rating;
        this.jobseeker_experience = jobseeker_experience;
        this.jobseeker_gender = jobseeker_gender;
    }

    // Read ID
    public PartTimer(String jobseeker_id) {
        this.jobseeker_id = jobseeker_id;
    }

    // Update the Jobseeker Profile Details
    public PartTimer(String jobseeker_id, String jobseeker_name, String jobseeker_dob, String jobseeker_ic, String jobseeker_address, String jobseeker_phone_number, String jobseeker_email, String jobseeker_img, char jobseeker_gender) { //Update details
        this.jobseeker_id = jobseeker_id;
        this.jobseeker_name = jobseeker_name;
        this.jobseeker_dob = jobseeker_dob;
        this.jobseeker_ic = jobseeker_ic;
        this.jobseeker_address = jobseeker_address;
        this.jobseeker_phone_number = jobseeker_phone_number;
        this.jobseeker_email = jobseeker_email;
        this.jobseeker_img = jobseeker_img;
        this.jobseeker_gender = jobseeker_gender;
    }

    public PartTimer(String jobseeker_id, String jobseeker_img) {
        this.jobseeker_id = jobseeker_id;
        this.jobseeker_img = jobseeker_img;
    }

    // Update Jobseeker Job And Location
    public PartTimer(String jobseeker_id, String jobseeker_preferred_job, String jobseeker_preferred_location) { //Preferred Job
        this.jobseeker_id = jobseeker_id;
        this.jobseeker_preferred_job = jobseeker_preferred_job;
        this.jobseeker_preferred_location = jobseeker_preferred_location;
    }

    public PartTimer(String jobseeker_name, int salary, int completed) {
        this.jobseeker_name = jobseeker_name;
        this.salary = salary;
        this.completed = completed;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public String getJobseeker_id() {
        return jobseeker_id;
    }

    public void setJobseeker_id(String jobseeker_id) {
        this.jobseeker_id = jobseeker_id;
    }

    public String getJobseeker_name() {
        return jobseeker_name;
    }

    public void setJobseeker_name(String jobseeker_name) {
        this.jobseeker_name = jobseeker_name;
    }

    public String getJobseeker_ic() {
        return jobseeker_ic;
    }

    public void setJobseeker_ic(String jobseeker_ic) {
        this.jobseeker_ic = jobseeker_ic;
    }

    public String getJobseeker_address() {
        return jobseeker_address;
    }

    public void setJobseeker_address(String jobseeker_address) {
        this.jobseeker_address = jobseeker_address;
    }

    public String getJobseeker_phone_number() {
        return jobseeker_phone_number;
    }

    public void setJobseeker_phone_number(String jobseeker_phone_number) {
        this.jobseeker_phone_number = jobseeker_phone_number;
    }

    public String getJobseeker_email() {
        return jobseeker_email;
    }

    public void setJobseeker_email(String jobseeker_email) {
        this.jobseeker_email = jobseeker_email;
    }

    public String getJobseeker_password() {
        return jobseeker_password;
    }

    public void setJobseeker_password(String jobseeker_password) {
        this.jobseeker_password = jobseeker_password;
    }

    public String getJobseeker_dob() {
        return jobseeker_dob;
    }

    public void setJobseeker_dob(String jobseeker_dob) {
        this.jobseeker_dob = jobseeker_dob;
    }

    public char getJobseeker_gender() {
        return jobseeker_gender;
    }

    public void setJobseeker_gender(char jobseeker_gender) {
        this.jobseeker_gender = jobseeker_gender;
    }

    public String getJobseeker_preferred_job() {
        return jobseeker_preferred_job;
    }

    public void setJobseeker_preferred_job(String jobseeker_preferred_job) {
        this.jobseeker_preferred_job = jobseeker_preferred_job;
    }

    public String getJobseeker_preferred_location() {
        return jobseeker_preferred_location;
    }

    public void setJobseeker_preferred_location(String jobseeker_preferred_location) {
        this.jobseeker_preferred_location = jobseeker_preferred_location;
    }

    public String getJobseeker_img() {
        return jobseeker_img;
    }

    public void setJobseeker_img(String jobseeker_img) {
        this.jobseeker_img = jobseeker_img;
    }

    public String getJobseeker_rating() {
        return jobseeker_rating;
    }

    public void setJobseeker_rating(String jobseeker_rating) {
        this.jobseeker_rating = jobseeker_rating;
    }

    public String getJobseeker_experience() {
        return jobseeker_experience;
    }

    public void setJobseeker_experience(String jobseeker_experience) {
        this.jobseeker_experience = jobseeker_experience;
    }
}
