/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy;


/**
 *
 * @author carlos
 */
public class TextoPlanoAlfaEjb extends TextoPlanoCBLAbs implements TextoPlanoAlfaPro {

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
