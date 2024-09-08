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
        else if (stockIn && this.qty-qty>product.getQuantity()){//check if there is enough stock
            System.out.println("Not enough stock if this record is changed to a quantity of lower value!");
            return false;
        }
        else if(!(stockIn) && qty-this.qty>product.getQuantity()){
            System.out.println("Not enough stock to stock out!");
            return false;
        }
        else{
            if(product!=null && stockIn){
                product.setQuantity(product.getQuantity()+qty);
                product.setQuantity(product.getQuantity()-this.qty);
            }
            else if(product!=null && !(stockIn)){//for use in stock outs
                product.setQuantity(product.getQuantity()-qty);
                product.setQuantity(product.getQuantity()+this.qty);
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
        else if(product==null){//product must be set
            System.out.println("Product not set! Changes cannot be made.");
            return false;
        }
        else if (this.product!=null && stockIn && this.qty>this.product.getQuantity()){
                System.out.println("Not enough stock to undo the stock in of the old product!\nConsider making a new stock in record for the old product first.");
                return false;
        }
        else if(this.product!=null && !(stockIn) && qty>product.getQuantity()){
            System.out.println("Not enough stock to stock out!");
            return false;
        }
        else{try{
            if(stockIn){
                if(this.product!=null){
                    this.product.setQuantity(this.product.getQuantity()-this.qty);
                }
                this.product=product;
                product.setQuantity(product.getQuantity()+qty);
                this.qty=qty;
            }
            else if(!(stockIn)){//for use in stock outs
                if(this.product!=null){
                    this.product.setQuantity(this.product.getQuantity()+qty);
                }
                this.product=product;
                product.setQuantity(product.getQuantity()-qty);
                this.qty=qty;
            }
            return true;
            }catch (Exception e){
                if(this.product!=null && stockIn){ 
                    product.setQuantity(product.getQuantity()+this.qty);
                }
                else if(this.product!=null && !(stockIn)){//reverse changes in case of exception thrown
                    product.setQuantity(product.getQuantity()-this.qty);
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