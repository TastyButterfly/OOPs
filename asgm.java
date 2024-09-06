public class asgm {
    public static void main(String[] args) {
        Cancellation[] cancel=new Cancellation[99];
        Product prod=new Product();
        cancel[0]=new Cancellation(prod,5);
        System.out.println(cancel[0].getDateObj().getDateTime());
    }
}
