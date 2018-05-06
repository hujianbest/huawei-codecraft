package com.elasticcloudservice.predict.funs;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.elasticcloudservice.predict.base.*;

public class Format {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Calendar Str2Cal(String string) {
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar Str2Calv2(String string) {
        Date date = null;
        try {
            date = sdf2.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static int Str2Int(String s) {
        int i=0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static Flavor GetFlavorFromName(String flavor_name) {
        switch (flavor_name) {
        case "flavor1":
            return new Flavor("flavor1", 1, 1024);
        case "flavor2":
            return new Flavor("flavor2", 1, 2048);
        case "flavor3":
            return new Flavor("flavor3", 1, 4096);
        case "flavor4":
            return new Flavor("flavor4", 2, 2048);
        case "flavor5":
            return new Flavor("flavor5", 2, 4096);
        case "flavor6":
            return new Flavor("flavor6", 2, 8192);
        case "flavor7":
            return new Flavor("flavor7", 4, 4096);
        case "flavor8":
            return new Flavor("flavor8", 4, 8192);
        case "flavor9":
            return new Flavor("flavor9", 4, 16384);
        case "flavor10":
            return new Flavor("flavor10", 8, 8192);
        case "flavor11":
            return new Flavor("flavor11", 8, 16384);
        case "flavor12":
            return new Flavor("flavor12", 8, 32768);
        case "flavor13":
            return new Flavor("flavor13", 16, 16384);
        case "flavor14":
            return new Flavor("flavor14", 16, 32768);
        case "flavor15":
            return new Flavor("flavor15", 16, 65536);
        case "flavor16":
            return new Flavor("flavor16", 32, 32768);
        case "flavor17":
            return new Flavor("flavor17", 32, 65536);
        case "flavor18":
            return new Flavor("flavor18", 32, 131072);
        default:
            return null;
        }
    }
    
    public static Map<Flavor,Integer> transform(List<Map<String,Integer>> former){
        Map<Flavor,Integer> transform = new LinkedHashMap<>();
        for(Map<String,Integer> map : former){
            for(String name : map.keySet()){
                Flavor flavor = Format.GetFlavorFromName(name);
                transform.put(flavor, transform.getOrDefault(flavor, 0)+map.get(name));
            }
        }
        return transform;
    }

    public static void setAvgForTest(Map<Flavor,Double> avgmap,double avg){
        for(Flavor  f : avgmap.keySet()){
            avgmap.put(f, avg);
        }
    }
}
