import java.time.LocalDateTime;

public class Date {
    private LocalDateTime date;

    public Date(){
        date=LocalDateTime.now();
    }
    //Constructor
    public void changeDateTime(int d, int m, int y, int h, int min, int s){
        date=LocalDateTime.of(y,m,d,h,min,s);
    }
     public void changeDate(int d, int m, int y){
        date=LocalDateTime.of(y,m,d,date.getHour(),date.getMinute(),date.getSecond());
    }
    public void changeTime(int h, int m, int s){
        date=LocalDateTime.of(date.getYear(),date.getMonthValue(),date.getDayOfMonth(),h,m,s);
    }
    //Getters
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
    //Getters
}
