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
 * Clase que describe un tipo de variable lingüística
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class Type implements Cloneable {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Nombre del tipo de variable
	 */
	private String name;
	
	/**
	 * Universo de discurso del tipo de variable
	 */
	private Universe u;
	
	/**
	 * Tipo padre del que se hereda el universo y todas sus familias y funciones
	 * de pertenencia
	 */
	private Type parent;
	
	/**
	 * Tipos hijos que heredan el universo y todas sus familias y funciones
	 * de pertenencia
	 */
	private Type[] child;
	
	/**
	 * Lista de familias de funciones de pertenencia del tipo de variable
	 */
	private Family fam[];
	
	/**
	 * Lista de etiquetas lingüísticas (funciones de pertenencia) del tipo
	 */
	private LinguisticLabel mf[];
	
	/**
	 * Marcador para indicar si algún parámetro del tipo es ajustable
	 */
	private boolean adjustable = true;
	
	/**
	 * Contador de usos
	 */
	private int link;
	
	/**
	 * Marcador para indicar que el tipo está siendo editado
	 */
	private boolean editing = false;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor con el universo por defecto
	 */
	public Type(String name) {
		this.name = name;
		this.u = new Universe();
		this.parent = null;
		this.child = new Type[0];
		this.fam = new Family[0];
		this.mf = new LinguisticLabel[0];
		this.link = 0;
	}

	/**
	 * Constructor con un universo dado
	 */
	public Type(String name, Universe u) {
		this.name = name;
		this.u = u;
		this.parent = null;
		this.child = new Type[0];
		this.fam = new Family[0];
		this.mf = new LinguisticLabel[0];
		this.link = 0;
	}

	/**
	 * Constructor de un tipo heredado
	 */
	public Type(String name, Type parent) {
		this.name = name;
		this.u = parent.getUniverse();
		this.parent = parent;
		this.parent.addChild(this);
		this.child = new Type[0];
		this.fam = new Family[0];
		this.mf = new LinguisticLabel[0];
		this.link = 0;
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	//----------------------------------------------------------------------------//
	// Métodos comunes de los objetos XFL3                                        //
	//----------------------------------------------------------------------------//

	/**
	 * Compara una cadena con el nombre del objeto
	 */
	public boolean equals(String name) {
		return this.name.equals(name);
	}

	/**
	 * Obtiene el nombre del objeto
	 */
	public String toString() {
		return this.name;
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
	 * Verifica si el objeto esta siendo utilizado
	 */
	public boolean isLinked() {
		return (this.link>0);
	}

	/**
	 * Genera la descripción XFL3 del objeto
	 */
	public String toXfl() {
		String eol = System.getProperty("line.separator", "\n");
		String code = "type "+this.name+" ";
		if(this.parent == null) code += this.u.toXfl();
		else code += "parent "+this.parent.getName();
		code += " {"+eol;
		for(int i=0; i<this.fam.length; i++) code += this.fam[i].toXfl()+eol;
		for(int i=0; i<this.mf.length; i++) code += this.mf[i].toXfl()+eol;
		code += " }"+eol+eol;
		return code;
	}

	//----------------------------------------------------------------------------//
	// Métodos de acceso a los campos                                             //
	//----------------------------------------------------------------------------//

	/**
	 *  Verifica si el tipo está siendo editado
	 */
	public boolean isEditing() {
		return this.editing;
	}

	/**
	 * Verifica si el tipo es ajustable por el aprendizaje
	 */
	public boolean isAdjustable() {
		return this.adjustable;
	}

	/**
	 * Verifica si el tipo hereda de un tipo ancestro
	 */
	public boolean hasParent() {
		return this.parent != null;
	}

	/**
	 * Verifica si el tipo es ancestro de otros tipos
	 */
	public boolean hasChild() {
		return this.child.length>0;
	}

	/**
	 * Obtiene el nombre del tipo
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Obtiene el universo del tipo
	 */
	public Universe getUniverse() {
		return this.u;
	}

	/**
	 *  Obtiene el valor del límite inferior del universo
	 */
	public double min() {
		return this.u.min();
	}

	/**
	 * Obtiene el valor del limite superior del universo
	 */
	public double max() {
		return this.u.max();
	}

	/**
	 * Obtiene la separación entre los puntos del universo
	 */
	public double step(){
		return this.u.step();
	}

	/**
	 * Obtiene el ancestro del tipo
	 */
	public Type getParent() {
		return this.parent;
	}

	/**
	 * Obtiene la lista de descendientes del tipo
	 */
	public Type[] getChild() {
		return this.child;
	}

	/**
	 * Obtiene la lista de familias de funciones de pertenencia
	 */
	public Family[] getFamilies() {
		return this.fam;
	}

	/**
	 * Obtiene la lista de familas, incluyendo las heredadas
	 */
	public Family[] getAllFamilies() {
		if(parent == null) return this.fam;
		Family[] pfam = parent.getAllFamilies();
		Family[] fullfam = new Family[this.fam.length+pfam.length];
		System.arraycopy(fam, 0, fullfam, 0, fam.length);
		int count = fam.length;
		for(int i=0; i<pfam.length; i++) {
			boolean overwrite = false;
			for(int j=0; j<this.fam.length; j++)
				if(this.fam[j].equals(pfam[i].getLabel())) overwrite = true;
			if(!overwrite) {
				fullfam[count] = pfam[i];
				count++;
			}
		}
		if(count == fullfam.length) return fullfam;
		Family[] allfam = new Family[count];
		System.arraycopy(fullfam,0,allfam,0,count);
		return allfam;
	}

	/**
	 * Obtiene la lista de funciones de pertenencia del tipo
	 */
	public LinguisticLabel[] getMembershipFunctions() {
		return this.mf;
	}

	/**
	 * Obtiene la lista de MFs del tipo, incluyendo las heredadas
	 */
	public LinguisticLabel[] getAllMembershipFunctions() {
		if(parent == null) return this.mf;
		LinguisticLabel[] pmf = parent.getAllMembershipFunctions();
		LinguisticLabel[] fullmf = new LinguisticLabel[mf.length+pmf.length];
		System.arraycopy(mf, 0, fullmf, 0, mf.length);
		int count = mf.length;
		for(int i=0; i<pmf.length; i++) {
			boolean overwrite = false;
			for(int j=0; j<this.mf.length; j++)
				if(this.mf[j].equals(pmf[i].toString())) overwrite = true;
			if(!overwrite) {
				fullmf[count] = pmf[i];
				count++;
			}
		}
		if(count == fullmf.length) return fullmf;
		LinguisticLabel[] allmf = new LinguisticLabel[count];
		System.arraycopy(fullmf,0,allmf,0,count);
		return allmf;
	}

	/**
	 * Obtiene la lista de funciones de pertenencia paramétricas
	 */
	public ParamMemFunc[] getParamMembershipFunctions() {
		int counter = 0;
		for(int i=0; i<mf.length; i++) if(mf[i] instanceof ParamMemFunc) counter++;
		ParamMemFunc pmf[] = new ParamMemFunc[counter];
		for(int i=0, j=0; i<mf.length; i++) if(mf[i] instanceof ParamMemFunc) {
			pmf[j] = (ParamMemFunc) mf[i];
			j++;
		}
		return pmf;
	}

	/**
	 * Obtiene la lista de funciones de pertenencia familiares
	 */
	public FamiliarMemFunc[] getFamiliarMembershipFunctions() {
		int counter = 0;
		for(int i=0; i<mf.length; i++) if(mf[i] instanceof FamiliarMemFunc) counter++;
		FamiliarMemFunc fmf[] = new FamiliarMemFunc[counter];
		for(int i=0, j=0; i<mf.length; i++) if(mf[i] instanceof FamiliarMemFunc) {
			fmf[j] = (FamiliarMemFunc) mf[i];
			j++;
		}
		return fmf;
	}

	//----------------------------------------------------------------------------//
	// Métodos de modificación de los campos                                      //
	//----------------------------------------------------------------------------//

	/**
	 * Asigna el nombre del tipo
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Asigna el universo del tipo
	 */
	public void setUniverse(Universe u) { 
		this.u = u;
		for(int i=0; i<this.mf.length; i++) if(mf[i] instanceof ParamMemFunc) {
			((ParamMemFunc) this.mf[i]).u = u;
		}
		for(int i=0; i<this.child.length; i++) this.child[i].setUniverse(u);
	}

	/**
	 * Asigna la lista de familias de funciones de pertenencia
	 */
	public void setFamilies(Family[] fam) {
		this.fam = fam;
	}

	/**
	 * Asigna la lista de funciones de pertenencia del tipo
	 */
	public void setMembershipFunctions(LinguisticLabel[] mf) {
		this.mf = mf;
	}

	/**
	 * Actualiza el caracter de ajustable del tipo
	 */
	public void setAdjustable() {
		for(int i=0; i<fam.length; i++) fam[i].setAdjustable();
		for(int i=0; i<mf.length; i++) if(mf[i] instanceof ParamMemFunc) {
			((ParamMemFunc) mf[i]).setAdjustable();
		}
		adjustable = false;
		for(int i=0; i<fam.length; i++) if(fam[i].isAdjustable()) adjustable = true;
		for(int i=0; i<mf.length; i++) if(mf[i] instanceof ParamMemFunc) {
			if(((ParamMemFunc) mf[i]).isAdjustable()) adjustable = true;
		}
	}

	//----------------------------------------------------------------------------//
	// Métodos genéricos                                                          //
	//----------------------------------------------------------------------------//

	/**
	 * Asigna el marcador de estar siendo editado
	 */
	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	/**
	 * Añade un tipo a la lista de descendientes
	 */
	public void addChild(Type newchild) {
		Type ach[] = new Type[this.child.length+1];
		System.arraycopy(this.child,0,ach,0,this.child.length);
		ach[this.child.length] = newchild;
		this.child = ach;
	}

	/**
	 * Verifica que los datos del universo de discurso son validos
	 */
	public boolean testUniverse(double min, double max, int card) {
		double oldmin = this.u.min();
		double oldmax = this.u.max();
		int oldcard = this.u.card();

		try { this.u.set(min,max,card); } catch(XflException ex) { return false; }

		boolean test = true;
		for(int i=0; i<this.fam.length; i++) if(!this.fam[i].test()) test = false;
		for(int i=0; i<this.mf.length; i++) if(mf[i] instanceof ParamMemFunc) {
			if(! ((ParamMemFunc) this.mf[i]).test()) test = false;
		}
		for(int i=0; i<this.child.length; i++) {
			if(!this.child[i].testUniverse(min,max,card)) test = false;
		}

		try { this.u.set(oldmin,oldmax,oldcard); } catch(XflException ex) {}
		return test;
	}

	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone() {
		//***** ESTO ESTA MAL PARA TIPOS CON HIJOS ****//
		try {
			Type clone = (Type) super.clone();
			Universe cu = (this.parent == null? (Universe) this.u.clone() : this.u);

			Family cfam[] = new Family[this.fam.length];
			for(int i=0; i<fam.length; i++) cfam[i]=(Family) this.fam[i].clone(clone);

			LinguisticLabel cmf[] = new LinguisticLabel[this.mf.length];
			for(int i=0; i<mf.length; i++) {
				if(mf[i] instanceof ParamMemFunc) {
					cmf[i]= (ParamMemFunc)  ((ParamMemFunc) this.mf[i]).clone(cu);
				}
				if(mf[i] instanceof FamiliarMemFunc) {
					Family of = ((FamiliarMemFunc) mf[i]).getFamily();
					int index = ((FamiliarMemFunc) mf[i]).getIndex();
					int j = 0;
					for(int k=0; k<fam.length; k++) if(of == fam[k]) j = k;
					cmf[i] = new FamiliarMemFunc(mf[i].toString(),cfam[j],index);
				}
			}

			clone.setName(this.name.toString());
			clone.setUniverse(cu);
			clone.setFamilies(cfam);
			clone.setMembershipFunctions(cmf);
			for(int i=0; i<child.length; i++) clone.addChild(child[i]);
			return clone;
		}
		catch (CloneNotSupportedException e) { return null; }
	}

	/**
	 * Actualiza los parámetros de las funciones de pertenencia
	 */
	public void update() {
		if(!adjustable) return;
		for(int i=0; i<fam.length; i++) fam[i].update();
		for(int i=0; i<mf.length; i++) {
			if(mf[i] instanceof ParamMemFunc) ((ParamMemFunc) mf[i]).update();
		}
	}

	/**
	 * Elimina las funciones de pertenencia no utilizadas
	 */
	public int prune() {
		int pruned = 0;
		for(int i=0; i<mf.length; i++) if(!mf[i].isLinked()) pruned++;
		if(pruned == 0) return 0;
		LinguisticLabel[] aux = new LinguisticLabel[mf.length - pruned];
		for(int i=0,j=0; i<mf.length; i++) if(mf[i].isLinked()) { aux[j]=mf[i]; j++;}
		mf = aux;
		return pruned;
	}

	//----------------------------------------------------------------------------//
	// Métodos de edición de las familias de MFs                                  //
	//----------------------------------------------------------------------------//

	/**
	 * Elimina todas las familias de funciones de pertenencia
	 */
	public void removeAllFamilies() {
		this.fam = new Family[0];
	}

	/**
	 * Elimina una familia de funciones de pertenencia
	 */
	public void removeFamily(Family rfam) {
		if(rfam.isLinked()) return;
		int i;
		for(i=0; i<this.fam.length ; i++) if(this.fam[i]==rfam) break;
		if(i == this.fam.length) return;
		Family[] al = new Family[this.fam.length-1];
		System.arraycopy(this.fam,0,al,0,i);
		System.arraycopy(this.fam,i+1,al,i,this.fam.length-i-1);
		this.fam = al;
	}

	/**
	 * Añade una familia de funciones de pertenencia
	 */
	public void addFamily(Family nfam) throws XflException {
		for(int i=0; i<this.fam.length; i++)
			if(this.fam[i].equals( nfam.getLabel() )) throw new XflException(38);

		Family al[] = new Family[this.fam.length+1];
		System.arraycopy(this.fam,0,al,0,this.fam.length);
		al[this.fam.length] = nfam;
		this.fam = al;
	}

	/**
	 * Busca una familia de funciones de pertenencia
	 */
	public Family searchFamily(String label) {
		for(int i=0; i<this.fam.length; i++) {
			if(this.fam[i].equals(label)) return fam[i];
		}
		if(parent!=null) return parent.searchFamily(label);
		return null;
	}

	//----------------------------------------------------------------------------//
	// Métodos de edición de las funciones de pertenencia                         //
	//----------------------------------------------------------------------------//

	/**
	 * Elimina todas las funciones de pertenencia
	 */
	public void removeAllLabels() {
		for(int i=0; i<this.mf.length; i++) this.mf[i].dispose();
		this.mf = new LinguisticLabel[0];
	}

	/**
	 * Elimina una función de pertenencia
	 */
	public void remove(LinguisticLabel pmf) {
		if(pmf.isLinked()) return;
		int i;
		for(i=0; i<this.mf.length ; i++) if(this.mf[i]==pmf) break;
		if(i == this.mf.length) return;
		pmf.dispose();
		LinguisticLabel[] amf = new LinguisticLabel[this.mf.length-1];
		System.arraycopy(this.mf,0,amf,0,i);
		System.arraycopy(this.mf,i+1,amf,i,this.mf.length-i-1);
		this.mf = amf;
	}

	/**
	 * Añade una función de pertenencia
	 */
	public void add(LinguisticLabel mf) throws XflException {
		for(int i=0; i<this.mf.length; i++)
			if(this.mf[i].equals( mf.getLabel() )) throw new XflException(8);

		LinguisticLabel amf[] = new LinguisticLabel[this.mf.length+1];
		System.arraycopy(this.mf,0,amf,0,this.mf.length);
		amf[this.mf.length] = mf;
		this.mf = amf;
	}

	/**
	 * Busca una función de pertenencia
	 */
	public LinguisticLabel search(String label) {
		for(int i=0; i<this.mf.length; i++) {
			if(this.mf[i].equals(label)) return mf[i];
		}
		if(parent!=null) return parent.search(label);
		return null;
	}

}
