/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cvjpy.cobol;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author carlos
 */
public class TrazaHandler extends FileHandler {

    private MiFormato mf;

    public TrazaHandler(String pattern) throws IOException, SecurityException {
        super(pattern, true);
        mf = new MiFormato();
        setFormatter(mf);
    }

    @Override
    public void publish(LogRecord logRecord) {
        // Must filter our own logRecords, (lame) Abstract Handler does not do it for us.
        /*if (!getFilter().isLoggable(logRecord)) {
        return;
        }*/
        super.publish(logRecord);
    }

    private class MiFormato extends Formatter {

        @Override
        public String format(LogRecord record) {
            String ret = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String fecha = sdf.format(record.getMillis());
            ret = fecha + " - " + record.getMessage() + "\n";
            return ret;
        }
    }
}
