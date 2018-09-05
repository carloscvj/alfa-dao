/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author carlos
 */
@Stateless
public class TextoPlanoAlfaEjb extends TextoPlanoCBLAbs implements TextoPlanoAlfaPro {

    @EJB
    private PrincipalCBLPro PrincipalPro;

    public TextoPlanoAlfaEjb() {
    }

    public TextoPlanoAlfaEjb(PrincipalCBLPro PrincipalPro) {
        this.PrincipalPro = PrincipalPro;
    }

    @Override
    protected PrincipalCBLPro getPrincipalPro() {
        return PrincipalPro;
    }

}
