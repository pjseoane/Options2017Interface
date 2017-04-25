package Option2017Interface;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author Paulino
 */
public class DistFunctions {
 /** Nuevas funciones usando la lib de Apache Commons
 * http://commons.apache.org/proper/commons-math/apidocs/index.html
     * @param z
     * @return 
 */
public static double CNDF(double z){
    
    
    NormalDistribution nD=new NormalDistribution();
    double cndf;              
    
    cndf =nD.cumulativeProbability(z); 
    return cndf;
        
}// end CNDF segun Apache Common

public static double PDF(double z){
    
    NormalDistribution nD=new NormalDistribution();
    double pdf;
    pdf=nD.density(z);
    return pdf;
        
}//end PDF segun Apache Common    

    
// funciones de gauss

 // return phi(x) = standard Gaussian pdf
    public static double phi(double x) {
        return Math.exp(-x*x / 2) / Math.sqrt(2 * Math.PI);
    }

    // return phi(x, mu, signma) = Gaussian pdf with mean mu and stddev sigma
    public static double phi(double x, double mu, double sigma) {
        return phi((x - mu) / sigma) / sigma;
    }

    // return Phi(z) = standard Gaussian cdf using Taylor approximation
    public static double Phi(double z) {
        if (z < -8.0) return 0.0;
        if (z >  8.0) return 1.0;
        double sum = 0.0, term = z;
        for (int i = 3; sum + term != sum; i += 2) {
            sum  = sum + term;
            term = term * z * z / i;
        }
        return 0.5 + sum * phi(z);
    }

    // return Phi(z, mu, sigma) = Gaussian cdf with mean mu and stddev sigma
    public static double Phi(double z, double mu, double sigma) {
        return Phi((z - mu) / sigma);
    } 

    // Compute z such that Phi(z) = y via bisection search
    public static double PhiInverse(double y) {
        return PhiInverse(y, .00000001, -8, 8);
    } 

    // bisection search
    private static double PhiInverse(double y, double delta, double lo, double hi) {
        double mid = lo + (hi - lo) / 2;
        if (hi - lo < delta) return mid;
        if (Phi(mid) > y) return PhiInverse(y, delta, lo, mid);
        else              return PhiInverse(y, delta, mid, hi);
    }

 // returns the cumulative normal distribution function (CNDF)
// for a standard normal: N(0,1)
public static double CNDF2(double x)
{
    int neg = (x < 0d) ? 1 : 0;
    if ( neg == 1) 
        x *= -1d;

    double k = (1d / ( 1d + 0.2316419 * x));
    double y = (((( 1.330274429 * k - 1.821255978) * k + 1.781477937) *
                   k - 0.356563782) * k + 0.319381530) * k;
    y = 1.0 - 0.398942280401 * Math.exp(-0.5 * x * x) * y;

    return (1d - neg) * y + neg * (1d - y);
}   
    
   
}
