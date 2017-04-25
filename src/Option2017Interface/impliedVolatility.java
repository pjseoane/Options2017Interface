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
public class impliedVolatility extends OptionParameters{
    //Constructors
    
    public impliedVolatility(OptionParameters opt){
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
    }
    /*
    public impliedVolatility(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
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
    
    }
    
    public double implied(){
        int contador=0;
        double dif;
        double Accuracy=0.000001;
        dif=optionMktValue-prima;      
        
        double newVlt=impliedVol;
        
         while(Math.abs(dif) > Accuracy && contador <20 && vega>0.000001 && optionMktValue>0 && newVlt>0.005){
         //newVlt+=(dif/vega/100);
            
                //Asignamos la nueva vlt calculada a la opcion en cuestion y la recalculamos
                //Utiliza el modelo correspondiente a esa opcion
                //setOptionVlt(newVlt);
                impliedVol+=(dif/vega/100);
                opt.runModel();
                dif=optionMktValue-prima;
                contador++;
         }
         DerivativesArray[0][6]=impliedVol;
         return impliedVol;
         //return contador;
        return 12.4;
    }
   */
}
