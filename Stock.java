public class Stock{
private Product product;
private Date date;
private int qty;

public Stock(){
System.out.println("This is a superclass. DO NOT create an object of this class!");
}

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
    public void setProdandQty(Product product, int qty){
        if(qty<=0){
            System.out.println("Quantity must be 1 or more!");
        }
        else{try{
            if(this.product!=null){
                product.qty-=this.qty;
            }
            this.product=product;
            product.qty+=qty;
            this.qty=qty;
            }catch (Exception e){
                if(this.product!=null){ 
                    product.qty+=this.qty;
                }
                System.out.println("Error referencing Product object");
            }
        }
    }
}