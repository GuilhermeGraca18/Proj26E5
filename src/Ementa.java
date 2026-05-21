import java.time.LocalDate;
import java.util.ArrayList;

public class Ementa {
    private LocalDate data;
    private ArrayList<ItemDia> itemsDia;

    public Ementa(LocalDate data) {
        this.data = data;
        this.itemsDia = new ArrayList<>();
    }

    public LocalDate getData() {
        return data;
    }

    public ArrayList<ItemDia> getItemsDia() {
        return itemsDia;
    }
    
    public void setItemDia(ArrayList<ItemDia> itens) {
    	itemsDia = itens;
    }
    
    public String toString() {
    	return "Data: " + data + " Itens do Dia: " + itemsDia ;
    }
}
