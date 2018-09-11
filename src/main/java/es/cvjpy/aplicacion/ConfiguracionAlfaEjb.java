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
public class ConfiguracionAlfaEjb extends ConfiguracionCBLAbs implements ConfiguracionAlfaPro {

    private PrincipalCBLPro principalPro;

    public ConfiguracionAlfaEjb() {
    }

    public ConfiguracionAlfaEjb(PrincipalCBLPro principalPro) {
        this.principalPro = principalPro;
    }

    @Override
    protected PrincipalCBLPro getPrincipalPro() {
        return principalPro;
    }

}
