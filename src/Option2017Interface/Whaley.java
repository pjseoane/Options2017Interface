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
public class Whaley extends BlackScholesModel implements DerivativesCalc{
     private double b,vlt2,VltSqrDayYear,h,alfa,beta,lambda,eex,s1,zerror,xx,corr,mBlackScholes,rhs,lhs,nd1,slope,a,vv;
 
    Whaley(){}
    public Whaley(Underlying Und,char callPut,double strike,double daysToExpiration,double tasa,double mktPrime){
                
        tipoContrato            =Und.getTipoContrato();
        underlyingValue         =Und.getUnderlyingValue();
        dividendRate            =Und.getDividendRate();
        impliedVol              =Und.getUnderlyingHistVlt();
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.tasa               =tasa;
        this.optionMktValue     =mktPrime;
        this.anUnderlying       =Und;
        buildW();
        
        }
    public Whaley(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
        
        
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
        
        BlackScholesModel BSOption=new BlackScholesModel(tipoContrato, underlyingValue,underlyingHistVolatility, dividendRate, callPut, strike, daysToExpiration, tasa, impliedVol, optionMktValue);
                                                            
        switch (callPut){
            
            case CALL: // si es call se devuelve Bscholes
		prima=(tipoContrato=='S')? BSOption.getPrima():wWhaley();
                break;

            case PUT:
		prima = wWhaley();
		break;
                
            default:
                prima=delta=gamma=theta=rho=0;
                break;
                
	}//end switch
        
        //greeks se devuelven de BScholes
        delta = BSOption.getDelta();
	gamma = BSOption.getGamma();
	vega = BSOption.getVega();
	theta = BSOption.getTheta();
	rho = BSOption.getRho();
    }
    
    private double wWhaley(){
       
        //underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        //z = Math.exp(-tasa*dayYear);
        //q=0;
        
        q=(tipoContrato==STOCK) ? 0:tasa;       
        b=(tipoContrato==STOCK) ? tasa:0;
        
       // zz=Math.exp((b-tasa))*dayYear;
        vlt2 = impliedVol*impliedVol;
        
        d1 = (Math.log(underlyingNPV / strike) + (b + vlt2 / 2)*dayYear) / (impliedVol*sqrDayYear);           
        d2 = d1 - (impliedVol*sqrDayYear);
        
        
	VltSqrDayYear = impliedVol*sqrDayYear;
	h = 1 - Math.exp(-tasa*dayYear);
	alfa = 2 * tasa / vlt2;
	beta = 2 * b / vlt2;

	lambda = (-(beta - 1) + multCallPut*Math.sqrt((beta - 1)*(beta - 1) + 4 * alfa / h)) / 2;

	eex = Math.exp(-q*dayYear);

	s1 = strike;
	//zz = 1 / Math.sqrt(2 * Math.PI);
	zerror = 1;
	do
	{
		d1 = multCallPut*(Math.log(s1 / strike) + (b + 0.5*vlt2)*dayYear) / VltSqrDayYear;
		xx = (1 - eex*DistFunctions.CNDF(d1));
		corr = s1 / lambda*xx;

                Underlying Und1=new Underlying(tipoContrato,s1,impliedVol,0); // ver si se pasa condividendos o sin en esta parte
		BlackScholesModel option=new BlackScholesModel (Und1, callPut,strike, daysToExpiration,tasa,impliedVol,0);
                                                                
                mBlackScholes = option.getPrima();
                rhs = mBlackScholes + multCallPut*corr;

		lhs = multCallPut*(s1 - strike);
		zerror = lhs - rhs;
                nd1   =DistFunctions.PDF(d1); 
		//nd1 = zz*Math.exp(-0.5*d1*d1); //standard normal prob?
		slope = multCallPut*(1 - 1 / lambda)*xx + 1 / lambda*(eex*nd1) * 1 / VltSqrDayYear;
		s1 = s1 - zerror / slope;

	} while (Math.abs(zerror)>0.000001);

	a = multCallPut*s1 / lambda*xx;
	
	switch (callPut)
	{
	case CALL: //Call
		if (underlyingNPV >= s1){
			vv = underlyingNPV - strike;
		}else{
			double yy = a*Math.pow((underlyingNPV / s1), lambda);
                        
                        Underlying Und2=new Underlying(tipoContrato,underlyingNPV,impliedVol,0);
                        BlackScholesModel option=new BlackScholesModel(Und2, 'C', strike, daysToExpiration, tasa,impliedVol,0);
                        vv = option.getPrima() + yy;//BlackScholes(tipoContrato, callPut, underlyingValue, strike, daysToExpiration, vlt,tasa,0)+yy;
		}
		break;

	case PUT: //Put
		if (underlyingNPV <= s1){
			vv = strike - underlyingNPV;
		}else{
                        Underlying Und2=new Underlying(tipoContrato,underlyingNPV,impliedVol,0);
			BlackScholesModel option=new BlackScholesModel(Und2, 'P', strike, daysToExpiration, tasa,impliedVol,0);
			vv = option.getPrima() + a*Math.pow((underlyingNPV / s1), lambda);//BlackScholes(tipoContrato, callPut, underlyingValue, strike, daysToExpiration, vlt,tasa,0)+a*pow((underlyingValue/s1),lambda);
                            
                }
		break;
	}
       
	return vv;
	
    }
    
}//end class
