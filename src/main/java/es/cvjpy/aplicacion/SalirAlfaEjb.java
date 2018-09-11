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
public class SalirAlfaEjb extends SalirCBLAbs implements SalirAlfaPro {

    private PrincipalCBLPro principalPro;

    public SalirAlfaEjb() {
    }

    public SalirAlfaEjb(PrincipalCBLPro principalPro) {
        this.principalPro = principalPro;
    }

    @Override
    protected PrincipalCBLPro getPrincipalPro() {
        return principalPro;
    }
}
