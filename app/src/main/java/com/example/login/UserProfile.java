package com.example.login;

public class UserProfile {

    public String username;
    public String useremail;
    public String userdob;
    public String usergender;
    public String usernumber;
    public String userdocname;
    public String userdocnum;

    public UserProfile (){}

    public UserProfile(String username, String useremail, String userdob, String usergender, String usernumber, String userdocname, String userdocnum) {
        this.username = username;
        this.useremail = useremail;
        this.userdob = userdob;
        this.usergender = usergender;
        this.usernumber = usernumber;
        this.userdocname = userdocname;
        this.userdocnum = userdocnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserdob() {
        return userdob;
    }

    public void setUserdob(String userdob) {
        this.userdob = userdob;
    }

    public String getUsergender() {
        return usergender;
    }

    public void setUsergender(String usergender) {
        this.usergender = usergender;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getUserdocname() {
        return userdocname;
    }

    public void setUserdocname(String userdocname) {
        this.userdocname = userdocname;
    }

    public String getUserdocnum() {
        return userdocnum;
    }

    public void setUserdocnum(String userdocnum) {
        this.userdocnum = userdocnum;
    }
}
