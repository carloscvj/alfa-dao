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
public class ReconstruirIdxGenAlfaEjb extends ReconstruirIdxGenAbs implements ReconstruirIdxGenAlfaPro {

    private PrincipalCBLPro principalPro;

    public ReconstruirIdxGenAlfaEjb() {
    }

    public ReconstruirIdxGenAlfaEjb(PrincipalCBLPro prog) {
        this.principalPro = prog;
    }

    @Override
    protected PrincipalCBLPro getPrincipalPro() {
        return principalPro;
    }

}
