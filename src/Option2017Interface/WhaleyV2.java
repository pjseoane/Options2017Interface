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
public class WhaleyV2 extends BlackScholesModel implements DerivativesCalc{
     private double b,vlt2,VltSqrDayYear,h,alfa,beta,lambda,eex,s1,zerror,xx,corr,mBlackScholes,rhs,lhs,nd1,slope,a,vv;
     //protected BlackScholesModel BSOption;
     
    public WhaleyV2(){}
    public WhaleyV2(Underlying und,char callPut,double strike,double daysToExpiration,double tasa,double impliedVol,double mktPrime){
                
        ticker                          =und.ticker;
        tipoContrato                    =und.tipoContrato;
        underlyingValue                 =und.underlyingValue;
        underlyingHistVolatility        =und.underlyingHistVolatility;
        dividendRate                    =und.dividendRate;
        this.callPut                    =callPut;
        this.strike                     =strike;
        this.daysToExpiration           =daysToExpiration;
        this.tasa                       =tasa;
        this.impliedVol                 =impliedVol;
        this.optionMktValue             =optionMktValue;
        this.anUnderlying               =und;
        
        buildW();
        
        }
    public WhaleyV2(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
        
        this.tipoContrato               =tipoContrato;
        this.underlyingValue            =underlyingValue;
        this.underlyingHistVolatility   =underlyingHistVolatility;
        this.dividendRate               =dividendRate;
        this.callPut                    =callPut;
        this.strike                     =strike;
        this.daysToExpiration           =daysToExpiration;
        this.tasa                       =tasa;
        this.impliedVol                 =impliedVol;
        this.optionMktValue             =optionMktValue;
        
        buildW();
    
    }
     private void buildW(){
        
        this.pModelName         ="Whaley ver2017 v2";
        this.modelNumber        =2;
        this.tipoEjercicio      ='A';
        build();
    }
    
    @Override
    public void runModel(){
        
        BlackScholesModel BSOption=new BlackScholesModel(tipoContrato, underlyingValue,underlyingHistVolatility, dividendRate, callPut, strike, daysToExpiration, tasa, impliedVol, 0);
        prima=BSOption.getPrima();
        
        if(tipoContrato=='F' || callPut=='P') {
            wWhaley();
        }
       
        //greeks se devuelven de BScholes
        delta   = BSOption.getDelta();
	gamma   = BSOption.getGamma();
	vega    = BSOption.getVega();
	theta   = BSOption.getTheta();
	rho     = BSOption.getRho();
    }
    
    private void wWhaley(){
        double zz;
        dayYear=daysToExpiration/365;
        sqrDayYear = Math.sqrt(dayYear);
        underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        
         switch(tipoContrato){
          case STOCK:
              q=dividendRate;
              b=tasa;
              break;
          
          case FUTURES:    
              q=tasa;
              b=0;
              break;
        }
        
        //zz=Math.exp((b-tasa))*dayYear; //para STOCK queda 1, 
        vlt2 = impliedVol*impliedVol;
        
        d1 = (Math.log(underlyingValue / strike) + ((tasa-q) + vlt2 / 2)*dayYear) / (impliedVol*sqrDayYear);
        d2 = d1 - (impliedVol*sqrDayYear);
       
        VltSqrDayYear = impliedVol*sqrDayYear;
	h = 1 - Math.exp(-tasa*dayYear); //descuento para valor presente
	alfa = 2 * tasa / vlt2;
	beta = 2 * (b-q) / vlt2;

	lambda = (-(beta - 1) + multCallPut*Math.sqrt((beta - 1)*(beta-1) + 4 * alfa / h)) / 2;

	eex = Math.exp(-q*dayYear);//descuento por dividendos

	s1 = strike;
	zz = 1 / Math.sqrt(2 * Math.PI);
	zerror = 1;
	do
	{
		d1=(Math.log(s1 / strike) + ((tasa-q) + vlt2/2)*dayYear) / VltSqrDayYear;
                xx = (1 - eex*DistFunctions.CNDF(multCallPut*d1));
		corr = s1 / lambda*xx;

                Underlying Und1=new Underlying(tipoContrato,s1,impliedVol,dividendRate); 
		BlackScholesModel option=new BlackScholesModel (Und1, callPut,strike, daysToExpiration,tasa,impliedVol,0);
                                                                
                mBlackScholes = option.getPrima();
                rhs = mBlackScholes + multCallPut*corr;

		lhs = multCallPut*(s1 - strike);
		zerror = lhs - rhs;
                nd1 = zz*Math.exp(-0.5*d1*d1); //standard normal prob?
		slope = multCallPut*(1 - 1 / lambda)*xx + 1 / lambda*(eex*nd1) * 1 / VltSqrDayYear;
		s1 = s1 - zerror / slope;

	} while (Math.abs(zerror)>0.000001);

	a = multCallPut*s1 / lambda*xx;
	
	switch (callPut)
	{
	case CALL: //Call
		
                if (underlyingValue >= s1){
		        prima = underlyingValue - strike;
		}else{
		        
                        prima += a*Math.pow((underlyingValue / s1), lambda);
                }
		break;

	case PUT: //Put
		
                if (underlyingValue <= s1){
			
                        prima = strike - underlyingValue;
		}else{
                	prima += a*Math.pow((underlyingValue / s1), lambda);   
                        
                }
		break;
	}
    }
    
}//end class
