import javax.swing.JOptionPane;
import java.util.*;

public class StockRequest extends Stock{
    private String SRID;
    private static Set<String> SRIDSet=new HashSet<String>();
    private static int count=0;
    private String status;
    private int outstanding; //Outstanding Quantity
    public StockRequest(){
        while(SRIDSet.contains(String.format("%s%04d","SR",++count))){
            if(count>=9999) count=0;//RESET COUNT IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        SRID=String.format("%s%04d","SR",count);
        SRIDSet.add(SRID);
        setStatus("Pending");
    }
    public StockRequest(Product product, int qty, String size){
        while(SRIDSet.contains(String.format("%s%04d","SR",++count))){
            if(count>=9999) count=0;//RESET COUNT IF IT EXCEEDS 9999, TO PREVENT OVERFLOW
        }//ENSURE NO DUPLICATE IDs
        SRID=String.format("%s%04d","SR",count);
        SRIDSet.add(SRID);
        setPQS(product, qty,size);
        setStatus("Pending");
    }
    public StockRequest(String SRID, String status,Product product, int qty, int outstanding, String size, int d, int m, int y, int h, int min, int s){//FOR USE IN LOADING FROM FILE, DO NOT USE FOR NEW RECORDS
        SRIDSet.add(SRID);
        this.SRID=SRID;
        setPQS(product, qty,size);
        setStatus(status);
        date.changeDateTime(d, m, y, h, min, s);
        count++;
    }
    //Constructors
    public void setStatus(String status){
        if(status.equals("Pending")||status.equals("Partially Fulfilled")||status.equals("Fulfilled")||status.equals("Rejected")){
            this.status=status;
            if(status.equals("Fulfilled")){
                outstanding=0;
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid Status.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    public void changeSRID(String SRID){
        if(SRID.matches("SR\\d+")&& SRID.length()==6 && !(SRIDSet.contains(SRID))){//REGEX FOR SRID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            SRIDSet.remove(this.SRID);
            this.SRID=SRID;
            SRIDSet.add(SRID);
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid Stock Request ID!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    //USE WITH CAUTION!!!! COULD BREAK SYSTEM
    public void setQty(int qty){
        if(qty<=0){
            JOptionPane.showMessageDialog(null, "Quantity must be 1 or more!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        else{
            outstanding+=qty-this.qty;
            if(outstanding<=0){
               setStatus("Fulfilled");      
            }
            this.qty=qty;
        }
    }
    public void setOutstanding(int outstanding){
        if(outstanding<=0){
            JOptionPane.showMessageDialog(null, "Outstanding Quantity must be 1 or more!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        else{
            this.outstanding=outstanding;
        }
    }
    public void setPQS(Product product, int qty, String size){
        if(qty<=0){
            JOptionPane.showMessageDialog(null, "Quantity must be 1 or more!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        else{try{
            this.product=product;
            this.size=size;
            outstanding+=qty-this.qty;
            if(outstanding<=0){
               setStatus("Fulfilled");      
            }
            this.qty=qty;
            }catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error setting Product, Quantity and Size.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //Setters
    public int getCount(){
        return count;
    }
    public String getSRID(){
        return SRID;
    }
    public String getStatus(){
        return status;
    }
    public int getOutstanding(){
        return outstanding;
    }
    //Getters
}
