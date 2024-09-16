import java.util.*;
import javax.swing.*;

public class StockOut extends Stock{
    private String stockOutID;
    private static Set<String> stockOutIDSet=new HashSet<String>();
    private static int SOcount=0;
    public StockOut(){
        while(stockOutIDSet.contains(String.format("%s%04d","SO",++SOcount))){
            if(SOcount>=9999) SOcount=0;//RESET SOcount IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        stockOutID=String.format("%s%04d","SO",SOcount);
        stockOutIDSet.add(stockOutID);
    }
    public StockOut(Product product, int qty, String size, Product staffProduct){
        while(stockOutIDSet.contains(String.format("%s%04d","SO",++SOcount))){
            if(SOcount>=9999) SOcount=0;//RESET SOcount IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        stockOutID=String.format("%s%04d","SO",SOcount);
        stockOutIDSet.add(stockOutID);
        setPQS(staffProduct, product, qty, size);
    }
    public StockOut(String stockOutID, Product product, int qty, String size, int d, int m, int y, int h, int min, int s, Product staffProduct){//FOR USE IN LOADING FROM FILE, DO NOT USE FOR NEW RECORDS
        stockOutIDSet.add(stockOutID);
        this.stockOutID=stockOutID;
        this.staffProduct=staffProduct;
        this.product=product;
        this.qty=qty;
        this.size=size;
        date.changeDateTime(d, m, y, h, min, s);
        SOcount++;
    }
    //Constructors
    public boolean changeSOID(String stockOutID){
        if(stockOutID.matches("SO\\d+")&& stockOutID.length()==6 && !(stockOutIDSet.contains(stockOutID))){//REGEX FOR SOID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            stockOutIDSet.remove(this.stockOutID);
            this.stockOutID=stockOutID;
            stockOutIDSet.add(stockOutID);
            return true;
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid Stock Out ID!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
    //USE WITH CAUTION!!!! COULD BREAK SYSTEM
    public boolean setQty(int qty){
       return super.setQty(false, qty);
    }
    public boolean setProdandQty(Product staffProduct, Product product, int qty){
        return super.setProdandQty(false,staffProduct,product,qty);
    }
    public boolean setPQS(Product staffProduct, Product product, int qty, String size){
        return super.setPQS(false,staffProduct,product,qty,size);
    }
    public static void setCount(int count){
        SOcount=count;
    }
    //Setters
    public int getCount(){
        return SOcount;
    }
    public String getSOID(){
        return stockOutID;
    }
    //Getters
}

