
public class StockOut {
    private String sOutID;
    private Product product;
    private int qty;
    private Date date;
    private Order order;
    private static int count=0;
    public StockOut(){
        sOutID=String.format("%s%04d","SO",++count);
        date=new Date();
    }
    public StockOut(Product product, int qty){
        sOutID=String.format("%s%04d","SO",++count);
        this.product=product;
        this.qty=qty;
        date=new Date();
        product.qty-=qty;
    }
    public StockOut(Order order){
        sOutID=String.format("%s%04d","SO",++count);
        this.order=order;
        date=new Date();
    }
    public StockOut(Order order, Product product, int qty){
        sOutID=String.format("%s%04d","SO",++count);
        this.order=order;
        this.product=product;
        this.qty=qty;
        date=new Date();
        product.qty-=qty;
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
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
        }
        else{
            if(product!=null){
                product.qty+=this.qty;
                product.qty-=qty;
            }
            this.qty=qty;
        }
    }
    public void setProdandQty(Product product, int qty){
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
        }
        else{try{
            if(this.product!=null){
                product.qty+=this.qty;
            }
            this.product=product;
            product.qty-=qty;
            this.qty=qty;
            }catch (Exception e){
                if(this.product!=null){ 
                    product.qty-=this.qty;
                }
                System.out.println("Error referencing Product object");
            }
        }
    }
    //Setters
    public Product getProductObj(){
        return product;
    }   
    public Order getOrderObj(){
        return order;
    }
    public int getQty(){
        return qty;
    }  
    public int getCount(){
        return count;
    }
    public String getSOID(){
        return sOutID;
    }
    public Date getDateObj(){
        return date;  
    }
    //Getters
}

