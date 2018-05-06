package com.elasticcloudservice.predict.base;

import com.elasticcloudservice.predict.funs.*;

public class Flavor{
    private int flavor_id;
    private String flavor_name;
    private int flavor_cpu;
    private int flavor_ram_MB;
    private int flavor_ram_GB;

    
    public Flavor(int flavor_id,String flavor_name, String flavor_cpu, String flavor_ram_MB) {
        this.flavor_id=flavor_id;
        this.flavor_name = flavor_name;
        this.flavor_cpu = Format.Str2Int(flavor_cpu);
        this.flavor_ram_MB = Format.Str2Int(flavor_ram_MB);
        this.flavor_ram_GB = this.flavor_ram_MB/1024;
    }

    public Flavor(int flavor_id,String flavor_name, int flavor_cpu, int flavor_ram_MB) {
        this.flavor_id=flavor_id;
        this.flavor_name = flavor_name;
        this.flavor_cpu = flavor_cpu;
        this.flavor_ram_MB = flavor_ram_MB;
        this.flavor_ram_GB = this.flavor_ram_MB/1024;
    }

    public Flavor(String flavor_name, int flavor_cpu, int flavor_ram_MB) {
        this.flavor_id=0;
        this.flavor_name = flavor_name;
        this.flavor_cpu = flavor_cpu;
        this.flavor_ram_MB = flavor_ram_MB;
        this.flavor_ram_GB = this.flavor_ram_MB/1024;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Flavor){
            if(((Flavor)object).flavor_id==flavor_id&&((Flavor)object).flavor_name.equals(flavor_name)){
                return true;
            }      
        }
        return false;
    }

    @Override
    public int hashCode(){
        if(flavor_id==0)
            return flavor_name.hashCode();
        else
            return ((Integer)flavor_id).hashCode();
    }


    @Override
    public String toString(){
        String line = flavor_id+" "+flavor_name+" "+flavor_cpu+" "+flavor_ram_MB;
        return line;
    }



    //访问器方法
    public String getName(){
        return flavor_name;
    }

    public int getCpu(){
        return flavor_cpu;
    }

    public int getRamGB(){
        return flavor_ram_GB;
    }

    public int getRamMB(){
        return flavor_ram_MB;
    }
}
