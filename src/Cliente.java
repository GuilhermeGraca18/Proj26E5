import java.util.ArrayList;

public class Cliente extends Utilizador {
    ArrayList<Pedido> pedidos;

    public Cliente(int codigo, String nome, String senha) {
        super(codigo, nome, senha);
        this.pedidos = new ArrayList<>();
    }
}
