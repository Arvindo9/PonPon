package com.jithvar.ponpon.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("India");
        cricket.add("Pakistan");
        cricket.add("Australia");
        cricket.add("England");
        cricket.add("South Africa");

        List<String> president = new ArrayList<String>();
        president.add("People 1");
        president.add("People 2");
        president.add("People 3");
        president.add("People 4");
        president.add("People 5");

        List<String> branches = new ArrayList<String>();
        branches.add("Branch 1");
        branches.add("Branch 2");
        branches.add("Branch 3");
        branches.add("Branch 4");
        branches.add("Branch 5");

//        expandableListDetail.put("CRICKET TEAMS", cricket);
        expandableListDetail.put("National President", president);
        expandableListDetail.put("Our Branches", branches);
        return expandableListDetail;
    }
}
