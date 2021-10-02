package com.example.stockrecheck.dao;

import com.google.gson.annotations.SerializedName;

public class Results {
    public String getPlan_doc() {
        return plan_doc;
    }

    public void setPlan_doc(String plan_doc) {
        this.plan_doc = plan_doc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    private String plan_doc;
    private String remark;

    public String getPlan_desc() {
        return plan_desc;
    }

    public void setPlan_desc(String plan_desc) {
        this.plan_desc = plan_desc;
    }

    private String plan_desc;
}
