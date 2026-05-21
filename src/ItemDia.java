public class ItemDia {
    private Item item;
    private int stock;

    public ItemDia(Item item, int stock) {
        this.item = item;
        this.stock = stock;
    }

    public Item getItem() {
        return item;
    }

    public void decrementarStock(){
        stock = stock - 1;
    }

    public int getStock() {
        return stock;
    }
    
    public String toString() {
    	return "Item: " + item + " Stock: " + stock;
    }
}

