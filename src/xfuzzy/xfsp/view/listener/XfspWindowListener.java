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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import xfuzzy.xfsp.view.IXfspView;

/**
 * <p> <b>Descripción:</b> clase de objetos que atienden los eventos producidos
 * en las ventanas del sistema Xfsp.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.0
 * @see IXfspView
 * @see WindowListener
 *
 */
public class XfspWindowListener
    implements WindowListener {

  //ventana cuyos eventos serán escuchados por el objeto actual
  private IXfspView frame;

  /**
   * <p> <b>Descripción:</b> crea un objeto que responde a los distintos
   * eventos de ventana producidos por una vista del sistema Xfsp.
   * @param frame Vista escuchada por el objeto.
   *
   */
  public XfspWindowListener(IXfspView frame) {
    this.frame = frame;
  }

  /**
   * <p> <b>Descripción:</b> acción realizada al abrir la ventana.
   *
   */
  public void windowOpened(WindowEvent arg0) {
  }

  /**
   * <p> <b>Descripción:</b> acción realizada mientras la ventana se está
   * cerrando.
   *
   */
  public void windowClosing(WindowEvent arg0) {
    frame.close();
  }

  /**
   * <p> <b>Descripción:</b> acción realizada al cerrar la ventana.
   *
   */
  public void windowClosed(WindowEvent arg0) {
  }

  /**
   * <p> <b>Descripción:</b> acción realizada al convertir la ventana en un
   * icono de escritorio.
   *
   */
  public void windowIconified(WindowEvent arg0) {
  }

  /**
   * <p> <b>Descripción:</b> acción realizada traer la ventana de un icono de
   * escritorio al primer plano.
   *
   */
  public void windowDeiconified(WindowEvent arg0) {
  }

  /**
   * <p> <b>Descripción:</b> acción realizada al establecer la ventan como
   * activa.
   *
   */
  public void windowActivated(WindowEvent arg0) {
  }

  /**
   * <p> <b>Descripción:</b> acción realizada al desactivar la ventana.
   *
   */
  public void windowDeactivated(WindowEvent arg0) {
  }
}
