package com.projecty.ddotybox.model;

/**
 * Created by byungwoo on 15. 4. 9..
 */
public class UserProfile {
    private static UserProfile userProfile = new UserProfile();
    String userName;
    String userPhotoUrl;
    String userGooglePlusProfile;
    String userEmail;

    private UserProfile(){

    }

    public static UserProfile getUser(){
        return userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserGooglePlusProfile() {
        return userGooglePlusProfile;
    }

    public void setUserGooglePlusProfile(String userGooglePlusProfile) {
        this.userGooglePlusProfile = userGooglePlusProfile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
