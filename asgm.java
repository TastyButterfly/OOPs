public class asgm {
    public static void main(String[] args) {
        Cancellation[] cancel=new Cancellation[99];
        cancel[0]=new Cancellation();
        cancel[0].getDateObj().changeDate(9,9,2024);
        cancel[0].getDateObj().changeTime(0, 0, 0);
        System.out.println(cancel[0].getDateObj().getDateTime());
    }
}
