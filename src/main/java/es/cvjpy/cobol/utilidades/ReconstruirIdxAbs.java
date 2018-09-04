/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.cobol.utilidades;

import es.cvjpy.PrincipalCBLPro;
import es.cvjpy.cobol.SesionCobol;

/**
 *
 * @author carlos
 */
public abstract class ReconstruirIdxAbs implements ReconstruirIdxPro {

    public ReconstruirIdxAbs() {
    }
    protected FicheroCobolFiltro ficheroCobolFiltro;

    protected abstract PrincipalCBLPro getPrincipalPro() throws Exception;

    protected SesionCobol getSesionCobol() throws Exception {
        return getPrincipalPro().getSesionCobol();
    }

    @Override
    public FicheroCobolFiltro crearFicheroCobolFiltro() throws Exception {
        ficheroCobolFiltro = new FicheroCobolFiltro();
        return ficheroCobolFiltro;
    }

    @Override
    public void guardarFicheroCobolFiltro(FicheroCobolFiltro ficheroCobolFiltro) throws Exception {
    }

    @Override
    public void reiniciarSesionCobol() throws Exception {
        getSesionCobol().reiniciar();
    }

}
