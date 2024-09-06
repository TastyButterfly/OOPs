public class asgm {
    public static void main(String[] args) {
        Stock[] stock=new Stock[1000];
        Product prod=new Product();
        Product prod2=new Product();
        Order order=new Order();
        stock[0]=new Cancellation();
        ((Cancellation) stock[0]).setProdandQty(prod,10);
        ((Cancellation) stock[0]).setQty(50);
        ((Cancellation) stock[0]).setProdandQty(prod2,10);
        ((Cancellation) stock[0]).setOrder(order);
        System.out.println(((Cancellation)stock[0]).getQty());
    }
}
