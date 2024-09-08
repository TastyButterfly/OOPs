import javax.swing.*;
public class asgm {
    public static void main(String[] args) {
        JFrame jf=new JFrame();
        jf.setSize(500,500);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setTitle("Inventory System");
        Stock[] stock=new Stock[1000];
        Product prod=new Product("P001","Shirt","M",10);
        Product prod2=new Product("P002","Pants","M",20);
        prod2.setQuantity(10);
        prod.setQuantity(10);
        Order order=new Order();
        stock[0]=new Cancellation();
        ((Cancellation) stock[0]).setProdandQty(prod,10);
        ((Cancellation) stock[0]).setQty(50);
        ((Cancellation) stock[0]).setProdandQty(prod2,10);
        ((Cancellation) stock[0]).setOrder(order);
        System.out.println(((Cancellation)stock[0]).getQty());
    }
}
