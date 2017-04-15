package edu.iit.xwu64.hw5_know_your_government;

import java.io.Serializable;
import java.security.SecureRandom;

/**
 * Created by xiaoliangwu on 2017/4/5.
 */

public class Channel implements Serializable{
    private String googlePlusId;
    private String facebookId;
    private String twitterId;
    private String youtubeId;

    public String getGooglePlusId() {
        return googlePlusId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setGooglePlusId(String googlePlusId) {
        this.googlePlusId = googlePlusId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }
}
