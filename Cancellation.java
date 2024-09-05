public class Cancellation{
    private String cancelID;
    private Order order;
    private Product product;
    private Date date;
    private int qty;
    private static int count=0;
    public Cancellation(Product product, int qty, Order order){
        cancelID=String.format("%s%03d","CA",++count);
        this.product=product;
        this.qty=qty;
        this.order=order;
        date=new Date();
        product.qty+=qty;
    }
    public Cancellation(Order order){
        cancelID=String.format("%s%03d","CA",++count);
        this.order=order;
        date=new Date();
    }
    public Cancellation(){
        cancelID=String.format("%s%03d","CA",++count);
        date=new Date();
    }
    //Constructor
    public void setCancelID(String cancelID){
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
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
        }
        else{
            if(product!=null){
                product.qty-=this.qty;
                product.qty+=qty;
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
                product.qty-=this.qty;
            }
            this.product=product;
            product.qty+=qty;
            this.qty=qty;
            }catch (Exception e){
                if(this.product!=null){ 
                    product.qty+=this.qty;
                }
                System.out.println("Error referencing Product object");
            }
        }
    }
    //Setters
    public static int addCancellation(){
        return 1;
    }
    //Functional Modules
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
    public String getCancelID(){
        return cancelID;
    }
    public Date getDateObj(){
        return date;  
    }
    //Getters
}
