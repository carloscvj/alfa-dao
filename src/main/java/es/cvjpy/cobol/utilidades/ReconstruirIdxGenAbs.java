/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.cobol.utilidades;

import es.cvjpy.PrincipalCBLPro;
import es.cvjpy.GenAbs;
import es.cvjpy.cobol.LlamarPrograma;
import es.cvjpy.cobol.SesionCobol;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carlos
 */
public abstract class ReconstruirIdxGenAbs extends GenAbs<String> implements ReconstruirIdxGenPro {

    public ReconstruirIdxGenAbs() {
    }
    private FicheroCobolFiltro filtros;

    private FicheroCobolFiltro getFiltros() {
        return filtros;
    }

    protected abstract PrincipalCBLPro getPrincipalPro() throws Exception;

    protected SesionCobol getSesionCobol() throws Exception {
        return getPrincipalPro().getSesionCobol();
    }

    @Override
    protected void procesar() {

        try {
            getMensajes().add("----------------Iniciando el proceso----------------------");
            getMensajes().add("CREANDO: " + getFiltros().getNombreExterno() + "OLD");
            getMensajes().add("(Esto puede tardar varios minutos, dependiendo del tamaño del fichero de datos. Espere, por favor)");
            getSesionCobol().toOLD(getFiltros().getNombreExterno());
            LlamarPrograma mlla = getSesionCobol().crearPruPar();
            mlla.setNombreprograma(getFiltros().getProgramaT());
            mlla.addParametro(getFiltros().getNombreExterno());
            String men;
            while (mlla.hasNext()) {
                men = mlla.next();
                getMensajes().add(men);
            }

            getMensajes().add("----------------Proceso terminado-------------------------");
        } catch (Exception ex) {
            getMensajes().add("ERROR:" + ex);
            Logger.getLogger(ReconstruirIdxGenAbs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void guardarFiltros(FicheroCobolFiltro filtros) {
        this.filtros = filtros;
    }

}
