import java.time.LocalDate;
import java.util.ArrayList;

public class Pedido {
    private Bebida bebida;
    private ArrayList<Item> items; // todo: verificar se tem prato, sobremesa e entrega e quantidades, por causa do preço
    private EstadoPedido estado;
    private LocalDate data;

    public Pedido(Bebida bebida, ArrayList<Item> items, LocalDate data) {
        this.bebida = bebida;
        this.items = items;
        this.estado = EstadoPedido.A_FAZER;
        this.data = data;
    }
}
