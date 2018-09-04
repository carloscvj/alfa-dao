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
public abstract class ConfiguracionCBLAbs implements ConfiguracionPro {

    protected abstract PrincipalCBLPro getPrincipalPro() throws Exception;

    @Override
    public Configuracion getConfiguracion() throws Exception {
        return getPrincipalPro().getConfiguracion();
    }

    @Override
    public void guardarConfiguracion(Configuracion configuracion) throws Exception {
        getPrincipalPro().guardarConfiguracion(configuracion);
    }

}
