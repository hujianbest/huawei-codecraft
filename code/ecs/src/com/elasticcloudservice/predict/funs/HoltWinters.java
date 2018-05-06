package com.elasticcloudservice.predict.funs;
import com.elasticcloudservice.predict.base.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @param alpha 
 * @param beta
 * @param gamma 
 * @param s �?要预测的Yi后的s�?
 * @param k i后的第k�?
 * @param debug 是否打印�?些结�?
 * 加�?�HW
 * level Lt = alpha*(Yt - St-s)+(1-alpha)*(Lt-1 + Bt-1)
 * trend Bt = beta(Lt-Lt-1) + (1-beta)*Bt-1
 * season St = gamma(Yt - Lt) + (1-gamma)St-s
 * forecast Ft+k = Lt + k*Bt + St+k-s
 * 乘�?�HW
 * level Lt = alpha*(Yt / St-s)+(1-alpha)*(Lt-1 + Bt-1)
 * trend Bt = beta(Lt-Lt-1) + (1-beta)*Bt-1
 * season St = gamma(Yt / Lt) + (1-gamma)St-s
 * forecast Ft+k = (Lt + k*Bt) * St+k-s-
 */
public class HoltWinters{

    public static Map<Flavor,Integer> getPredict(Map<Flavor,Map<String,Integer>> orgDataWithZeros,
            InputMsg input,double alpha,double beta,double gamma,int s){
        Map<Flavor,Integer> result = new LinkedHashMap<>();
        for(Flavor f : orgDataWithZeros.keySet() ){
            List<Integer> y = new ArrayList<>();
            for(String str : orgDataWithZeros.get(f).keySet() ){
                y.add(orgDataWithZeros.get(f).get(str));
            }
            result.put(f, numInNdyas(y, input, alpha, beta, gamma, s));
        }
        return result;
    }



    public static Map<Flavor,double[]> getLtMap(Map<Flavor,Map<String,Integer>> orgDataWithZeros,
            InputMsg input,double alpha,double beta,double gamma,int s){
        Map<Flavor,double[]> result = new LinkedHashMap<>();
        for(Flavor f : orgDataWithZeros.keySet() ){
            List<Integer> y = new ArrayList<>();
            for(String str : orgDataWithZeros.get(f).keySet() ){
                y.add(orgDataWithZeros.get(f).get(str));
            }

            double L0 = initLevel(y, s);
            double B0 = initTrend(y, s);
            double[] initSeanson = initSt(y, s, L0);
            double[] Lt = getLt(y, L0, B0, alpha, beta, gamma, initSeanson, s);
            result.put(f, Lt);
        }
        return result;
    }

    private static double[] getLt(List<Integer> y, double L0, double B0, double alpha,
            double beta, double gamma, double[] initSeanson, int s){
    	
        int len = y.size();        
        double Lt[] = new double[len];
        double Bt[] = new double[len];
        double St[] = new double[len];
        
        Lt[0]=L0;
        Bt[0]=B0;

        for(int i=0;i<s;i++){
            St[i] = initSeanson[i];
        }

        for(int i=1;i<len;i++){

            //计算Lt
            if((i-s)>=0){
                Lt[i] = alpha * (y.get(i) - St[i-s]) + (1.0 - alpha) * (Lt[i-1] + Bt[i-1]);
            }else{
                Lt[i] = alpha * y.get(i) + (1.0 - alpha) * (Lt[i-1] + Bt[i-1]);
            }

            //计算Bt
            Bt[i] = beta * (Lt[i] - Lt[i - 1]) + (1.0 - beta) * Bt[i-1];

            //计算St
            if((i-s)>=0){
                St[i] = gamma * (y.get(i) - Lt[i]) + (1.0 -gamma) * St[i-s];
            }
        }

        
        return Lt;
    }



    private static int numInNdyas(List<Integer> y, InputMsg input,double alpha,
            double beta,double gamma,int s){
        int days = input.getDays();
        int end = input.getInterval()+input.getDays();
        double sum = 0;
        double[] rt = forecast(y, alpha, beta, gamma, s, end);//seacon-s取多�?
        for(int i=y.size()+end-1;i>y.size()+end-1-days;i--){
            sum+=rt[i];
        }
        
        int result = (int)Math.round(sum);
        return result>0?result:0;
    }

