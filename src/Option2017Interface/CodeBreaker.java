/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Option2017Interface;

/**
 *Sirve para desarmar el codigo en sus partes
 * La forma del codigo es int Steps+modelo+tipoEjercicio+TipoContrato+CallPut+VtoF
 * @author Paulino
 */
public class CodeBreaker {
    
    
    public int ValueToFind;
    public int iCallPut;
    public int iTipoContrato;
    public int iTipoEjercicio;
    public int Modelo;
    public int Steps;
    public char CP,TC,TE,vtof;
    public char Model;
    
    public CodeBreaker(){};
    public CodeBreaker(int code){
        ValueToFind=code % 10;
        iCallPut= (int)(code /10)% 10;
        iTipoContrato=(int)(code /100) % 10;
        iTipoEjercicio=(int)(code /1000)% 10;
        Modelo=(int)(code /10000)% 10;
        Steps=(int)(code/100000);
        
        CP =(iCallPut==0)? 'C':'P'; //Call o Put
        TC =(iTipoContrato==0)? 'S':'F'; //Stock Future
        TE =(iTipoEjercicio==0)? 'E':'A'; //European American
          
    }
    public int getValueToFind(){return ValueToFind;}
    
    public CodeBreaker(String code){
    //El string viene armado; 
     // modelo+Ejerc+Contrato+callPut + value to find +Steps  
    //    Modelo+TipoEj ('A' o 'E')+TipoContrato('F' o 'S)+callPut ('C' o 'P')
    // Son todos de 1 char salvo steps que es variable. VER
    // usar integer.toString(i) para los steps con alguna S para indicar donde empieza.
    //char c = s.charAt(0);
    // code example 1ASC01001 (1 American Stock Call 0 1001) 
    
        Model       =code.charAt(0);
        Modelo      =Integer.parseInt(String.valueOf(Model));
        TE          =code.charAt(1);
        TC          =code.charAt(2);
        CP          =code.charAt(3);
        vtof        = code.charAt(4);
        ValueToFind=Integer.parseInt(String.valueOf(vtof));
        String stp  =code.substring(5);
        Steps       = Integer.parseInt(stp);
        
        
    }
    
}
