/*
 * Ficherable.java
 *
 * Created on 6 de junio de 2007, 11:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.cvjpy.cobol;

/**
 *
 * @author CarlosVJ
 */
public interface Ficherable extends java.io.Serializable {

    public void setEnlace(ProcesoCobolPro enlace);

    public String getProgramaServidor();

    public String getNombreInterno();

    public void setNombreExterno(String nombreexterno) throws Exception;

    public String getNombreExterno();

    public boolean isInvalidkey();

    public String getErrores() throws Exception;

    public void setBloquear(boolean sino) throws Exception;

    public boolean isBloquear();

    public void OPEN_I_O() throws Exception;

    public void OPEN_OUTPUT() throws Exception;

    public boolean READ() throws Exception;

    public boolean READ_KEY_IS(KeyCobol key) throws Exception;

    public boolean WRITE() throws Exception;

    public boolean REWRITE() throws Exception;

    public boolean DELETE() throws Exception;

    public boolean START_MENOR() throws Exception;

    public boolean START_MENOR_KEY_IS(KeyCobol key) throws Exception;

    public boolean START_MAYOR() throws Exception;

    public boolean START_MAYOR_KEY_IS(KeyCobol key) throws Exception;

    public boolean START_NO_MENOR() throws Exception;

    public boolean START_NO_MENOR_KEY_IS(KeyCobol key) throws Exception;

    public boolean START_NO_MAYOR() throws Exception;

    public boolean START_NO_MAYOR_KEY_IS(KeyCobol key) throws Exception;

    public boolean NEXT() throws Exception;

    public boolean NEXT(String condicion) throws Exception;

    public boolean PREVIOUS() throws Exception;

    public boolean PREVIOUS(String condicion) throws Exception;

    public void CLOSE() throws Exception;

    public Registrable getRegistro();
}
