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
public class EFHull2017 extends BinomialModel2016{
    
    
    private double ds, timeStep,coef;
    private int noTimeSteps;
    private double tasaTimeStep;
    private double[] vOld;
    private double[] vNew;
    private double[] vPayoff;
    private double[] a;
    private double[] b;
    private double[] c;
    
    
    EFHull2017(){};
    public EFHull2017(char tipoEjercicio,Underlying Und,char callPut,double strike,double daysToExpiration,double tasa,double mktPrime,int steps){
           
        pModelName="ExplicitFiniteModel Hull";
        this.ModelNumber        =5; 
        this.tipoEjercicio      =tipoEjercicio;
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
        this.build();
    }//end constructor EFHull2017   

    @Override
    public void RunModel(){
    if (steps <= 9) {steps = 10;}
    if (steps > 50000){steps = 50000;}  
    
    steps=((int)(steps/2)+1)*2; //para que steps sea par
    
    q=0;
    q=(tipoContrato==STOCK) ? 0:tasa;
    underlyingNPV=underlyingNPV*(Math.exp(-q*dayYear));
    
    ds=underlyingNPV*2/steps;
    //ds=strike*2/steps;
    vOld = new double[steps+1];
    vNew = new double[steps+1];
    vPayoff = new double[steps+1];
    a = new double[steps+1];
    b = new double[steps+1];
    c = new double[steps+1];
    

    timeStep = ds *ds/ (optionVlt *optionVlt * 4 * strike *strike);
    noTimeSteps = (int)(dayYear / timeStep) + 1;
    
    timeStep = dayYear / noTimeSteps;
  
    tasaTimeStep = tasa * timeStep;
    coef = 1 / (1 + tasaTimeStep);
    double UndMax=underlyingNPV*2;
    double UndStep=UndMax/steps;
    double primeAtNode;
       
    for (int j = 0; j <= steps; j++){
        a[j]=coef*(-0.5*tasaTimeStep *j + 0.5*optionVlt*optionVlt*timeStep*j*j);
        b[j]=coef*(1-optionVlt*optionVlt*timeStep*j*j);
        c[j]=coef*(0.5*tasaTimeStep *j  + 0.5*optionVlt*optionVlt*timeStep*j*j);
        
        vPayoff[j]=Math.max((UndStep*j-strike)*multCallPut, 0);
        vOld[j]=vPayoff[j];    
    }
    if (callPut == CALL){
		//Call
		vNew[0] = 0;
	}else{
		//Put
		vNew[steps] = 0;
	}
    double prePrime=0;
    
    for (int timeLap=1;timeLap<=noTimeSteps;timeLap++){
        
        prePrime=vOld[steps/2];
        
        for(int i=1;i<=steps-1;i++){    
           
            primeAtNode=a[i]*vOld[i-1]+b[i]*vOld[i]+c[i]*vOld[i+1];
            if (tipoEjercicio == EUROPEAN){
				//European
				vNew[i]=primeAtNode;
			}else{
				//American
				vNew[i] = Math.max(vPayoff[i], primeAtNode);
			}
            }
        
      System.arraycopy(vNew,0,vOld,0,vNew.length);          
    }
    
    int gridPoint=steps/2;
    prima   =vOld[gridPoint];
    delta   =(vOld[gridPoint+1]-vOld[gridPoint-1])/(UndStep*(gridPoint+1)-UndStep*(gridPoint-1));

    gamma   =((vOld[gridPoint-2]-prima)/(UndStep*(gridPoint-2)-UndStep*(gridPoint))-delta)/(UndStep*(gridPoint-1)-UndStep*(gridPoint));
    theta   =(prePrime-prima)/(daysToExpiration/noTimeSteps);
   
      AbstractOption BSoption;
        Und.setUnderlyingVolatility(optionVlt);
        Und.setUnderlyingValue(underlyingNPV);
        BSoption =new BlackScholes(Und, callPut, strike, daysToExpiration, tasa,0);
        vega= BSoption.getVega();
        rho = BSoption.getRho();       
    }
    
}
    
   
