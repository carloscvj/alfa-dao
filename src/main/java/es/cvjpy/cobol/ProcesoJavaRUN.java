package es.cvjpy.cobol;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProcesoJavaRUN implements ProcesoJavaPro {

    private EntornoCobol entorno;

    public ProcesoJavaRUN() {
    }

    private String nombreReal(String nombre) {
        if (nombre.length() > 2) {
            String varCobol = nombre.substring(0, 3);
            if (varCobol.equals("dir")) {
                nombre = entorno.getDd_dir() + nombre.substring(3);
            }
            if (varCobol.equals("mnu")) {
                nombre = entorno.getDd_mnu() + nombre.substring(3);
            }
            if (varCobol.equals("trz")) {
                nombre = entorno.getDd_trz() + nombre.substring(3);
            }
            if (varCobol.equals("tmp")) {
                nombre = entorno.getDd_tmp() + nombre.substring(3);
            }
            if (varCobol.equals("rom")) {
                nombre = entorno.getDd_rom() + nombre.substring(3);
            }
            if (varCobol.equals("obj")) {
                nombre = entorno.getDd_obj() + nombre.substring(3);
            }
        }
        return nombre;
    }

    @Override
    public void setEntornoCobol(EntornoCobol entornoCobol) {
        this.entorno = entornoCobol;
    }

    @Override
    public void desconecta() throws Exception {
        entorno = null;
    }

    @Override
    public byte[] leerArchivo(String nombre) throws Exception {
        byte[] ret = null;
        if (existe(nombreReal(nombre))) {
            File f = new File(nombreReal(nombre));
            FileInputStream fis = new FileInputStream(f);
            byte[] bufer = new byte[(int) f.length()];
            fis.read(bufer);
            fis.close();
            ret = bufer;
        }
        return ret;
    }

    @Override
    public Boolean existe(String nombre) {
        Boolean ret;
        File file = new File(nombreReal(nombre));
        ret = file.exists();
        return ret;
    }

    @Override
    public void guardarArchivo(byte[] datos, String camino) throws Exception {
        File f = new File(nombreReal(camino));
        f.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(datos);
        fos.close();
    }

    @Override
    public List<String> directorioDe(String camino, FileFilter ff) throws Exception {
        List<String> ret = new ArrayList();
        if (ff == null) {
            ff = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return true;
                }
            };
        }
        String real = nombreReal(camino);
        File carpeta = new File(real);
        File[] tiene = carpeta.listFiles(ff);
        if (tiene != null) {
            for (int i = 0; i < tiene.length; i++) {
                String uno = camino + "/" + tiene[i].getName();
                ret.add(uno);
            }
        }
        return ret;
    }

    @Override
    public void borrarArchivo(String nombre) throws Exception {
        File f = new File(nombreReal(nombre));
        f.delete();
    }

    @Override
    public void toOLD(String nombreExterno) throws Exception {
        String nombreOld = nombreReal(nombreExterno) + "OLD";
        File f = new File(nombreOld);
        if (f.exists()) {
            throw new Exception("ATENCION:YA EXISTE UN " + nombreOld);
        }
        String nombreDat = nombreReal(nombreExterno);
        FileInputStream fis = new FileInputStream(nombreDat);
        FileOutputStream fos = new FileOutputStream(nombreOld);
        int leido;
        while ((leido = fis.read()) > -1) {
            if (leido > 0) {
                fos.write(leido);
            }
        }
        fis.close();
        fos.close();
    }
}
