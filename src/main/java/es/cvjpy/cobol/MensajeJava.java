/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.cobol;

import es.cvjpy.aplicacion.VersionAlfa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CarlosVJ
 */
public class MensajeJava implements Serializable {

    private EntornoCobol entorno;
    private List<Object> comunicar;
    private boolean ultimo = false;

    public MensajeJava() {
        this.comunicar = new ArrayList();
    }

    public void addMensaje(Object linea) {
        comunicar.add(linea);
    }

    public Object getMensaje(int ind) {
        Object ret = comunicar.get(ind);
        return ret;
    }

    public boolean isUltimo() {
        return ultimo;
    }

    public void setUltimo(boolean ultimo) {
        this.ultimo = ultimo;
    }

    public void removeall() {
        comunicar = null;
    }

    public List<Object> getComunicar() {
        return comunicar;
    }

    public VersionAlfa getVersion() {
        return getEntorno().getVersionAlfa();
    }

    public EntornoCobol getEntorno() {
        return entorno;
    }

    public void setEntorno(EntornoCobol entorno) {
        this.entorno = entorno;
    }
}
