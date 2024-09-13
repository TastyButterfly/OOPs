import java.util.*;
import javax.swing.*;

public class StockOut extends Stock{
    private String stockOutID;
    private static Set<String> stockOutIDSet=new HashSet<String>();
    private static int count=0;
    public StockOut(){
        while(stockOutIDSet.contains(String.format("%s%04d","SO",++count))){
            if(count>=9999) count=0;//RESET COUNT IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        stockOutID=String.format("%s%04d","SO",count);
        stockOutIDSet.add(stockOutID);
    }
    public StockOut(Product product, int qty, String size){
        while(stockOutIDSet.contains(String.format("%s%04d","SO",++count))){
            if(count>=9999) count=0;//RESET COUNT IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        stockOutID=String.format("%s%04d","SO",count);
        stockOutIDSet.add(stockOutID);
        setPQS(product, qty, size);
    }
    public StockOut(String stockOutID, Product product, int qty, String size, int d, int m, int y, int h, int min, int s){//FOR USE IN LOADING FROM FILE, DO NOT USE FOR NEW RECORDS
        stockOutIDSet.add(stockOutID);
        this.stockOutID=stockOutID;
        this.product=product;
        this.qty=qty;
        this.size=size;
        date.changeDateTime(d, m, y, h, min, s);
        count++;
    }
    //Constructors
    public void changeSOID(String stockOutID){
        if(stockOutID.matches("SO\\d+")&& stockOutID.length()==6 && !(stockOutIDSet.contains(stockOutID))){//REGEX FOR SOID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            stockOutIDSet.remove(this.stockOutID);
            this.stockOutID=stockOutID;
            stockOutIDSet.add(stockOutID);
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid Stock Out ID!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    //USE WITH CAUTION!!!! COULD BREAK SYSTEM
    public boolean setQty(int qty){
       return super.setQty(false, qty);
    }
    public boolean setProdandQty(Product product, int qty){
        return super.setProdandQty(false,product,qty);
    }
    public boolean setPQS(Product product, int qty, String size){
        return super.setPQS(false,product,qty,size);
    }
    //Setters
    public int getCount(){
        return count;
    }
    public String getSOID(){
        return stockOutID;
    }
    //Getters
}

