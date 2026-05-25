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

    @Override
    public String toString() {
        return super.getNome() + " (" + super.getTipo() + " - " + tipoPrato + " ) - Preço: " + super.getPreco() + "€";
    }

    public String toStringLista() {
        return "CODIGO #" + super.getCodigo() + "| " + super.getNome() + " (" + super.getTipo() + " - " + tipoPrato + ")";
    }

    public String toStringDetalhado() {
        return "Código: " + super.getCodigo() +
                "\nNome: " + super.getNome() +
                "\nDescrição: " + super.getDescricao() +
                "\nPreço: " + super.getPreco() + "€" +
                "\nTipo: " + super.getTipo() + " ( " + tipoPrato + " )" +
                "\n===========\n";
    }
}
