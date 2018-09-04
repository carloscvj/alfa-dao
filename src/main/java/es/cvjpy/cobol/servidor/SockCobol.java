package es.cvjpy.cobol.servidor;

import es.cvjpy.cobol.EntornoCobol;
import es.cvjpy.cobol.ServidorConfiguracion;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SockCobol extends Thread implements java.io.Serializable {

    private int puerto;
    private String ver;
    private EntornoCobol entorno;

    public SockCobol(ServidorConfiguracion sc) throws Exception {
        this.ver = "aplicación:" + sc.getVersionAlfa().getAplicacion() + ", versión:" + sc.getVersionAlfa().getVersion() + ", actualización:" + sc.getVersionAlfa().getActualizacion();
        this.entorno = sc.getEntornoCobol();
        this.puerto = sc.getEntornoCobol().getEntornoRemoto().getPuertoCob();
    }

    @Override
    public void run() {
        try {
            System.out.println("Servidor de datos de COBOL " + ver + ", " + this.puerto);// + ", puerto=" + puerto);
            ServerSocket ss = new ServerSocket(puerto);
            while (true) {
                Socket s = ss.accept();
                synchronized (this) {
                    s.setTcpNoDelay(true);
                    ServiCobol sc = new ServiCobol(s, entorno);
                    sc.start();
                }
                System.gc();
            }
        } catch (IOException ex) {
            Logger.getLogger(SockCobol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
