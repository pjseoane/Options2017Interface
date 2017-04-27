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
public class impliedVolatility extends AbstractOptionClass2017{
    
    //private AbstractOptionClass2017 option;
    
    //Constructors
    public impliedVolatility(AbstractOptionClass2017 opt){
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
    public impliedVolatility (char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
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
   
    
    public void runModel(){
        int contador=0;
        double dif;
        double accuracy=0.000001;
        dif=optionMktValue-prima;      
        
        double newVlt=impliedVol;
        
         while(Math.abs(dif) > accuracy && contador <20 && this.getVega()>0.000001 && this.optionMktValue>0 && newVlt>0.005){
         //newVlt+=(dif/vega/100);
            
                //Asignamos la nueva vlt calculada a la opcion en cuestion y la recalculamos
                //Utiliza el modelo correspondiente a esa opcion
                //setOptionVlt(newVlt);
                impliedVol+=(dif/getVega()/100);
                runModel();
                dif=optionMktValue-prima;
                contador++;
         }
         DerivativesArray[0][6]=impliedVol;
        // return option.impliedVol;
         //return contador;
       
    }//end method implied2
   
}
