import java.time.LocalDate;
import java.util.ArrayList;

public class Ementa {
    private LocalDate data;
    private ArrayList<Item> items;

    public Ementa(LocalDate data, ArrayList<Item> items) {
        this.data = data;
        this.items = items;
    }
}
