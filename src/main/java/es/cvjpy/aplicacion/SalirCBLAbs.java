/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.aplicacion;

import es.cvjpy.PrincipalCBLPro;

/**
 *
 * @author carlos
 */
public abstract class SalirCBLAbs implements SalirPro {

    protected abstract PrincipalCBLPro getPrincipalPro() throws Exception;

    @Override
    public void salir() throws Exception {
        getPrincipalPro().getSesionCobol().terminar();
        getPrincipalPro().fin();
    }

}
