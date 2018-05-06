package com.elasticcloudservice.predict.base;


import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.elasticcloudservice.predict.base.ServerStd;

public class Server implements Comparable<Server> ,Cloneable {
    private String server_name;
    private int server_id;
    private final int server_cpu;
    private final int server_ram_GB;
    private final int server_ram_MB;
    private final int server_disk;
    private int cpu_left;
    private int ramMB_left;
    private List<Flavor> flavorlist;
    private double CPUfi;
    private double RAMfi;
    private double fi;



    public Server(int id,ServerStd serverStd){
        this.server_id=id;
        this.server_name=serverStd.getNAMEStd();
        this.server_cpu=serverStd.getCPUStd();
        this.server_ram_GB = serverStd.getRAMGBStd();;
        this.server_ram_MB=serverStd.getRAMGBStd()*1024;
        this.server_disk=serverStd.getDISKStd();
        this.cpu_left=server_cpu;
        this.ramMB_left=server_ram_GB*1024;
        this.flavorlist = new LinkedList<>();
        this.CPUfi=0.0;
        this.RAMfi=0.0;
        this.fi=0.0;
    }







    //自定义方法

    public boolean contain(Flavor flavor){
        return flavorlist.contains(flavor);
    }


    /**判断剩下的空间能不能容纳下某虚拟机 */
    public boolean biggerThan(Flavor flavor){
        if(cpu_left>=flavor.getCpu() && ramMB_left>=flavor.getRamMB())
            return true;
        else
            return false;
    }
    
    /**将虚拟机插入到该服务器中 */
    public void addFlavor(Flavor flavor){
        flavorlist.add(flavor);
        cpu_left-=flavor.getCpu();
        ramMB_left-=flavor.getRamMB();
        refreshCPUFi();
        refreshRAMFi();
        refreshFi();
    }

    /**更新服务器的CPU利用率 */
    private void refreshCPUFi(){
        CPUfi=(double)(server_cpu-cpu_left)/server_cpu;
    }

    /**更新服务器的RAM利用率 */
    private void refreshRAMFi(){
        RAMfi=(double)(server_ram_MB-ramMB_left)/server_ram_MB;
    }

    /**更新服务器的利用率 */
    private void refreshFi(){
        fi=(RAMfi+CPUfi)/2.0;
    }

    /**由flavorlist得到flavormap，计数便于最后输出 */
    public Map<Flavor,Integer> getFlavorNum(List<Flavor> flavorlist){
        Map<Flavor,Integer> flavormap = new LinkedHashMap<>();
        for(Flavor f: flavorlist){
            flavormap.put(f,flavormap.getOrDefault(f,0)+1);
        }
        return flavormap;
    }

    

    




    ///访问器方法
    public String getName(){
        return server_name;
    }
    public int getId(){
        return server_id;
    }
    public double getCPUFi(){
        return CPUfi;
    }
    public double getRAMFi(){
        return RAMfi;
    }
    public double getFi(){
        return fi;
    }
    public List<Flavor> getFlavorlist(){
        return flavorlist;
    }
    public int getCpu(){
        return server_cpu;
    }
    public int getRamMB(){
        return server_ram_MB;
    }



    






    //重写方法
    @Override
    public boolean equals(Object object){
        if(object instanceof Server){
            if(((Server)object).server_id==this.server_id){
                return true;
            }      
        }
        return false;
    }



    @Override
    public int hashCode(){
        return ((Integer)server_id).hashCode();
    }
    


    @Override
    public Object clone(){
        Server server =null;
        try {
            server = (Server)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        server.flavorlist = new LinkedList<>();
        server.flavorlist.addAll(flavorlist);
        return server;
    }

    /**
     * 比较器，为了能按fi的大小将服务器有序放置，方便删除
     * 怎么实现CPU与RAM比较可选？
     * 这里是CPU利用率比较
     */
    @Override
    public int compareTo(Server server){
        if(this.fi>server.fi){
            return 1;
        }else if(this.fi<server.fi){
            return -1;
        }else{
            return 0;
        }
    }


    @Override
    public String toString(){
        String line = server_name+"-"+server_id;//还需修改
        Map<Flavor,Integer> flavormap = getFlavorNum(flavorlist);
        for(Flavor f : flavormap.keySet()){
            line+=" "+f.getName()+" "+flavormap.get(f);
        }
        return line;
    }


}
