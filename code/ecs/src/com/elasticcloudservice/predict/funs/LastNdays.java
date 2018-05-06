package com.elasticcloudservice.predict.funs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.elasticcloudservice.predict.base.*;

public class LastNdays{
    public static Map<Flavor,Integer> getResult(Map<Flavor,Map<String,Integer>> orgDataWithZeros,InputMsg input,double ratio){
        Map<Flavor,Integer> result = new LinkedHashMap<>();
        Map<Flavor,List<Integer>> tmp = new LinkedHashMap<>();
        for(Flavor f : orgDataWithZeros.keySet()){
            List<Integer> tmplist = new ArrayList<>();
            for(String s : orgDataWithZeros.get(f).keySet()){
                tmplist.add(orgDataWithZeros.get(f).get(s));
            }
            tmp.put(f, tmplist);
        }
        for(Flavor f : tmp.keySet()){
            int count = 0;
            for(int i=tmp.get(f).size()-1;i>=tmp.get(f).size()-input.getDays();i--){
                count+=tmp.get(f).get(i);
            }
            result.put(f, (int)Math.round(count*ratio));
        }
        return result;
    }
}