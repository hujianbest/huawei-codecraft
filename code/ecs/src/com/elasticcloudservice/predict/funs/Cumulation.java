package com.elasticcloudservice.predict.funs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.elasticcloudservice.predict.base.*;;

public class Cumulation{
    /**
     * 统计累加值
     */
    public static Map<Flavor,List<Integer>> getCumulation(Map<Flavor, Map<String, Integer>> former){
        //Map转List
        Map<Flavor,List<Integer>> result = new LinkedHashMap<>();
        for(Flavor f : former.keySet()){
            List<Integer> val = new ArrayList<>();
            for(String s : former.get(f).keySet()){
                val.add(former.get(f).get(s));
            }
            result.put(f, val);
        }

        //计算累加值
        for(Flavor f : result.keySet()){
            List<Integer> cal = result.get(f);
            for(int i=1;i<cal.size();i++){
                cal.set(i, cal.get(i-1)+cal.get(i));
            }
            result.put(f, cal);
        }

        return result;
    }
}