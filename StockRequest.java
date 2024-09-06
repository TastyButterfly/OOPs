public class StockRequest extends Stock{
    private String SRID;
    private static int count=0;
    private String status;
    private int outstanding; //Outstanding Quantity
    public StockRequest(){
        SRID=String.format("%s%03d","SR",++count);
    }
    public StockRequest(Product product, int qty){
        SRID=String.format("%s%03d","SR",++count);
        setProdandQty(product, qty);
        outstanding=qty;
    }
    //Constructors
    public void setStatus(String status){
        if(status.equals("Pending")||status.equals("Partially Fulfilled")||status.equals("Fulfilled")||status.equals("Rejected")){
            this.status=status;
        }
        else{
            System.out.println("Invalid Status.");
        }
    }
    public void changeSRID(String SRID){
        if(SRID.matches("SR\\d+") && SRID.length()==5 && Integer.parseInt(SRID.replaceAll("[^0-9]", ""))!=count){//REGEX FOR SOID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            if(Integer.parseInt(SRID.replaceAll("[^0-9]", ""))>count){ //ENSURE NO DUPLICATE IDs
                count=Integer.parseInt(SRID.replaceAll("[^0-9]", ""));
            }
            this.SRID=SRID;
        }
        else{
            System.out.println("Invalid Stock Request ID!");
        }
    }
    //USE WITH CAUTION!!!! COULD BREAK SYSTEM
    public void setQty(int qty){
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
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
            System.out.println("Quantity must be 1 or more!");
        }
        else{
            this.outstanding=outstanding;
        }
    }
    public void setProdandQty(Product product, int qty){
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
        }
        else{try{
            this.product=product;
            outstanding+=qty-this.qty;
            if(outstanding<=0){
               setStatus("Fulfilled");      
            }
            this.qty=qty;
            }catch (Exception e){
                System.out.println("Error referencing Product object");
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
