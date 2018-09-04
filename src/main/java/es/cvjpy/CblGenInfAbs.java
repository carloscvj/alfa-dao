/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy;

import es.cvjpy.cobol.SesionCobol;

/**
 *
 * @author carlos
 * @param <COSA> Algún pro
 */
public abstract class CblGenInfAbs<COSA> extends GenAbs<COSA> implements GenInfPro<COSA> {

    protected abstract PrincipalCBLPro getPrincipalPro() throws Exception;

    protected SesionCobol getSesionCobol() throws Exception {
        return getPrincipalPro().getSesionCobol();
    }

}
