import javax.swing.JOptionPane;
import java.util.*;

public class StockIn extends Stock{
    private String stockInID;
    private static Set<String> stockInIDSet=new HashSet<String>();
    private static int SIcount=0;
    private StockRequest SR;
    private int temp;
    public StockIn(){
        while(stockInIDSet.contains(String.format("%s%04d","SI",++SIcount))){
            if(SIcount>=9999) SIcount=0;//RESET SIcount IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        stockInID=String.format("%s%04d","SI",SIcount);
        stockInIDSet.add(stockInID);
    }
    public StockIn(StockRequest SR){
        while(stockInIDSet.contains(String.format("%s%04d","SI",++SIcount))){
            if(SIcount>=9999) SIcount=0;//RESET SIcount IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        stockInID=String.format("%s%04d","SI",SIcount);
        stockInIDSet.add(stockInID);
        setSR(SR);
    }
    public StockIn(Product product, int qty, String size, Product staffProduct){
        while(stockInIDSet.contains(String.format("%s%04d","SI",++SIcount))){
            if(SIcount>=9999) SIcount=0;//RESET SIcount IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        stockInID=String.format("%s%04d","SI",SIcount);
        stockInIDSet.add(stockInID);
        setPQS(staffProduct, product, qty, size);
    }
    public StockIn(String stockInID, StockRequest SR, Product product, int qty, String size, int d, int m, int y, int h, int min, int s, Product staffProduct){//FOR USE IN LOADING FROM FILE, DO NOT USE FOR NEW RECORDS
        stockInIDSet.add(stockInID);
        this.stockInID=stockInID;
        this.staffProduct=staffProduct;
        this.product=product;
        this.qty=qty;
        this.size=size;
        setSR(SR);
        date.changeDateTime(d, m, y, h, min, s);
        SIcount++;
    }
    //Constructors
    public boolean changeSIID(String stockInID){
        if(stockInID.matches("SI\\d+")&& stockInID.length()==6 && !(stockInIDSet.contains(stockInID))){//REGEX FOR SIID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            stockInIDSet.remove(this.stockInID);
            this.stockInID=stockInID;
            stockInIDSet.add(stockInID);
            return true;
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid Stock In ID!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
    //USE WITH CAUTION!!!! COULD BREAK SYSTEM
    public void setSR(StockRequest SR){
        try{
            if(product==SR.getProduct()){
                if(this.SR!=null){
                    this.SR.setOutstanding(SR.getOutstanding()+qty);//RESET OUTSTANDING OF OLD SR
                }
                this.SR=SR;
                SR.setOutstanding(SR.getOutstanding()-qty);
            }
            else{
                JOptionPane.showMessageDialog(null, "Product does not match Stock Request.\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error setting Stock Request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public boolean setQty(int qty){
        temp=this.qty;
        if(SR!=null && super.setQty(true,qty)){
            SR.setOutstanding((SR.getOutstanding()+temp));
            SR.setOutstanding((SR.getOutstanding()-qty));
            if(SR.getOutstanding()<=0){
                SR.setStatus("Fulfilled");
                SR.setOutstanding(0);
            }
            else{
                SR.setStatus("Partially Fulfilled");
            }
            return true;
        }
        else if(SR==null){
            super.setProdandQty(true, staffProduct, product,qty);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean setProdandQty(Product staffProduct, Product product, int qty){
        temp=this.qty;
        if(SR!=null && super.setProdandQty(true,staffProduct, product,qty) && product==SR.getProduct()){
            SR.setOutstanding((SR.getOutstanding()+temp));
            SR.setOutstanding((SR.getOutstanding()-qty));
            if(SR.getOutstanding()<=0){
                SR.setStatus("Fulfilled");
                SR.setOutstanding(0);
            }
            else{
                SR.setStatus("Partially Fulfilled");
            }
            return true;
        }
        else if(SR==null){
            super.setProdandQty(true, staffProduct, product,qty);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean setPQS(Product staffProduct, Product product, int qty, String size){
        temp=this.qty;
        if(SR!=null && product==SR.getProduct() && size.equals(SR.getSize())){
            if(super.setPQS(true,staffProduct,product,qty,size)){
                SR.setOutstanding((SR.getOutstanding()+temp));
                SR.setOutstanding((SR.getOutstanding()-qty));
                if(SR.getOutstanding()<=0){
                    SR.setStatus("Fulfilled");
                    SR.setOutstanding(0);
                }
                else{
                    SR.setStatus("Partially Fulfilled");
                }
                return true;
            }
            else{
                return false;
            }
        }
        else if(SR==null){
            super.setPQS(true,staffProduct,product,qty,size);
            return true;
        }
        else if(product!=SR.getProduct()||size!=SR.getSize()){
            JOptionPane.showMessageDialog(null, "Product or size does not match Stock Request.\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{
            JOptionPane.showMessageDialog(null, "Unexpected error occured.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public static void setCount(int count){
        SIcount=count;
    }
    //Setters
    public StockRequest getSR(){
        return SR;
    }
    public int getCount(){
        return SIcount;
    }
    public String getSIID(){
        return stockInID;
    }
    public static Set<String> getSIIDSet(){
        return stockInIDSet;
    }
    //Getters
}
