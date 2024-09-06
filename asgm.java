public class asgm {
    public static void main(String[] args) {
        Cancellation[] cancel=new Cancellation[99];
        Product prod=new Product();
        Product prod2=new Product();
        cancel[0]=new Cancellation(prod,5);
        cancel[0].setQty(10);
        System.out.println(prod.qty);
    }
}
