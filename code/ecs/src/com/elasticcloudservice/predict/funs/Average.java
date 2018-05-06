package com.elasticcloudservice.predict.funs;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.elasticcloudservice.predict.base.*;

public class Average{

    /**
     * 返回平均�?
     * @param orgDataWithZeros 原始数据
     * @param N 平均数步�?
     * @return Map<falvorName,Average)
     */
    static public Map<Flavor, Double> getSimpleAverage(Map<Flavor, Map<String, Integer>> orgDataWithZeros) {
        //LogUtil.printLog("Begin to calculate average");
        Map<Flavor, Double> average = new LinkedHashMap<>();
        int count;
        double sum;
        Iterator<Map.Entry<Flavor, Map<String, Integer>>> it = orgDataWithZeros.entrySet().iterator();
        while (it.hasNext()) {
            count = 0;
            sum = 0;
            Map.Entry<Flavor, Map<String, Integer>> entry = it.next();
            for (String key : entry.getValue().keySet()) {
                sum += entry.getValue().get(key);
                count++;
            }
            average.put(entry.getKey(), sum / count);
        }
        return average;
    }

    /**
     * 根据天数预测
     * @param ratio 对预测的结果乘以�?�? 适当 的系�?
     */
    public static Map<Flavor,Integer> avgAdjust(Map<Flavor,Double> avg,int days,InputMsg input, double ratio){
        Map<Flavor,Integer> result = new LinkedHashMap<>();
        Random random = new Random();
        //double r = (double)(random.nextInt(400)/1000)+ratio;//r没有生效
        for(Flavor flavor:avg.keySet()){
        	double r = (double)random.nextDouble()*0.3+ratio;
            result.put(flavor, (int)Math.round(avg.get(flavor)*days*(input.getEcsDays()+input.getInterval())*ratio/90));
            //result.put(flavor, (int)Math.round(avg.get(flavor)*days*ratio));
        }
        return result;
    }

    public static Map<Flavor,Integer> avgAdjust(Map<Flavor,Double> avg, Map<Flavor,Double> rate, InputMsg input, double ratio){
        Map<Flavor,Integer> result = new LinkedHashMap<>();
        for(Flavor flavor:avg.keySet()){
            result.put(flavor, (int)Math.round(avg.get(flavor)
                *(rate.get(flavor)+(input.getInterval()+input.getDays())/ratio)));
        }
        return result;
    }



    public static Map<Flavor,Integer> newAvgAdjust(Map<Flavor,Double> avg,InputMsg input){
        int days = input.getDays();
        double ratio;
        Map<Flavor,Integer> result = new LinkedHashMap<>();
        for(Flavor flavor:avg.keySet()){
            switch(flavor.getName()){
                case "flavor1":
                    ratio= 0.5;
                case "flavor2":
                    ratio= 2.0;///1.5-2.0 
                case "flavor3":
                    ratio= 2.0;///1.5-2.0
                case "flavor4":
                    ratio= 1.5;//0.5-1.5
                case "flavor5":
                    ratio= 1.0;//1.0
                case "flavor6":
                    ratio= 2.0;//1.0-2.0
                case "flavor7":
                    ratio= 1.0;////2.0-3.0-1.0
                case "flavor8":
                    ratio= 3.0;///1.5-3.0
                case "flavor9":
                    ratio= 3.0;///1.5-3.0
                case "flavor10":
                    ratio= 3.0;///1.5-3.0
                case "flavor11":
                    ratio= 3.0;///1.5-3.0
                case "flavor12":
                    ratio= 3.0;///1.5-3.0
                case "flavor13":
                    ratio= 1.0;
                case "flavor14":
                    ratio= 1.0;
                case "flavor15":
                    ratio= 1.0;
                case "flavor16":
                    ratio= 1.0;
                case "flavor17":
                    ratio= 1.0;
                case "flavor18":
                    ratio= 1.0;
                default:
                    ratio = 2.7;
            }
            result.put(flavor, (int)Math.round(avg.get(flavor)*days*(input.getEcsDays()+input.getInterval())*ratio/90));
            //result.put(flavor, (int)Math.round(avg.get(flavor)*days*ratio));
        }
        return result;
    }

}
