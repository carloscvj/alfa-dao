package es.cvjpy.cobol.servidor;

import es.cvjpy.cobol.EntornoCobol;
import es.cvjpy.cobol.ServidorConfiguracion;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SockJava extends Thread implements java.io.Serializable {

    private final int puerto;
    private final String ver;
    private final EntornoCobol entorno;

    public SockJava(ServidorConfiguracion sc) throws Exception {
        this.ver = "aplicación:" + sc.getVersionAlfa().getAplicacion() + ", versión:" + sc.getVersionAlfa().getVersion() + ", actualización:" + sc.getVersionAlfa().getActualizacion();
        this.entorno = sc.getEntornoCobol();
        this.puerto = sc.getEntornoCobol().getEntornoRemoto().getPuertoSys();
    }

    @Override
    public void run() {
        try {
            System.out.println("Servidor de objetos de JAVA " + ver + ", " + this.puerto);// + ", puerto=" + puerto);
            ServerSocket ss = new ServerSocket(puerto);
            while (true) {
                Socket s = ss.accept();
                synchronized (this) {
                    s.setTcpNoDelay(true);
                    ServiJava sc = new ServiJava(s, entorno);
                    sc.start();
                }
                System.gc();
            }
        } catch (IOException ex) {
            Logger.getLogger(SockJava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
