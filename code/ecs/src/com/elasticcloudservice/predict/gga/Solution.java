package com.elasticcloudservice.predict.gga;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.elasticcloudservice.predict.base.*;

public class Solution{
    public static DNA solution(Map<Flavor,Integer> prdtresult,InputMsg input){

        List<Flavor> VMdata = new LinkedList<>();
        int flavor_id = 0;
        for(Flavor f : prdtresult.keySet()){
            for(int i=0;i<prdtresult.get(f);i++){
                flavor_id++;
                VMdata.add(new Flavor(flavor_id, f.getName(), f.getCpu(), f.getRamMB()));
            }
        }

        int cpuneed=0;
        int ramneed=0;
        //System.out.println();
        for(Flavor f: prdtresult.keySet()){
            cpuneed+=f.getCpu()*prdtresult.get(f);
            ramneed+=f.getRamMB()*prdtresult.get(f);
            //System.out.println(f.toString());
        }
        
        int minCpu = input.getServerstds().get(0).getCPUStd();
        int minRam = input.getServerstds().get(0).getRAMMBStd();
        for(ServerStd sd: input.getServerstds()){
            minCpu=Math.min(minCpu, sd.getCPUStd());
            minRam=Math.min(minRam, sd.getRAMMBStd());
        }

        
        int servermin = Math.max((int)((cpuneed/minCpu)+1),(int)((ramneed/minRam)+1));//运行时间要求降低规格
        int serveramonut = (int)(servermin*3.0);
        //int serveramonut = (int)(servermin);//创建个体对象时随机DNA长度
        int numOfServerstd = input.getNumOfServerstds();
        //int numOfServerstd = 2;//只采用通用型
        List<ServerStd> serverstds = input.getServerstds();
        int groupsize = 230;
        double ratec = 0.8;
        double ratem = 0.2;
        int generation = 40;

        Evolution evolution = new Evolution(VMdata, numOfServerstd,serverstds, serveramonut, groupsize, ratec, ratem, generation,cpuneed,ramneed);
        //System.err.println("遗传算法�?�?");
        evolution.start();
        //evolution.printcpuneed();
        //System.err.println("理想情况下的�?小服务器数量 = "+servermin);
        //System.err.println("CPU�?高利用率 = "+((double)cpuneed/(servermin*serverstd.getCPUStd())));
        //System.err.println("RAM�?高利用率 = "+((double)ramneed/(servermin*serverstd.getRAMMBStd())));
        //System.err.println("当前CPU利用�?  = "+evolution.end().getCpuFitness());
        //System.err.println("当前RAM利用�?  = "+evolution.end().getRamFitness());
        //System.out.println("当前总体利用�?  = "+evolution.end().getFitness());
        return evolution.end();

    }
}
