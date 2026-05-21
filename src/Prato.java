import java.io.Serializable;

public class Prato extends Item implements Serializable {
    private TipoPrato tipoPrato;

    public Prato(int codigo, String nome, String descricao, double preco, TipoItem tipo, TipoPrato tipoPrato) {
        super(codigo, nome, descricao, preco, tipo);
        this.tipoPrato = tipoPrato;
    }
    
    public TipoPrato getTipoPrato() {
    	return tipoPrato;
    }
}
