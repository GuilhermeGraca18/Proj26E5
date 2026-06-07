import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Ementa implements Serializable {
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
    /**
     * @author Simão Gonçalves - 53570
     * Pesquisa um item na ementa pelo seu código
     */
    public Item pesquisarItem(int codigoItem){
        for (ItemDia itemdia : itemsDia){
            if (itemdia.getItem().getCodigo() == codigoItem){
                return itemdia.getItem();
            }
        }
        return null;
    }
    /**
     * @author Simão Gonçalves - 53570
     * Adiciona um item à ementa do dia com o stock definido
     */
    public void adicionarItemDia(Item item, int stock){
        itemsDia.add(new ItemDia(item, stock));
    }

    @Override
    public String toString(){
        String listaItems = "";

        for (ItemDia itemdia : itemsDia) {
            listaItems += "\n - " + itemdia;
        }

        return "EMENTA DIA: " + data +
                "\n Items: " + listaItems +
                "\n===========";
    }
}
