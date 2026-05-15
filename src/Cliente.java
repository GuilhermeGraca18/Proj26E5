import java.util.ArrayList;

public class Cliente extends Utilizador {
    ArrayList<Pedido> pedidos;

    public Cliente(int codigo, String nome, String senha, ArrayList<Pedido> pedidos) {
        super(codigo, nome, senha);
        this.pedidos = pedidos;
    }
}
