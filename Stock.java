public abstract class Stock{
    protected Product product;
    protected CDate date;
    protected int qty;
    //Attributes
    public Stock(){
        date=new CDate();
    }
    //Constuctor
    public boolean setQty(boolean stockIn, int qty){
        if(qty<=0){//quantity must be more than 0
            System.out.println("Invalid quantity!");
            return false;
        }
        else if(product==null){//product must be set
            System.out.println("Product not set! Changes cannot be made.");
            return false;
        }
        else if (stockIn && this.qty-qty>product.qty){//check if there is enough stock
            System.out.println("Not enough stock if this record is changed to a quantity of lower value!");
            return false;
        }
        else if(!(stockIn) && qty-this.qty>product.qty){
            System.out.println("Not enough stock to stock out!");
            return false;
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
            return true;
        }
    }
    public boolean setProdandQty(boolean stockIn, Product product, int qty){
        if(qty<=0){//quantity must be more than 0
            System.out.println("Quantity must be more than 0!");
            return false;
        }
        else if (stockIn && this.qty>this.product.qty){
            System.out.println("Not enough stock to undo the stock in of the old product!\nConsider making a new stock in record for the old product first.");
            return false;
        }
        else if(!(stockIn) && qty>product.qty){
            System.out.println("Not enough stock to stock out!");
            return false;
        }
        else{try{
            if(stockIn){
                if(this.product!=null){
                    this.product.qty-=this.qty;
                }
                this.product=product;
                product.qty+=qty;
                this.qty=qty;
            }
            else if(!(stockIn)){//for use in stock outs
                if(this.product!=null){
                    this.product.qty+=qty;
                }
                this.product=product;
                product.qty-=qty;
                this.qty=qty;
            }
            return true;
            }catch (Exception e){
                if(this.product!=null && stockIn){ 
                    product.qty+=this.qty;
                }
                else if(this.product!=null && !(stockIn)){//reverse changes in case of exception thrown
                    product.qty-=this.qty;
                }
                System.out.println("Error referencing Product object");
                return false;
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