/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.cobol;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CarlosVJ
 */
public class SesionCobol implements java.io.Serializable {

    private final EntornoCobol entornoCobol;
    private Map<String, Ficherable> usadosMant;
    private Map<String, Ficherable> usadosBusq;
    private Map<String, Ficherable> usadosList;
    private Map<String, Ficherable> usadosCons;
    private ProcesoCobolPro enlacemant;
    private ProcesoCobolPro enlacebusq;
    private ProcesoCobolPro enlacelist;
    private ProcesoCobolPro enlacecons;
    private List<ProcesoCobolPro> creados;

    public SesionCobol(EntornoCobol entornoCobol) {
        this.entornoCobol = entornoCobol;
    }

    private EntornoCobol getEntornoCobol() {
        return entornoCobol;
    }

    private Map<String, Ficherable> getUsadosMant() {
        if (usadosMant == null) {
            usadosMant = new HashMap();
        }
        return usadosMant;
    }

    private Map<String, Ficherable> getUsadosBusq() {
        if (usadosBusq == null) {
            usadosBusq = new HashMap();
        }
        return usadosBusq;
    }

    private Map<String, Ficherable> getUsadosList() {
        if (usadosList == null) {
            usadosList = new HashMap();
        }
        return usadosList;
    }

    private Map<String, Ficherable> getUsadosCons() {
        if (usadosCons == null) {
            usadosCons = new HashMap();
        }
        return usadosCons;
    }

    private List<ProcesoCobolPro> getCreados() {
        if (creados == null) {
            creados = new ArrayList();
        }
        return creados;
    }

    public Ficherable nuevoFichero(String classname) throws Exception {
        Ficherable ret;
        ret = (Ficherable) Class.forName(classname).newInstance();
        ret.setEnlace(creaEnlace());
        return ret;
    }

    public Ficherable getFicheroMantenimiento(String classname) throws Exception {
        Ficherable ret;
        if (getUsadosMant().containsKey(classname)) {
            ret = getUsadosMant().get(classname);
        } else {
            ret = (Ficherable) Class.forName(classname).newInstance();
            ret.setEnlace(getEnlaceMantenimientos());
            getUsadosMant().put(classname, ret);
        }
        return ret;
    }

    public Ficherable getFicheroBusquedas(String classname) throws Exception {
        Ficherable ret;
        if (getUsadosBusq().containsKey(classname)) {
            ret = getUsadosBusq().get(classname);
        } else {
            ret = (Ficherable) Class.forName(classname).newInstance();
            ret.setEnlace(getEnlaceBusquedas());
            getUsadosBusq().put(classname, ret);
        }
        return ret;
    }

    public Ficherable getFicheroListados(String classname) throws Exception {
        Ficherable ret;
        if (getUsadosList().containsKey(classname)) {
            ret = getUsadosList().get(classname);
        } else {
            ret = (Ficherable) Class.forName(classname).newInstance();
            ret.setEnlace(getEnlaceListados());
            getUsadosList().put(classname, ret);
        }
        return ret;
    }

    public Ficherable getFicheroConsultas(String classname) throws Exception {
        Ficherable ret;
        if (getUsadosCons().containsKey(classname)) {
            ret = getUsadosCons().get(classname);
        } else {
            ret = (Ficherable) Class.forName(classname).newInstance();
            ret.setEnlace(getEnlaceConsultas());
            getUsadosCons().put(classname, ret);
        }
        return ret;
    }

    public synchronized ProcesoCobolPro getEnlaceMantenimientos() throws Exception {
        if (enlacemant == null) {
            enlacemant = this.creaEnlace();
        }
        return enlacemant;
    }

    public synchronized ProcesoCobolPro getEnlaceBusquedas() throws Exception {
        if (enlacebusq == null) {
            enlacebusq = this.creaEnlace();
        }
        return enlacebusq;
    }

    public synchronized ProcesoCobolPro getEnlaceListados() throws Exception {
        if (enlacelist == null) {
            enlacelist = this.creaEnlace();
        }
        return enlacelist;
    }

    public synchronized ProcesoCobolPro getEnlaceConsultas() throws Exception {
        if (enlacecons == null) {
            enlacecons = this.creaEnlace();
        }
        return enlacecons;
    }

