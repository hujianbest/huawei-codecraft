package com.elasticcloudservice.predict.gga;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.elasticcloudservice.predict.base.*;


public class Evolution{

    private List<DNA> group;
    private final int groupsize;
    private final double ratec;
    private final double ratem;
    private final int generation;

    private List<Flavor> VMdata;
    private final List<ServerStd> serverstds;
    private final int vmamount;
    private final int cpuneed;
    private final int ramneed;
    private final int serveramonut;
    private final int numOfServerstd;


    public Evolution(List<Flavor> VMdata,int numOfServerstd, List<ServerStd> serverstds,int serveramonut,int groupsize, double ratec,double ratem,int generation,int cpuneed,int ramneed){
        this.group = new LinkedList<>();
        this.VMdata=VMdata;
        this.serverstds=serverstds;
        this.serveramonut = serveramonut;
        this.groupsize = groupsize;
        this.ratec=ratec;
        this.ratem=ratem;
        this.generation=generation;
        this.vmamount =VMdata.size();
        this.numOfServerstd=numOfServerstd;
        this.cpuneed=cpuneed;
        this.ramneed=ramneed;
        
    }

    public void start(){
        this.init();
        for(int i =0;i<generation;i++){
            this.copulate();
            this.naturalSelection();
            //System.out.println("GroupSize = "+this.group.size());
            //System.out.println("MAX_CPUfiness = "+this.getMaxFitness());
            //System.out.println("MIN_CPUfiness = "+this.getMinFitness());
            //System.out.println();
        }
    }


    /**
     * 初试化族�?
     * @param groupsize 族群大小
     * @param ratec 交配比率
     * @param ratem 突变几率
     * @param generation 代数
     */
    private void init(){
        for(int i=0;i<groupsize;i++){
            Collections.shuffle(VMdata);
            this.group.add(new DNA(VMdata, numOfServerstd, serverstds, vmamount, cpuneed, ramneed, serveramonut));
            //Random rd = new Random();
            //double r = rd.nextDouble()*3.0+1.0;
            //this.group.add(new DNA(VMdata, numOfServerstd, serverstds, vmamount, cpuneed, ramneed, (int)(serveramonut*r)));
        }
    }

    /**
     * 交配�?次，返回孩子
     * 要去除重复分配的部分
     * 以dad的优�?基因为参照，删除其他�?有与dad基因有重复的地方，未分配的重新分配到当前fi�?小的server上，放不下就�?fi次小的server上放，以此类�?
     * �?要单独写个分配未分配flavor的函数，后面基因突变会再次用�?
     */
    private DNA childoOf(DNA dad,DNA mom){
        Server bestgene = dad.getBest();
        DNA child = (DNA)mom.clone();//有可能失败，加判断，失败就不要这个孩子？要mom
        if(child.add(bestgene)){
            return child;
        }else{
            return (DNA)mom.clone();
        }
            
    }




    /**�?代交�? */
    private void copulate(){
        int pickc = (int)(groupsize*ratec);
        Collections.shuffle(group);
        List<DNA> childgroup = new LinkedList<>();
        for(int i=0;i<pickc;i++){//随机选择
            childgroup.add(this.childoOf(group.get(i), group.get(i+1)));
        }
        //基因突变
         int pickm = (int)(childgroup.size()*ratem);
         Collections.shuffle(childgroup);
         for(int i=0;i<pickm;i++){
             DNA mutation = (DNA)childgroup.get(i).clone();
             if(mutation.mute()){
                 childgroup.remove(i);
                 childgroup.add(mutation);
             }
         }
        group.addAll(childgroup);//将孩子们加入到族群中
    }


    /**
     * �?代筛选，删掉多少？保持数量稳定，删掉groupsize*ratec 
     * 对DNA进行排序，按CPUfitness，DNA�?要实现compareTo
     */
    private void naturalSelection(){
        Collections.sort(group);//默认从小达到排序
        for(int i=0;i<groupsize*ratec;i++){
            group.remove(0);
        }
    }

    //返回当前的最大CPUfitness
    public double getMaxFitness(){
        double Max = 0;
        for(DNA dna : group){
            Max = Math.max(Max, dna.getFitness());
        }
        return Max;
    }

    //返回当前的最小CPUfitness
    public double getMinFitness(){
        double Min = 1;
        for(DNA dna : group){
            Min = Math.min(Min, dna.getFitness());
        }
        return Min;
    }


    public DNA end(){
        Collections.sort(group);//默认从小达到排序
        for(int i=1;i<group.size();) {
        	if(group.get(groupsize-i).getVmamount()==group.get(groupsize-i).getFlavoramount()) {
	        	return group.get(groupsize-i);
	        }else if(i<=group.size()) {
	        	i++;
	        }else {
	        	System.err.println("VMamount has changed unexpectly");
	        }
        }
        return null;
//        	if(group.get(groupsize-1).vmamount==group.get(groupsize-1).getFlavoramount()) {
//	        	return group.get(groupsize-1);
//	        }else {
//	        	LogUtil.printLog("VMamount has changed unexpectly");
//	            return null;
//	        }    
    }

    public void printcpuneed(){
        int i=1;
        for(DNA dna : group){
            System.out.println(i+" "+dna.getFlavoramount());
            i++;
        }
    }

}   
