/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author carlos
 */
public abstract class TextoPlanoCBLAbs extends CblGenInfAbs<Object> implements TextoPlanoPro {

    private String nombreFichero;

    @Override
    protected void procesar() {
        try {
            getMensajes().add("DEBUG:-------------------INICIANDO----------------------------");
            byte[] leerArchivo = getSesionCobol().leerArchivo(nombreFichero.trim());
            ByteArrayInputStream bais = new ByteArrayInputStream(leerArchivo);
            BufferedReader br = new BufferedReader(new InputStreamReader(bais));
            String lineaCobol;
            while ((lineaCobol = br.readLine()) != null) {
                getMensajes().add("DEBUG:" + lineaCobol);
                TextoPlanoLinea linea = new TextoPlanoLinea(lineaCobol);
                getMensajes().add(linea);
            }
            getMensajes().add("DEBUG:-------------------TERMINADO-----------------------------");
        } catch (Exception ex) {
            getMensajes().add("ERROR:" + ex);
        }
    }

    @Override
    public void setNombreFicheroCobol(String este) {
        this.nombreFichero = este;
    }

    @Override
    public void preparado() {
        super.preparado();
    }
}

