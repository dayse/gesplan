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


package xfuzzy.xfsim;

import xfuzzy.lang.*;

/**
 * Valores límite del proceso de simulación
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfsimLimit {
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Ventana principal de la aplicación
	 */
	private Xfsim xfsim;
	
	/**
	 * Lista de valores límite
	 */
	private double[] limit;
	
	/**
	 * Marcadores de los valores asignados
	 */
	private boolean[] limitflag;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public XfsimLimit(Xfsim xfsim) {
		this.xfsim = xfsim;
		Variable output[] = xfsim.getSpecification().getSystemModule().getOutputs();
		Variable input[] = xfsim.getSpecification().getSystemModule().getInputs();
		this.limit = new double[2*(output.length+input.length+1)];
		this.limitflag = new boolean[2*(output.length+input.length+1)];
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//
	
	/**
	 * Devuelve el valor textual de un límite
	 */
	public String getValue(int i) {
		if(limit == null || i<0 || i> limit.length) return "";
		if(!limitflag[i]) return "";
		return ""+limit[i];
	}
	
	/**
	 * Devuelve el valor numérico de un límite
	 */
	public double getLimit(int i) {
		if(i<0 || i> limit.length) return 0;
		return limit[i];
	}
	
	/**
	 * Asigna el valor a un límite
	 */
	public void setLimit(int i, boolean flag, double value) {
		if(i<0 || i> limit.length) return;
		limitflag[i] = flag;
		if(flag) limit[i] = value; else limit[i] = 0;
	}
	
	/**
	 * Asigna los valores a todos los límites
	 */
	public void setLimits(boolean[] flag, double[] value) {
		this.limitflag = flag;
		this.limit = value;
	}
	
	/**
	 * Añade un límite desde el fichero de configuración
	 */
	public void add(String id,double value,int kind) {
		if(id.equals("_n") && kind == 0)
		{ limitflag[0] = true; limit[0] = value; return; }
		if(id.equals("_t") && kind == 0)
		{ limitflag[1] = true; limit[1] = value; return; }
		Variable output[] = xfsim.getSpecification().getSystemModule().getOutputs();
		for(int i=0; i<output.length; i++) if(output[i].getName().equals(id)) {
			if(kind==0) { limitflag[2*i+3] = true; limit[2*i+3] = value; return; }
			else { limitflag[2*i+2] = true; limit[2*i+2] = value; return; }
		}
		Variable input[] = xfsim.getSpecification().getSystemModule().getInputs();
		int base = 2+2*output.length;
		for(int i=0; i<input.length; i++) if(input[i].getName().equals(id)) {
			if(kind==1) { limitflag[2*i+base] = true; limit[2*i+base] = value; return; }
			else { limitflag[2*i+base+1] = true; limit[2*i+base+1] = value; return; }
		}
	}
	
	/**
	 * Verifica que algún límite se haya asignado
	 */
	public boolean isConfigured() {
		if(limit == null) return false;
		boolean flag = false;
		for(int i=0; i<limitflag.length; i++) if(limitflag[i]) flag = true;
		return flag;
	}
	
	/**
	 * Elimina todos los límites
	 */
	public void clearLimits() {
		for(int i=0; i<limitflag.length; i++) limitflag[i] = false;
		for(int i=0; i<limit.length; i++) limit[i] = 0.0;
	}
	
	/**
	 * Verifica que los límites no se hayan alcanzado
	 */
	public boolean test(double iter, double time, double[] fzst, double[] ptst) {
		if(limitflag[0] && iter >= limit[0]) return false;
		if(limitflag[1] && time >= limit[1]) return false;
		for(int i=0; i<fzst.length; i++) {
			if(limitflag[2*i+2] && fzst[i] <= limit[2*i+2]) return false;
			if(limitflag[2*i+3] && fzst[i] >= limit[2*i+3]) return false;
		}
		int base = 2+2*fzst.length;
		for(int i=0; i<ptst.length; i++) {
			if(limitflag[2*i+base] && ptst[i] <= limit[2*i+base]) return false;
			if(limitflag[2*i+base+1] && ptst[i] >= limit[2*i+base+1]) return false;
		}
		return true;
	}
	
	/**
	 * Descripción a mostrar en la ventana
	 */
	public String toString() {
		if(limit == null) return "unconfigured";
		boolean flag = false;
		for(int i=0; i<limitflag.length; i++) if(limitflag[i]) flag = true;
		if(!flag) return "unconfigured";
		
		String code = "";
		if(limitflag[0]) code += " & _n < "+limit[0];
		if(limitflag[1]) code += " & _t < "+limit[1];
		
		Variable output[] = xfsim.getSpecification().getSystemModule().getOutputs();
		for(int i=0; i<output.length; i++) {
			if(limitflag[2*i+2]) code += " & "+output[i].getName()+" > "+limit[2*i+2];
			if(limitflag[2*i+3]) code += " & "+output[i].getName()+" < "+limit[2*i+3];
		}
		Variable input[] = xfsim.getSpecification().getSystemModule().getInputs();
		int base = 2+2*output.length;
		for(int i=0; i<input.length; i++) {
			if(limitflag[2*i+base])
				code += " & "+input[i].getName()+" > "+limit[2*i+base];
			if(limitflag[2*i+base+1])
				code += " & "+input[i].getName()+" < "+limit[2*i+base+1];
		}
		return code.substring(3);
	}
	
	/**
	 * Descripción a mostrar en el fichero de configuración
	 */
	public String toCode() {
		String st = toString();
		if(st.equals("unconfigured")) return "";
		return "xfsim_limit("+st+")";
	}
}

