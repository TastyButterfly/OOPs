public class StockOut extends Stock{
    private String stockOutID;
    private Order order;
    private static int count=0;
    public StockOut(){
        stockOutID=String.format("%s%04d","SO",++count);
    }
    public StockOut(Product product, int qty){
        stockOutID=String.format("%s%04d","SO",++count);
        setProdandQty(product, qty);
    }
    public StockOut(Order order){
        stockOutID=String.format("%s%04d","SO",++count);
        setOrder(order);
    }
    public StockOut(Order order, Product product, int qty){
        stockOutID=String.format("%s%04d","SO",++count);
        setProdandQty(product, qty);
        setOrder(order);
    }
    //Constructors
    public void changeSOID(String stockOutID){
        if(stockOutID.matches("SO\\d+") && stockOutID.length()==6 && Integer.parseInt(stockOutID.replaceAll("[^0-9]", ""))!=count){//REGEX FOR SOID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            if(Integer.parseInt(stockOutID.replaceAll("[^0-9]", ""))>count){ //ENSURE NO DUPLICATE IDs
                count=Integer.parseInt(stockOutID.replaceAll("[^0-9]", ""));
            }
            this.stockOutID=stockOutID;
        }
        else{
            System.out.println("Invalid Stock Out ID!");
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
        return stockOutID;
    }
    //Getters
}

