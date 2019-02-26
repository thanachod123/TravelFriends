package com.finalproject.it.travelfriend.Model;

public class UserSetting {


    private GuideData guideData;
    private UserData userData;


    public UserSetting() {

    }

    public UserSetting(GuideData guideData, UserData userData) {
        this.guideData = guideData;
        this.userData = userData;
    }



    public GuideData getGuideData() {
        return guideData;
    }

    public void setGuideData(GuideData guideData) {
        this.guideData = guideData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    @Override
    public String toString() {
        return "UserSetting{" +
                "guideData=" + guideData +
                ", userData=" + userData +
                '}';
    }
}



