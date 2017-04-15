package edu.iit.xwu64.hw5_know_your_government;


import java.io.Serializable;

/**
 * Created by xiaoliangwu on 2017/4/5.
 */

public class Official implements Serializable{

    private String officeName;
    private String name;
    private String address;
    private String party;
    private String phone;
    private String url;
    private String email;
    private String photoUrl;
    private Channel channel;

    public Official(String officeName) {
        this.officeName = officeName;
    }

    public Official(String officeName, String name) {
        this.name = name;
        this.officeName = officeName;
    }


    public String getName() {
        return name;
    }

    public String getOfficeName() {
        return officeName;
    }

    public String getAddress() {
        return address;
    }

    public String getParty() {
        return party;
    }

    public String getPhone() {
        return phone;
    }

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
