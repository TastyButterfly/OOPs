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
    public void changeDate(int d, int m, int y){
        date=LocalDateTime.of(y,m,d,date.getHour(),date.getMinute(),date.getSecond());
    }
    public void changeTime(int h, int m, int s){
        date=LocalDateTime.of(date.getYear(),date.getMonthValue(),date.getDayOfMonth(),h,m,s);
    }
    public String getDateTime(){
        return String.format("%d-%d-%d %d:%d:%d",date.getDayOfMonth(),date.getMonthValue(),date.getYear(),date.getHour(),date.getMinute(),date.getSecond());
    }
    public String getDate(){
        return String.format("%d-%d-%d",date.getDayOfMonth(),date.getMonthValue(),date.getYear());
    }
    public String getTime(){
        return String.format("%d:%d:%d",date.getHour(),date.getMinute(),date.getSecond());
    }
    public String getDMY(){
        return String.format("%d %s %d",date.getDayOfMonth(),date.getMonth(),date.getYear());
    }
    public String getMY(){
        return String.format("%s %d",date.getMonth(),date.getYear());
    }
    public String getDM(){
        return String.format("%d %s",date.getDayOfMonth(),date.getMonth());
    }
    public String getDMValue(){
        return String.format("%d-%d",date.getDayOfMonth(),date.getMonthValue());
    }
    public int getDay(){
        return date.getDayOfMonth();
    }
    public int getMonthValue(){
        return date.getMonthValue();
    }
    public int getYear(){
        return date.getYear();
    }
    public String getMonth(){
        return String.format("%s",date.getMonth());
    }
    public int getDayOfYear(){
        return date.getDayOfYear();
    }
    public static int addCancellation(){
        return 1;
    }
    public int getCount(){
        return count;
    }
}
