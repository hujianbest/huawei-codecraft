package com.elasticcloudservice.predict.base;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.elasticcloudservice.predict.funs.*;

public class InputMsg{
    private int num_of_server;
    private List<ServerStd> serverstds;
    private int num_of_flavor;
    private List<Flavor> flavors;
    private int days;
    private int interval;
    private int ecsDays;
    
    public InputMsg(String[] inputContent,String[] ecsContent){
        num_of_server = Format.Str2Int(inputContent[0]);//服务器类型数
        serverstds = new LinkedList<>();
        int i;
        for(i=1;i<=num_of_server;i++){
            serverstds.add(new ServerStd(inputContent[i]));//初试化服务器信息
        }
        

        num_of_flavor = Format.Str2Int(inputContent[++i]);//设定要预测的虚拟机类型数
        i++;
        flavors = new LinkedList<>();
        while(inputContent[i].length()!=0){//要预测的虚拟机种�?
            String[] flavor = inputContent[i].split(" ");
            flavors.add(new Flavor(flavor[0], Format.Str2Int(flavor[1]), Format.Str2Int(flavor[2])));
            i++;
        }


        String[] ecsStart = ecsContent[0].split("\t");

        String[] ecsEnd = ecsContent[ecsContent.length-2].split("\t");

        String sstartTime = inputContent[++i];//只取�?
        String sendTime = inputContent[++i];

        Calendar ecsStartTime = Format.Str2Cal(ecsStart[2].substring(0,10));
        Calendar startTime=Format.Str2Calv2(sstartTime);//�?始时�?
        Calendar endTime=Format.Str2Calv2(sendTime);//结束时间
        Calendar ecsEndTime = Format.Str2Cal(ecsEnd[2].substring(0,10));

        ecsDays = (int)Math.ceil((ecsEndTime.getTimeInMillis() - ecsStartTime.getTimeInMillis())/(1000*60*60*24.0))+1;
        interval = (int)Math.ceil((startTime.getTimeInMillis() - ecsEndTime.getTimeInMillis())/(1000*60*60*24.0))-1;
        days = (int)Math.ceil((endTime.getTimeInMillis() - startTime.getTimeInMillis())/(1000*60*60*24.0));//要预测的总天�?
    }


    //访问器方法
    public int getNumOfFlavor(){
        return num_of_flavor;
    }
    
    public int getEcsDays(){
        return ecsDays;
    }

    public List<Flavor> getFlavorList(){
        return flavors;
    }

    public int getInterval(){
        return interval;
    }

    public int getDays(){
        return days;
    }

    public int getNumOfServerstds(){
        return num_of_server;
    }

    public List<ServerStd> getServerstds(){
        return serverstds;
    }
}

