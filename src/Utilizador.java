import java.io.Serializable;

public class Utilizador implements Serializable {
    private final int codigo;
    private String nome;
    private String senha; // todo: hash
    private TipoUtilizador tipo;
    private String fotoPerfil;

    public Utilizador(int codigo, String nome, String senha, TipoUtilizador tipo){
        this.codigo = codigo;
        this.nome = nome;
        this.senha = senha;
        this.tipo = tipo;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNome() {
        return nome;
    }

    public TipoUtilizador getTipo() {
        return tipo;
    }

    public String getSenha() {
        return senha;
    }
    
    public String toString() {
    	return "\nNome: " + nome + " Código: " + codigo;
    }
}
