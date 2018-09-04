/*
 * Key.java
 *
 * Created on 8 de marzo de 2007, 9:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.cvjpy.cobol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author CarlosVJ
 */
public class Key implements KeyCobol {

    private final String nombre;
    private final List<CampoCobol> campos = new ArrayList();

    /**
     * Creates a new instance of Key
     *
     * @param nombre
     */
    public Key(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public void add(CampoCobol uncampo) {
        campos.add(uncampo);
    }

    @Override
    public CampoCobol getCampo(String nombre) {
        Campo ret = null;
        Campo uno;
        Iterator iter = campos.iterator();
        while (iter.hasNext()) {
            uno = (Campo) iter.next();
            if (uno.getNombre().equals(nombre)) {
                ret = uno;
            }
        }
        return ret;
    }

    @Override
    public Iterator getCamposIterator() {
        return campos.iterator();
    }

    @Override
    public String getCobolText() {
        StringBuilder ret = new StringBuilder("");
        Campo uno;
        Iterator iter = campos.iterator();
        while (iter.hasNext()) {
            uno = (Campo) iter.next();
            ret.append(uno.getCobolText());
        }
        return ret.toString();
    }

    @Override
    public void setCobolText(String texto) {
        StringBuilder met = new StringBuilder(texto);
        int totlon = 0;
        Campo uno;
        Iterator iter = campos.iterator();
        while (iter.hasNext()) {
            uno = (Campo) iter.next();
            totlon += uno.getLongitud();
        }
        met.append(repetir(' ', totlon).toString());
        int van = 0;
        iter = campos.iterator();
        while (iter.hasNext()) {
            uno = (Campo) iter.next();
            uno.setCobolText(met.substring(van, van + uno.getLongitud()));
            van += uno.getLongitud();
        }
    }

    private StringBuffer repetir(char caracter, int veces) {
        StringBuffer ret = new StringBuffer("");
        for (int i = 0; i < veces; i++) {
            ret.append(caracter);
        }
        return ret;
    }

}
