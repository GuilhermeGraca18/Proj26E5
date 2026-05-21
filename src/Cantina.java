import java.io.Serializable;
import java.util.ArrayList;

public class Cantina implements Serializable {
    public ArrayList<Ementa> ementas;
    public ArrayList<Item> items;

    public Cantina() {
        this.ementas = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public void registarItem(Item item){
        items.add(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
