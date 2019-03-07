package nju.edu.mongodb;

import java.text.DecimalFormat;

//import org.json.JSONObject;
import org.junit.Test;

import edu.nju.util.PMF;
//import edu.nju.util.StringMatch;

public class test1 {
	
	private static int N = 2;//用户的数目
    private static int M = 4;//产品的数目
    private static int K = 2;//特征的数目
    private static DecimalFormat df = new DecimalFormat("0.00");
    
	@Test
	public void pmf() {
		double[][] R = new double[N][M];
        double[][] P = new double[N][K];
        double[][] Q = new double[K][M];
        R[0][0] = 5;
        R[0][1]=3;
        R[0][2]=0;
        R[0][3]=1;
        R[1][0]=4;
        R[1][1]=0;
        R[1][2]=0;
        R[1][3]=1;
//        R[8]=1;
//        R[9]=1; 
//        R[10]=0;
//        R[11]=5;
//        R[12]=1;
//        R[13]=0;
//        R[14]=0;
//        R[15]=4;
//        R[16]=0;
//        R[17]=1;
//        R[18]=5;
//        R[19]=4;
        System.out.println("R矩阵");
        for(int i = 0; i < N; ++i) { 
        	for(int j = 0; j < M; ++j){ 
        		System.out.print(R[i][j] + ",");
        	}
        	System.out.println();
        } 
       //初始化P，Q矩阵，这里简化了，通常也可以对服从正态分布的数据进行随机数生成 
        for(int i = 0; i < N; ++i) 
        {
            for(int j = 0; j < K; ++j)
            {
                P[i][j] = Math.random() % 9;
            }
        }
        for(int i = 0; i < K; ++i) 
        {
            for(int j = 0;j < M; ++j)
            {
                Q[i][j] = Math.random() % 9;
            }
        }           
        System.out.println("矩阵分解开始");
        PMF.matrix_factorization(R, P, Q, N, M, K);  
        System.out.println("重构出来的R矩阵");
        for(int i = 0; i < N; ++i) 
        { 
         for(int j = 0; j < M; ++j) 
         { 
          double temp = 0; 
          for (int k = 0; k < K; ++k){
              temp += P[i][k] * Q[k][j]; 
           } 
          System.out.print(df.format(temp)+","); 
         } 
         System.out.println();
        }
	}
}
