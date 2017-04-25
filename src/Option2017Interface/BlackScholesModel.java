/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Anda bien asi como esta las greeks tambien chequeado con modelo android
package Option2017Interface;



/**
 *
 * @author Paulino
 */
public class BlackScholesModel extends AbstractOptionClass2017 implements DerivativesCalc{
    //tipoEjercicio='E';
    

    protected double q,z,ww,d1,d2,CNDFd1,CNDFd2,PDFd1,CNDF_d1,CNDF_d2;
    
    //Constructors
    public BlackScholesModel(OptionParameters opt){
        pModelName                  ="Black-Scholes ver2016";
        modelNumber                 =1;
        tipoEjercicio               ='E';
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
        
        build();
    //System.out.println("Inside Black and Scholes Constructor #1 un solo parametro");    
    //System.out.println("Hist Volatility:"+ underlyingHistVolatility);
    }
    public BlackScholesModel(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
        pModelName                  ="Black-Scholes ver2016";
        modelNumber                 =1;
        tipoEjercicio               ='E';
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
        //System.out.println("Und value +2.1="+(underlyingValue+2.1));
        build();
        //impliedVlt() debe ir aca esta instruccion en cada constructor?
    }
    
    @Override public void runModel(){
        
       //Estas variables son heredadas dayYear,sqrDayYear,
       //dayYear = daysToExpiration / 365;
       //sqrDayYear = Math.sqrt(dayYear);
      
       q=(tipoContrato==STOCK) ? dividendRate:tasa; 
       //q: si es una accion q es el dividendo, si es un futuro q se toma la tasa para descontar el valor futr a presente 
      
       z  = Math.exp(-tasa*dayYear); // e^(-r*t);
       ww = Math.exp(-q*dayYear); // e^(-q*t)
       
       d1 = (Math.log(underlyingValue / strike) + (tasa-q + (impliedVol*impliedVol) / 2)*dayYear) / (impliedVol*sqrDayYear);
       d2 = d1 - (impliedVol*sqrDayYear);
       
       CNDFd1    =DistFunctions.CNDF(d1);  //Cumulative Normal Distribution
       CNDFd2    =DistFunctions.CNDF(d2);
       PDFd1     =DistFunctions.PDF(d1);    //Probability density Function
      
       //gamma y vega son iguales para call y put
       gamma     =PDFd1 *ww / (underlyingValue*impliedVol*sqrDayYear);
       vega      =underlyingValue*ww * sqrDayYear*PDFd1 / 100;
       
       switch (callPut)
            {
              
            case CALL: 
                            
		prima = ww * underlyingValue * CNDFd1 - z * strike*CNDFd2;
		delta = ww*CNDFd1;
		theta   = (-(ww*underlyingValue*impliedVol*PDFd1 / (2 * sqrDayYear)) - strike*tasa*z*CNDFd2+q*ww*underlyingValue*CNDFd1) / 365;
		rho   = strike*dayYear*z*CNDFd2 / 100;
		break;

            case PUT: 
                
                CNDF_d1=DistFunctions.CNDF(-d1);
                CNDF_d2=DistFunctions.CNDF(-d2);
                
		prima = -ww * underlyingValue * CNDF_d1 + z * strike*CNDF_d2;
		delta = ww*(CNDFd1 - 1);
		theta = (-(ww *underlyingValue*impliedVol*PDFd1 / (2 * sqrDayYear)) + strike*tasa*z*CNDF_d2-q*ww*underlyingValue*CNDF_d1) / 365;
                rho = -strike*dayYear*z*CNDF_d2 / 100;
		break;
            
            default:
                prima=delta=gamma=theta=rho=0;
                break;
                
        }//end switch
    }
}//end BlackScholesModel
   


