package com.example.churchlocation.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserObject implements Parcelable {
    private String fullname, email, password, title, gender, church, displayPic,
            id, leaderCountry, username, state, dateOfBirth, dateOfBaptism, bio, hobby,
            titleVerification;
    private boolean emailVerification;

    private String status;

    public UserObject() {
    }


    public UserObject(String fullname, String email, String church, String id, String status, String username) {
        this.fullname = fullname;
        this.email = email;
        this.church = church;
        this.id = id;
        this.status = status;
        this.username = username;
    }

    public UserObject(String fullname, String email, String church, String id, String leaderCountry, String status, String username) {
        this.fullname = fullname;
        this.email = email;
        this.church = church;
        this.id = id;
        this.leaderCountry = leaderCountry;
        this.status = status;
        this.username = username;
    }

    public UserObject(String fullname, String email, String password, String title, String gender, String church, String displayPic, String id, String leaderCountry, String username, String state, String dateOfBirth, String dateOfBaptism, String bio, String hobby, String titleVerification) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.title = title;
        this.gender = gender;
        this.church = church;
        this.displayPic = displayPic;
        this.id = id;
        this.leaderCountry = leaderCountry;
        this.username = username;
        this.state = state;
        this.dateOfBirth = dateOfBirth;
        this.dateOfBaptism = dateOfBaptism;
        this.bio = bio;
        this.hobby = hobby;
        this.titleVerification = titleVerification;
    }

    public UserObject(String fullname, String email, String password, String title, String gender, String church, String displayPic, String id, String leaderCountry, String username, String state, String dateOfBirth, String dateOfBaptism, boolean emailVerification, String titleVerification, String status) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.title = title;
        this.gender = gender;
        this.church = church;
        this.displayPic = displayPic;
        this.id = id;
        this.leaderCountry = leaderCountry;
        this.username = username;
        this.state = state;
        this.dateOfBirth = dateOfBirth;
        this.dateOfBaptism = dateOfBaptism;
        this.emailVerification = emailVerification;
        this.titleVerification = titleVerification;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String isTitleVerification() {
        return titleVerification;
    }

    public void setTitleVerification(String titleVerification) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfBaptism() {
        return dateOfBaptism;
    }

    public void setDateOfBaptism(String dateOfBaptism) {
        this.dateOfBaptism = dateOfBaptism;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullname);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(title);
        parcel.writeString(gender);
        parcel.writeString(church);
        parcel.writeString(displayPic);
        parcel.writeString(id);
        parcel.writeString(leaderCountry);
        parcel.writeString(username);
        parcel.writeString(state);
        parcel.writeString(dateOfBirth);
        parcel.writeString(dateOfBaptism);
        parcel.writeString(bio);
        parcel.writeString(hobby);
        parcel.writeString(titleVerification);
    }

    protected UserObject(Parcel in) {
        fullname = in.readString();
        email = in.readString();
        password = in.readString();
        title = in.readString();
        gender = in.readString();
        church = in.readString();
        displayPic = in.readString();
        id = in.readString();
        leaderCountry = in.readString();
        username = in.readString();
        state = in.readString();
        dateOfBirth = in.readString();
        dateOfBaptism = in.readString();
        titleVerification = in.readString();
        bio = in.readString();
        hobby = in.readString();
        emailVerification = in.readByte() != 0;
        status = in.readString();
    }

    public static final Creator<UserObject> CREATOR = new Creator<UserObject>() {
        @Override
        public UserObject createFromParcel(Parcel in) {
            return new UserObject(in);
        }

        @Override
        public UserObject[] newArray(int size) {
            return new UserObject[size];
        }
    };

}
