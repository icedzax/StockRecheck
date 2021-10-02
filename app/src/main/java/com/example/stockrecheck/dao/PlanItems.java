package com.example.stockrecheck.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanItems {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("plan_doc")
    @Expose
    private String planDoc;
    @SerializedName("plant")
    @Expose
    private String plant;
    @SerializedName("matcode")
    @Expose
    private String matcode;
    @SerializedName("matdesc")
    @Expose
    private String matdesc;
    @SerializedName("batch")
    @Expose
    private String batch;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("sap_qty")
    @Expose
    private Integer sapQty;
    @SerializedName("sap_weight")
    @Expose
    private Double sapWeight;
    @SerializedName("act_qty")
    @Expose
    private Integer actQty;
    @SerializedName("act_weight")
    @Expose
    private Double actWeight;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("updated_on")
    @Expose
    private String updatedOn;
    @SerializedName("update_by")
    @Expose
    private String updateBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanDoc() {
        return planDoc;
    }

    public void setPlanDoc(String planDoc) {
        this.planDoc = planDoc;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getMatcode() {
        return matcode;
    }

    public void setMatcode(String matcode) {
        this.matcode = matcode;
    }

    public String getMatdesc() {
        return matdesc;
    }

    public void setMatdesc(String matdesc) {
        this.matdesc = matdesc;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSapQty() {
        return sapQty;
    }

    public void setSapQty(Integer sapQty) {
        this.sapQty = sapQty;
    }

    public Double getSapWeight() {
        return sapWeight;
    }

    public void setSapWeight(Double sapWeight) {
        this.sapWeight = sapWeight;
    }

    public Integer getActQty() {
        return actQty;
    }

    public void setActQty(Integer actQty) {
        this.actQty = actQty;
    }

    public Double getActWeight() {
        return actWeight;
    }

    public void setActWeight(Double actWeight) {
        this.actWeight = actWeight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

}
