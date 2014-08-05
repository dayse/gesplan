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

package xfuzzy.xfsp;

/**
 * <p> <b>Descripción:</b> Clase que representa a los eventos que son enviados
 * por las vistas o los modelos y que deben ser coordinados por los
 * controladores.
 * <p> <b>E-mail</b>: <ADDRESS>joragupra@us.es</ADDRRESS>
 * @author Jorge Agudo Praena
 * @version 1.2
 * @see XfspController
 * @see XfspModel
 * @see XfspStore
 * @see XfspView
 *
 */
public class XfspEvent {

  //indica el tipo de evento del que se trata
  private String type;

  //contiene los argumentos que debe transportar el evento
  private Object[] args;

  /**
   * <p> <b>Descripción:</b> crea un nuevo evento del tipo especificado que
   * transportará los argumentos indicados.
   * @param type Tipo de evento que se quiere crear.
   * @param args Argumentos que debe transportar el evento.
   *
   */
  public XfspEvent(String type, Object[] args) {
    //registra el tipo del evento actual
    this.type = type;
    //almacena los argumentos que debe transportar
    this.args = args;
  }

  /**
   * <p> <b>Descripción:</b> indica el tipo del evento actual.
   * @return Cadena que representa el tipo de envento del que se trata.
   */
  public String getType() {
    //devuelve el tipo que ha registrado el evento cuando fue creado
    return type;
  }

  /**
   * <p> <b>Descripción:</b> permite obtener los argumentos que transporta el
   * evento.
   * @return Argumentos que transporta el evento actual.
   *
   */
  public Object[] getArgs() {
    //devuelve los argumentos transportados
    return args;
  }
}
