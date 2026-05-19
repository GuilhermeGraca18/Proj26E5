public abstract class Utilizador {
    private final int codigo;
    private String nome;
    private String senha;

    public Utilizador(int codigo, String nome, String senha){
        this.codigo = codigo;
        this.nome = nome;
        this.senha = senha;
    }

    public int getCodigo() {
        return codigo;
    }

    public abstract void adicionarPedido(Pedido pedido);
}
