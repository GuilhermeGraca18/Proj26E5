import java.time.LocalDate;
import java.util.ArrayList;

public class Pedido {
    private Bebida bebida;
    private ArrayList<Item> items; // todo: verificar se tem prato, sobremesa e entrega e quantidades, por causa do preço
    private EstadoPedido estado;
    private String notas;
    private LocalDate data;

    public Pedido(Bebida bebida, String notas, LocalDate data) {
        this.bebida = bebida;
        this.items = new ArrayList<>();
        this.estado = EstadoPedido.A_FAZER;
        this.notas = notas;
        this.data = data;
    }
}
