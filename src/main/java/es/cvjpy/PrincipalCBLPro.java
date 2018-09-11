/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy;

import es.cvjpy.cobol.SesionCobol;

/**
 *
 * @author carlos
 */
public interface PrincipalCBLPro extends PrincipalPro {

    SesionCobol getSesionCobol() throws Exception;

}
