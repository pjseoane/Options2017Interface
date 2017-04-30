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
public class BinomialModel2017 extends AbstractOptionClass2017 implements DerivativesCalc{
    public int UpBound;
    private double interv, sqrtInterv;
    public double u, a, d, p, pp,z;
    public double AssetAtNode,BtreeEsp,vPrices[];
    private double vtheta, delt1, delt2, UVu4, UVd4, UVu2d2;   
    protected int steps,mult;
    
    
    BinomialModel2017(){}
    
    public BinomialModel2017(char tipoEjercicio,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue,int steps){
        
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
        
        buildBinom();
    
    }
    public BinomialModel2017(char tipoEjercicio,Underlying Und, char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue,int steps){
        
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
        
        buildBinom();
              
     }//end constructor BinomialModel1    
    
    private void buildBinom(){
        pModelName              ="BinomialModel ver2017 ";
        this.modelNumber        =3; 
      
        build();  //build esta definida en AbstractOptionClass para no tener un override en el constructor
    }
    
    @Override
    public void runModel(){
        /**
        *Nueva version 02/may/2014	
        *tipoEjercicio: E: European, A:American
        *tipoContrato: S:Acciones, F:Futuros
        *Copiado de la version C++
        *Conviene que _steps sea par para ver despues como chequear _steps=integer(x);
        *double Btree(_steps+2);
        *Trabajamos con un arbol 4dt mayor al numero de _steps para mejorar la precision de las Greeks
        *tamaño razonable de _steps >=160
        *Basado en el Modelo Binomial de Modelos 2008.xla
        *Dividendos: Pasar un Stock Adjusted y procesar modelo normal.
        */   
    
        if (steps <= 9) {steps = 10;}
        if (steps > 50000){steps = 50000;}
        steps=((int)(steps/2)+1)*2; //para que steps sea par
        UpBound = (steps + 4);
       
        vPrices=new double[UpBound+1];
        dayYear=daysToExpiration/365;
        underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        
        q=(tipoContrato==STOCK) ? 0:tasa;
        
        interv = dayYear / steps;
	sqrtInterv = Math.sqrt(interv);
	u = Math.exp(impliedVol*sqrtInterv);
	a = Math.exp((tasa - q)*interv);//Para Futuros queda a=1
	d = Math.exp(-impliedVol*sqrtInterv);
	p = (a - d) / (u - d);
	pp = 1 - p;
	z = Math.exp(-tasa*interv); 
                    
        //Boundary Conditions

	for (int i = 0; i <UpBound+1; i++){
		AssetAtNode = underlyingNPV*Math.pow(u, (UpBound - i))*Math.pow(d, i);
		vPrices[i] = Math.max((AssetAtNode-strike)*multCallPut, 0);
	}
	//Fin Boundary Conditions 
        
        //Resolving Tree Backward
        UVu4 = underlyingNPV*Math.pow(u, 4);
	UVd4 = underlyingNPV*Math.pow(d, 4);
	UVu2d2 = underlyingNPV*u*u*d*d;

        //Resolving Tree Backward
	//Se procesa el arbol hasta el nivel -8, toma valor para calcular theta
	//En Upbound -8 toma el valor 4 del array para el calculo de theta

	//multiplicador ahora se usa para para resolver el arbol en funcion si es
	//European o Americam
	
        mult=(tipoEjercicio == EUROPEAN)? 0:1;
        
        for (int i = 1; i <= (UpBound - 8); i++){
            
            //System.out.println("---Start TimeStep: "+i);
            //el proceso sig puede hacerse multithreading ?
            arrayProcess(UpBound-i);
        }
        vtheta = vPrices[4];

	//Continua loop hasta nivel -4 para tomar valores para gamma
	for (int i = (UpBound - 7); i <= UpBound; i++){
		
                arrayProcess(UpBound-i);
                
		if (i == steps){
			//Aca se procesan las variables para prima, delta y gamma
			//Calculo Greeks
			//Prima
			prima = vPrices[2];
			//Delta
                        
			delta = (vPrices[0] - vPrices[4]) / (UVu4 - UVd4);
			//Gamma
			delt1 = (vPrices[0] - vPrices[2]) / (UVu4 - UVu2d2);
			delt2 = (vPrices[2] - vPrices[4]) / (UVu2d2 - UVd4);
			gamma = (delt1 - delt2) / (underlyingNPV*Math.pow(u, 3)*d - underlyingNPV*Math.pow(d, 3)*u); //Gamma
		}
	}

	//Theta
	theta = (vtheta - vPrices[0]) / (8 * interv * 365);
        
        //Vega y Rho van por BlackScholes
        //Hay que ver vega pq se usa para Implied V.
        //No se puede calcular vega desde aca dentro pq la recursivad de llamar al mismo metodo no tiene corte.
        
        BlackScholesModel BSOption;
        BSOption =new  BlackScholesModel(anUnderlying, callPut, strike, daysToExpiration, tasa,impliedVol,0);
                
        //el problema de tomar vega del BS es que no es suficiente mente exacto y genera muchas iteraciones para converger
        vega= BSOption.getVega();
        rho = BSOption.getRho();
        
    }//end CalculateDerivatives Binomial
 
    
    private void arrayProcess(int UpLimit){
     //este proceso puede hacerse multithreading?   
        
     for (int j = 0; j <= UpLimit; j++){
			AssetAtNode = underlyingNPV*Math.pow(u, (UpLimit - j))*Math.pow(d, j);
			BtreeEsp = (p*vPrices[j] + pp*vPrices[j + 1])*z;
			vPrices[j] = Math.max((AssetAtNode-strike)*mult*multCallPut, BtreeEsp);
    }  
          
}
   
    
   

}
