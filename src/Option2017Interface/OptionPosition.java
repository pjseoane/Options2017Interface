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



public class OptionPosition {
    protected AbstractOptionClass2017 anOption;
    protected double lots, lotSize, lotPrice,multiplier;
    protected String optionRoot;
    
    //armar alguna collection
    
    
    public OptionPosition(){}
    public OptionPosition(AbstractOptionClass2017 anOption, double lots,double lotSize,double lotPrice){
    
            this.optionRoot=anOption.getTicker();
            this.anOption=anOption;
            this.lots=lots;
            this.lotSize=lotSize;
            this.lotPrice=lotPrice;
            multiplier=lots*lotSize;
            // addToCollection()
    }
}
