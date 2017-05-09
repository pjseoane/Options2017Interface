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
public class OptionParameters extends Underlying{
    //char callPut, double strike, double daysToExpiration, double tasa,double MktPrime
    enum TipoOpcion {CALL,PUT}
    enum eDerivatives{PRIMA, DELTA, GAMMA, VEGA,THETA,RHO,IV}
    enum TipoEjercicio {AMERICAN,EUROPEAN}
    
    public final static char CALL='C';
    public final static char PUT='P';
             
    public final static char EUROPEAN='E';
    public final static char AMERICAN='A';
    
    protected double  strike, daysToExpiration, tasa, impliedVol, optionMktValue;
    protected char callPut;
    protected double prima, delta, gamma, vega,theta,rho;
    
    //Constructors
    public OptionParameters(){}
    public OptionParameters(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
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
    public OptionParameters(Underlying und,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue)
    {
        ticker                      =und.ticker;
        tipoContrato                =und.tipoContrato;
        underlyingValue             =und.underlyingValue;
        underlyingHistVolatility    =und.underlyingHistVolatility;
        dividendRate                =und.dividendRate;
        this.callPut                =callPut;
        this.strike                 =strike;
        this.daysToExpiration       =daysToExpiration;
        this.tasa                   =tasa;
        this.impliedVol             =impliedVol;
        this.optionMktValue         =optionMktValue;
        
    }
    
    //getters
    protected char getCallPut(){return callPut;}
    protected double getStrike(){return strike;}
    protected double getDaysToExpiration(){return daysToExpiration;}
    protected double getTasa(){return tasa;}
    protected double getImpliedVol(){return impliedVol;}
    protected double getOptionMktValue(){return optionMktValue;}
    
    //setters
    protected void setTipoOpcion(char callPut){this.callPut=callPut;}
    protected void setStrike(double strike){this.strike=strike;}
    protected void setDaysToExpiration(double daysToExpiration){this.daysToExpiration=daysToExpiration;}
    protected void setTasa(double tasa){this.tasa=tasa;}
    protected void setImpliedVol(double impliedVol){this.impliedVol=impliedVol;}
    protected void setOptionMktValue(double optionMktValue){this.optionMktValue=optionMktValue;}
    
}
