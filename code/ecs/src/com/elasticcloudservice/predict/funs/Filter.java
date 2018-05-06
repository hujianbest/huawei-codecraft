package com.elasticcloudservice.predict.funs;

import java.util.Map;

import com.elasticcloudservice.predict.base.*;

//单天超过10就视为异常
public class Filter{

    public static void filte(Map<Flavor,Map<String,Integer>> orgDataWithZeros){
        for(Flavor f : orgDataWithZeros.keySet()){
            for(String s : orgDataWithZeros.get(f).keySet()){
                if(orgDataWithZeros.get(f).get(s)>10){
                    orgDataWithZeros.get(f).put(s, 0);
                }
            }
        }
    }

    public static void filte2(Map<Flavor,Map<String,Integer>> orgDataWithZeros){
        for(Flavor f : orgDataWithZeros.keySet()){
            for(String s : orgDataWithZeros.get(f).keySet()){
                if(f.getName().equals("flavor2")||f.getName().equals("flavor15")||f.getName().equals("flavor16")||f.getName().equals("flavor17")||f.getName().equals("flavor18")){
                    if(orgDataWithZeros.get(f).get(s)>5){
                        orgDataWithZeros.get(f).put(s, 0);
                    }
                }else if(orgDataWithZeros.get(f).get(s)>10){
                    orgDataWithZeros.get(f).put(s, 0);
                }
            }
        }
    }

    public static void filte2(Map<Flavor,Map<String,Integer>> orgDataWithZeros,
                             Map<Flavor,Double> average){
        for(Flavor f : orgDataWithZeros.keySet()){
            for(String s : orgDataWithZeros.get(f).keySet()){
                double avg = average.get(f);
                if( orgDataWithZeros.get(f).get(s) > (int)Math.round(avg*5) 
                        ){//超过n周总量算为异常点，后期加上节假日条件
                    //如果是异常点把它变为平均值
                    orgDataWithZeros.get(f).put(s, (int)Math.round(avg));
                }
            }
        }
    }

    

    public static void filte(Map<Flavor,Map<String,Integer>> orgDataWithZeros,
                             Map<Flavor,Double> average){
        for(Flavor f : orgDataWithZeros.keySet()){
            for(String s : orgDataWithZeros.get(f).keySet()){
                int tempInt = (int)Math.round(average.get(f));
                if( ((orgDataWithZeros.get(f).get(s) - tempInt)
                        / tempInt) > 0.7
                        ){//超过平均值一定程度(30%)算为异常点，后期加上节假日条件
                    //如果是异常点把它变为平均值
                    orgDataWithZeros.get(f).put(s, tempInt);
                }
            }
        }
    }



    public static void newfilte(Map<Flavor,Map<String,Integer>> orgDataWithZeros,Map<Flavor,double[]> LtMap){
        for(Flavor f : orgDataWithZeros.keySet()){
            int i = 0;
            double[] Lt = LtMap.get(f);
            for(String s : orgDataWithZeros.get(f).keySet()){
                if( ((orgDataWithZeros.get(f).get(s) >= (int)(Lt[i]*2.0))) ){
                    orgDataWithZeros.get(f).put(s, (int)Lt[i]);
                    i++;
                }
            }
        }
    }

}
