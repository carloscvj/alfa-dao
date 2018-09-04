/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.cobol;

/**
 *
 * @author carlos
 */
public interface ProcesoCobolPro {

    static public final String separar = "|@|";

    void desconecta() throws Exception;

    void enviar(String env) throws Exception;

    String recibir() throws Exception;

    EntornoCobol getEntornoCobol();

    void setEntornoCobol(EntornoCobol entornoCobol);

    void trazar(String traza) throws Exception;

}
