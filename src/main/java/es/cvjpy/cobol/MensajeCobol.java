/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.cobol;

import es.cvjpy.aplicacion.VersionAlfa;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CarlosVJ
 */
public class MensajeCobol implements java.io.Serializable {

    private List<String> comunicar;
    private List<String> trazar;
    private boolean ultimo = false;
    private VersionAlfa version;

    public MensajeCobol() {
        this.comunicar = new ArrayList<String>();
        this.trazar = new ArrayList<String>();
    }

    public void addMensaje(String linea) {
        comunicar.add(linea);
    }

    public void addTraza(String traza) {
        trazar.add(traza);
    }

    public String getMensaje(int ind) {
        String ret = comunicar.get(ind);
        return ret;
    }

    public String getTraza(int ind) {
        String ret = trazar.get(ind);
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

    public List<String> getComunicar() {
        return comunicar;
    }

    public List<String> getTrazar() {
        return trazar;
    }

    public VersionAlfa getVersion() {
        return version;
    }

    public void setVersion(VersionAlfa version) {
        this.version = version;
    }
}
