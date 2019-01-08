/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Option2017Interface;

/**
 *
 * @author Paulino
 */
public class PriceRange {
    protected double[] PriceRange;
    private int elements;
    private double coef,underlyingHistVlt,precioMin,precioMax,underlyingValue,desvStd,daysProjected,ratioLog;
   
    public PriceRange(double underlyingValue, double underlyingHistVlt,double daysProjected,int elements,double desvStd){
        //Construye un array de precios log 
        
        
        this.underlyingValue    =underlyingValue;
        this.underlyingHistVlt  =underlyingHistVlt;
        this.daysProjected      =daysProjected;
        this.elements           =elements;
        this.desvStd            =desvStd;
               
    }
    
    public double[] getPriceRange(){
        //elements debe ser impar para dejar el precio actual justo en el centro 
        //ej 101
        PriceRange  =new double[elements];
        coef         =Math.sqrt(daysProjected/365)*underlyingHistVlt;
        precioMin    =underlyingValue*Math.exp(coef*-desvStd);
        precioMax    =underlyingValue*Math.exp(coef*desvStd);
        ratioLog     =Math.exp(Math.log(precioMax/precioMin)/(elements-1));
       
        for (int i=0; i<elements;i++) {
             PriceRange[i]=  precioMin*Math.pow(ratioLog,i);
        
        }
        return PriceRange;
        
    }
}
