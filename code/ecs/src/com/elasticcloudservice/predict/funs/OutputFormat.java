package com.elasticcloudservice.predict.funs;

import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.elasticcloudservice.predict.base.*;

public class OutputFormat{
	
	//output格式化输出函数
     public static String[] getResult(Map<Flavor,Integer> prdtresult,List<Server> assignresult){
         /**生成string[] */
        List<String> resultlist= new ArrayList<>();
        int sum=0;
        for(Flavor flavor:prdtresult.keySet()){
            sum = sum + prdtresult.get(flavor);
        }
        resultlist.add(sum+"");
        for(Flavor flavor:prdtresult.keySet()){
            resultlist.add(flavor.getName()+" "+prdtresult.get(flavor));
        }        
        resultlist.add("");
        resultlist.add(assignresult.size()+"");
        for(Server server : assignresult){
            resultlist.add(server.toString());
        }
        String[] result = new String[resultlist.size()];
        for(int i=0;i<resultlist.size();i++){
            result[i] = resultlist.get(i);
        }
        return result;
     }
     
     
     //新版最后格式化输出函数
     public static String[] getResultv2(Map<Flavor,Integer> prdtresult,List<Server> assignresult){
         /**生成string[] */
        List<String> resultlist= new ArrayList<>();
        int sum=0;
        for(Flavor flavor:prdtresult.keySet()){
            sum = sum + prdtresult.get(flavor);
        }
        resultlist.add(sum+"");
        for(Flavor flavor:prdtresult.keySet()){
            resultlist.add(flavor.getName()+" "+prdtresult.get(flavor));
        }        
        resultlist.add("");
        resultlist.add(assignresult.size()+"");


        int server_id=0;
        for(Server server : assignresult){
            Map<String,Integer> map = new LinkedHashMap<>();
            server_id++;
            String line = server_id+"";
            for(Flavor f : server.getFlavorlist()){
                map.put(f.getName(), map.getOrDefault(f.getName(), 0)+1);
            }
            for(String name : map.keySet()){
                line=line+" "+name+" "+map.get(name);
            }
            resultlist.add(line);
        }



        String[] result = new String[resultlist.size()];
        for(int i=0;i<resultlist.size();i++){
            result[i] = resultlist.get(i);
        }
        return result;
     }




     //复赛格式化输出函数
     public static String[] getResultv3(Map<Flavor,Integer> prdtresult,List<Server> assignresult,InputMsg input){
         /**生成string[] */
        List<String> resultlist= new ArrayList<>();
        int sum=0;
        for(Flavor flavor:prdtresult.keySet()){
            sum = sum + prdtresult.get(flavor);
        }
        resultlist.add(sum+"");
        for(Flavor flavor:prdtresult.keySet()){
            resultlist.add(flavor.getName()+" "+prdtresult.get(flavor));
        }        
        resultlist.add("");



        Map<String,List<Server>> serverMap = new LinkedHashMap<>();
        for(int i=0;i<input.getNumOfServerstds();i++){
            List<Server> sameTypeServers = new ArrayList<>();
            serverMap.put(input.getServerstds().get(i).getNAMEStd(), sameTypeServers);
        }

        for(Server s : assignresult){
            for(String str : serverMap.keySet()){
                if(str.equals(s.getName())){
                    serverMap.get(str).add(s);
                    break;
                }
            }
        }



        for(String s : serverMap.keySet()){
			if(serverMap.get(s).size()>0){
				resultlist.add(s+" "+serverMap.get(s).size());

				int server_id=0;
				for(Server server : serverMap.get(s)){
					Map<String,Integer> map = new LinkedHashMap<>();
					server_id++;
					String line = s+"-"+server_id;
					for(Flavor f : server.getFlavorlist()){
						map.put(f.getName(), map.getOrDefault(f.getName(), 0)+1);
					}
					for(String name : map.keySet()){
						line=line+" "+name+" "+map.get(name);
					}
					resultlist.add(line);
				}

				resultlist.add("");
			}
            
        }

        String[] result = new String[resultlist.size()-1];
        for(int i=0;i<resultlist.size()-1;i++){
            result[i] = resultlist.get(i);
        }
        return result;

     }
     



     /**控制台输出函�? */
    public static void printConsole(Map<Flavor,Map<String,Integer>> timesinoneday){
        timesinoneday.forEach((k,v)->{
            System.out.println("VM_Name: "+k.getName());
            v.forEach((subk,subv)->{
                System.out.println(subk+"\t"+subv);
            });
            System.out.println();
        });
    }

    public static void printConsoleDouble(Map<Flavor,Map<String,Double>> normal){
        normal.forEach((k,v)->{
            System.out.println("VM_Name: "+k.getName());
            v.forEach((subk,subv)->{
                System.out.println(subk+"\t"+subv);
            });
            System.out.println();
        });
    }

    public static void printConsole(String[] result){
        for(int i=0;i<result.length;i++){
            System.out.println(result[i]);
        }
    }

    public static void printConsole(List<Server> assignresult){
        System.out.println(assignresult.size());
        for(Server server : assignresult){
            System.out.println(server.toString());
        }
    }
    

    
    //打印新版Server类的格式输出
    public static void printConsolev2(List<Server> assignresult){
        System.out.println(assignresult.size());
        
        for(Server server : assignresult){
            Map<String,Integer> map = new LinkedHashMap<>();
            for(Flavor f : server.getFlavorlist()){
                map.put(f.getName(), map.getOrDefault(f.getName(), 0)+1);
            }
            for(String name : map.keySet()){
                System.out.print(" "+name+" "+map.get(name));
            }
        }
    }



    public static void printConsolePrdt(Map<Flavor, Integer> prdtresult){
        int sum=0;
        for(Flavor flavor:prdtresult.keySet()){
            sum = sum + prdtresult.get(flavor);
        }
        System.out.println(sum);
        for(Flavor flavor:prdtresult.keySet()){
            System.out.println(flavor.getName()+" "+prdtresult.get(flavor));
        }
        System.out.println();
    }

}