    public synchronized ProcesoCobolPro creaEnlace() throws Exception {
        ProcesoCobolPro enl;
        if (getEntornoCobol().isLocal()) {
            enl = new ProcesoCobolRUN();
        } else {
            enl = new ProcesoCobolCli();
        }

        enl.setEntornoCobol(getEntornoCobol());
        getCreados().add(enl);
        return enl;
    }

    public synchronized LlamarPrograma crearPruPar() throws Exception {
        LlamarPrograma lla = new LlamarPrograma();
        lla.setProcesoCobol(creaEnlace());
        return lla;
    }

    public synchronized void terminar() throws Exception {
        Logger.getLogger(SesionCobol.class.getName()).log(Level.INFO, "terminando procesos de COBOL...");
        if (enlacemant != null) {
            enlacemant.desconecta();
        }
        if (enlacebusq != null) {
            enlacebusq.desconecta();
        }
        if (enlacelist != null) {
            enlacelist.desconecta();
        }
        if (enlacecons != null) {
            enlacecons.desconecta();
        }
        if (creados != null) {
            for (ProcesoCobolPro enl : creados) {
                enl.desconecta();
            }
        }
        this.creados = null;
    }

    @Override
    @SuppressWarnings("FinalizeDeclaration")
    public void finalize() throws Exception, Throwable {
        this.terminar();
        super.finalize();
    }

    public synchronized Boolean existe(String nombre) throws Exception {
        ProcesoJavaPro runJava;
        if (getEntornoCobol().isLocal()) {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaRUN").newInstance();
        } else {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaCLI").newInstance();
        }
        runJava.setEntornoCobol(getEntornoCobol());
        Boolean ret = runJava.existe(nombre);
        runJava.desconecta();
        return ret;
    }

    public synchronized byte[] leerArchivo(String nombre) throws Exception {
        ProcesoJavaPro runJava;
        if (getEntornoCobol().isLocal()) {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaRUN").newInstance();
        } else {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaCLI").newInstance();
        }
        runJava.setEntornoCobol(getEntornoCobol());
        byte[] ret = runJava.leerArchivo(nombre);
        runJava.desconecta();
        return ret;
    }

    public synchronized void guardarArchivo(byte[] datos, String camino) throws Exception {
        ProcesoJavaPro runJava;
        if (getEntornoCobol().isLocal()) {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaRUN").newInstance();
        } else {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaCLI").newInstance();
        }
        runJava.setEntornoCobol(getEntornoCobol());
        runJava.guardarArchivo(datos, camino);
        runJava.desconecta(); //Los mÃ©todos void no hay que desconectar();
    }

    public synchronized List<String> directorioDe(String camino, FileFilter ff) throws Exception {
        ProcesoJavaPro runJava;
        if (getEntornoCobol().isLocal()) {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaRUN").newInstance();
        } else {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaCLI").newInstance();
        }
        runJava.setEntornoCobol(getEntornoCobol());
        List<String> ret = runJava.directorioDe(camino, ff);
        runJava.desconecta();
        return ret;
    }

    public synchronized void borrarArchivo(String nombre) throws Exception {
        ProcesoJavaPro runJava;
        if (getEntornoCobol().isLocal()) {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaRUN").newInstance();
        } else {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaCLI").newInstance();
        }
        runJava.setEntornoCobol(getEntornoCobol());
        runJava.borrarArchivo(nombre);
        runJava.desconecta(); //Los métodos void no hay que desconectar();
    }
    public synchronized void toOLD(String nombreExterno) throws Exception {
        ProcesoJavaPro runJava;
        if (getEntornoCobol().isLocal()) {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaRUN").newInstance();
        } else {
            runJava = (ProcesoJavaPro) Class.forName("es.cvjpy.cobol.ProcesoJavaCLI").newInstance();
        }
        runJava.setEntornoCobol(getEntornoCobol());
        runJava.toOLD(nombreExterno);
        runJava.desconecta(); //Los métodos void no hay que desconectar();
    }

    public LlamarPrograma crearLlamador() throws Exception {
        return crearPruPar();
    }

    public void reiniciar() throws Exception {
        terminar();
        usadosMant = null;
        usadosBusq = null;
        usadosList = null;
        usadosCons = null;
        enlacemant = null;
        enlacebusq = null;
        enlacelist = null;
        enlacecons = null;
        creados = null;        
    }

}
