package es.cvjpy.cobol;

import java.util.Iterator;

public interface Registrable extends java.io.Serializable {

    KeyCobol getKey();

    KeyCobol getAlterKey(String nombre);

    Iterator getAlterKeyIterator();

    CampoCobol getCampo(String nombre);

    Iterator getCamposIterator();

    void initAll();

    void lastAll();

    String getAll();

    void setAll(String todo);
}
