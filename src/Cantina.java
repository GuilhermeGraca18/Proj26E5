import java.util.ArrayList;

public class Cantina {
    private ArrayList<Ementa> ementas;
    private ArrayList<Item> items;
    private ArrayList<Bebida> bebidas;

    public Cantina(ArrayList<Ementa> ementas, ArrayList<Item> items, ArrayList<Bebida> bebidas) {
        this.ementas = ementas;
        this.items = items;
        this.bebidas = bebidas;
    }
}
