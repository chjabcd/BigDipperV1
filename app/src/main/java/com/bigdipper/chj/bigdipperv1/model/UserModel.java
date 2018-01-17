package com.bigdipper.chj.bigdipperv1.model;

import java.util.Map;

/**
 * Created by chj on 2017-12-26.
 */

public class UserModel {
    public String userName;
    public String profileImageUrl;
    public String uid;
    public String pushToken;
    public String comment;

    public Map<String,PhoneBook> phonebook;

    public static class PhoneBook{
        public String id;
        public String name;
        public String phoneNumber;
        public String email;
        public String note;
        public String adrPoBox;
        public String adrStreet;
        public String adrCity;
        public String adrRegion;
        public String adrPostCode;
        public String adrCountry;
        public String adrType;
        public String office;
        public String officeTitle;
    }


}
