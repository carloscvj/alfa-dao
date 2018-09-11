/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.cobol.utilidades;

import es.cvjpy.PrincipalCBLPro;

/**
 *
 * @author carlos
 */
public class ReconstruirIdxAlfaEjb extends ReconstruirIdxAbs implements ReconstruirIdxAlfaPro {

    private PrincipalCBLPro principalPro;

    public ReconstruirIdxAlfaEjb() {
    }

    public ReconstruirIdxAlfaEjb(PrincipalCBLPro prog) {
        this.principalPro = prog;
    }

    @Override
    protected PrincipalCBLPro getPrincipalPro() {
        return principalPro;
    }

}
