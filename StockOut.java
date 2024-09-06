
public class StockOut extends Stock{
    private String sOutID;
    private Order order;
    private static int count=0;
    public StockOut(){
        sOutID=String.format("%s%04d","SO",++count);
    }
    public StockOut(Product product, int qty){
        sOutID=String.format("%s%04d","SO",++count);
        setProdandQty(product, qty);
    }
    public StockOut(Order order){
        sOutID=String.format("%s%04d","SO",++count);
        setOrder(order);
    }
    public StockOut(Order order, Product product, int qty){
        sOutID=String.format("%s%04d","SO",++count);
        setProdandQty(product, qty);
        setOrder(order);
    }
    //Constructors
    public void setSOID(String sOutID){
        if(sOutID.matches("SO\\d+") && sOutID.length()==6 && Integer.parseInt(sOutID.replaceAll("[^0-9]", ""))!=count){//REGEX FOR SOID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            if(Integer.parseInt(sOutID.replaceAll("[^0-9]", ""))>count){ //ENSURE NO DUPLICATE IDs
                count=Integer.parseInt(sOutID.replaceAll("[^0-9]", ""));
            }
            this.sOutID=sOutID;
        }
        else{
            System.out.println("Invalid Cancellation ID");
        }
    }
    //USE WITH CAUTION!!!! COULD BREAK SYSTEM
    public void setOrder(Order order){
        try{
            this.order=order;
        }catch (Exception e){
            System.out.println("Error setting order.");
        }
    }
    public void setQty(int qty){
       super.setQty(false, qty);
    }
    public void setProdandQty(Product product, int qty){
        super.setProdandQty(false,product,qty);
    }
    //Setters
    public Order getOrderObj(){
        return order;
    }
    public int getCount(){
        return count;
    }
    public String getSOID(){
        return sOutID;
    }
    //Getters
}

