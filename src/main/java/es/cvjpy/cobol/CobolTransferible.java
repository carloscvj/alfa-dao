/*
 * CobolTransferible.java
 *
 * Created on 11 de junio de 2007, 13:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.cvjpy.cobol;

import java.io.Serializable;

/**
 *
 * @author CarlosVJ
 */
public interface CobolTransferible extends Serializable {

  public String getCobolText();
  
  public void setCobolText(String cobolText);
  
}
