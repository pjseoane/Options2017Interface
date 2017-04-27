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
public class Whaley2017 extends BlackScholesModel{
     private double zz, vlt2,VltSqrDayYear,h,alfa,beta,lambda,eex,s1,zerror,xx,corr,mBlackScholes,rhs,lhs,nd1,slope,a,vv;
     
     //private OptionParameters parameters;

     
    Whaley2017(){}
    public Whaley2017(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
        pModelName                      ="Whaley ver 2017";
        modelNumber                     =2;
        tipoEjercicio                   ='A';
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
        //System.out.println("Inside Whaley 2017 Constructor #2,todos los parametros");
       
        buildW();
       
    }
    public Whaley2017(Underlying Und,char callPut,double strike,double daysToExpiration,double tasa,double mktPrime){
                
        this.pModelName         ="Whaley ver2016";
        this.modelNumber        =2;
        this.tipoEjercicio      ='A';
        this.tipoContrato       =Und.getTipoContrato();
        this.underlyingValue    =Und.getUnderlyingValue();
        this.dividendRate       =Und.getDividendRate();
        this.impliedVol         =Und.getUnderlyingHistVlt();
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.tasa               =tasa;
        this.optionMktValue     =mktPrime;
        this.anUnderlying       =Und;
        buildW();
        }
   
    private void buildW(){
        build();
    }
    @Override
    public void runModel(){
        
        BlackScholesModel BSOption=new BlackScholesModel(tipoContrato, underlyingValue,underlyingHistVolatility, dividendRate, callPut, strike, daysToExpiration, tasa, impliedVol, optionMktValue);
       
        switch (callPut){
            
            case CALL:
		prima=(tipoContrato=='S')? BSOption.getPrima():wWhaley();
                break;

            case PUT:
		prima = wWhaley();
		break;
            default:
                prima=delta=gamma=theta=rho=0;
                break;
	}//end switch
        
        delta = BSOption.getDelta();
	gamma = BSOption.getGamma();
	vega = BSOption.getVega();
	theta = BSOption.getTheta();
	rho = BSOption.getRho();
    }
    
    private double wWhaley(){
       
       //heredadas
       //dayYear=daysToExpiration/365;
       //sqrDayYear = Math.sqrt(dayYear);
              
       q=(tipoContrato==STOCK) ? dividendRate:tasa; 
       //q: si es una accion q es el dividendo, si es un futuro q se toma la tasa para descontar el valor futr a presente 
      
       z  = Math.exp(-tasa*dayYear); // e^(-r*t);
       ww = Math.exp(-q*dayYear); // e^(-q*t)
       
       d1 = (Math.log(underlyingValue / strike) + (tasa-q + (impliedVol*impliedVol) / 2)*dayYear) / (impliedVol*sqrDayYear);
       d2 = d1 - (impliedVol*sqrDayYear);
        
       vlt2 = impliedVol*impliedVol;
        
	VltSqrDayYear = impliedVol*sqrDayYear;
	h = 1 - z;
	alfa = 2 * tasa / vlt2;
	beta = 2 * (tasa-q) / vlt2;

	lambda = (-(beta - 1) + multCallPut*Math.sqrt((beta - 1)*(beta - 1) + 4 * alfa / h)) / 2;

	//eex = Math.exp(-q*dayYear); es ww
        //b=tasa-q    
	s1 = strike;
	zz = 1 / Math.sqrt(2 * Math.PI);
	zerror = 1;
	do
	{
		d1 = multCallPut*(Math.log(s1 / strike) + (tasa-q + 0.5*vlt2)*dayYear) / VltSqrDayYear;
		xx = (1 - eex*DistFunctions.CNDF(d1));
		corr = s1 / lambda*xx;

                Underlying Und1=new Underlying(tipoContrato,s1,impliedVol,dividendRate);
		BlackScholesModel option=new BlackScholesModel(Und1, callPut,strike, daysToExpiration,tasa,impliedVol,0);
                mBlackScholes = option.getPrima();
                rhs = mBlackScholes + multCallPut*corr;

		lhs = multCallPut*(s1 - strike);
		zerror = lhs - rhs;
		nd1 = zz*Math.exp(-0.5*d1*d1);
		slope = multCallPut*(1 - 1 / lambda)*xx + 1 / lambda*(eex*nd1) * 1 / VltSqrDayYear;
		s1 = s1 - zerror / slope;

	} while (Math.abs(zerror)>0.000001);

	a = multCallPut*s1 / lambda*xx;
	
	switch (callPut)
	{
	case CALL: //Call
		if (underlyingValue >= s1){
			vv = underlyingValue - strike;
		}
		else{
			double yy = a*Math.pow((underlyingValue / s1), lambda);
                        
                        Underlying und2=new Underlying(tipoContrato,underlyingValue,impliedVol,dividendRate);
                        BlackScholesModel option=new BlackScholesModel(und2, 'C',strike, daysToExpiration,tasa,impliedVol,0);
                        vv = option.getPrima() + yy;//BlackScholesModel(tipoContrato, callPut, underlyingValue, strike, daysToExpiration, vlt,tasa,0)+yy;
		}
		break;

	case PUT: //Put
		if (underlyingValue <= s1){
			vv = strike - underlyingValue;
		}else{
                        Underlying und2=new Underlying(tipoContrato,underlyingValue,impliedVol,dividendRate);
			BlackScholesModel option=new BlackScholesModel(und2, 'P', strike, daysToExpiration, tasa,impliedVol,0);
			vv = option.getPrima() + a*Math.pow((underlyingValue / s1), lambda);//BlackScholesModel(tipoContrato, callPut, underlyingValue, strike, daysToExpiration, vlt,tasa,0)+a*pow((underlyingValue/s1),lambda);
		}
		break;
	}
       
	return vv;
	
    }
    
}//end class
