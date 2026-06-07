import java.io.Serializable;
/**
 * @author Simão Gonçalves  53570
 * Classe item do dia
 */
public class ItemDia implements Serializable {
    private Item item;
    private int stock;

    public ItemDia(Item item, int stock) {
        this.item = item;
        this.stock = stock;
    }

    public Item getItem() {
        return item;
    }
    /**
     * @author Simão Gonçalves  53570
     * Decrementa o stock do item disponível na ementa do dia
     */
    public void decrementarStock(){
        stock = stock - 1;
    }

    public int getStock() {
        return stock;
    }
    
    public String toString() {
    	return item + " Stock: " + stock;
    }
}

