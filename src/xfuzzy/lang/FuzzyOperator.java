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
 * Clase que describe un operador difuso (en general)
 * 
 * @author Francisco José Moreno Velo
 *
 */
public abstract class FuzzyOperator extends ParametricFunction 
implements Cloneable {
	
	//----------------------------------------------------------------------------//
	//                            CONSTANTES PÚBLICAS                             //
	//----------------------------------------------------------------------------//
	
	public final static int AND = 1;
	public final static int OR = 2;
	public final static int NOT = 3;
	public final static int ALSO = 4;
	public final static int IMP = 5;
	public final static int MOREORLESS = 6;
	public final static int VERY = 7;
	public final static int SLIGHTLY = 8;
	public final static int DEFUZMETHOD = 9;
	
	//----------------------------------------------------------------------------//
	//                             MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Indica que el operador toma el valor por defecto
	 */
	private boolean isdefault = false;
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//
	
	/**
	 * Constructor
	 */	
	public FuzzyOperator(String pkg, String function) {
		super(pkg, function);
	}
	
	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Asigna el valor del campo isdefault (función por defecto)
	 */
	public void setDefault(boolean isdefault) {
		this.isdefault = isdefault;
	}
	
	/**
	 * Estudia si el objeto es la función por defecto
	 */
	public boolean isDefault() {
		return this.isdefault;
	}
	
	/**
	 *  Genera la descripcion XFL3 del objeto
	 */
	public String toXfl() {
		if(isdefault) return "";
		else return getXflDescription();
	}
	
	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone() {
		try {
			FuzzyOperator clone = (FuzzyOperator) getClass().newInstance();
			clone.set(get());
			clone.setDefault(isdefault);
			return clone;
		}
		catch (Exception e) { return null; }
	}
	
	//----------------------------------------------------------------------------//
	//                       MÉTODOS PÚBLICOS ABSTRACTOS                          //
	//----------------------------------------------------------------------------//
	
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
