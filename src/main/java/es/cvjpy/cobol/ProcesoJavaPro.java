/*
 * Enlazable.java
 *
 * Created on 3 de julio de 2007, 11:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.cvjpy.cobol;

import java.io.FileFilter;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author CarlosVJ
 */
public interface ProcesoJavaPro extends Serializable {

    void setEntornoCobol(EntornoCobol entornoCobol);

    void desconecta() throws Exception;

    Boolean existe(String nombre) throws Exception;

    byte[] leerArchivo(String nombre) throws Exception;

    void guardarArchivo(byte[] datos, String camino) throws Exception;

    List<String> directorioDe(String camino, FileFilter ff) throws Exception;

    void borrarArchivo(String nombre) throws Exception;
    
    void toOLD(String nombreExterno) throws Exception;
}
