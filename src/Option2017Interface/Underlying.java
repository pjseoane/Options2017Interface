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
public class Underlying {
   
    enum tipoContrato {STOCK,FUTURES};
    public final static char STOCK='S';
    public final static char FUTURES='F';
    
    protected char  tipoContrato; //'S': Stock 'F':Futuro
    protected String ticker,ISIN;;
    protected String underlyingName;
    protected double underlyingValue;
    protected double underlyingHistVolatility;
    protected double dividendRate;
    
    public Underlying(){}
    
   
    public Underlying(Underlying und){
        this.tipoContrato               =und.tipoContrato;
        this.underlyingValue            =und.underlyingValue;
        this.underlyingHistVolatility   =und.underlyingHistVolatility;
        this.dividendRate               =und.dividendRate;
    }
    public Underlying(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate)
    {
        this.tipoContrato               =tipoContrato;
        this.underlyingValue            =underlyingValue;
        this.underlyingHistVolatility   =underlyingHistVolatility;
        this.dividendRate               =dividendRate;
    }
    protected char getTipoContrato(){return tipoContrato;}
    protected double getUnderlyingValue(){return underlyingValue;}
    protected double getUnderLyingHistVolatility(){return underlyingHistVolatility;}
    protected double getDividendRate(){return dividendRate;}
    //setters
    protected void setTipoContrato(char TipoContrato){this.tipoContrato=TipoContrato;}
    protected void setUnderlyingValue(double UnderlyingValue){underlyingValue=UnderlyingValue;}
    protected void setUnderlyingHistVolatility(double Volatility){underlyingHistVolatility=Volatility;}
    protected void setDividendRate(double DividendRate){this.dividendRate=DividendRate;}
    //getters
    
    
}
