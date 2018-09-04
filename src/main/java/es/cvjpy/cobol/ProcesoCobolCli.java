package es.cvjpy.cobol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ProcesoCobolCli implements Serializable, ProcesoCobolPro {

    private ProcesoCobolRemoto remoto;
    private EntornoCobol entornoCobol;

    public ProcesoCobolCli() {
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
        if (remoto != null) {
            desconecta();
        }
        super.finalize();
    }

    @Override
    public synchronized void enviar(String aux) throws Exception {
        if (remoto == null) {
            conecta();
        }
        remoto.enviar(aux);
    }

    @Override
    public synchronized String recibir() throws Exception {
        String aux = remoto.recibir();
        return aux;

    }

    private synchronized void conecta() throws Exception {
        if (remoto == null) {
            remoto = new ProcesoCobolRemoto(getEntornoCobol());
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
        if (remoto != null) {
            enviar("STP|@|");
            String s = recibir(); //Porque en realidad es en recibir cuando se manda el mensaje.
            remoto = null;
        }
    }

    @Override
    public void trazar(String traza) throws Exception {
        remoto.trazando(traza);
    }

    private class ProcesoCobolRemoto {

        private final Socket sok;
        private final ObjectOutputStream objsalida;
        private final ObjectInputStream objentrada;
        private MensajeCobol mensajelocal;
        private final EntornoCobol entorno;
        private int nmensajes;
        private MensajeCobol mensajeremoto;

        public ProcesoCobolRemoto(EntornoCobol entorno) throws IOException {
            this.entorno = entorno;
            sok = new Socket(entorno.getEntornoRemoto().getHost(), entorno.getEntornoRemoto().getPuertoCob());
            sok.setTcpNoDelay(true);
            objsalida = new ObjectOutputStream(sok.getOutputStream());
            objentrada = new ObjectInputStream(sok.getInputStream());
        }

        private synchronized void recibiendo() throws Exception {
            if (mensajelocal == null) {
                mensajelocal = new MensajeCobol(); //Si quiere recibir sin enviar es que viene por el PRUPARJ
                mensajelocal.setVersion(entorno.getVersionAlfa());
            }
            objsalida.writeObject(mensajelocal);
            objsalida.flush();
            if (mensajelocal != null) {
                mensajelocal.removeall();
            }
            mensajelocal = null;
            mensajeremoto = (MensajeCobol) objentrada.readObject();

            if (!mensajeremoto.getVersion().equals(entorno.getVersionAlfa())) {
                throw new Exception("Versiones incompatibles");
            }
        }

        public synchronized void enviar(String aux) throws Exception {
            if (mensajelocal == null) {
                this.mensajelocal = new MensajeCobol();
                this.mensajelocal.setVersion(entorno.getVersionAlfa());
                nmensajes++;
                if (nmensajes > 128) {
                    nmensajes = 1;
                    objsalida.reset();
                }
            }
            mensajelocal.addMensaje(aux);
        }

        public synchronized String recibir() throws Exception {
            String aux;
            if (mensajeremoto == null || mensajeremoto.getComunicar().isEmpty()) {
                recibiendo();
            }

            aux = mensajeremoto.getMensaje(0);
            mensajeremoto.getComunicar().remove(aux);
            return aux;
        }

        public synchronized void trazando(String traza) throws Exception {
            if (mensajelocal == null) {
                this.mensajelocal = new MensajeCobol();
                this.mensajelocal.setVersion(entorno.getVersionAlfa());
                nmensajes++;
                if (nmensajes > 128) {
                    nmensajes = 1;
                    objsalida.reset();
                }
            }
            mensajelocal.addTraza(traza);
        }

    }

}
