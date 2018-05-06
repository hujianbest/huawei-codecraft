package com.elasticcloudservice.predict.base;
import com.elasticcloudservice.predict.funs.*;

public class ServerStd{
	private String NAME;
    private int CPU;
    private int RAMGB;
    private int DISK;

    public ServerStd(String NAME,int CPU,int RAMGB,int DISK){
        this.NAME=NAME;
        this.CPU=CPU;
        this.RAMGB=RAMGB;
        this.DISK=DISK;
    }

    public ServerStd(String inputContent){
        String[] serverinfo = inputContent.split(" ");
        this.NAME=serverinfo[0];
        this.CPU=Format.Str2Int(serverinfo[1]);
        this.RAMGB=Format.Str2Int(serverinfo[2]);
        this.DISK=Format.Str2Int(serverinfo[3]);
    }




    public int getCPUStd(){
        return CPU;
    }
    public int getRAMGBStd(){
        return RAMGB;
    }
    public int getDISKStd(){
        return DISK;
    }
    public int getRAMMBStd(){
        return RAMGB*1024;
    }
    public String getNAMEStd(){
        return NAME;
    }
}
