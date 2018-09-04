package es.cvjpy.cobol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Registro implements Registrable {

    private final char buffer[];
    private final List campos = new ArrayList();
    private KeyCobol key;
    private final List alterkeys = new ArrayList();
    private final String nombre;

    public Registro(String nombre, int longitud) {
        this.nombre = nombre;
        this.buffer = new char[longitud];
        this.initAll();
    }

    void mete(char[] esto, int aqui) {
        char c;
        int max = buffer.length;
        for (int i = 0; (i < esto.length && (i + aqui) < max); i++) {
            c = esto[i];
            if (c == 0) {
                c = 32;
            }
            buffer[i + aqui] = c;
        }
    }

    char[] saca(int desde, int cuantos) {
        char[] esto = new char[cuantos];
        for (int i = 0; i < cuantos; i++) {
            esto[i] = buffer[i + desde];
        }
        return esto;
    }

    String paraenviar() throws Exception {
        return new String(buffer);
    }

    @Override
    public String getAll() {
        return new String(buffer);
    }

    @Override
    public void setAll(String todo) {
        mete(todo.toCharArray(), 0);
    }

    void pararecibir(String recibido) throws Exception {
        if (recibido != null) {
            mete(recibido.toCharArray(), 0);
        }
    }

    protected void add(Campo uncampo) {
        campos.add(uncampo);
    }

    @Override
    public CampoCobol getCampo(String nombre) {
        Campo ret = null;
        Campo uno;
        Iterator iter = campos.iterator();
        while (iter.hasNext()) {
            uno = (Campo) iter.next();
            if (uno.getNombre().equals(nombre)) {
                ret = uno;
                break;
            }
        }
        return ret;
    }

    @Override
    public Iterator getCamposIterator() {
        return campos.iterator();
    }

    protected void setKey(KeyCobol key) {
        this.key = key;
    }

    @Override
    public KeyCobol getKey() {
        return key;
    }

    protected void addAlterKey(Key key) {
        alterkeys.add(key);
    }

    @Override
    public KeyCobol getAlterKey(String nombre) {
        KeyCobol ret = null;
        KeyCobol una;
        Iterator iter = alterkeys.iterator();
        while (iter.hasNext()) {
            una = (KeyCobol) iter.next();
            if (una.getNombre().equals(nombre)) {
                ret = una;
                break;
            }
        }
        return ret;
    }

    @Override
    public Iterator getAlterKeyIterator() {
        return alterkeys.iterator();
    }

    public String getNombre() {
        return nombre;
    }

    public void setCobolText(String texto) {
        mete(texto.toCharArray(), 0);
    }

    public String getCobolText() {
        return new String(buffer);
    }

    @Override
    public final void initAll() {
        Campo este;
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 32;
        }
        Iterator iter = campos.iterator();
        while (iter.hasNext()) {
            este = (Campo) iter.next();
            if (este instanceof Numero) {
                ((Numero) este).set(new BigDecimal(0));
            } else if (este instanceof Fecha) {
                este.setCobolText("00000000");
            } else if (este instanceof DiaMesAno) {
                este.setCobolText("00000000");
            } else {
                este.setCobolText("");
            }
        }
    }

    @Override
    public void lastAll() {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (char) 255; //En
        }
    }
}
