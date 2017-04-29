/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Anda bien asi como esta las greeks tambien chequeado con modelo android
package Option2017Interface;
//import funcionesexportables.deDistribucion.;


/**
 *
 * @author Paulino
 */
public class BlackScholesModel extends AbstractOptionClass2017 implements DerivativesCalc{
    
    protected double z,ww,d1,d2,CNDFd1,CNDFd2,PDFd1,CNDF_d1,CNDF_d2;
        
    //Constructors
    protected BlackScholesModel(){};
    public BlackScholesModel(Underlying und, char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
       
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
        //System.out.println("Inside Black and Scholes Constructor #2,todos los parametros");
       
        buildBS();
    }
    public BlackScholesModel(OptionParameters opt){
        
        tipoContrato               =opt.tipoContrato;
        underlyingValue            =opt.underlyingValue;
        underlyingHistVolatility   =opt.underlyingHistVolatility;
        dividendRate               =opt.dividendRate;
        callPut                    =opt.callPut;
        strike                     =opt.strike;
        daysToExpiration           =opt.daysToExpiration;
        tasa                       =opt.tasa;
        impliedVol                 =opt.impliedVol;
        optionMktValue             =opt.optionMktValue;
        
        buildBS();
    //System.out.println("Inside Black and Scholes Constructor #1 un solo parametro");    
    
    }
    public BlackScholesModel(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
       
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
        //System.out.println("Inside Black and Scholes Constructor #2,todos los parametros");
       
        buildBS();
       
    }
    
    private void buildBS(){
        pModelName                      ="Black-Scholes ver2017";
        modelNumber                     =1;
        tipoEjercicio                   ='E';
        
        build();  //build esta definida en AbstractOptionClass para no tener un override en el constructor
    }
    
    @Override public void runModel(){
      //heredadas:  
      // dayYear=daysToExpiration/365;
      // sqrDayYear = Math.sqrt(dayYear);
       dayYear=daysToExpiration/365;
       sqrDayYear = Math.sqrt(dayYear);
       
       underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
       
       q=(tipoContrato==STOCK) ? dividendRate:tasa; 
       //q: si es una accion q es el dividendo, si es un futuro q se toma la tasa para descontar el valor futr a presente 
       //Se hace este reemplazo para poder usar la misma form en STOCK y FUTURO
       
       z  = Math.exp(-tasa*dayYear); // e^(-r*t);
       ww = Math.exp(-dividendRate*dayYear); // e^(-dividendRate*t) //stocks
       
       d1 = (Math.log(underlyingValue / strike) + dayYear*(tasa-q + impliedVol*impliedVol / 2)) / (impliedVol*sqrDayYear);
       d2 = d1 - impliedVol*sqrDayYear;
       
       CNDFd1    =DistFunctions.CNDF(d1);  //Cumulative Normal Distribution
       CNDFd2    =DistFunctions.CNDF(d2);
       PDFd1     =DistFunctions.PDF(d1);    //Probability density Function
      
       //gamma y vega son iguales para call y put
       gamma     =PDFd1 *ww / (underlyingValue*impliedVol*sqrDayYear);
       vega      =underlyingNPV * sqrDayYear*PDFd1 / 100;
       
       switch (callPut)
            {
              
            case CALL: 
                            
		prima = underlyingValue*Math.exp(-q*dayYear) * CNDFd1 - z * strike*CNDFd2;
		delta = ww*CNDFd1;
		theta   = (-(underlyingNPV*impliedVol*PDFd1 / (2 * sqrDayYear)) - strike*tasa*z*CNDFd2+dividendRate*underlyingNPV*CNDFd1) / 365;
		rho   = strike*dayYear*z*CNDFd2 / 100;
		break;

            case PUT: 
                
                CNDF_d1=DistFunctions.CNDF(-d1);
                CNDF_d2=DistFunctions.CNDF(-d2);
                
		prima = -underlyingValue*Math.exp(-q*dayYear) * CNDF_d1 + z * strike*CNDF_d2;
		delta = ww*(CNDFd1 - 1);
		theta = (-(underlyingNPV*impliedVol*PDFd1 / (2 * sqrDayYear)) + strike*tasa*z*CNDF_d2-dividendRate*underlyingNPV*CNDF_d1) / 365;
                rho = -strike*dayYear*z*CNDF_d2 / 100;
		break;
            
            default:
                prima=delta=gamma=theta=rho=0;
                break;
                
        }//end switch
    }//end runModel()
    
    
}//end BlackScholesModel
   


