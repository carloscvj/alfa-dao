package es.cvjpy.cobol;

import es.cvjpy.GenAbs;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import es.cvjpy.GenPro;

public final class LlamarPrograma extends GenAbs<String> implements GenPro<String>, Serializable {

    private ProcesoCobolPro enlace;
    private String nombreprograma;
    private List<String> parametros;

    public LlamarPrograma() {
    }

    public void setProcesoCobol(ProcesoCobolPro procesoCobolPro) {
        this.enlace = procesoCobolPro;
    }

    @Override
    public void procesar() {
        try {
            String mensaje = "";
            enlace.enviar("LLA|@|PRUPARJ|@|" + nombreprograma);
            for (String par : getParametros()) {
                enlace.enviar(par);
            }
            enlace.enviar("LLAMA@");
            while (!mensaje.equals("|FIN|")) {
                mensaje = enlace.recibir();
                if (mensaje.startsWith("ERROR:")) {
                    CobolException ce = new CobolException(mensaje);
                    throw ce;
                }
                if (!mensaje.equals("|FIN|")) {
                    getMensajes().add(mensaje);
                }
            }
        } catch (CobolException cex) {
            Logger.getLogger(LlamarPrograma.class.getName()).log(Level.SEVERE, null, cex);
        } catch (Exception ex) {
            Logger.getLogger(LlamarPrograma.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                enlace.desconecta();
            } catch (Exception ex) {
                Logger.getLogger(LlamarPrograma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws Throwable {
        enlace.desconecta();
        super.finalize();
    }

    public String getNombreprograma() {
        return nombreprograma;
    }

    public void setNombreprograma(String nombreprograma) {
        this.nombreprograma = nombreprograma;
    }

    public List<String> getParametros() {
        if (parametros == null) {
            parametros = new ArrayList();
        }
        return parametros;
    }

    public void setParametros(List<String> parametros) {
        this.parametros = parametros;
    }

    public void addParametro(String parametro) {
        getParametros().add(parametro);
    }

    @Override
    public void preparado() {
    }

    @Override
    public void remove() {
    }
}
