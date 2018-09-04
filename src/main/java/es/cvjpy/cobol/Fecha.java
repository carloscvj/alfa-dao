package es.cvjpy.cobol;

import java.text.*;
import java.util.*;

public class Fecha extends Campo {

    private Date midate = new Date();

    public Fecha(Registro registro, String nombre, int inicio) {
        super(registro, nombre, inicio, 8);
    }

    void loadFromRegistro() {
        Date mifecha = new Date();
        mifecha.setTime(0L);
        String f = new String(getRegistro().saca(getInicio(), getLongitud()));
        if (f.equals("") || f.equals("00000000")) {
            mifecha = null;
        } else {
            try {
                mifecha = new SimpleDateFormat("yyyyMMdd").parse(f);
            } catch (ParseException ex) {
                mifecha.setTime(-999999999999L);
            }
        }
        midate = mifecha;
    }

    void storeToRegistro() {
        String s;
        if (midate == null) {
            s = "00000000";
        } else {
            s = new SimpleDateFormat("yyyyMMdd").format(midate);
        }
        getRegistro().mete(s.toCharArray(), getInicio());
    }

    public Date get() {
        loadFromRegistro();
        return midate;
    }

    public void set(Date midate) {
        if (midate != null) {
            Long t = midate.getTime();
            if (t.equals(-62170160400000L) || t.equals(-62075466000000L)) {
                midate = null;
            }
        }
        this.midate = midate;
        storeToRegistro();
    }
}
