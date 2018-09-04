package es.cvjpy.cobol;

import java.io.FileFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ProcesoJavaCLI implements ProcesoJavaPro {

    private static final int maxmensajes = 128;
    private Socket socket;
    private MensajeJava mensajeremoto;
    private MensajeJava mensajelocal;
    private transient ObjectInputStream objentrada;
    private transient ObjectOutputStream objsalida;
    private int nmensajes = 0;
    private EntornoCobol entorno;

    public ProcesoJavaCLI() {
    }

    private synchronized void enviar() throws Exception {
        getSalida().writeObject(getMensajeLocal());
        getSalida().flush();
        setNmensajes(getNmensajes() + 1);
        if (getNmensajes() > maxmensajes) {
            setNmensajes(1);
            objsalida.reset();
        }
        mensajelocal = null;
    }

    private synchronized MensajeJava recibir() throws Exception {
        enviar(); //Pa poder recibir primero hay que enviar.
        mensajeremoto = (MensajeJava) getEntrada().readObject();
        if (!mensajeremoto.getVersion().equals(entorno.getVersionAlfa())) {
            throw new Exception("Versiones incompatibles");
        }
        return mensajeremoto;
    }

    private int getNmensajes() {
        return nmensajes;
    }

    private void setNmensajes(int nmensajes) {
        this.nmensajes = nmensajes;
    }

    @Override
    public void setEntornoCobol(EntornoCobol entornoCobol) {
        this.entorno = entornoCobol;
    }

    @Override
    public void desconecta() throws Exception {
        MensajeJava local = getMensajeLocal();
        local.setUltimo(true);
        MensajeJava remoto = recibir();
        socket.close();
    }

    @Override
    public Boolean existe(String nombre) throws Exception {
        MensajeJava local = getMensajeLocal();
        local.addMensaje("existe");
        local.addMensaje(nombre);
        MensajeJava remoto = recibir();
        Boolean ret = (Boolean) remoto.getMensaje(0);
        return ret;
    }

    @Override
    public byte[] leerArchivo(String nombre) throws Exception {
        MensajeJava local = getMensajeLocal();
        local.addMensaje("leerArchivo");
        local.addMensaje(nombre);
        MensajeJava remoto = recibir();
        byte[] ret = (byte[]) remoto.getMensaje(0);
        return ret;

    }

    @Override
    public void guardarArchivo(byte[] datos, String camino) throws Exception {
        MensajeJava local = getMensajeLocal();
        local.addMensaje("guardarArchivo");
        local.addMensaje(datos);
        local.addMensaje(camino);
        MensajeJava remoto = recibir(); //Pa que realmente lo mande.
    }

    @Override
    public List<String> directorioDe(String camino, FileFilter ff) throws Exception {
        MensajeJava local = getMensajeLocal();
        local.addMensaje("directorioDe");
        local.addMensaje(camino);
        local.addMensaje(ff);
        MensajeJava remoto = recibir();
        List<String> ret = (List<String>) remoto.getMensaje(0);
        return ret;
    }

    @Override
    public void borrarArchivo(String nombre) throws Exception {
        MensajeJava local = getMensajeLocal();
        local.addMensaje("borrarArchivo");
        local.addMensaje(nombre);
        MensajeJava remoto = recibir(); //Pa que realmente lo mande.
    }

    private MensajeJava getMensajeLocal() {
        if (mensajelocal == null) {
            mensajelocal = new MensajeJava();
            mensajelocal.setEntorno(entorno);
        }
        return mensajelocal;
    }

    private ObjectInputStream getEntrada() throws Exception {
        if (objentrada == null) {
            objentrada = new ObjectInputStream(getSocket().getInputStream());
        }
        return objentrada;
    }

    private ObjectOutputStream getSalida() throws Exception {
        if (objsalida == null) {
            objsalida = new ObjectOutputStream(getSocket().getOutputStream());
        }
        return objsalida;
    }

    private Socket getSocket() throws Exception {
        if (socket == null) {
            socket = new Socket(entorno.getEntornoRemoto().getHost(), entorno.getEntornoRemoto().getPuertoSys());
            socket.setTcpNoDelay(true);
        }
        return socket;
    }

    @Override
    public void toOLD(String nombreExterno) throws Exception {
        MensajeJava local = getMensajeLocal();
        local.addMensaje("toOLD");
        local.addMensaje(nombreExterno);
        MensajeJava remoto = recibir(); //Pa que realmente lo mande.
    }
}
