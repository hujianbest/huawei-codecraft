package com.elasticcloudservice.predict.gga;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.elasticcloudservice.predict.base.*;

public class DNA implements Cloneable,Comparable<DNA>{
    private List<Server> servers;
    // private double CPUfitness;
    // private double RAMfitness;
    private double fitness;
    // private List<ServerStd> serverstds;
    private int vmamount;
    private int cpuneed;
    private int ramneed;
    private int cpuused;
    private int ramused;

    /**要求先要把存储虚拟机数据的map转换成一个个单独虚拟机组成的list,flavor要有id */
    public DNA(List<Flavor> VMdata,int numOfServerstd,List<ServerStd> serverstds,int vmamount,int cpuneed,int ramneed,int serveramonut){
        // this.serverstds=serverstds;
        this.cpuneed=cpuneed;
        this.ramneed=ramneed;
        this.vmamount=vmamount;
        this.cpuused=0;
        this.ramused=0;
        servers = new LinkedList<>();
        for(int i=0;i<serveramonut;)
            for(int j=0;j<numOfServerstd;j++){
            servers.add(new Server(i+1, serverstds.get(j)));//�?多三种服务器怎么设定初�?�？
            this.cpuused+=serverstds.get(j).getCPUStd();
            this.ramused+=serverstds.get(j).getRAMMBStd(); 
            i++;
        }
        for(int i=0;i<vmamount;){
            for(int j=0;j<serveramonut&&i<vmamount;j++){
                if(servers.get(j).biggerThan(VMdata.get(i))){
                    servers.get(j).addFlavor(VMdata.get(i));
                    i=i+1;
                }    
            }
        }



        
        // this.CPUfitness = (double)cpuneed/cpuused;
        // this.RAMfitness = (double)ramneed/ramused;
        this.fitness = ((double)ramneed/ramused+(double)cpuneed/cpuused)/2;
    }

    

    /**修改后需要重新计算�?�应�?*/
    public void update(){
        int cpuused=0;
        int ramused=0;
        for(Server s : servers){
            cpuused+=s.getCpu();
            ramused+=s.getRamMB();
        }
        this.cpuused = cpuused;
        this.ramused = ramused;

        // this.CPUfitness = (double)cpuneed/cpuused;
        // this.RAMfitness = (double)ramneed/ramused;
        // this.fitness = (CPUfitness+RAMfitness)/2;
        this.fitness = ((double)this.ramneed/this.ramused+(double)this.cpuneed/this.cpuused)/2;
    }

    
    /**返回CPUfi�?大的作为�?好基�? */
    // public Server getBestCPU(){
    //     Server best = servers.get(0);
    //     for(Server s : servers){
    //         best=s.getCPUFi()>best.getCPUFi()?s:best;
    //     }
    //     return (Server)best.clone();
    // }
    
    
    

    // /**CPUfi�?小作为最差基�? */
    // public Server getWorstCPU(){
    //     Server worst = servers.get(0);
    //     for(Server s : servers){
    //         if(s.getCPUFi()<worst.getCPUFi()){
    //             worst = s;
    //         }
    //         //worst=s.getCPUFi()<worst.getCPUFi()?s:worst;
    //     }
    //     return worst;
    // }
    
    
    // /**返回RAMfi�?大的作为�?好基�? */
    // public Server getBestRAM(){
    //     Server best = servers.get(0);
    //     for(Server s : servers){
    //         best=s.getRAMFi()>best.getRAMFi()?s:best;
    //     }
    //     return (Server)best.clone();
    // }
    
    
    
    // /**RAMfi�?小作为最差基�? */
    // public Server getWorstRAM(){
    //     Server worst = servers.get(0);
    //     for(Server s : servers){
    //         if(s.getRAMFi()<worst.getRAMFi()){
    //             worst = s;
    //         }
    //         //worst=s.getCPUFi()<worst.getCPUFi()?s:worst;
    //     }
    //     return worst;
    // }
    
    



    /**随机返回�?个基�? */
    public Server getRandom(){
        Random r = new Random();
        Server random = servers.get(r.nextInt(servers.size()));
        return random;
    }

    public Server getBest(){
        Server best = Collections.max(servers);
        // Server best = servers.get(0);
        // for(Server s : servers){
        //     best=s.getFi()>best.getFi()?s:best;
        // }
        return (Server)best.clone();
    }

    public Server getWorst(){
        Server worst = Collections.min(servers);
        // Server worst = servers.get(0);
        // for(Server s : servers){
        //     if(s.getFi()<worst.getFi()){
        //         worst = s;
        //     }
        //     //worst=s.getCPUFi()<worst.getCPUFi()?s:worst;
        // }
        return worst;
    }



