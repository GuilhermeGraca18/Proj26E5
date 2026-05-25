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

    public Item pesquisarItem(int codigoItem){
        for (Item item : items){
            if(item.getCodigo() == codigoItem){
                return item;
            }
        }
        return null;
    }

    public void eliminarItem(int codigoItem){
        Item item = pesquisarItem(codigoItem);
        items.remove(item);
    }

    public ArrayList<Ementa> getEmentas() {
        return ementas;
    }
}
