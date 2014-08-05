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

import xfuzzy.xfsp.view.IXfspView;

/**
 * <p> <b>Description:</b> Clase principal de objetos que atienden eventos de
 * ventana de la que heredar�n todas las dem�s.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.2
 * @see ActionListener
 *
 */
public abstract class XfspActionListener
    implements ActionListener {

  //ventana cuyos eventos se escucha
  private IXfspView frame;

  /**
   * <p> <b>Descripci�n:</b> crea un objeto que escucha eventos de ventana.
   * @param frame Vetana cuyos eventos ser�n escuchados por el objeto actual.
   *
   */
  public XfspActionListener(IXfspView frame) {
    this.frame = frame;
  }

  /**
   * <p> <b>Descripci�n:</b> acci�n realizada por la ventana cuando se produce
   * un evento.
   * @param e Evento producido por la ventana escuchada.
   *
   */
  public void actionPerformed(ActionEvent e) {
    //obtiene el comando del evento recibido
    String command = e.getActionCommand();
    //si el comando era de cancelaci�n...
    if (command.equals("Cancel")) {
      //...cierra la ventana escuchada
      close();
    }
    //si el comando era de aceptaci�n...
    else if (command.equals("Ok")) {
      //...env�a los par�metros introducidos en la ventana
      sendParameters();
    }
    //en otro caso...
    else {
      //...se realizan acciones espec�ficas seg�n la ventana escuchada
      specificActions(e);
    }
  }

  /**
   * <p> <b>Descripci�n:</b> env�a los par�metros introducidos en la ventana
   * escuchada.
   *
   */
  protected abstract void sendParameters();

  /**
   * <p> <b>Descripci�n:</b> realiza las acciones espec�ficas de la ventana que
   * se est� escuchando.
   * @param e Evento producido por la ventana escuchada.
   *
   */
  protected abstract void specificActions(ActionEvent e);

  /**
   * <p> <b>Descripci�n:</b> cierra la ventana cuyos eventos se est�n atendiendo.
   *
   */
  protected void close() {
    frame.close();
  }
}
