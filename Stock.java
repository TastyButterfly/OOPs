public class Stock{
    protected Product product;
    protected CDate date;
    protected int qty;
    //Attributes
    public Stock(){
        date=new CDate();
    }
    //Constuctor
    public void setQty(boolean stockIn, int qty){
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
        }
        else{
            if(product!=null && stockIn){
                product.qty+=qty;
                product.qty-=this.qty;
            }
            else if(product!=null && !(stockIn)){//for use in stock outs
                product.qty-=qty;
                product.qty+=this.qty;
            }
            this.qty=qty;
        }
    }
    public void setProdandQty(boolean stockIn, Product product, int qty){
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
        }
        else{try{
            if(stockIn){
                if(this.product!=null){
                    product.qty-=this.qty;
                }
                this.product=product;
                product.qty+=qty;
                this.qty=qty;
            }
            else if(!(stockIn)){//for use in stock outs
                if(this.product!=null){
                    product.qty+=this.qty;
                }
                this.product=product;
                product.qty-=qty;
                this.qty=qty;
            }
            }catch (Exception e){
                if(this.product!=null && stockIn){ 
                    product.qty+=this.qty;
                }
                else if(this.product!=null && !(stockIn)){//reverse changes in case of exception thrown
                    product.qty-=this.qty;
                }
                System.out.println("Error referencing Product object");
            }
        }
    }
    //Setters
    public CDate getDateObj(){
        return date;  
    }
    public Product getProductObj(){
        return product;
    }
    public int getQty(){
        return qty;
    }
    //Getters
}