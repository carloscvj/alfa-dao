/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.cobol;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author carlos
 */
public class Trazador {

    private static Map<String, Logger> misLogs = new HashMap();

    @SuppressWarnings("LoggerStringConcat")
    public static void traza(String traza) throws Exception {
        List<String> parte = trocear(traza);

        String path = parte.get(0);                                      //parte[0] debe ser el valor de ${dd_trz} por ejemplo dd_trz=/ALFA/TRAZA
        String usuario = parte.get(1);                                   //parte[1] debe ser el user por ejemplo el c�digo de inspector.
        String nombreExterno = parte.get(2);                             //parte[2] debe ser dir/${SUBDIR}/NOMBRE, por ejemplo "dir/IT/FIT08"
        if (nombreExterno.startsWith("dir")) {
            nombreExterno = nombreExterno.replaceFirst("dir", path);     //Reemplaza /ALFA/FILES por /ALFA/TRAZA
        } else {
            nombreExterno = path + "/" + nombreExterno;                  //Si es un fichero con nombre extra�o hace lo que puede. Bueno, realmente, le mete parte[0] por delante
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fecha = sdf.format(new Date());
        int pos = nombreExterno.lastIndexOf("/");
        String pathName = nombreExterno.substring(0, pos);
        String baseName = nombreExterno.substring(pos + 1);
        new File(pathName).mkdirs();
        nombreExterno = pathName + "/" + fecha + baseName;

        getCual(nombreExterno).info(usuario + ":" + parte.get(3));    //parte[3] debe ser todo lo que hay que trazar

    }

    private static List<String> trocear(String traza) {
        List<String> ret = new ArrayList();
        String queda = traza;
        int pos = queda.indexOf(ProcesoCobolPro.separar);
        while (pos > -1) {
            String va = queda.substring(0, pos);
            ret.add(va);
            queda = queda.substring(pos + 3);
            pos = queda.indexOf(ProcesoCobolPro.separar);
        }
        ret.add(queda);
        return ret;
    }

    private static Logger getCual(String nombre) throws IOException {
        Logger log = null;
        if (!misLogs.containsKey(nombre)) {
            TrazaHandler trah = new TrazaHandler(nombre);
            //trah.setLevel(Level.ALL);
            Logger.getLogger(nombre).addHandler(trah);
            log = Logger.getLogger(nombre);
            misLogs.put(nombre, log);
        }
        log = misLogs.get(nombre);
        return log;
    }
}
