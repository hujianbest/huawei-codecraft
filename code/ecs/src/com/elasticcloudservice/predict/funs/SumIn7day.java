package com.elasticcloudservice.predict.funs;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.elasticcloudservice.predict.base.*;

public class SumIn7day{

    public static Map<Flavor,List<Integer>> getSumIn7day(Map<Flavor,Map<String,Integer>> orgDataWithZeros){
        Map<Flavor,List<Integer>> result = new LinkedHashMap<>();
        for(Flavor f : orgDataWithZeros.keySet() ){
            List<Integer> dataList = new ArrayList<>();
            List<Integer> sumList = new ArrayList<>();
            for(String s : orgDataWithZeros.get(f).keySet() ){
                dataList.add(orgDataWithZeros.get(f).get(s));
            }
            int len = dataList.size();
            for(int i=7;i<=len;i++){
                int sum = 0;
                for(int j = i-7;j<i;j++){
                    sum+=dataList.get(j);
                }
                sumList.add(sum);
            }
            result.put(f, sumList);
        }
        // printMap(result);
        return result;
    }

    private static void printMap(Map<Flavor,List<Integer>> result){
        for(Flavor f : result.keySet()){
            System.out.print(f.getName()+"\t");
            for(Integer i : result.get(f)){
                System.out.print(i+"\t");
            }
            System.out.println();
        }
    }
}