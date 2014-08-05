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
 * Clase que define un conjunto de bloques no difusos
 * 
 * @author Francisco Joé Moreno Velo
 *
 */
public class CrispBlockSet implements Cloneable {
	
	//----------------------------------------------------------------------------//
	//                             MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//
	
	/**
	 * Lista de bloques no difusos
	 */
	private CrispBlock[] block;
	
	//----------------------------------------------------------------------------//
	//                               CONSTRUCTOR                                  //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public CrispBlockSet() {
		this.block = new CrispBlock[0];
	}
	
	//----------------------------------------------------------------------------//
	//                            MÉTODOS PÚBLICOS                                //
	//----------------------------------------------------------------------------//
	
	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone() {
		CrispBlockSet clone = new CrispBlockSet();
		for(int i=0; i<block.length; i++) {
			clone.add((CrispBlock) block[i].clone());
		}
		return clone;
	}
	
	/**
	 * Genera la descripcion XFL3 del objeto
	 */
	public String toXfl() {
		String eol = System.getProperty("line.separator", "\n");
		if(this.block.length == 0) return "";
		String code = "crisp {"+eol;
		for(int i=0; i<this.block.length; i++) code += this.block[i].toXfl()+eol;
		code += " }"+eol+eol;
		return code;
	}
		
	/**
	 * Obtiene la lista de bloques
	 */
	public CrispBlock[] getBlocks() {
		return block;
	}
	
	/**
	 * Añade un bloque al conjunto
	 */
	public void add(CrispBlock newblock) {
		CrispBlock ab[] = new CrispBlock[this.block.length+1];
		System.arraycopy(this.block,0,ab,0,this.block.length);
		ab[this.block.length] = newblock;
		this.block = ab;
	}
	
	/**
	 * Elimina un bloque del conjunto
	 */
	public void remove(CrispBlock rmblock) {
		if(rmblock.isLinked()) return;
		int i;
		for(i=0; i<this.block.length ; i++) if(this.block[i]==rmblock) break;
		if(i == this.block.length) return;
		CrispBlock[] ab = new CrispBlock[this.block.length-1];
		System.arraycopy(this.block,0,ab,0,i);
		System.arraycopy(this.block,i+1,ab,i,this.block.length-i-1);
		this.block = ab;
	}
		
	/**
	 * Intercambia dos bloques no difusos
	 */
	public void exchange(CrispBlock oldblock, CrispBlock newblock) {
		for(int i=0; i<this.block.length ; i++) if(this.block[i]==oldblock) {
			this.block[i]=newblock;
			oldblock.dispose();
		}
	}
	
	/**
	 * Busca un bloque en el conjunto
	 */
	public CrispBlock search(String label) {
		for(int i=0; i<this.block.length; i++) {
			if(this.block[i].equals(label)) return block[i];
		}
		return null;
	}
	
}

