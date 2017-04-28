/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Option2017Interface;

/**
 * call american futures da el european ????
 * @author paulino.seoane
 */
public class EFWilmott2017 extends BinomialModel2017 implements DerivativesCalc {
    
    private double ds, dummy, timeStep;
    private int noTimesteps,nearestGridP;
    private double tasaTimeStep;
    private double half_volatility2;
    private double ds2;
    private double dssqd;

    private double thetaUp, thetaDn;
    private double d;
    private int k;
    private double primaUp, primaDn;
    private double deltaUp, deltaDn;
    private double gammaUp, gammaDn;
    
    EFWilmott2017(){};
    public EFWilmott2017(char tipoEjercicio,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue,int steps){
        
        this.tipoEjercicio      =tipoEjercicio;
        this.tipoContrato       =tipoContrato;
        this.underlyingValue    =underlyingValue;
        this.dividendRate       =dividendRate;
        this.impliedVol         =impliedVol;
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.tasa               =tasa;
        this.optionMktValue     =optionMktValue;
        this.steps              =steps;
        
        buildEFW();
    
    }
    public EFWilmott2017(char tipoEjercicio,Underlying Und, char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue,int steps){
                                              
        
        this.tipoEjercicio      =tipoEjercicio;
        tipoContrato            =Und.tipoContrato;
        underlyingValue         =Und.underlyingValue;
        dividendRate            =Und.dividendRate;
        this.impliedVol         =impliedVol;
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.tasa               =tasa;
        this.optionMktValue     =optionMktValue;
        this.anUnderlying       =Und;
        this.steps              =steps;
        
        buildEFW();
              
     }
    
       private void buildEFW(){
        pModelName              ="Explicit Finite Model by Wilmott ";
        modelNumber        =4; 
        build();  //build esta definida en AbstractOptionClass para no tener un override en el constructor
    }        
    
     @Override
    public void runModel(){
        if (steps <= 9) {steps = 10;}
        if (steps > 50000){steps = 50000;}    
        ds = strike * 2 / steps;
        
        // ***** ver futuros put , genra un underlying mas bajo y por ende un put mas alto
        
        //underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        
        q=(tipoContrato==STOCK) ? 0:tasa;
        underlyingNPV=underlyingNPV*(Math.exp(-q*dayYear));
        
        
        nearestGridP = (int)(underlyingNPV / ds);
        dummy = (underlyingNPV - nearestGridP*ds) / ds;
        timeStep = (ds*ds) / (impliedVol*impliedVol * 4 * strike*strike);
        noTimesteps = (int)(dayYear / timeStep) + 1;
        timeStep = dayYear / noTimesteps;


	//Boundary Conditions

	double[] vOld = new double[steps + 1];
	double[] vNew = new double[steps + 1];
	double[] vPayoff = new double[steps + 1];
	double[] vJdssqd = new double[steps];
	double[] vTasajds = new double[steps];


	//vector <double> vPayoff;

       // mult=(tipoEjercicio == EUROPEAN)? 0:1;
        
	for (int j = 0; j <= steps; j++){
			vOld[j] = Math.max((j*ds-strike)*multCallPut, 0);
			vPayoff[j] = vOld[j];
	}

	for (int j = 1; j <= (steps - 1); j++){
			vJdssqd[j] = (j*ds)*(j*ds);
			vTasajds[j] = (tasa*j*ds);
	}

	tasaTimeStep = tasa*timeStep;
	half_volatility2 = (impliedVol*impliedVol) / 2;
	ds2 = 2 * ds;
	dssqd = ds*ds;

	if (callPut == CALL){
		//Call
		vNew[0] = 0;
	}else{
		//Put
		vNew[steps] = 0;
	}

	for (int i = 1; i <= noTimesteps; i++){
		for (int j = 1; j <= (steps - 1); j++){
			delta = (vOld[j + 1] - vOld[j - 1]) / ds2;
			gamma = (vOld[j + 1] - 2 * vOld[j] + vOld[j - 1]) / dssqd;

			vNew[j] = vOld[j] + timeStep*(half_volatility2*vJdssqd[j] * gamma + vTasajds[j] * delta - tasa*vOld[j]);
		}

		if (callPut == CALL){
                        //Call
			vNew[steps] = 2 * vNew[steps - 1] - vNew[steps - 2];
			//Wilmott page 624
		}else{
			//Put
			vNew[0] = (1 - tasaTimeStep)*vOld[0];
		}
		//Calculo de Theta
		if (i == noTimesteps){
			thetaUp = (vOld[nearestGridP] - vNew[nearestGridP]) / (timeStep * 365);
			thetaDn = (vOld[nearestGridP + 1] - vNew[nearestGridP + 1]) / (timeStep * 365);
		}
		for (int k = 0; k <= steps; k++){

        		if (tipoEjercicio == EUROPEAN){
				//European
				vOld[k] = vNew[k];
			}else{
				//American
				vOld[k] = Math.max(vPayoff[k], vNew[k]);
			}
		}
	}
	d = 1 - dummy;
	k = nearestGridP;

	//0:Prima
	primaUp = vOld[k];
	primaDn = vOld[k + 1];
	prima = d*primaUp + dummy*primaDn;

	//1:Delta
	deltaUp = (vOld[k + 1] - vOld[k - 1]) / (2 * ds);
	deltaDn = (vOld[k + 2] - vOld[k]) / (2 * ds);
	delta = d*deltaUp + dummy*deltaDn;

	//2:Gamma
	gammaUp = (vOld[k + 1] - 2 * vOld[k] + vOld[k - 1]) / (ds*ds);
	gammaDn = (vOld[k + 2] - 2 * vOld[k + 1] + vOld[k]) / (ds*ds);
	gamma = d*gammaUp + dummy*gammaDn;

	//4:Theta
	theta = (d*thetaUp + dummy*thetaDn);

		
	//Lo siguiente funciona bien es para obtener Vega Y Rho por Black & Scholes
        //Vega y Rho van por BlackScholes
        //Hay que ver vega pq se usa para Implied V.
        //No se puede caclcular vega desde aca dentro pq la recursivad de llamar al mismo metodo no tiene corte.
        
        anUnderlying.setUnderlyingValue(underlyingNPV);
        BlackScholesModel BSoption;
        BSoption =new BlackScholesModel(anUnderlying, callPut, strike, daysToExpiration, tasa,impliedVol,0);
                                        
        vega= BSoption.getVega();
        rho = BSoption.getRho();
        
       
               
		
}//end ClassEFmodel
     
}//end ExplicitFiniteModel Class
