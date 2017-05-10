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
public class PositionAnalysis extends BookOfOptionsPositions{
    protected double[][] PLArray;
    private int elements;
            
    protected double coef,underlyingHistVlt,precioMin,precioMax,underlyingValue,desvStd;
    protected double ratioLog,esperanza;
    protected double daysProjected,optionLifeScenario;
  
    protected AbstractOptionClass2017 option;
    
    public PositionAnalysis(){}
    public PositionAnalysis(BookOfOptionsPositions optionPosition, int daysUntilFirstExpiration,int daysProjected, double desvStd){
    
        
    this.lots                       =optionPosition.lots;
    this.lotSize                    =optionPosition.lotSize;
    this.lotPrice                   =optionPosition.lotPrice;
    this.option                     =optionPosition.anOption;
    this.desvStd                    =desvStd;
    this.underlyingValue            =option.getUnderlyingValue();
    this.underlyingHistVlt          =option.getUnderlyingHistVlt();
    this.daysProjected              =daysProjected;
    this.optionLifeScenario         =daysUntilFirstExpiration;
    multiplier=lots*lotSize;
    }
   
    public PositionAnalysis(AbstractOptionClass2017 option, double lots,double lotSize, double lotPrice,int daysUntilFirstExpiration,int daysProjected, double desvStd){
                                       
        
    this.multiplier                 =lots*lotSize;
    this.lotPrice                   =lotPrice;
    this.option                     =option;
    this.desvStd                    =desvStd;
    this.underlyingValue            =option.getUnderlyingValue();
    this.underlyingHistVlt          =option.getUnderlyingHistVlt();
    this.daysProjected              =daysProjected;
    this.optionLifeScenario         =daysUntilFirstExpiration;
    }
  
    
    public PositionAnalysis(AbstractOptionClass2017 option, double multiplier, double lotPrice,int daysUntilFirstExpiration,int daysProjected, double desvStd){
                                       
        
    this.multiplier                 =multiplier;
    this.lotPrice                   =lotPrice;
    this.option                     =option;
    this.desvStd                    =desvStd;
    this.underlyingValue            =option.getUnderlyingValue();
    this.underlyingHistVlt          =option.getUnderlyingHistVlt();
    this.daysProjected              =daysProjected;
    this.optionLifeScenario         =daysUntilFirstExpiration;
    }
    
     public double[][] PLArray(){
       elements=502;
       PLArray  =new double[2][elements];
       
       //La ultima posicion del arreglo se usa para almacenar el valor de Expected Value, no es P&L
      
       coef                         =Math.sqrt(daysProjected/365)*underlyingHistVlt;
       precioMin                    =underlyingValue*Math.exp(coef*-desvStd);
       precioMax                    =underlyingValue*Math.exp(coef*desvStd);
       ratioLog                     =Math.exp(Math.log(precioMax/precioMin)/(elements-2));
       
                for (int i=0; i<elements-0;i++) {
       
                    //Aca va todo el calculo del P & L
                    PLArray[0][i]=  precioMin*Math.pow(ratioLog,i);
                    Underlying Und=new Underlying(option.tipoContrato,PLArray[0][i],underlyingHistVlt,option.dividendRate);
                    WhaleyV2 Option=new WhaleyV2(Und,option.callPut,option.strike,optionLifeScenario,option.tasa,underlyingHistVlt,0);
                    PLArray[1][i]=(Option.getPrima()-lotPrice)*multiplier;
                   }
                
                //Calculo esperanza Matematica
               
                double aux2;
                for (int i=0;i<elements-1; i++){
                    aux2=DistFunctions.CNDF(Math.log(PLArray[0][i+1]/underlyingValue)/coef)-DistFunctions.CNDF(Math.log(PLArray[0][i]/underlyingValue)/coef);
                    esperanza+=PLArray[1][i]*aux2;
                }
               
                PLArray[1][elements-1]=esperanza; //en la ultima posicion esta el expected value
              
    return PLArray; //hasta n-1 devuelve precio y P&L correspondiente, en la posicion n devuelve expected value 
    }
}
