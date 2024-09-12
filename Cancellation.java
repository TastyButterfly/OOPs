import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

public class Cancellation extends Stock{
    private static Set<String> cancelIDSet=new HashSet<String>();
    private String cancelID;
    private Order order;
    private static int count=0;
    private String status;
    //Attributes
    public Cancellation(Product product, int qty, Order order, String size){
        while(cancelIDSet.contains(String.format("%s%04d","CA",++count))){
            if(count>=9999) count=0;//RESET COUNT IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        cancelID=String.format("%s%04d","CA",count);
        cancelIDSet.add(cancelID);
        setPQS(product, qty,size);
        setOrder(order);
    }
    public Cancellation(Order order){
        while(cancelIDSet.contains(String.format("%s%04d","CA",++count))){
            if(count>=9999) count=0;
        }//ENSURE NO DUPLICATE IDs
        cancelID=String.format("%s%04d","CA",count);
        cancelIDSet.add(cancelID);
        setOrder(order);
    }
    public Cancellation(){
        while(cancelIDSet.contains(String.format("%s%04d","CA",++count))){
            if(count>=9999) count=0;
        }//ENSURE NO DUPLICATE IDs
        cancelID=String.format("%s%04d","CA",count);
        cancelIDSet.add(cancelID);
    }
    public Cancellation(Product product, int qty, String size){
        while(cancelIDSet.contains(String.format("%s%04d","CA",++count))){
            if(count>=9999) count=0;
        }//ENSURE NO DUPLICATE IDs
        cancelID=String.format("%s%04d","CA",count);
        cancelIDSet.add(cancelID);
        setPQS(product, qty,size);
    }
    public Cancellation(String cancelID, String status, int qty, int d, int m, int y, int h, int min, int s,String size, Product product, Order order){//FOR USE IN LOADING FROM FILE, DO NOT USE FOR NEW RECORDS
        cancelIDSet.add(cancelID);
        this.cancelID=cancelID;
        this.status=status;
        date.changeDateTime(d, m, y, h, min, s);
        this.size=size;
        setProdandQty(product, qty);
        setOrder(order);
        count++;
    }
    //Constructor
    public boolean setStatus(String status){
        if(status.equals("Pending")||status.equals("Approved")||status.equals("Rejected")){
            this.status=status;
            return true;
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid Status.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;

        }
    }
    public void changeCancelID(String cancelID){
        if(cancelID.matches("CA\\d+")&& cancelID.length()==6 && !(cancelIDSet.contains(cancelID))){//REGEX FOR CANCELID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            cancelIDSet.remove(this.cancelID);
            this.cancelID=cancelID;
            cancelIDSet.add(cancelID);
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid Cancellation ID!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    //USE WITH CAUTION!!!! COULD BREAK SYSTEM
    public void setOrder(Order order){
        try{
            this.order=order;
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error referencing Order object", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public boolean setQty(int qty){
        return super.setQty(true, qty);
    }
    public boolean setSize(String size){
        return super.setSize(true, size);
    }
    public boolean setProdandQty(Product product, int qty){
        return super.setProdandQty(true,product,qty);
    }
    public boolean setPQS(Product product, int qty, String size){
        return super.setPQS(true,product,qty,size);
    }
    //Setters
    public Order getOrder(){
        return order;
    }
    public int getCount(){
        return count;
    }
    public String getCancelID(){
        return cancelID;
    }
    public String getStatus(){
        return status;
    }
    //Getters
}
