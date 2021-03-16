package com.example.gpsenglish.Class;

public class Person {

    private String CircleUid;
    private String Name;
    private String Phone;
    private String Email;
    private String code;
    private String lat;
    private String lng;
    private String isSharing;
    private String userId;

    public Person()
    {

    }

    public Person(String circleuid, String name, String phone, String email, String code, String lat, String lng, String isSharing, String userId) {
        this.CircleUid = circleuid;
        this.Name = name;
        this.Phone = phone;
        this.Email = email;
        this.code = code;
        this.lat = lat;
        this.lng = lng;
        this.isSharing = isSharing;
        this.userId = userId;
    }

    // GETTERS

    public String getCircleUid() {
        return CircleUid;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public String getEmail() {
        return Email;
    }

    public String getCode() {
        return code;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getIsSharing() {
        return isSharing;
    }

    public String getUserId() {
        return userId;
    }

    //SETTERS

    public void setName(String name) {
        Name = name;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setIsSharing(String newIsSharing) {
        isSharing = newIsSharing;
    }
}
