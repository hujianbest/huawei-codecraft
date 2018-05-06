package com.elasticcloudservice.predict.funs;
import java.util.LinkedHashMap;
import java.util.Map;

import com.elasticcloudservice.predict.base.*;

public class Adjust{

    public static void adjustPrdResult(Map<Flavor,Integer> predictResult,double ratio){
        for(Flavor f : predictResult.keySet()){
            int value = (int)Math.round((double)(predictResult.get(f))*ratio);
            predictResult.put(f, value);
        }
    }

    public static Map<Flavor,Integer> adjustPrdResult(Map<Flavor,double[]> LtMap,InputMsg input){
        Map<Flavor,Integer> result = new LinkedHashMap<>();
        for(Flavor f : LtMap.keySet()){
            int value = (int)Math.round(LtMap.get(f)[LtMap.get(f).length-1]*input.getDays());

            result.put(f, value>=0?value:0);
        }
        return result;
    }
}