    /**复制，生成child的时候用 */    
    //各种链表，一定要全部深拷贝，用Object的clone()
    @Override
    public Object clone(){
        DNA dna = null;
        try {
            dna = (DNA)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        dna.servers = new LinkedList<>();
        for(Server s:servers){
            dna.servers.add((Server)s.clone());
        }
        return dna;
    }    


    


    /**在DNA中插入一段基因，不能有重复元素，�?要重�? */
    public boolean add(Server bestgene){

        List<Flavor> deleted = new LinkedList<>();//记录被删除的�?有基因元�?

        //System.out.println(this+" �?始构造子代，构�?�前DNA长度�? "+this.servers.size()+" 适应度为 "+this.CPUfitness);

        // for(Flavor f : bestgene.getFlavorlist()){
        //     servers.removeIf(s -> s.contain(f));
        //     //deleted.addAll(s.getFlavorlist());
        // }

        Iterator<Server> it = servers.iterator();
        while(it.hasNext()){//删除含有在gene中出现过元素的基�?
            Server s = it.next();
            for(Flavor f : bestgene.getFlavorlist()){
                if(s.contain(f)){
                    deleted.addAll(s.getFlavorlist());
                    it.remove();
                    break;
                }
            }

        }

        servers.add(bestgene);//添加gene
        this.update();
        //System.out.println(this+" 构�?�后DNA长度�? "+this.servers.size()+" 适应度为 "+this.CPUfitness);
        //System.out.println();
        

        Iterator<Flavor> dlit = deleted.iterator();
        while(dlit.hasNext()){//去除deleted中在gene中出现过的元�?
            Flavor f= dlit.next();
            if(bestgene.contain(f)){
                dlit.remove();
            }
        }
        
        Server worst = this.getRandom();
        for(int i=0,watch =0;i<deleted.size();){//未分配的元素�?�?差的server里面插入，插不下了再找最差的
            if(worst.biggerThan(deleted.get(i))){
                worst.addFlavor(deleted.get(i));
                i++;
            }else{
                watch++;
                if(watch<=servers.size()){      //怎么防止死循环呢，就是已经很优了，突变一个之后根本放不进去了
                    worst = this.getRandom();    //这种情况直接不删了，怎么复原呢？
                }else{
                    return false;
                }                               //正常执行返回1，不该删除返�?0，是1的时候用这个对象，是0则不产生孩子或�?�不产生变异
            } 
                                          
        }                               
        return true;
    }



    /**子类单个DNA变异 */
    public boolean mute(){
        Server worst = this.getWorst();
       // System.out.println(this+" 准备变异，变异前DNA长度�? "+this.servers.size()+" 适应度为 "+this.CPUfitness);
       this.servers.remove(worst);//System.out.println("变异成功");
        //this.update();
       // System.out.println(this+" 变异后DNA长度�? "+this.servers.size()+" 适应度为 "+this.CPUfitness);
        

        //重新分配被删除的元素
        Server nextworst = this.getRandom();
        List<Flavor> deleted = worst.getFlavorlist();
        for(int i=0,watch =0;i<deleted.size();){//未分配的元素�?�?差的server里面插入，插不下了再找最差的
            if(nextworst.biggerThan(deleted.get(i))){
                nextworst.addFlavor(deleted.get(i));
                i++;
            }else{
                if(watch<servers.size()){      //怎么防止死循环呢，就是已经很优了，突变一个之后根本放不进去了
                    nextworst = this.getRandom();    //这种情况直接不删了，怎么复原呢？
                    watch++;
                }else{
                    //System.out.println("变异失败�?始恢复数�?");
                   //System.out.println(this+" 恢复后DNA长度�? "+this.servers.size()+" 适应度为 "+this.CPUfitness);
                   // System.out.println();
                    return false;
                }                               //正常执行返回1，不该删除返�?0，是1的时候用这个对象，是0则不产生孩子或�?�不产生变异
            }
                                          
        }    
        //System.out.println();                        
        return true;
    }


    @Override
    public int compareTo(DNA dna){
        if(this.fitness>dna.fitness){
            return 1;
        }else if(this.fitness<dna.fitness){
            return -1;
        }else{
            return 0;
        }
    }


    //访问器方�?
    public List<Server> getServerList(){
        return servers;
    }

    public int getFlavoramount(){
        int i=0;
        for(Server s:servers){
            i=i+s.getFlavorlist().size();
        }
        return i;
    }

    public double getFitness(){
        return fitness;
    }

    // public double getCpuFitness(){
    //     return CPUfitness;
    // }

    // public double getRamFitness(){
    //     return RAMfitness;
    // }

    public int getVmamount(){
        return vmamount;
    }
}
