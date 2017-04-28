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
public class EFControlVariate extends EFWilmott2016{
/*
Solo Para American
Modelo Con tecnica de estabilizacioon Control Variate
EF american+bseuropean-EF european.
Hull pag 333 y 351

*/
public EFControlVariate(Underlying Und,char callPut,double strike,double daysToExpiration,double tasa,double mktPrime,int steps){
        
        pModelName="Exp Finite Control Variate";
        
        this.ModelNumber        =7; 
        this.tipoEjercicio      ='A';
        this.tipoContrato       =Und.getTipoContrato();
        this.underlyingValue    =Und.getUnderlyingValue();
        this.dividendRate       =Und.getDividendRate();
        this.optionVlt          =Und.getUnderlyingVolatility();
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.tasa               =tasa;
        this.MktPrime           =mktPrime;
        this.Und                =Und;
        this.steps              =steps;
        
        build();
    }//end constructor EF Control Variate
    
    
    @Override
    public void RunModel(){
        AbstractOption EFAmerican,BSopt,EFEurop;
        
        EFAmerican=new EFWilmott2016('A',Und, callPut, strike, daysToExpiration, tasa,0,steps);
        BSopt=new BlackScholes(Und, callPut, strike, daysToExpiration, tasa,0);
        EFEurop=new EFWilmott2016('E',Und, callPut, strike, daysToExpiration, tasa,0,steps);
        
        prima=EFAmerican.getPrima()+BSopt.getPrima()-EFEurop.getPrima();
        delta=EFAmerican.getDelta();
        gamma=EFAmerican.getGamma();
        vega=EFAmerican.getVega();
        theta=EFAmerican.getTheta();
        rho=EFAmerican.getRho();
    
    }
}//end Class BinomialControlVariate



