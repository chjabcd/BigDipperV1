package com.bigdipper.chj.bigdipperv1.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chj on 2018-01-04.
 */

public class PhoneBookModel extends ListModel{


    public Map<String,Id> phoneList = new HashMap<>();

    public static class Id{
        public String uid;
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
