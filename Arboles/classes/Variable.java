package Arboles.classes;

import java.util.Objects;

/**
 *
 * @author Willy
 */
public class Variable {

    private String var;
    private int valor;

    public Variable() {
    }
    
    public Variable(String var){
        this.var = var;
    }
    
    public Variable(String var, int valor) {
        this.var = var;
        this.valor = valor;
    }

    public String getVar() {
        return var;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.var);
        hash = 83 * hash + this.valor;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Variable other = (Variable) obj;
        return Objects.equals(this.var, other.var);
    }
    
    

}
