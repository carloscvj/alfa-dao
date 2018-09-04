package es.cvjpy.cobol.servidor;

import es.cvjpy.cobol.EntornoCobol;
import es.cvjpy.cobol.MensajeCobol;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiCobol extends Thread implements java.io.Serializable {

    private static int maxmensajes = Integer.parseInt(System.getProperty("cobolservidor.maxmensajes", "128"));
    private Socket sok;
    private RunCobol pro;
    private ObjectInputStream objentrada;
    private ObjectOutputStream objsalida;
    private MensajeCobol mensajeremoto;
    private MensajeCobol mensajelocal;
    private int nmensajes = 0;
    private EntornoCobol entorno;

    public ServiCobol(Socket s, EntornoCobol entorno) throws IOException {
        this.sok = s;
        this.entorno = entorno;
        this.inisok();
    }

    private void inisok() throws IOException {
        objentrada = new ObjectInputStream(sok.getInputStream());
        objsalida = new ObjectOutputStream(sok.getOutputStream());
    }

    private void inipro() throws Exception {
        pro = new RunCobol(entorno);
    }

    @Override
    public void run() {
        try {
            this.inicia();
        } catch (Exception ex) {
            Logger.getLogger(ServiCobol.class.getName()).log(Level.SEVERE, null, ex);
            this.termina();
        }

    }

    private synchronized void inicia() throws Exception  {
        boolean salir = false;
        while (!salir) {
            Object obj = null;

            try {
                obj = objentrada.readObject();
            } catch (EOFException ex) {
                //Logger.getLogger(ServiCobol.class.getName()).log(Level.SEVERE, null, ex);
                //No me gusta hacer esto, pero en este caso se atrapa la EOFException que queda controlada por el null de la variable obj.
            }

            if (obj == null) {
                break;
            }
            mensajeremoto = (MensajeCobol) obj;
            salir = mensajeremoto.isUltimo();
            if (mensajeremoto.getComunicar().size() > 0) {
                toPro();
            } else {
                fromPro();
            }
        }
    }

    private void termina() {
        if (pro != null) {
            try {
                pro.enviar("STP|@|");
            } catch (Exception ex) {
                Logger.getLogger(ServiCobol.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!sok.isClosed()) {
            try {
                sok.close();
            } catch (IOException ex) {
                Logger.getLogger(ServiCobol.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private synchronized void toPro() throws Exception, InterruptedException {
        if (pro == null) {
            inipro();
        }
        for (int i = 0; i < mensajeremoto.getComunicar().size(); i++) {
            String linea = mensajeremoto.getMensaje(i);
            if (!linea.equals("|FIN|")) {
                pro.enviar(linea);
            }
        }
        fromPro();
    }

    private synchronized void fromPro() throws Exception {
        if (mensajelocal != null) {
            mensajelocal.removeall();
        }
        mensajelocal = new MensajeCobol();
        mensajelocal.setVersion(entorno.getVersionAlfa());
        nmensajes++;
        if (nmensajes > maxmensajes) {
            nmensajes = 0;
            objsalida.reset();
            System.gc();
        }
        String recibe = null;
        recibe = pro.recibir();
        mensajelocal.addMensaje(recibe);
        if (recibe.trim().equals("|INI-MUCHOS|")) {
            do {
                recibe = pro.recibir();
                mensajelocal.addMensaje(recibe);
            } while (!recibe.trim().equals("|FIN-MUCHOS|"));
        }
        toSok();
    }

    private synchronized void toSok() throws IOException {
        objsalida.writeObject(mensajelocal);
        objsalida.flush();
        //RunCobol.decirln("MENSAJE ENVIADO AL CLIENTE");
    }

    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws Throwable {
        termina();
        super.finalize();
    }
}
