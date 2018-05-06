package com.elasticcloudservice.predict.funs;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.elasticcloudservice.predict.base.*;

public class Pretreatment {

    

    /**
     * 按天计数
     * @param history
     * @return Map<String,Map<String,Integer>>
     */
    static public Map<Flavor, Map<String, Integer>> timesInOneDayWithZeros(List<String> history, InputMsg input) {
        String[] startarray = history.get(0).split(" ");
        String[] endarray = history.get(history.size() - 1).split(" ");
        String start = startarray[2].substring(0, 10);
        String end = endarray[2].substring(0, 10);

        Calendar startCalendar = Format.Str2Cal(start);
        Calendar endCalendar = Format.Str2Cal(end);
        endCalendar.add(Calendar.DAY_OF_YEAR, 1);

        //根据输入文件创建外层MAP
        Map<Flavor, Map<String, Integer>> countResult = new LinkedHashMap<>();
        for (int i = input.getNumOfFlavor()-1; i >=0; i--) {
            //根据测试文件创建内层MAP
            Map<String, Integer> date_and_times = new LinkedHashMap<>();
            try {
            	Date date = null;
            	date = Format.sdf.parse(start);
                startCalendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            while (startCalendar.before(endCalendar)) {
                date_and_times.put(Format.sdf.format(startCalendar.getTime()), 0);
                startCalendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            //System.out.println(input.getFlavorList().get(i).getName());
            countResult.put(input.getFlavorList().get(i), date_and_times);
        }

        for (int i = 0; i < history.size(); i++) {
            String[] array = history.get(i).split(" ");
            String flavorName = array[1];
            Flavor flavor =  Format.GetFlavorFromName(flavorName);
            String createTime = array[2];
            if (countResult.containsKey(flavor)) {
                Map<String,Integer> val = countResult.get(flavor);
                    val.put(createTime.substring(0,10), val.getOrDefault(createTime.substring(0,10),0)+1);
                    countResult.put(flavor, val);
            }
        }
        return countResult;
    }



    static public Map<Flavor, Map<String, Integer>> timesInOneDayWithoutZeros(List<String> history, InputMsg input){
        
        Map<Flavor, Map<String, Integer>> countResult = new LinkedHashMap<>();
        //根据输入文件创建外层MAP
        for (int i = input.getNumOfFlavor()-1; i >=0; i--){
            Map<String, Integer> date_and_times = new LinkedHashMap<>();
            countResult.put(input.getFlavorList().get(i), date_and_times);
        }
        //遍历历史数据
        for (int i = 0; i < history.size(); i++) {
            String[] array = history.get(i).split(" ");
            String flavorName = array[1];
            Flavor flavor =  Format.GetFlavorFromName(flavorName);
            String createTime = array[2];
            if (countResult.containsKey(flavor)) {
                Map<String,Integer> val = countResult.get(flavor);
                    val.put(createTime.substring(0,10), val.getOrDefault(createTime.substring(0,10),0)+1);
                    countResult.put(input.getFlavorList().get(i), val);
            }
        }
        return countResult;
    }
    
    

    static public Map<Flavor,List<Integer>> timesInNdaysSplit(Map<Flavor,Map<String,Integer>> former,InputMsg input){
        int N = input.getDays();
        Map<Flavor,List<Integer>> result = new LinkedHashMap<>();
        for(Flavor f: former.keySet()){
            List<Integer> list = new ArrayList<>();
            for(String s: former.get(f).keySet()){
                list.add(former.get(f).get(s));
            }
            List<Integer> listfinal = new ArrayList<>();
            
            for(int i=list.size()-1;i>(list.size()%N);){
                int sum=0;
                for(int j=0;j<N;j++){
                    sum+=list.get(i-j);
                }
                listfinal.add(0,sum);
                i-=N;
            }
            result.put(f, listfinal);
        }
        return result;

    }


    static public Map<Flavor, Map<String, Double>> normalization(List<String> history, InputMsg input) {
        String[] startarray = history.get(0).split(" ");
        String[] endarray = history.get(history.size() - 1).split(" ");
        String start = startarray[2].substring(0, 10);
        String end = endarray[2].substring(0, 10);

        Calendar startCalendar = Format.Str2Cal(start);
        Calendar endCalendar = Format.Str2Cal(end);
        endCalendar.add(Calendar.DAY_OF_YEAR, 1);

        //根据输入文件创建外层MAP
        Map<Flavor, Map<String, Double>> countResult = new LinkedHashMap<>();
        for (int i = input.getNumOfFlavor()-1; i >=0; i--) {
            //根据测试文件创建内层MAP
            Map<String, Double> date_and_times = new LinkedHashMap<>();
            try {
            	Date date = null;
            	date = Format.sdf.parse(start);
                startCalendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            while (startCalendar.before(endCalendar)) {
                date_and_times.put(Format.sdf.format(startCalendar.getTime()), 0.1);
                startCalendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            countResult.put(input.getFlavorList().get(i), date_and_times);
        }

        for (int i = 0; i < history.size(); i++) {
            String[] array = history.get(i).split(" ");
            String flavorName = array[1];
            Flavor flavor =  Format.GetFlavorFromName(flavorName);
            String createTime = array[2];
            if (countResult.containsKey(flavor)) {
                Map<String,Double> val = countResult.get(flavor);
                    val.put(createTime.substring(0,10), val.getOrDefault(createTime.substring(0,10),0.1)+0.1);
                    countResult.put(flavor, val);
            }
        }
        return countResult;
    }

    
}
