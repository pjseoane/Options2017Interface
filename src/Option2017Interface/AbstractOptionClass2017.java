/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Option2017Interface;

/**
 *
 * @author paulino.seoane
 */
public abstract class AbstractOptionClass2017 extends Underlying implements DerivativesCalc{
   
    
    enum TipoOpcion {CALL,PUT}
    enum eDerivatives{PRIMA, DELTA, GAMMA, VEGA,THETA,RHO,IV}
    enum TipoEjercicio {AMERICAN,EUROPEAN}
    
    public final static char CALL='C';
    public final static char PUT='P';
             
    public final static char EUROPEAN='E';
    public final static char AMERICAN='A';
    
    //Greeks
    public final static int PRIMA=0,DELTA=1,GAMMA=2,VEGA=3,THETA=4,RHO=5,IV=6;
    protected double[][] DerivativesArray = new double[1][10];
    
    public static int modelCounter;
    protected String pModelName;
    protected int modelNumber;
    
    protected char tipoEjercicio;
    protected char callPut;
        
   //protected double optionVlt;
    protected double strike,daysToExpiration, tasa;//optionMktValue,q;
    protected double prima=-2,delta=-2,gamma=-2,vega=-2,theta=-2,rho=-2,impliedVol,optionMktValue=0; 
    protected double dayYear,sqrDayYear;
    
    protected double underlyingValueNPV;
    protected long startTime,elapsedTime;
    
    //protected int multiplicador;
    protected int multCallPut;
    private boolean tieneVida;
        
    //AbstractOptionClass2017(){}
      
        
    //overridable method:   
    @Override
    abstract public void runModel(); //Cada modelo implementa runModel()
    
    private void opcionTieneVida(){
        //Si hay vida se cacula el modelo
        dayYear=daysToExpiration/365;
        sqrDayYear = Math.sqrt(dayYear);
        underlyingValueNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        //do some works with price for dividends, stock or futures or leave it to each model?
        
        runModel(); //Aca se llama a la implementacion de cada modelo
        
        //A continuacion se recalcula la opcion si esta vieen con un valor de mercado 
        //si el valor de mercado es cero se calcula con la vlt historica, sino se calcula 
        //la implicita y queda recalculada toda la opcion y sus greeks.
        impliedVlt(); 
    }
    private void opcionSinVida(){
        delta=multCallPut;  
        gamma=vega=theta=rho=0;
        prima = Math.max((underlyingValue - strike)*multCallPut, 0);
        delta=(prima==0)?0:multCallPut;
       
    }
    
    public void build(){
        modelCounter++;
        startTime=System.currentTimeMillis();
        multCallPut=(callPut==CALL)?1:-1;
        tieneVida=(daysToExpiration>0);
        
        if (tieneVida){
            opcionTieneVida();
        }else{
            opcionSinVida();
        }
         elapsedTime = System.currentTimeMillis() - startTime;
         fillDerivativesArray();
    }
 
    protected void fillDerivativesArray(){
        DerivativesArray[0][0]=prima;
        DerivativesArray[0][1]=delta;
        DerivativesArray[0][2]=gamma;
        DerivativesArray[0][3]=vega;
        DerivativesArray[0][4]=theta;
        DerivativesArray[0][5]=rho;
        DerivativesArray[0][6]=impliedVol;
        DerivativesArray[0][7]=elapsedTime;
        DerivativesArray[0][8]=modelCounter;
        DerivativesArray[0][9]=modelNumber;
 }
 
    //otros methods: getters
    
    //Getters Opcion
    public double getPrima(){return prima;}
    public double getDelta(){return delta;}
    public double getGamma(){return gamma;}
    public double getVega() {return vega;}
    public double getTheta(){return theta;}
    public double getRho()  {return rho;}
    public double getImpVlt(){return impliedVol;}
    public double getOptionMktValue(){return optionMktValue;}
    public char getTipoEjercicio(){return tipoEjercicio;}
    public char getCallPut(){return callPut;}
    public double getStrike(){return strike;}
    public double getDaysToExpiration(){return daysToExpiration;}
    public double getImpliedVlt(){return impliedVol;}
    public double getTasa(){return tasa;}
    public double getValueToFind(int i){
     
        if (i>9){i=0;}
        return DerivativesArray[0][i];
    }    
    public double[][] getDerivativesArray(){
       
        return DerivativesArray;}
    public String getOptionString(){
    StringBuilder builder =new StringBuilder();
    builder.append(modelNumber);
    builder.append("-");
    builder.append(pModelName);
    builder.append("prima");
    builder.append(prima);
    builder.append("delta");
    builder.append(delta);
    builder.append("gamma");
    builder.append(gamma);
    builder.append("vega");
    builder.append(vega);
    builder.append("theta");
    builder.append(theta);
    builder.append("rho");
    builder.append(rho);
    builder.append("impVlt");
    builder.append(impliedVol);
    builder.append("z");
    return builder.toString();
    }//end getString
    
