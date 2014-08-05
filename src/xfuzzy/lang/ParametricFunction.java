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
 * Clase abstracta que describe el comportamiento común de todas
 * las funciones paramétricas definidas en los paquetes
 * 
 * @author Francisco José Moreno Velo
 *
 */
public abstract class ParametricFunction {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Indicador para sabe si algún parámetro de la función debe ser
	 * ajustado en un proceso de aprendizaje automático
	 */
	private boolean adjustable = true;
	
	/**
	 * Nombre de la función
	 */
	private String function;
	
	/**
	 * Nombre del paquete al que pertenece la función
	 */
	private String pkg;
	
	/**
	 * Nombre de la lista de parámetros
	 */
	private String paramlistname;
	
	/**
	 * Lista de parámetros definidos individualmente
	 */
	protected Parameter singleparam[];
	
	/**
	 * Lista de parámetros definidos bajo una lista
	 */
	protected Parameter paramlist[];

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public ParametricFunction(String pkg, String function) {
		this.pkg = pkg;
		this.function = function;
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	//----------------------------------------------------------------------------//
	// Métodos comunes de los objetos XFL3                                        //
	//----------------------------------------------------------------------------//

	/**
	 * Genera la descripción XFL3 de la función paramétrica
	 */
	public String getXflDescription() {
		String code = pkg+"."+function+"(";
		double p[] = get();
		for(int i=0; i<p.length; i++) code += (i>0? "," : "")+p[i];
		code += ");";
		return code;
	}

	//----------------------------------------------------------------------------//
	// Métodos de acceso a los campos                                             //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el nombre del paquete de funciones
	 */
	public String getPackageName() {
		return this.pkg;
	}

	/**
	 * Obtiene el nombre de la función
	 */
	public String getFunctionName() {
		return this.function;
	}

	//----------------------------------------------------------------------------//
	// Métodos de acceso a la condición de ajustabilidad                          //
	//----------------------------------------------------------------------------//

	/**
	 * Analiza si algún parámetro de la función es ajustable
	 */
	public void setAdjustable() {
		this.adjustable = false;
		for(int i=0; i<singleparam.length; i++)
			if(singleparam[i].isAdjustable()) this.adjustable = true;
		if(paramlist != null) {
			for(int i=0; i<paramlist.length; i++)
				if(paramlist[i].isAdjustable()) this.adjustable = true;
		}
	}

	/**
	 * Verifica si algún parámetro de la función es ajustable
	 */
	public boolean isAdjustable() {
		return this.adjustable;
	}

	/**
	 * Obtiene cuales de los parámetros son ajustables
	 */
	public boolean[] getAdjustable() {
		int singlelength = singleparam.length;
		int listlength = (paramlist == null? 0 : paramlist.length);

		boolean[] d = new boolean[singlelength+listlength];
		for(int i=0; i<singlelength; i++) d[i] = singleparam[i].isAdjustable();
		for(int i=0; i<listlength; i++) d[singlelength+i]=paramlist[i].isAdjustable();
		return d;
	}

	//----------------------------------------------------------------------------//
	// Métodos de manejo de los parámetros                                        //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el nombre de la lista de parámetros
	 */
	public String getParamListName() {
		return this.paramlistname;
	}

	/**
	 * Asigna el nombre de la lista de parámetros
	 */
	public void setParamListName(String name) {
		this.paramlistname = name;
	}

	/**
	 * Verifica si la función define una lista de parámetros
	 */
	public boolean hasParamList() {
		return (this.paramlistname != null && this.paramlistname.length() > 0);
	}

	/**
	 * Obtiene las referencias a los parámetros simples
	 */
	public Parameter[] getSingleParameters() {
		return this.singleparam;
	}

	/**
	 * Asigna las referencias a los parametros simples
	 */
	public void setSingleParameters(Parameter[] param) {
		this.singleparam = param;
	}

	/**
	 * Obtiene las referencias de la lista de parámetros
	 */
	public Parameter[] getParamList() {
		return this.paramlist;
	}

	/**
	 * Asigna las referencias a la lista de parámetros
	 */
	public void setParamList(Parameter[] list) {
		this.paramlist = list;
	}

	/**
	 * Obtiene las referencias a todos los parámetros
	 */
	public Parameter[] getParameters() {
		int listlength = (paramlist == null? 0 : paramlist.length);

		Parameter param[] = new Parameter[singleparam.length+listlength];
		for(int i=0;i<singleparam.length; i++) param[i] = singleparam[i];
		for(int i=0;i<listlength; i++) param[i+singleparam.length]=paramlist[i];
		return param;
	}

	//-------------------------------------------------------------//
	// 				//
	//-------------------------------------------------------------//

	/**
	 * Obtiene el número de parámetros
	 */
	public int getNumberOfParameters() {
		int listlength = (paramlist == null? 0 : paramlist.length);
		return singleparam.length+listlength;
	}

	/**
	 * Asigna los valores de los parámetros de la función
	 */
	public void set(double p[]) throws XflException {
		if(p.length<singleparam.length) throw new XflException(34);
		if(p.length>singleparam.length && !hasParamList()) throw new XflException(34);

		for(int i=0; i<singleparam.length; i++) singleparam[i].value = p[i];
		paramlist = new Parameter[p.length-singleparam.length];
		for(int i=0; i<paramlist.length; i++) {
			paramlist[i] = new Parameter(paramlistname);
			paramlist[i].value = p[singleparam.length+i];
		}
	}

	/**
	 * Asigna el valor del primer parámetro de la función
	 */
	public void set(double p) {
		singleparam[0].value = p;
	}

	/**
	 * Obtiene los valores de los parámetros de la función
	 */
	public double[] get() {
		int listlength = (paramlist == null? 0 : paramlist.length);

		double[] p = new double[singleparam.length+listlength];
		for(int i=0; i<singleparam.length; i++) p[i] = singleparam[i].value;
		for(int i=0; i<listlength; i++) p[singleparam.length+i] = paramlist[i].value;
		return p;
	}

	/**
	 * Obtiene los valores de la lista de parámetros de la función
	 */
	public double[] getParamListValues() {
		int listlength = (paramlist == null? 0 : paramlist.length);

		double[] p = new double[listlength];
		for(int i=0; i<listlength; i++) p[i] = paramlist[i].value;
		return p;
	}

	/**
	 * Obtiene los desplazamientos de los parámetros de la función
	 */
	public double[] getDesp() {
		int singlelength = singleparam.length;
		int listlength = (paramlist == null? 0 : paramlist.length);

		double[] d = new double[singlelength+listlength];
		for(int i=0; i<singlelength; i++) d[i] = singleparam[i].getDesp();
		for(int i=0; i<listlength; i++) d[singlelength+i] = paramlist[i].getDesp();
		return d;
	}

	/**
	 * Añade un valor a la derivada de un parametro
	 */
	public void addDeriv(int index, double deriv) {
		if(index < singleparam.length) this.singleparam[index].addDeriv(deriv);
		else this.paramlist[index-singleparam.length].addDeriv(deriv);
	}

	/**
	 * Método por defecto para actualizar los parámetros
	 */
	public void update() {
		if(!adjustable) return;
		Parameter parameter[] = getParameters();
		double[] desp = getDesp();
		double[] prevvalue = get();
		for(int i=0; i<parameter.length; i++) parameter[i].value += desp[i];
		if(!test()) {
			for(int i=0; i<parameter.length; i++) parameter[i].value -= desp[i];
			for(int j=0; j<10; j++) {
				for(int i=0; i<desp.length; i++) desp[i] /= 2;
				for(int i=0; i<parameter.length; i++) parameter[i].value += desp[i];
				if(!test())
					for(int i=0; i<parameter.length; i++) parameter[i].value -= desp[i];
			}
		}
		for(int i=0; i<parameter.length; i++) {
			parameter[i].setDesp(0.0);
			parameter[i].setPrevDesp(parameter[i].value - prevvalue[i]);
		}
	}

 	//----------------------------------------------------------------------------//
	//                        MÉTODOS PÚBLICOS ABSTRACTOS                         //
	//----------------------------------------------------------------------------//

	/**
	 * Verifica que los valores de los parámetros sean correctos
	 */
	public abstract boolean test();

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Actualiza los valores y desplazamientos previos
	 */
	protected void updateValues(double[] pos) {
		for(int i=0; i<singleparam.length; i++) {
			double prev = singleparam[i].value;
			singleparam[i].value = pos[i];
			singleparam[i].setDesp(0.0);
			singleparam[i].setPrevDesp(pos[i] - prev);
		}
		if(paramlist!= null) for(int i=0; i<paramlist.length; i++) {
			int j = i+singleparam.length;
			double prev = paramlist[i].value;
			paramlist[i].value = pos[j];
			paramlist[i].setDesp(0.0);
			paramlist[i].setPrevDesp(pos[j] - prev);
		}
	}

	/**
	 * Verifica que una lista de valores esté ordenada
	 */
	protected boolean sorted(double[] p) {
		for(int i=1; i<p.length; i++) if(p[i] <= p[i-1]) return false;
		return true;
	}

	/**
	 * Método que desplaza un conjunto de valores manteniendo su orden
	 */
	protected double[] sortedUpdate(double[] value, double[] desp, double step,
			boolean[] enable){
		int[] cont = new int[value.length];
		double[] time = new double[value.length-1];
		double[] pos = new double[value.length];

		double oldval = value[0]-3*step;
		for(int i=0; i<value.length; i++) {
			pos[i] = (value[i] < oldval+step? oldval+step : value[i]);
			cont[i] = 1;
			oldval = pos[i];
		}

		double timeup = 1;
		int num = value.length;
		while(timeup>0) {
			double mintime= timeup;

			for(int i=0;i<num-1;i++) {
				double dist = (pos[i+1]-pos[i] -step*(cont[i]+cont[i+1])/2 );
				if(desp[i]==desp[i+1]) time[i]=2;
				else if(dist<0 && desp[i]>desp[i+1]) time[i]=0; /* por redondeos */
				else time[i]= dist/(desp[i]-desp[i+1]);
				if(time[i]>=0 && time[i]<mintime) mintime = time[i];
			}

			for(int i=0;i<num;i++) pos[i]+=desp[i]*mintime;

			for(int i=0;i<num-1;i++)
				if(time[i]==mintime) {
					if(enable[i] && enable[i+1]) {
						desp[i]=(cont[i]*desp[i]+cont[i+1]*desp[i+1])/(cont[i]+cont[i+1]);
						pos[i]=(cont[i]*pos[i]+cont[i+1]*pos[i+1])/(cont[i]+cont[i+1]);
						cont[i] += cont[i+1];
						num--;
						for(int j=i+1;j<num;j++) {
							desp[j]= desp[j+1];
							pos[j]= pos[j+1];
							cont[j]= cont[j+1];
							enable[j]= enable[j+1];
						}
					} else if(!enable[i] && enable[i+1]) {
						desp[i+1]= 0;
						pos[i+1]= pos[i]+step;
					} else if(enable[i] && !enable[i+1]) {
						desp[i]= 0;
						pos[i]= pos[i+1]-step;
					}
				}

			timeup -= mintime;
		}

		double [] output = new double[value.length];
		int i=0, j=1-cont[i];
		for(int k=0; k<value.length; k++) {
			output[k] = pos[i] + j*step/2;
			j = j+2;
			cont[i]--;
			if(cont[i]==0 && k<value.length-1) { i++; j=1-cont[i]; }
		}
		return output;
	}

}
