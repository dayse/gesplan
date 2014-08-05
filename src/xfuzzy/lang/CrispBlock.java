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


package xfuzzy.lang;

/**
 * Definición de un bloque no difuso
 * 
 * @author Francisco José Moreno Velo
 *
 */
public abstract class CrispBlock extends ParametricFunction 
implements Cloneable {
	
	//----------------------------------------------------------------------------//
	//                             MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Identificador del bloque
	 */
	private String label;
	
	/**
	 * Número de enlaces al bloque
	 */
	private int link = 0;
	
	/**
	 * Flag para indicar si está abierto el editor del bloque
	 */
	private boolean editing = false;
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */
	public CrispBlock(String pkg, String function) {
		super(pkg, function);
	}
	
	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Compara la cadena con el nombre del objeto
	 */
	public boolean equals(String label) {
		return this.label.equals(label);
	}
	
	/**
	 * Obtiene el nombre del objeto
	 */
	public String getName() {
		return this.label;
	}
	
	/**
	 * Obtiene el nombre del objeto
	 */
	public String toString() {
		return this.label;
	}
	
	/**
	 * Incrementa el contador de enlaces (usos) del objeto
	 */
	public void link() {
		this.link++;
	}
		
	/**
	 * Decrementa el contador de enlaces (usos) del objeto
	 */
	public void unlink() {
		this.link--;
	}
	
	/**
	 * Estudia si el objeto está siendo utilizado
	 */
	public boolean isLinked() {
		return (this.link>0);
	}
	
	/**
	 * Selecciona si se esta editando o no el objeto
	 */
	public void setEditing(boolean editing) {
		this.editing = editing;
	}
	
	/**
	 * Verifica si se esta editando el objeto
	 */
	public boolean isEditing() {
		return this.editing;
	}
	
	/**
	 * Elimina los enlaces del objeto
	 */
	public void dispose() {
	}
		
	/**
	 * Genera la descripcion XFL3 del objeto
	 */
	public String toXfl() {
		return " "+label+" "+getXflDescription();
	}
	
	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone() {
		try {
			CrispBlock clone = (CrispBlock) getClass().newInstance();
			clone.setLabel(this.label);
			clone.set(get());
			return clone;
		}
		catch(Exception ex) { return null; }
	}
	
	/**
	 * Obtiene el nombre del objeto
	 */
	public String getLabel() {
		return this.label;
	}
	
	/**
	 * Asigna el nombre del objeto
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	//----------------------------------------------------------------------------//
	//                         MÉTODOS PÚBLICOS ABSTRACTOS                        //
	//----------------------------------------------------------------------------//
	
	/**
	 * Ejecuta la función
	 */
	public abstract double compute(double[] x);
	
	/**
	 * Obtiene el número de entradas de la función
	 */
	public abstract int inputs();
	
	/**
	 * Obtiene el código Java de la función
	 */
	public abstract String getJavaCode();
	
	/**
	 * Obtiene el código C de la función
	 */
	public abstract String getCCode();
	
	/**
	 * Obtiene el código C++ de la función
	 */
	public abstract String getCppCode();
	
}
