package es.cvjpy.cobol;

import es.cvjpy.cobol.servidor.RunCobol;
import java.io.Serializable;

public class ProcesoCobolRUN implements Serializable, ProcesoCobolPro {

    private RunCobol runCobol;
    private EntornoCobol entornoCobol;

    public ProcesoCobolRUN() {
    }

    @Override
    public EntornoCobol getEntornoCobol() {
        return entornoCobol;
    }

    @Override
    public void setEntornoCobol(EntornoCobol entornoCobol) {
        this.entornoCobol = entornoCobol;
    }

    @Override
    @SuppressWarnings("FinalizeDeclaration")
    public void finalize() throws Throwable {
        if (runCobol != null) {
            desconecta();
        }
        super.finalize();
    }

    @Override
    public synchronized void enviar(String aux) throws Exception {
        if (runCobol == null) {
            conecta();
        }
        runCobol.enviar(aux);
    }

    @Override
    public synchronized String recibir() throws Exception {
        String aux = runCobol.recibir();
        return aux;

    }

    private synchronized void conecta() throws Exception {
        if (runCobol == null) {
            runCobol = new RunCobol(getEntornoCobol());
            enviar("login" + ProcesoCobolPro.separar + getEntornoCobol().getUsuario());
            enviar("dd_dir" + ProcesoCobolPro.separar + getEntornoCobol().getDd_dir());
            enviar("dd_mnu" + ProcesoCobolPro.separar + getEntornoCobol().getDd_mnu());
            enviar("dd_tmp" + ProcesoCobolPro.separar + getEntornoCobol().getDd_tmp());
            enviar("dd_rom" + ProcesoCobolPro.separar + getEntornoCobol().getDd_rom());
            enviar("dd_obj" + ProcesoCobolPro.separar + getEntornoCobol().getDd_obj());
        }
    }

    @Override
    public synchronized void desconecta() throws Exception {
        if (runCobol != null) {
            enviar("STP|@|");
            String s = recibir(); //Porque en realidad es en recibir cuando se manda el mensaje.
            runCobol = null;
        }
    }

    @Override
    public void trazar(String traza) throws Exception {
        Trazador.traza(traza);
    }
}
