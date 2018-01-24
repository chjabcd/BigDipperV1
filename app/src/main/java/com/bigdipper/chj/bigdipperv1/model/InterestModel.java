package com.bigdipper.chj.bigdipperv1.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chj on 2018-01-23.
 */

public class InterestModel {

    public String interestUid;
    public String interestName;
    public String interestGroup;
    public String interestLikeUid;
    public String interestPhotoUrl;
    public String comments;

    public InterestModel() {
    }

    public InterestModel(String interestName, String interestGroup, String interestLikeUid, String interestPhotoUrl, String comments) {

        this.interestName = interestName;
        this.interestGroup = interestGroup;
        this.interestLikeUid = interestLikeUid;
        this.interestPhotoUrl = interestPhotoUrl;
        this.comments = comments;
    }
}
