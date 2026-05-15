public class Item {
    private int codigo;
    private String nome;
    private String descricao;
    private TipoItem tipo;

    public Item(int codigo, String nome, String descricao, TipoItem tipo) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
    }
}
