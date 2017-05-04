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
public class EFControlVariate extends EFWilmott2017 implements DerivativesCalc{
/*
Solo Para American
Modelo Con tecnica de estabilizacioon Control Variate
EF american+bseuropean-EF european.
Hull pag 333 y 351

*/
   
    public EFControlVariate(){}
   
    public EFControlVariate(double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue,int steps){
        
        this.underlyingValue    =underlyingValue;
        this.dividendRate       =dividendRate;
        this.impliedVol         =impliedVol;
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.tasa               =tasa;
        this.optionMktValue     =optionMktValue;
        this.steps              =steps;
        
        buildEFCV();
    
    }
    public EFControlVariate(Underlying Und, char callPut, double strike, double daysToExpiration, double tasa, double impliedVol, double optionMktValue,int steps){
                                
       
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
        
        buildEFCV();
    }//end constructor BinomialModel1    
    
    private void buildEFCV(){
         
        pModelName="Explicit Finite Control Variate ";
        modelNumber        =6; 
        tipoEjercicio      ='A';
        
        build();  //build esta definida en AbstractOptionClass para no tener un override en el constructor
    }
    
    @Override
    public void runModel(){
        
        EFWilmott2017       EFAmericanOpt;
        BlackScholesModel   BSOption;
        EFWilmott2017       EFEuropeanOpt;
        
        EFAmericanOpt       =new EFWilmott2017('A',anUnderlying, callPut, strike, daysToExpiration, tasa,impliedVol,0,steps);
        BSOption            =new BlackScholesModel (anUnderlying, callPut, strike, daysToExpiration, tasa,impliedVol,0);
        EFEuropeanOpt       =new EFWilmott2017('E',anUnderlying, callPut, strike, daysToExpiration, tasa,impliedVol,0,steps);
        
        prima               =EFAmericanOpt.getPrima()+BSOption.getPrima()-EFEuropeanOpt.getPrima();
        delta               =EFAmericanOpt.getDelta();
        gamma               =EFAmericanOpt.getGamma();
        vega                =EFAmericanOpt.getVega();
        theta               =EFAmericanOpt.getTheta();
        rho                 =EFAmericanOpt.getRho();
    
    }
    
    
    
}//end Class EFControlVariate



