public class StockRequest extends Stock{
    private String SRID;
    private static int count=0;

    public StockRequest(){
        SRID=String.format("%s%03d","SR",++count);
    }
    public StockRequest(Product product, int qty){
        SRID=String.format("%s%03d","SR",++count);
        setProdandQty(product, qty);
    }
    //Constructors
    public void setSRID(String SRID){
        if(SRID.matches("SR\\d+") && SRID.length()==5 && Integer.parseInt(SRID.replaceAll("[^0-9]", ""))!=count){//REGEX FOR SOID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            if(Integer.parseInt(SRID.replaceAll("[^0-9]", ""))>count){ //ENSURE NO DUPLICATE IDs
                count=Integer.parseInt(SRID.replaceAll("[^0-9]", ""));
            }
            this.SRID=SRID;
        }
        else{
            System.out.println("Invalid Cancellation ID");
        }
    }
    //USE WITH CAUTION!!!! COULD BREAK SYSTEM
    public void setQty(int qty){
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
        }
        else{
            this.qty=qty;
        }
    }
    public void setProdandQty(Product product, int qty){
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
        }
        else{try{
            this.product=product;
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
    //Getters
}
