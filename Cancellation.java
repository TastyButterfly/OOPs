import java.time.LocalDateTime;

public class Cancellation{
    private String cancelID;
    private Order order;
    private Product product;
    private LocalDateTime date;
    private int qty;
    private static int count;
    public Cancellation(Product product, int qty, Order order){
        cancelID=String.format("%c%d",'C',++count);
        this.product=product;
        this.qty=qty;
        this.order=order;
        date=LocalDateTime.now();
        product.qty+=qty;
    }
    public Cancellation(Order order){
        cancelID=String.format("%c%d",'C',++count);
        this.order=order;
        date=LocalDateTime.now();
    }
    public Cancellation(){
        cancelID=String.format("%c%d",'C',++count);
        date=LocalDateTime.now();
    }
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
    public static int addCancellation(){
        return 1;
    }
    public LocalDateTime getDate(){
        return date;
    }
    public int getCount(){
        return count;
    }
}
