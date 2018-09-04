package es.cvjpy.cobol.servidor;

import es.cvjpy.cobol.EntornoCobol;
import es.cvjpy.cobol.ProcesoJavaRUN;
import es.cvjpy.cobol.ProcesoJavaPro;
import es.cvjpy.cobol.MensajeJava;
import java.io.FileFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiJava extends Thread implements java.io.Serializable {

    private static int maxmensajes = Integer.parseInt(System.getProperty("cobolservidor.maxmensajes", "128"));
    private Socket socket;
    private ObjectInputStream objentrada;
    private ObjectOutputStream objsalida;
    private MensajeJava mensajeremoto;
    private MensajeJava mensajelocal;
    private int nmensajes = 0;
    private EntornoCobol entornoAqui;

    public ServiJava(Socket s, EntornoCobol entorno) throws IOException {
        this.socket = s;
        this.entornoAqui = entorno;
        this.inisok();
    }

    private void inisok() throws IOException {
        objentrada = new ObjectInputStream(socket.getInputStream());
        objsalida = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            rulando();
        } catch (Exception ex) {
            Logger.getLogger(ServiJava.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private synchronized void rulando() throws Exception {
        boolean salir = false;
        while (!salir) {
            mensajeremoto = (MensajeJava) objentrada.readObject();
            if (!mensajeremoto.getVersion().equals(entornoAqui.getVersionAlfa())) {
                mandarFinal();
                break;
            }
            if (mensajeremoto.isUltimo()) {
                mandarFinal();
                break;
            }
            aMandar();
        }
    }

    private void mandarFinal() throws Exception {
        mensajelocal = new MensajeJava();
        mensajelocal.setEntorno(entornoAqui);
        mensajelocal.setUltimo(true);
        enviar();
        if (!socket.isClosed()) {
            socket.close();
        }
    }

    private void mandarError() throws Exception {
        mensajelocal = new MensajeJava();
        mensajelocal.setEntorno(entornoAqui);
        mensajelocal.setUltimo(true);
        mensajelocal.addMensaje(new Exception("NO ENTIENDO"));
        enviar();
        if (!socket.isClosed()) {
            socket.close();
        }
    }

    private void enviar() throws Exception {
        objsalida.writeObject(mensajelocal);
        objsalida.flush();
        nmensajes++;
        if (nmensajes > maxmensajes) {
            objsalida.reset();
            nmensajes = 0;
        }
    }

    private void aMandar() throws Exception {
        Object orden = mensajeremoto.getComunicar().get(0);
        if (orden.equals("existe")) {
            String parametro = (String) mensajeremoto.getComunicar().get(1);
            ProcesoJavaPro javaLocal = new ProcesoJavaRUN();
            javaLocal.setEntornoCobol(mensajeremoto.getEntorno());
            mensajelocal = new MensajeJava();
            mensajelocal.setEntorno(entornoAqui);
            mensajelocal.addMensaje(javaLocal.existe(parametro));
            javaLocal.desconecta();
            enviar();
            return;
        }
        if (orden.equals("leerArchivo")) {
            String parametro = (String) mensajeremoto.getComunicar().get(1);
            ProcesoJavaPro javaLocal = new ProcesoJavaRUN();
            javaLocal.setEntornoCobol(mensajeremoto.getEntorno());
            mensajelocal = new MensajeJava();
            mensajelocal.setEntorno(entornoAqui);
            mensajelocal.addMensaje(javaLocal.leerArchivo(parametro.toString()));
            javaLocal.desconecta();
            enviar();
            return;
        }
        if (orden.equals("guardarArchivo")) {
            byte[] parametro1 = (byte[]) mensajeremoto.getComunicar().get(1);
            String parametro2 = (String) mensajeremoto.getComunicar().get(2);
            ProcesoJavaPro javaLocal = new ProcesoJavaRUN();
            javaLocal.setEntornoCobol(mensajeremoto.getEntorno());
            mensajelocal = new MensajeJava();
            mensajelocal.setEntorno(entornoAqui);
            javaLocal.guardarArchivo(parametro1, parametro2);
            javaLocal.desconecta();
            enviar();
            return;
        }
        if (orden.equals("directorioDe")) {
            String parametro1 = (String) mensajeremoto.getComunicar().get(1);
            FileFilter parametro2 = (FileFilter) mensajeremoto.getComunicar().get(2);
            ProcesoJavaPro javaLocal = new ProcesoJavaRUN();
            javaLocal.setEntornoCobol(mensajeremoto.getEntorno());
            mensajelocal = new MensajeJava();
            mensajelocal.setEntorno(entornoAqui);
            mensajelocal.addMensaje(javaLocal.directorioDe(parametro1, parametro2));
            javaLocal.desconecta();
            enviar();
            return;
        }
        if (orden.equals("borrarArchivo")) {
            String parametro1 = (String) mensajeremoto.getComunicar().get(1);
            ProcesoJavaPro javaLocal = new ProcesoJavaRUN();
            javaLocal.setEntornoCobol(mensajeremoto.getEntorno());
            mensajelocal = new MensajeJava();
            mensajelocal.setEntorno(entornoAqui);
            javaLocal.borrarArchivo(parametro1);
            javaLocal.desconecta();
            enviar();
            return;
        }
        if (orden.equals("toOLD")) {
            String parametro1 = (String) mensajeremoto.getComunicar().get(1);
            ProcesoJavaPro javaLocal = new ProcesoJavaRUN();
            javaLocal.setEntornoCobol(mensajeremoto.getEntorno());
            mensajelocal = new MensajeJava();
            mensajelocal.setEntorno(entornoAqui);
            javaLocal.toOLD(parametro1);
            javaLocal.desconecta();
            enviar();
            return;
        }

        mandarError(); // Como no te entiendo, corto y cierro!
    }
}
