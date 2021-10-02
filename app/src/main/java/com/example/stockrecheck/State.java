package com.example.stockrecheck;

import com.example.stockrecheck.dao.Results;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State  {
    public String getPickedPlan() {
        return pickedPlan;
    }

    public void setPickedPlan(String pickedPlan) {
        this.pickedPlan = pickedPlan;
    }


    public static List<Results> getPickedList() {
        return pickedList;
    }

    public static void setPickedList(List<Results> pikedList) {
        State.pickedList = pickedList;
    }

    public static List<Results> pickedList;
    public static String pickedPlan;
}
