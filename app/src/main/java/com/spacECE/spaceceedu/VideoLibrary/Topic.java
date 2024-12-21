package com.spacECE.spaceceedu.VideoLibrary;
public class Topic {
    private String status, title, v_id, filter, length, v_URL, v_date,v_uni_no, desc, cntlike, cntdislike, views, cntcomment;

    public Topic(String status, String title, String v_id,
                 String filter, String length, String v_URL,
                 String v_date, String v_uni_no, String desc,
                 String cntlike, String cntdislike, String views,
                 String cntcomment) {
        this.status = status;
        this.title = title;
        this.v_id = v_id;
        this.filter = filter;
        this.length = length;
        this.v_URL = v_URL;
        this.v_date = v_date;
        this.v_uni_no = v_uni_no;
        this.desc = desc;
        this.cntlike = cntlike;
        this.cntdislike = cntdislike;
        this.views = views;
        this.cntcomment = cntcomment;
    }

    public String getCntlike() {
        return cntlike;
    }

    public String getCntdislike() {
        return cntdislike;
    }

    public String getViews() {
        return views;
    }

    public String getCntcomment() {
        return cntcomment;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getV_id() {
        return v_id;
    }

    public String getFilter() {
        return filter;
    }

    public String getLength() {
        return length;
    }

    public String getV_URL() {
        return v_URL;
    }

    public String getV_date() {
        return v_date;
    }

    public String getV_uni_no() {
        return v_uni_no;
    }

    public String getDesc() {
        return desc;
    }
}