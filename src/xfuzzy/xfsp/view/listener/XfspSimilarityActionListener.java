//--------------------------------------------------------------------------------//
//                               COPYRIGHT NOTICE                                 //
//--------------------------------------------------------------------------------//
// Copyright (c) 2012, Instituto de Microelectronica de Sevilla (IMSE-CNM)        //
//                                                                                //
// All rights reserved.                                                           //
//                                                                                //
// Redistribution and use in source and binary forms, with or without             //
// modification, are permitted provided that the following  conditions are met:   //
//                                                                                //
//     * Redistributions of source code must retain the above copyright notice,   //
//       this list of conditions and the following disclaimer.                    // 
//                                                                                //
//     * Redistributions in binary form must reproduce the above copyright        // 
//       notice, this list of conditions and the following disclaimer in the      //
//       documentation and/or other materials provided with the distribution.     //
//                                                                                //
//     * Neither the name of the IMSE-CNM nor the names of its contributors may   //
//       be used to endorse or promote products derived from this software        //
//       without specific prior written permission.                               //
//                                                                                //
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"    //
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE      //
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE // 
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE  //
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL     //
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR     //
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER     //
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,  //
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  //
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.           //
//--------------------------------------------------------------------------------//

package xfuzzy.xfsp.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import xfuzzy.lang.Type;

import xfuzzy.xfsp.XfspEvent;

import xfuzzy.xfsp.controller.XfspStore;

import xfuzzy.xfsp.view.XfspSimilarityParametersView;

/**
 * <p> <b>Descripci�n:</b> clase de objetos que atienden los eventos producidos
 * en las ventanas de introducci�n de par�metros para el m�todo de
 * simplificaci�n de tipos por medidas de similitud.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.4
 * @see XfspSimilarityView
 * @see XfspActionListener
 *
 */
public class XfspSimilarityActionListener
    extends XfspActionListener
    implements ActionListener {

  //ventana para la introducci�n de los par�metros del m�todo de similaridad
  private XfspSimilarityParametersView frame;

  //almac�n donde se deben dejar los eventos del modelo y la vista del sistema
  //para ser procesados por el controlador
  private XfspStore store;

  //tipo a simplificar mediante similaridad
  private Type type;

  /**
   * <p> <b>Descripci�n:</b> crea un objeto que responde a los distintos
   * eventos producidos por la ventana de introducci�n de par�metros para el
   * m�todo de similaridad.
   * @param frame Ventana que permite la introducci�n de los par�metros para el
   * m�todo de tipos mediante similaridad.
   * @param store Almac�n donde hay que enviar los mensajes para que puedan ser
   * procesados convenientemente por alg�n controlador.
   * @param type Tipo a simplificar mediante medidas de similaridad.
   *
   */
  public XfspSimilarityActionListener(XfspSimilarityParametersView frame,
                                      XfspStore store, Type type) {
    //llama al constructor de la clase padre
    super(frame);
    //establece la ventana de par�metros para clustering que se escuchar�
    this.frame = frame;
    //establece el almac�n donde se deben dejar los eventos que se quieran
    //procesar
    this.store = store;
    //establece la base de reglas a simplificar
    this.type = type;
  }

  /**
   * <p> <b>Descripci�n:</b> env�a los par�metros introducidos en la vista
   * escuchada a un almac�n para que sea procesado por alg�n controlador del
   * sistema.
   *
   */
  protected void sendParameters() {
    Object[] args = new Object[2];
    double threshold = frame.getThreshold();
    //cambiamos esto porque ahora el m�todo es siempre el mismo
    String method = "Dubois&Prade";

    if (method != null) {
      if (threshold != 0) {
        Double ths = new Double(threshold);
        args[0] = type;
        args[1] = ths;

        XfspEvent ev = new XfspEvent("Similarity", args);
        store.store(ev);
        super.close();
      }
    }
  }

  /**
   * <p> <b>Descripci�n:</b> acciones espec�ficas para la vista destinada a la
   * introducci�n de los par�metros del m�todo de similaridad que, en este
   * caso, no hay ninguna.
   * @param e Evento producido por la ventana escuchada.
   *
   */
  protected void specificActions(ActionEvent e) {
  }
}
