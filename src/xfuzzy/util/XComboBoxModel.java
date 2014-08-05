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


package xfuzzy.util;

import javax.swing.*;
import java.util.Vector;

/**
 * Modelo de una lista desplegable deseleccionable, es decir, que pueda no
 * tener ningún elemento seleccionado
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XComboBoxModel extends AbstractListModel
implements MutableComboBoxModel {

	//----------------------------------------------------------------------------//
	//                           CONSTANTES PRIVADAS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603002L;

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Lista de objetos
	 */
	private Vector objects;
	
	/**
	 * Objeto seleccionado
	 */
	private Object selectedObject;
	
	/**
	 * Marcador para evitar la doble llamada a setSelectedItem()
	 */
	private boolean blockage = false;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	public XComboBoxModel() {
		objects = new Vector();
	}

	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Selecciona un elemento de la lista (que puede ser nulo)
	 */
	public void setSelectedItem(Object anObject) {
		if(anObject == null) blockage = true;
		else if(blockage) { blockage = false; return; }
		if(selectedObject == anObject) return;
		if(selectedObject == null && anObject == null) return;
		selectedObject = anObject;
		fireContentsChanged(this, -1, -1);
	}

	/**
	 * Devuelve la referencia al objeto seleccionado
	 */
	public Object getSelectedItem() {
		return selectedObject;
	}

	/**
	 * Obtiene el número de elementos de la lista
	 */
	public int getSize() {
		return objects.size();
	}

	/**
	 * Devuelve el elemento de una posición
	 */
	public Object getElementAt(int index) {
		if( index>=0 && index<objects.size() ) return objects.elementAt(index);
		else return null;
	}

	/**
	 * Obtiene el indice de un determinado elemento
	 */
	public int getIndexOf(Object anObject) {
		return objects.indexOf(anObject);
	}

	/**
	 * Añade un elemento a la lista
	 */
	public void addElement(Object anObject) {
		objects.addElement(anObject);
		fireIntervalAdded(this,objects.size()-1, objects.size()-1);
	}

	/**
	 * Inserta un elemento en una determinada posición
	 */
	public void insertElementAt(Object anObject,int index) {
		objects.insertElementAt(anObject,index);
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Elimina el elemento que ocupa una cierta posición
	 */
	public void removeElementAt(int index) {
		if( getElementAt( index ) == selectedObject ) selectedObject = null;
		objects.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
	}

	/**
	 * Elimina un cierto elemento de la lista
	 */
	public void removeElement(Object anObject) {
		int index = objects.indexOf(anObject);
		if( index != -1 ) removeElementAt(index);
	}

	/**
	 * Elimina todos los elementos de la lista
	 */
	public void removeAllElements() {
		if( objects.size() > 0 ) {
			int firstIndex = 0;
			int lastIndex = objects.size() - 1;
			objects.removeAllElements();
			selectedObject = null;
			fireIntervalRemoved(this, firstIndex, lastIndex);
		}
	}
}

