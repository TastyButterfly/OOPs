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
    public Cancellation(String status, Product product, int qty, Order order, String size){
        while(cancelIDSet.contains(String.format("%s%04d","CA",++count))){
            if(count>=9999) count=0;//RESET COUNT IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        cancelID=String.format("%s%04d","CA",count);
        cancelIDSet.add(cancelID);
        this.status=status;
        setPQS(status, product, qty,size);
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
        setPQS(status, product, qty,size);
    }
    public Cancellation(String cancelID, String status, int qty, int d, int m, int y, int h, int min, int s,String size, Product product, Order order){//FOR USE IN LOADING FROM FILE, DO NOT USE FOR NEW RECORDS
        cancelIDSet.add(cancelID);
        this.cancelID=cancelID;
        this.status=status;
        date.changeDateTime(d, m, y, h, min, s);
        this.product=product;
        this.qty=qty;
        this.size=size;
        setOrder(order);
        count++;
    }
    //Constructor
    public boolean setStatus(String status){
        if(status.equals("Pending")||status.equals("Rejected")){
            this.status=status;
            return true;
        }
        else if(status.equals("Approved")){
            try{
                product.updateProductQuantities(size, -qty);
                product.setStaffQty(true, size, qty);
                this.status=status;
                return true;
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Error setting status.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
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
            int index;
            for(index=0;index<order.getProdList().size();index++){
                if(order.getProdList().get(index).getProdID().equals(product.getProdID())) break;
                else if(index==order.getProdList().size()-1){
                    JOptionPane.showMessageDialog(null, "Product does not match Order.\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            if(product==null){
                JOptionPane.showMessageDialog(null, "Product has to be set first.\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            else if(order.getSizes().get(index).equals(size) && order.getQty().get(index)>=qty){
                this.order=order;
            }
            else if(!order.getSizes().get(index).equals(size)){
                JOptionPane.showMessageDialog(null, "Size does not match Order.\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            else if(order.getQty().get(index)<qty){
                JOptionPane.showMessageDialog(null, "Cancelled quantity exceeds order!.\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Unexpected error occured.\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error referencing Order object", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public boolean setQty(String status, int qty){
        if(status.equals("Approved")) return super.setQty(true, qty);
        else if(qty<=0){
            JOptionPane.showMessageDialog(null, "Invalid Quantity.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{
            this.qty=qty;
            return true;
        }
    }
    public boolean setSize(String status, String size){
        if(status.equals("Approved")) return super.setSize(true, size);
        else if(product.getSizeIndex(size)==-1){
            JOptionPane.showMessageDialog(null, "Invalid Size.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{
            this.size=size;
            return true;
        }
    }
    public boolean setProdandQty(String status, Product product, int qty){
        if(status.equals("Approved")) return super.setProdandQty(true,product,qty);
        else if(qty<=0){
            JOptionPane.showMessageDialog(null, "Invalid Quantity.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(product==null){
            JOptionPane.showMessageDialog(null, "Invalid Product.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{
            this.product=product;
            this.qty=qty;
            return true;
        }
    }
    public boolean setPQS(String status, Product product, int qty, String size){
        if(status.equals("Approved")) return super.setPQS(true,product,qty,size);
        else if(qty<=0){
            JOptionPane.showMessageDialog(null, "Invalid Quantity.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(product==null){
            JOptionPane.showMessageDialog(null, "Invalid Product.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(product.getSizeIndex(size)==-1){
            JOptionPane.showMessageDialog(null, "Invalid Size.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{
            this.product=product;
            this.qty=qty;
            this.size=size;
            return true;
        }
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
