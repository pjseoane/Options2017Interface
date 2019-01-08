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
public class PositionAnalysis extends OptionPosition{
    protected double[][] PLArray;
    private int elements;
            
    protected double coef,underlyingHistVlt,precioMin,precioMax,underlyingValue,desvStd;
    protected double ratioLog,esperanza;
    protected double daysProjected,optionLifeScenario;
  
    protected AbstractOptionClass2017 option;
    
    public PositionAnalysis(){}
    public PositionAnalysis(OptionPosition optionPosition, double daysUntilFirstExpiration,double daysProjected, double desvStd){
    
        
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
   
    public PositionAnalysis(AbstractOptionClass2017 option, double lots,double lotSize, double lotPrice,double daysUntilFirstExpiration,double daysProjected, double desvStd){
                                       
        
    this.multiplier                 =lots*lotSize;
    this.lotPrice                   =lotPrice;
    this.option                     =option;
    this.desvStd                    =desvStd;
    this.underlyingValue            =option.getUnderlyingValue();
    this.underlyingHistVlt          =option.getUnderlyingHistVlt();
    this.daysProjected              =daysProjected;
    this.optionLifeScenario         =daysUntilFirstExpiration;
    }
  
    
    public PositionAnalysis(AbstractOptionClass2017 option, double multiplier, double lotPrice,double daysUntilFirstExpiration,double daysProjected, double desvStd){
                                       
        
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
       /* El array elements se declara de 1 elemento mas al necesario por ej 502.
          Los precios y P&L se ubican en un arreglo impar por ejemplo 501.
          en la posicion intermedia (250) queda el valor actual del underlying y esto sive para
          que luego quede centrado en el grafico.
          La posicion adicional al final se usa para alojar al expected value.
          PLArray[0][i]= precios
          PLArray[1][i]= P & L
          PLArray[2][i]= Delta
          PLArray[3][i]= Gamma
       */
         
       elements     =102; 
       PLArray      =new double[4][elements];
       
       coef         =Math.sqrt(daysProjected/365)*underlyingHistVlt;
       precioMin    =underlyingValue*Math.exp(coef*-desvStd);
       precioMax    =underlyingValue*Math.exp(coef*desvStd);
       ratioLog     =Math.exp(Math.log(precioMax/precioMin)/(elements-2));
       
        for (int i=0; i<elements;i++) {
       
           //Aca va todo el calculo del P & L
           PLArray[0][i]=  precioMin*Math.pow(ratioLog,i);
           Underlying Und=new Underlying(option.tipoContrato,PLArray[0][i],underlyingHistVlt,option.dividendRate);
           WhaleyV2 Option=new WhaleyV2(Und,option.callPut,option.strike,optionLifeScenario,option.tasa,underlyingHistVlt,0);
           PLArray[1][i]=(Option.getPrima()-lotPrice)*multiplier;
           PLArray[2][i]=Option.getDelta()*multiplier;
           PLArray[3][i]=Option.getGamma()*multiplier;
        }
                
                //Calculo esperanza Matematica
               
            double aux2;
            for (int i=0;i<elements-1; i++){
                 aux2=DistFunctions.CNDF(Math.log(PLArray[0][i+1]/underlyingValue)/coef)-DistFunctions.CNDF(Math.log(PLArray[0][i]/underlyingValue)/coef);
                 esperanza+=(PLArray[1][i]+PLArray[1][i+1])/2*aux2;
            }
               
            PLArray[1][elements-1]=esperanza; //en la ultima posicion esta el expected value
              
    return PLArray; 
    }
}
