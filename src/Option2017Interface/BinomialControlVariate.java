/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Option2017Interface;

/**
 *
 * @author paulino.seoane branch 1?
 */
public class BinomialControlVariate extends BinomialModel2017 implements DerivativesCalc{
/*
Solo Para American
Modelo Con tecnica de estabilizacioon Control Variate
binomial american+bseuropean-binomialeuropean.
Hull pag 333 y 351
*/
    public BinomialControlVariate(){}
    public BinomialControlVariate(double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue,int steps){
        
        this.underlyingValue    =underlyingValue;
        this.dividendRate       =dividendRate;
        this.impliedVol         =impliedVol;
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.tasa               =tasa;
        this.optionMktValue     =optionMktValue;
        this.steps              =steps;
        
        buildBinomCV();
    
    }
    public BinomialControlVariate(Underlying Und, char callPut, double strike, double daysToExpiration, double tasa, double impliedVol, double optionMktValue,int steps){
                                
       
        tipoContrato            =Und.getTipoContrato();
        underlyingValue         =Und.getUnderlyingValue();
        dividendRate            =Und.getDividendRate();
        this.impliedVol         =impliedVol;
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.tasa               =tasa;
        this.optionMktValue     =optionMktValue;
        anUnderlying            =Und;
        this.steps              =steps;
        
        buildBinomCV();
    }//end constructor BinomialModel1    
    
    private void buildBinomCV(){
         
        pModelName="Binomial Control Variate ";
        modelNumber        =6; 
        tipoEjercicio      ='A';
        
        build();  //build esta definida en AbstractOptionClass para no tener un override en el constructor
    }
    
    @Override
    public void runModel(){
        
        BinomialModel2017 binomAmericanOpt;
        BlackScholesModel blackScholesOpt;
        BinomialModel2017 binomEuropeanOpt;
        
        binomAmericanOpt    =new BinomialModel2017('A',anUnderlying, callPut, strike, daysToExpiration, tasa,impliedVol,0,steps);
        blackScholesOpt     =new BlackScholesModel (anUnderlying, callPut, strike, daysToExpiration, tasa,impliedVol,0);
        binomEuropeanOpt    =new BinomialModel2017('E',anUnderlying, callPut, strike, daysToExpiration, tasa,impliedVol,0,steps);
        
        prima               =binomAmericanOpt.getPrima()+blackScholesOpt.getPrima()-binomEuropeanOpt.getPrima();
        delta               =binomAmericanOpt.getDelta();
        gamma               =binomAmericanOpt.getGamma();
        vega                =binomAmericanOpt.getVega();
        theta               =binomAmericanOpt.getTheta();
        rho                 =binomAmericanOpt.getRho();
    
    }
}//end Class BinomialControlVariate



