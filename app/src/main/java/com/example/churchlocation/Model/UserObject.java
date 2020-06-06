package com.example.churchlocation.Model;

public class UserObject {
    private String fullname, email, password, title, gender, church, displayPic, id, leaderCountry;
    private boolean emailVerification, titleVerification;

    public UserObject() {
    }

    public UserObject(String email, String id) {
        this.email = email;
        this.id = id;
    }

    public UserObject(String fullname, String email, String password, String title, String gender, String church, String displayPic, boolean emailVerification, boolean titleVerification, String id) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.title = title;
        this.gender = gender;
        this.church = church;
        this.displayPic = displayPic;
        this.emailVerification = emailVerification;
        this.titleVerification = titleVerification;
        this.id = id;
    }

    public UserObject(String fullname, String email, String password, String title, String gender, String church, String displayPic, String id, String leaderCountry, boolean emailVerification, boolean titleVerification) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.title = title;
        this.gender = gender;
        this.church = church;
        this.displayPic = displayPic;
        this.id = id;
        this.leaderCountry = leaderCountry;
        this.emailVerification = emailVerification;
        this.titleVerification = titleVerification;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getChurch() {
        return church;
    }

    public void setChurch(String church) {
        this.church = church;
    }

    public String getDisplayPic() {
        return displayPic;
    }

    public void setDisplayPic(String displayPic) {
        this.displayPic = displayPic;
    }

    public boolean isEmailVerification() {
        return emailVerification;
    }

    public void setEmailVerification(boolean emailVerification) {
        this.emailVerification = emailVerification;
    }

    public boolean isTitleVerification() {
        return titleVerification;
    }

    public void setTitleVerification(boolean titleVerification) {
        this.titleVerification = titleVerification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaderCountry() {
        return leaderCountry;
    }

    public void setLeaderCountry(String leaderCountry) {
        this.leaderCountry = leaderCountry;
    }
}
