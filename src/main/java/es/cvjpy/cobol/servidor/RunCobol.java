package es.cvjpy.cobol.servidor;

import es.cvjpy.cobol.EntornoCobol;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunCobol implements java.io.Serializable {

    private String sep = ":";
    private String cobpath;
    private String basepath;
    private boolean debug;
    private String comandline;
    private Process proceso;
    private boolean iniciado = false;
    private BufferedReader errores;
    private boolean hayerror = false;
    private String lineadeerror;
    private static long van = 0;
    private long id;
    private BufferedWriter orden;
    private BufferedReader respuesta;
    private final EntornoCobol entorno;

    public RunCobol(EntornoCobol entorno) throws Exception {
        this.id = van++;
        this.entorno = entorno;
        establecepropis();

        decirln(entorno.getVersionAlfa().toString());

        decirln("creando:" + id + ", por Thread:" + Thread.currentThread().getId());
    }

    private void establecepropis() {
        String os = System.getProperty("os.name", "linux").toLowerCase();
        os = os.substring(0, 1);
        if (os.equals("w")) {
            sep = ";";
        }
        debug = entorno.getEntornoLocal().isCOBDEBUG();

        establececobpath();
    }

    private void establececobpath() {
        if (!entorno.getEntornoLocal().isMontado()) {
            basepath = entorno.getEntornoLocal().getBaseCOBPATH();
            montacobpath(basepath);
            entorno.getEntornoLocal().setCOBPATH(cobpath);
            entorno.getEntornoLocal().setMontado(true);
        }
    }

    private void montacobpath(String base) {
        if (cobpath == null) {
            cobpath = base;
        }
        File fpath = new File(base);
        if (fpath != null) {
            for (File unf : fpath.listFiles()) {
                if (unf.isDirectory()) {
                    cobpath = cobpath + sep + base + "/" + unf.getName();
                    montacobpath(base + "/" + unf.getName());
                }
            }
        }
    }

    private void decir(String esto) {
        if (debug) {
            System.out.print(esto);
        }
    }

    private void decirln(String esto) {
        if (debug) {
            System.out.println(esto);
        }
    }

    private String getComandline() {
        String ret = "";
        ret = ret + entorno.getEntornoLocal().getCOBRUN() + " ";
        ret = ret + entorno.getEntornoLocal().getCOBPROG();
        if (comandline != null) {
            ret = comandline;
        }
        return ret;
    }

    public void setComandline(String comandline) {
        this.comandline = comandline;
    }

    private void suma(Properties prop1, Properties prop2) {
        Enumeration<String> en = (Enumeration<String>) prop2.propertyNames();
        while (en.hasMoreElements()) {
            String key = en.nextElement();
            prop1.setProperty(key, prop2.getProperty(key));
        }
    }

    private String[] getEnv() {
        Properties prop = System.getProperties();
        prop = entorno.toProperties(prop);
        Object hay[] = prop.entrySet().toArray();
        String ret[] = new String[hay.length];
        for (int i = 0; i < hay.length; i++) {
            ret[i] = hay[i].toString();
        }
        return ret;
    }

    public void esperar() throws InterruptedException {
        proceso.waitFor();
    }

    private boolean hayerror() throws Exception {
        boolean ret = false;
        try {
            Thread.sleep(1000);

        } catch (InterruptedException ex) {
            Logger.getLogger(RunCobol.class.getName()).log(Level.SEVERE, null, ex);
        }
        ret = errores.ready();
        return ret;
    }

    private String recibeerror() throws Exception {
        String ret = "";
        String entra = "";
        while (hayerror()) {
            entra = errores.readLine();
            if (entra == null) {
                throw new Exception("El proceso de COBOL ha finalizado");
            }
            ret = ret + ";" + entra;
            Logger.getLogger(RunCobol.class.getName()).log(Level.SEVERE, entra);
        }
        return ret;
    }

    private void comprobandoerror() {
        Thread tr = new Thread() {
            @Override
            public void run() {
                while (iniciado) {
                    try {
                        if (hayerror()) {
                            lineadeerror = recibeerror();
                            hayerror = true;
                            break;

                        }
                    } catch (Exception ex) {
                        Logger.getLogger(RunCobol.class.getName()).log(Level.SEVERE, null, ex);

                        break;
                    }
                }

            }
        };
        tr.start();
    }

    public boolean isIniciado() {
        return iniciado;
    }

    private void ejecutaD() throws Exception {
        proceso = Runtime.getRuntime().exec(getComandline() + " " + "DISPLAYMODE" + "@" + this.id, getEnv());
        orden = new BufferedWriter(new OutputStreamWriter(proceso.getOutputStream(), entorno.getCharSet()));
        respuesta = new BufferedReader(new InputStreamReader(proceso.getInputStream(), entorno.getCharSet()));
    }

    public void ejecuta() throws Exception, InterruptedException {

        ejecutaD();
        errores = new BufferedReader(new InputStreamReader(proceso.getErrorStream()));
        if (hayerror()) {
            throw new Exception("Se ha producido un error en el proceso  de COBOL:" + getComandline() + ", Lineas de ERROR=" + recibeerror());
        }
        iniciado = true;
        comprobandoerror();
    }

    public synchronized void enviar(String mensaje) throws Exception {
        if (!isIniciado()) {
            ejecuta();
        }
        if (hayerror) {
            proceso.destroy();
            iniciado = false;
            throw new Exception("Se ha producido un error en el proceso  de COBOL:" + lineadeerror);
        }

        //Para que los LF NO lleguen al los ficheros de COBOL porque se joden.
        mensaje = mensaje.replace((char) 10, (char) 11);

        decir(">-(" + id + ")->toPro:" + mensaje);

        orden.write(mensaje);
        orden.newLine();
        orden.flush();        //Si tienes un error aquí, mira a ver la licencia del COBOL.

        if (mensaje.equals("STP|@|")) {

            cierraD();

            iniciado = false;
        }

    }

    private String recibirD() throws Exception {
        String entra;
        //long antes = new Date().getTime();
        //long ahora;
        //while (!respuesta.ready()) {
        //  ahora = new Date().getTime();
        //  if ((ahora - antes) > timeout) {
        //    throw new IOException("El proceso de COBOL no responde: ");
        //  }
        //}
        entra = respuesta.readLine();
        if (entra == null) {
            throw new Exception("El proceso de COBOL ha finalizado");
        }

        //Recuperar el LF que se cambió antes de grabar.
        entra = entra.replace(((char) 11), ((char) 10));

        return entra;
    }

    public synchronized String recibir() throws Exception {
        decirln("");
        if (hayerror) {
            proceso.destroy();
            iniciado = false;
            throw new Exception("Se ha producido un error en el proceso de COBOL:" + lineadeerror);
        }
        String entra;

        entra = recibirD();

        decirln("<-(" + id + ")-<to Sock:" + entra);
        if (!iniciado) {
            decirln("terminado:" + id);
        }
        return entra;
    }

    @Override
    @SuppressWarnings("FinalizeDeclaration")
    public void finalize() throws Throwable {
        if (proceso != null) {
            proceso.destroy();
        }
        super.finalize();
    }

    private void cierraD() throws Exception {
        //parece que ya estan cerrados.
        //errores.close();
        //respuesta.close();
        //orden.close();
    }
}
