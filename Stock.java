import javax.swing.*;

public abstract class Stock{
    protected Product product;
    protected Product staffProduct;
    protected String size;
    protected CDate date;
    protected int qty;
    //Attributes
    public Stock(){
        date=new CDate();
    }
    //Constuctor
    public boolean setSize(boolean stockIn, String size){
        if(product==null){
            JOptionPane.showMessageDialog(null, "Product not set! Changes cannot be made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(product.getSizeIndex(size)==-1){
            JOptionPane.showMessageDialog(null, "Invalid size.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(!(stockIn) && product.getStaffQty()[product.getSizeIndex(size)]<qty){
            JOptionPane.showMessageDialog(null, "Not enough stock to stock out for size change!\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } 
        else if(this.size!=null && stockIn && product.getQtySizes()[product.getSizeIndex(this.size)]<qty){
            JOptionPane.showMessageDialog(null, "Undoing the stock in would cause quantities to fall below negative!\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{
            if(stockIn){
                if(this.size!=null){ 
                    product.updateProductQuantities(this.size, qty);
                    product.setStaffQty(false, this.size, qty);
                }
                product.updateProductQuantities(size, -(qty));
                product.setStaffQty(true, size, qty);
                this.size=size;
                return true;
            }
            else if(!(stockIn) && product.setStaffQty(false, size, qty)){
                if(this.size!=null) product.setStaffQty(true, this.size, qty);
                this.size=size;
                return true;
            }
            else{
                return false;
            }
        }
    }
    public boolean setQty(boolean stockIn, int qty){
        if(qty<=0){//quantity must be more than 0
            JOptionPane.showMessageDialog(null, "Quantity must be more than 0!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(product==null){//product must be set
            JOptionPane.showMessageDialog(null, "Product not set! Changes cannot be made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(product.getSizeIndex(size)==-1){
            JOptionPane.showMessageDialog(null, "Size not set or invalid! Changes cannot be made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if (this.product!=null && stockIn && this.qty-qty>this.product.getQtySizes()[product.getSizeIndex(size)]){//check if there is enough stock
            JOptionPane.showMessageDialog(null, "Not enough stock if this record is changed to a quantity of lower value!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(!(stockIn) && qty-this.qty>product.getStaffQty()[product.getSizeIndex(size)]){
            JOptionPane.showMessageDialog(null, "Not enough stock to stock out!\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{
            if(stockIn){
                product.updateProductQuantities(size, -(qty));
                product.updateProductQuantities(size, this.qty);
                product.setStaffQty(true, size, qty);
                product.setStaffQty(false, size, this.qty);
            }
            else if(!(stockIn)){//for use in stock outs
                product.setStaffQty(false, size, qty);
                product.setStaffQty(true, size, this.qty);
            }
            this.qty=qty;
            return true;
        }
    }
    public boolean setProdandQty(boolean stockIn, Product product, int qty){
        if(qty<=0){//quantity must be more than 0
            JOptionPane.showMessageDialog(null, "Quantity must be more than 0!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(product==null){//product must be set
            JOptionPane.showMessageDialog(null, "Product not set! Changes cannot be made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(product.getSizeIndex(size)==-1){
            JOptionPane.showMessageDialog(null, "Size not set or invalid! Changes cannot be made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if (this.product!=null && stockIn && this.qty>this.product.getQtySizes()[product.getSizeIndex(size)]){
            JOptionPane.showMessageDialog(null, "Not enough stock to undo the stock in of the old product!\nConsider making a new stock in record for the old product first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(!(stockIn) && qty>product.getStaffQty()[product.getSizeIndex(size)]){
            JOptionPane.showMessageDialog(null, "Not enough stock to stock out!\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{try{
            if(stockIn){
                if(this.product!=null){
                    this.product.updateProductQuantities(size,this.qty);
                    this.product.setStaffQty(false, size, this.qty);
                }
                this.product=product;
                product.updateProductQuantities(size,-(qty));
                product.setStaffQty(true, size, qty);
                this.qty=qty;
            }
            else if(!(stockIn)){//for use in stock outs
                if(this.product!=null){
                    this.product.setStaffQty(true,size,this.qty);
                }
                this.product=product;
                product.setStaffQty(false,size,qty);
                this.qty=qty;
            }
            return true;
            }catch (Exception e){
                if(this.product!=null && stockIn){ 
                    this.product.updateProductQuantities(size,-(this.qty));
                    this.product.setStaffQty(true, size, this.qty);
                }
                else if(this.product!=null && !(stockIn)){//reverse changes in case of exception thrown
                    this.product.setStaffQty(false,size,this.qty);
                }
                JOptionPane.showMessageDialog(null, "Error referencing Product object.nChanges not made.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }
    public boolean setPQS(boolean stockIn, Product product, int qty, String size){
        if(qty<=0){//quantity must be more than 0
            JOptionPane.showMessageDialog(null, "Quantity must be more than 0!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(product==null){//product must be set
            JOptionPane.showMessageDialog(null, "Product not set! Changes cannot be made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(product.getSizeIndex(size)==-1){
            JOptionPane.showMessageDialog(null, "Invalid size.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if (this.product!=null && this.size!=null && stockIn && this.qty>this.product.getQtySizes()[product.getSizeIndex(this.size)]){
            JOptionPane.showMessageDialog(null, "Not enough stock to undo the stock in of the old product!\nConsider making a new stock in record for the old product first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(!(stockIn) && qty>product.getStaffQty()[product.getSizeIndex(size)]){
            JOptionPane.showMessageDialog(null, "Not enough stock to stock out!\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{try{
            if(stockIn){
                if(this.product!=null){
                    this.product.updateProductQuantities(this.size,this.qty);
                    this.product.setStaffQty(false, this.size, qty);
                }
                this.product=product;
                this.size=size;
                product.updateProductQuantities(size,-(qty));
                product.setStaffQty(true, size, qty);
                this.qty=qty;
            }
            else if(!(stockIn)){//for use in stock outs
                if(this.product!=null){
                    this.product.setStaffQty(true,this.size,this.qty);
                }
                this.product=product;
                this.size=size;
                product.setStaffQty(true,size,qty);
                this.qty=qty;
            }
            return true;
            }catch (Exception e){
                if(this.product!=null && stockIn){ 
                    this.product.updateProductQuantities(this.size,-(this.qty));
                    this.product.setStaffQty(true, this.size, this.qty);
                }
                else if(this.product!=null && !(stockIn)){//reverse changes in case of exception thrown
                    this.product.setStaffQty(false, this.size, this.qty);
                }
                JOptionPane.showMessageDialog(null, "Error referencing Product object.\nChanges not made.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }
    //Setters
    public CDate getDate(){
        return date;  
    }
    public Product getProduct(){
        return product;
    }
    public int getQty(){
        return qty;
    }
    public String getSize(){
        return size;
    }
    public Product getStaffProduct(){
        return staffProduct;
    }
    //Getters
}