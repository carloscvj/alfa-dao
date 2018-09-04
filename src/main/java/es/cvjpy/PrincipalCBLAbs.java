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
public abstract class PrincipalCBLAbs extends PrincipalAbs implements PrincipalCBLPro {

    private SesionCobol sesionCobol;

    private SesionCobol crearSesionCobol() throws Exception {
        return new SesionCobol(getConfiguracion().getEntornoCobol());
    }

    @Override
    public SesionCobol getSesionCobol() throws Exception {
        if (sesionCobol == null) {
            sesionCobol = crearSesionCobol();
        }
        return sesionCobol;
    }

    @Override
    public void fin() {
        sesionCobol = null;
        super.fin();
    }
}
