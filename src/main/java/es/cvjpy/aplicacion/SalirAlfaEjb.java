/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.aplicacion;

import es.cvjpy.PrincipalCBLPro;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author carlos
 */
@Stateless
public class SalirAlfaEjb extends SalirCBLAbs implements SalirAlfaPro {

    @EJB
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
