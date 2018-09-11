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
public class PreferenciasAlfaEjb extends PreferenciasCBLAbs implements PreferenciasAlfaPro {

    private PrincipalCBLPro principalPro;

    public PreferenciasAlfaEjb() {
    }

    public PreferenciasAlfaEjb(PrincipalCBLPro principalPro) {
        this.principalPro = principalPro;
    }

    @Override
    protected PrincipalCBLPro getPrincipalPro() {
        return principalPro;
    }

}