    public static Map<Flavor,Integer> getPredictFromSum(Map<Flavor,List<Integer>> Sum7day,
            InputMsg input,double alpha,double beta,double gamma,int s){
        Map<Flavor,Integer> result = new LinkedHashMap<>();
        for(Flavor f : Sum7day.keySet() ){
            List<Integer> y = Sum7day.get(f);
            result.put(f, numInNdyasForSum(y, input, alpha, beta, gamma, s));
        }
        return result;
    }


    private static int numInNdyasForSum(List<Integer> y, InputMsg input,double alpha,
            double beta,double gamma,int s){
        int days = input.getDays();
        int end = input.getInterval()+input.getDays();
        double sum = 0;
        double[] rt = forecast(y, alpha, beta, gamma, s, end);//seacon-s取多�?
        int weeks = days/7;
        int left = days%7;
        for(int i=0;i<weeks;i++) {
        	sum+=rt[y.size()+end-1-7*i];
        }
        sum+=rt[y.size()+end-1-7*weeks]*left/7;
        
        int result = (int)Math.round(sum);
        return result>0?result:0;
    }



    private static double[] forecast(List<Integer> y,double alpha,double beta,
            double gamma,int s,int k){
        
        //int seasons = y.size()/s;
        double L0 = initLevel(y, s);
        double B0 = initTrend(y, s);
        double[] initSeanson = initSt(y, s, L0);

        double[] forecast = doHoltWinters(y, L0, B0, alpha, beta, gamma, initSeanson, s, k);
        
        return forecast;
    }

    private static double[] doHoltWinters(List<Integer> y, double L0, double B0, double alpha,
            double beta, double gamma, double[] initSeanson, int s, int k){
    	
        int len = y.size();        
        double Lt[] = new double[len];
        double Bt[] = new double[len];
        double St[] = new double[len];
        double Ft[] = new double[len+k];
        
        Lt[0]=L0;
        Bt[0]=B0;

        for(int i=0;i<s;i++){
            St[i] = initSeanson[i];
        }

        //Ft[s]=Lt[0]+Bt[0]+St[0];

        //�?始迭�?,采用乘�??
        for(int i=1;i<len;i++){

            //计算Lt
            if((i-s)>=0){
                Lt[i] = alpha * (y.get(i) - St[i-s]) + (1.0 - alpha) * (Lt[i-1] + Bt[i-1]);
            }else{
                Lt[i] = alpha * y.get(i) + (1.0 - alpha) * (Lt[i-1] + Bt[i-1]);
            }

            //计算Bt
            Bt[i] = beta * (Lt[i] - Lt[i - 1]) + (1.0 - beta) * Bt[i-1];

            //计算St
            if((i-s)>=0){
                St[i] = gamma * (y.get(i) - Lt[i]) + (1.0 -gamma) * St[i-s];
            }

            //计算Ft
            if(i-s+k%s>=0){
                Ft[i+k] = Lt[i] +k*Bt[i] + St[i-s+k%s];
            }
        }
        
        	
        // printArray(Lt);
        // printArray(Bt);
        // printArray(St);
        // printArray(Ft);
        
        return Ft;
    }


    private static double initLevel(List<Integer> y, int s){

        double sum = 0.0;
        for(int i=0;i<s;i++){
            sum+=y.get(i);
        }
        //return 0.0;
        return sum/s;
    }

    private static double initTrend(List<Integer> y, int s){
        double sum = 0;
        for(int i=0;i<s;i++){
            sum+=y.get(s+i)-y.get(i);
        }
        return sum/(s*s);
    }

    private static double[] initSt(List<Integer> y, int s,double L0){

        double[] initSt = new double[s];
        for(int i=0;i<s;i++){
            initSt[i]=y.get(i)-L0;
        }
        return initSt;
    }

    private static void printArray(double[] data){

        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i]+"\t");
        }
        System.out.println();  
    }

    


}