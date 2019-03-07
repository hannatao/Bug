package edu.nju.util;

public class PMF {
    public static void matrix_factorization(double[][] R, double[][] U, double[][] V, int N, int M, int K){
       int steps = 6000;
       double alpha = 0.0002;
       double beta = 0.02;
       for(int step = 0 ; step < steps ; ++step){
           for(int i = 0 ; i < N ; ++i) {
               for(int j = 0 ; j < M ; ++j){
                   if(R[i][j] > 0){
                       double eij = R[i][j];
                       for(int k = 0 ; k < K ; ++k){
                           eij -= U[i][k] * V[k][j]; 
                       }
                       for(int k = 0 ; k < K ; ++k){
                           U[i][k] += alpha * (2 * eij * V[k][j] - beta * U[i][k]);
                           V[k][j] += alpha * (2 * eij * U[i][k] - beta * V[k][j]);
                       } 
                       } 
                   }
               }
           double loss = 0;
           for(int i = 0 ; i < N ; ++i){
                for(int j = 0 ; j < M ; ++j) {
                    if(R[i][j] > 0){
                        double eij = 0;
                        	for(int k = 0; k < K; ++k){
                               eij += U[i][k] * V[k][j]; 
                        	} 
                            loss += Math.pow(R[i][j] - eij,2); 
                            for(int k = 0; k < K; ++k){
                                loss += (beta / 2) * (Math.pow(U[i][k], 2) + Math.pow(V[k][j], 2)); 
                            }  
                    }
                }
           } 
           if(loss < 0.001) {
        	   System.out.println("total step:" + step);
               break;
           } 
           if (step % 1000 == 0) {
               System.out.println("step:" + step + ",loss:" + loss);
           }
       }
   }
}
