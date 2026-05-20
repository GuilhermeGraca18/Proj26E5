import java.time.LocalDate;
import java.util.ArrayList;

public class Ementa {
    private LocalDate data;
    private ArrayList<ItemDia> itemsDia;

    public Ementa(LocalDate data) {
        this.data = data;
        this.itemsDia = new ArrayList<>();
    }
}
