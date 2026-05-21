import java.io.Serializable;

public class Item implements Serializable {
    private int codigo;
    private String nome;
    private String descricao;
    private double preco;
    private TipoItem tipo;

    public Item(int codigo, String nome, String descricao, double preco, TipoItem tipo) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.tipo = tipo;
    }

    public double getPreco() {
        return preco;
    }

    public int getCodigo() {
        return codigo;
    }

    public TipoItem getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return nome + " (" + tipo + ")";
    }

    public String toStringDetalhado() {
        return "Código: " + codigo +
                "\nNome: " + nome +
                "\nDescrição: " + descricao +
                "\nPreço: " + preco + "€" +
                "\nTipo: " + tipo +
                "\n===========\n";
    }
}
