import java.time.LocalDateTime;

public class Cancellation{
    String cancelID;
    Order order;
    Product product;
    LocalDateTime date;
    int qty;
    static int latestCancelID;
    public Cancellation(String cancelID, Product product, int qty, Order order){
        this.cancelID=cancelID;
        this.product=product;
        this.qty=qty;
        this.order=order;
        date=LocalDateTime.now();
        product.qty+=qty;
    }
    public Cancellation(Order order){
        cancelID=++latestCancelID;
    }
    public void changeDate(LocalDateTime date){
        try{
            this.date=date;
        }catch (Exception e){
            System.out.println("Error changing date.");
        }
    }
    public void setProdandQty(Product product, int qty){
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
        }
        try{
            this.product=product;
            product.qty-=this.qty;
            product.qty+=qty;
            this.qty=qty;
        }catch (Exception e){
            System.out.println("Error referencing Product object");
        }
    }

}
