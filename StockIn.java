public class StockIn extends Stock{
    private String stockInID;
    private static int count=0;
    private StockRequest SR;
    private int temp;
    public StockIn(){
        stockInID=String.format("%s%04d","SI",++count);
    }
    public StockIn(StockRequest SR){
        stockInID=String.format("%s%04d","SI",++count);
        setSR(SR);
    }
    public StockIn(Product product, int qty){
        stockInID=String.format("%s%04d","SI",++count);
        setProdandQty(product, qty);
    }
    public StockIn(StockRequest SR, Product product, int qty){
        stockInID=String.format("%s%04d","SI",++count);
        setSR(SR);
        setProdandQty(product, qty);
    }
    //Constructors
    public void changeSIID(String stockInID){
        if(stockInID.matches("SI\\d+") && stockInID.length()==6 && Integer.parseInt(stockInID.replaceAll("[^0-9]", ""))!=count){//REGEX FOR SOID AND TO ENSURE IT IS NOT A DUPLICATE OF LATEST ID
            if(Integer.parseInt(stockInID.replaceAll("[^0-9]", ""))>count){ //ENSURE NO DUPLICATE IDs
                count=Integer.parseInt(stockInID.replaceAll("[^0-9]", ""));
            }
            this.stockInID=stockInID;
        }
        else{
            System.out.println("Invalid Stock In ID!");
        }
    }
    //USE WITH CAUTION!!!! COULD BREAK SYSTEM
    public void setSR(StockRequest SR){
        try{
            if(product==SR.getProductObj()){
                SR.setOutstanding(SR.getOutstanding()+qty);
                this.SR=SR;
                SR.setOutstanding(SR.getOutstanding()-qty);
            }
            else{
                System.out.println("Product does not match Stock Request.");
            }
        }catch (Exception e){
            System.out.println("Error setting Stock Request.");
        }
    }
    public boolean setQty(int qty){
        temp=this.qty;
        if(SR!=null && super.setQty(true,qty)){
            SR.setOutstanding((SR.getOutstanding()+temp));
            SR.setOutstanding((SR.getOutstanding()-qty));
            if(SR.getOutstanding()<=0){
                SR.setStatus("Fulfilled");
            }
            else{
                SR.setStatus("Partially Fulfilled");
            }
            return true;
        }
        else if(SR==null){
            super.setProdandQty(true,product,qty);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean setProdandQty(Product product, int qty){
        temp=this.qty;
        if(SR!=null && super.setProdandQty(true,product,qty) && product==SR.getProductObj()){
            SR.setOutstanding((SR.getOutstanding()+temp));
            SR.setOutstanding((SR.getOutstanding()-qty));
            if(SR.getOutstanding()<=0){
                SR.setStatus("Fulfilled");
            }
            else{
                SR.setStatus("Partially Fulfilled");
            }
            return true;
        }
        else if(SR==null){
            super.setProdandQty(true,product,qty);
            return true;
        }
        else{
            return false;
        }
    }
    //Setters
    public StockRequest getSR(){
        return SR;
    }
    public int getCount(){
        return count;
    }
    public String getSIID(){
        return stockInID;
    }
    //Getters

}
