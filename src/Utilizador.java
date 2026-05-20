import java.io.Serializable;

public class Utilizador implements Serializable {
    private final int codigo;
    private String nome;
    private String senha;
    private TipoUtilizador tipo;

    public Utilizador(int codigo, String nome, String senha, TipoUtilizador tipo){
        this.codigo = codigo;
        this.nome = nome;
        this.senha = senha;
        this.tipo = tipo;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public TipoUtilizador getTipo() {
        return tipo;
    }
}
