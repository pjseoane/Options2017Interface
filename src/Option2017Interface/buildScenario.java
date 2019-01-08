/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Option2017Interface;

/**
 *
 * @author paulino.seoane
 */
public class buildScenario {
    protected double[]plArray,deltaArray,gammaArray;
    protected double mathExpectedPl;
    protected Underlying und;
    protected AbstractOptionClass2017 opt;
    protected char callPut;
    protected double strike, optionLifeScenario;
    protected double tasa,underlyingHistVlt;
//protected char tipoContrato;
    
    public buildScenario(OptionPosition []book, double []prices){
        plArray     =new double[prices.length];
        deltaArray  =new double[prices.length];
        gammaArray  =new double[prices.length];
        
        
        
        for (int j=0;j<book.length;j++){
           // und                 =book[j].anOption.anUnderlying;
           // callPut             =book[j].anOption.getCallPut();
           // strike              =book[j].anOption.strike;
           // optionLifeScenario  =book[j].anOption.daysToExpiration; //corregir por days to 1st expiration
           // tasa                =book[j].anOption.tasa;
           // underlyingHistVlt   =book[j].anOption.underlyingHistVolatility;
            
            
            for (int i=0;i<prices.length;i++){
                
            
                //Underlying Und=new Underlying(book[j].anOption.tipoContrato,prices[i],underlyingHistVlt,option.dividendRate);
                und.setUnderlyingValue(prices[i]);
                //WhaleyV2 Option=new WhaleyV2(und,callPut,strike,optionLifeScenario,tasa,underlyingHistVlt,0);
                book[j].anOption.setUnderlying(und);
                
                
                
                
            }
        }
    }
    
    public double[] getPlArray(){return plArray;}
    public double[] getDeltaArray(){return deltaArray;}
    public double[] getGammaArray(){return gammaArray;}
    public double getMathExpectedPl(){return mathExpectedPl;}
    
}
