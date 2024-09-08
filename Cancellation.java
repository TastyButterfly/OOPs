public class Cancellation extends Stock{
    private String cancelID;
    private Order order;
    private static int count=0;
    private String status;
    //Attributes
    public Cancellation(Product product, int qty, Order order){
        cancelID=String.format("%s%03d","CA",++count);
        setProdandQty(product, qty);
        setOrder(order);
    }
    public Cancellation(Order order){
        cancelID=String.format("%s%03d","CA",++count);
        setOrder(order);
    }
    public Cancellation(){
        cancelID=String.format("%s%03d","CA",++count);
    }
    public Cancellation(Product product, int qty){
        cancelID=String.format("%s%03d","CA",++count);
        setProdandQty(product, qty);
    }
    //Constructor
    public void setStatus(String status){
        if(status.equals("Pending")||status.equals("Approved")||status.equals("Rejected")){
            this.status=status;
        }
        else{
            System.out.println("Invalid Status.");
        }
    }
    public void changeCancelID(String cancelID){
        if(cancelID.matches("CA\\d+")&& cancelID.length()==5 && Integer.parseInt(cancelID.replaceAll("[^0-9]", ""))!=count){//REGEX FOR CANCELID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            if(Integer.parseInt(cancelID.replaceAll("[^0-9]", ""))>count){ //ENSURE NO DUPLICATE IDs
                count=Integer.parseInt(cancelID.replaceAll("[^0-9]", ""));
            }
            this.cancelID=cancelID;
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
        super.setQty(true, qty);
    }
    public void setProdandQty(Product product, int qty){
        super.setProdandQty(true,product,qty);
    }
    //Setters
    public static int addCancellation(){
        return 1;
    }
    //Functional Modules
    public Order getOrderObj(){
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