    //getters model
    public double getModelNumber(){return modelNumber;}
    public String getModelName(){return pModelName;}
    public double getElapsedTime(){return elapsedTime;}
    
    
    
    //setters
    void setTipoEjercicio(char tipoEjercicio){this.tipoEjercicio=tipoEjercicio;}
    void setCallPut(char callPut){this.callPut=callPut;}
    void setDaysToExpiration(double daysToExpiration){this.daysToExpiration=daysToExpiration;}
    void setOptionVlt(double vlt){this.impliedVol=vlt;}
   
    
    /*public double ImpliedVolatility(double MktPrime){
    /*Anda ok el tema es que deja seteada a la opcion con la imp vlt y todos los valores
    actualizados a nueva Vlt implicita.
        No estoy seguro si esto es util o no, puede que se quiera o no.
        Si se quiere solo calcular la Imp Vlt y dejar la opcion como viene eontonces hay que construir 
        otra opcion una copia real y trabajar sobre esta copia.
        Este metodo es lineal, no recursivo.
        Recursivo esta hecho mas abajo.
        No es Biseccion, es mejor, utiliza el vega o sea la derivada.
        Llega a la solucion mucho mas rapido, entre 3 y 5 iteraciones.
        Este metodo tiene un corte en 20 iteraciones antes que se vaya a la mierda.
        Este metodo permite devolver el contador de iteraciones para debug-
        El recursivo no
            
        int Contador=0;
        double dif;
        double Accuracy=0.00001;
        dif=MktPrime-getPrima();      
        double newVlt=getOptionVlt();
        
         while(Math.abs(dif) > Accuracy && Contador <20 && getVega()>0.000001 && MktPrime>0 && newVlt>0.005){
         newVlt+=(dif/getVega()/100);
            
                //Asignamos la nueva vlt calculada a la opcion en cuestion y la recalculamos
                //Utiliza el modelo correspondiente a esa opcion
                setOptionVlt(newVlt);
                this.RunModel();
                dif=MktPrime-getPrima();
                Contador++;
         }
        
        //return Contador++;
        // newVlt=Contador;
        // fillDerivativesArray();
        return newVlt;
    }*/
    /*public double IVRecursive(double MktPrime){
        //Este algoritmo esta hecho en forma recursiva.
        //No se puede llevar un contador de las iteraciones para hacer debug.
        //Habria que poner un contador de tipo publico, pero no justifica. 
        
        double dif;
        double Accuracy=0.000001;
        int contador=0;
        dif=MktPrime-getPrima();
        double newVlt=getOptionVlt();
                
        if (Math.abs(dif)>Accuracy && MktPrime >0 && getVega()>0.000001){
            setOptionVlt(newVlt+=(dif/getVega()/100));
            RunModel();
                       
           return IVRecursive(MktPrime);
        }
        fillDerivativesArray();
        return newVlt;
        
    }*/
    /*public double IVRecursive(double MktPrime,double Accuracy){
        //Este algoritmo esta hecho en forma recursiva y acepta Accuracy como parametro
        double dif;
        if (Accuracy<0.0000000001){Accuracy=0.0000000001;}
        
        dif=MktPrime-getPrima();
        double newVlt=getOptionVlt();
                
        if (Math.abs(dif)>Accuracy && MktPrime >0&& getVega()>0.000001){
            setOptionVlt(newVlt+=(dif/getVega()/100));
            RunModel(); // construye la nueva opcion con la nueva vlt
            
            return IVRecursive(MktPrime,Accuracy);
        }
    return newVlt;
    }*/
    public void impliedVlt(){
        
    /*Anda ok el tema es que deja seteada a la opcion con la imp vlt y todos los valores
    actualizados a nueva Vlt implicita.
        No estoy seguro si esto es util o no, puede que se quiera o no.
        Si se quiere solo calcular la Imp Vlt y dejar la opcion como viene eontonces hay que construir 
        otra opcion una copia real y trabajar sobre esta copia.
        Este metodo es lineal, no recursivo.
        Recursivo esta hecho mas abajo.
        No es Biseccion, es mejor, utiliza el vega o sea la derivada.
        Llega a la solucion mucho mas rapido, entre 3 y 5 iteraciones.
        Este metodo tiene un corte en 20 iteraciones antes que se vaya a la mierda.
        Este metodo permite devolver el contador de iteraciones para debug-
        El recursivo no
        */    
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
                runModel();
                dif=optionMktValue-prima;
                contador++;
         }
    }
    
}//end class
