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
public abstract class PreferenciasCBLAbs implements PreferenciasPro {

    protected abstract PrincipalCBLPro getPrincipalPro() throws Exception;

    @Override
    public Preferencias getPreferencias() throws Exception {
        return getPrincipalPro().getPreferencias();
    }

    @Override
    public void guardarPreferencias(Preferencias configuracion) throws Exception {
        getPrincipalPro().guardarPreferencias(configuracion);
    }

}
