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

package xfuzzy.xfvhdl;

import java.util.ArrayList;
import xfuzzy.lang.CrispBlockCall;
import xfuzzy.lang.CrispBlockSet;
import xfuzzy.lang.FamiliarMemFunc;
import xfuzzy.lang.LinguisticLabel;
import xfuzzy.lang.ModuleCall;
import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Parameter;
import xfuzzy.lang.RulebaseCall;
import xfuzzy.lang.Specification;
import xfuzzy.lang.SystemModule;
import xfuzzy.lang.Variable;
import xfuzzy.xfsg.XfsgInOrderMemFunc;
import xfuzzy.xfsg.XfsgOutData;
import xfuzzy.xfsg.XfsgPrintRulesFactory;
import xfuzzy.xfsg.XfsgRulesMemData;
import xfuzzy.xfsg.XfsgSTPrintRules;
import xfuzzy.xfsg.XfsgStaticMethods;

/**
 * Clase que incluye los métodos "inicializarFLCs" e "inicializarCrispBlocks"
 * encargados de construir los objetos de tipo XfvhdlFLC y XfvhdlCrisp de una
 * especificación XFL. También incluye métodos para determinar los tipos de
 * funciones de pertenencia y para calcular el segundo parámetro de ponderación
 * usado en determinados métodos de defuzzificacción.
 * 
 * @author Lidia Delgado Carretero.
 */

public class XfvhdlLeerXfl {

	// ATRIBUTOS
	/**Lista de módulos de inferencia.*/
	private ArrayList<XfvhdlFLC> listaflc;
	
	/**Lista de módulos de crisp.*/
	private ArrayList<XfvhdlCrisp> listacrisp;
	
	/**Señales necesarias a la hora de generar el código de nivel superior o 
	 * jerarquía del sistema jerárquico.*/
	private String señalesjerarquia;

	/**Especificación XFL*/
	private Specification spec;

	// El siguiente atributo es para las funciones de pertenencia de las
	// variables de entrada ordenadas
	/**Funciones de pertenencia de las variables de entrada 
	 * ordenadas.*/
	private XfsgInOrderMemFunc[] inOrderMfInput;

	// El siguiente atributo es para las funciones de pertenencia de las
	// variables de salida ordenadas
	/**Funciones de pertenencia de las variables de salida ordenadas.*/
	private XfsgInOrderMemFunc[] inOrderMfOutput;

	private XfsgSTPrintRules printer;
	
	private String asignaEntrada="";
	
	CrispBlockSet set_de_crisps;

	// CONSTRUCTOR
	/**Constructor de la clase.*/
	public XfvhdlLeerXfl(Specification spec) {
		this.spec = spec;
		listaflc = new ArrayList<XfvhdlFLC>();
		listacrisp = new ArrayList<XfvhdlCrisp>();
		señalesjerarquia="";
		set_de_crisps=spec.getCrispBlockSet();
	}

	// MÉTODOS GET

	/**@return listaflc*/
	public ArrayList<XfvhdlFLC> getlistaflc() {
		return listaflc;
	}
	

	/**@return listacrisp*/
	public ArrayList<XfvhdlCrisp> getlistacrisp() {
		return listacrisp;
	}

	/**@return spec*/
	public Specification getspec() {
		return spec;
	}

	// MÉTODOS SET

	/**@param listaflc*/
	public void setlistaflc(ArrayList<XfvhdlFLC> listaflc) {
		this.listaflc = listaflc;
	}

	/**@param listacrisp*/
	public void setlistacrisp(ArrayList<XfvhdlCrisp> listacrisp) {
		this.listacrisp = listacrisp;
	}
	/**@param spec*/
	public void setspec(Specification spec) {
		this.spec = spec;
	}

	

	/**Método encargado de inicializar toda la información relativa a los 
	 * módulos de inferencia, capturándola de la especificación XFL.*/
	public void inicializarFLCs() {

		String num_mf = "", puntos_corte = "", pendientes = "";
		ArrayList<Double> listaPendientes=new ArrayList<Double>();
		ArrayList<Double> listaPuntos=new ArrayList<Double>();
		SystemModule sistema = spec.getSystemModule();
		RulebaseCall rbcarray[] = sistema.getRulebaseCalls();
		try {
			for (int s = 0; s < rbcarray.length; s++) {

				RulebaseCall rbc = rbcarray[s];

				String name = rbc.getName();
				/* MFC */
				// Crea el FLC
				XfvhdlFLC flc = new XfvhdlFLC(name, rbc,spec);
				String defuzzy = rbc.getRulebase().getOperatorset().defuz
						.getFunctionName();
				flc.setdefuzzy(defuzzy);
				// Dependiendo del método de defuzzificación se calcularán los 
			    // pesos de las funciones de pertenencia  
				if (defuzzy.equalsIgnoreCase(
				  "WeightedFuzzyMean")
				  || defuzzy.equalsIgnoreCase(
				  "GammaQuality")
				  || defuzzy.equalsIgnoreCase(
				  "Quality")) 
				{
					flc.setCalculateWeights(true);
				}
				
				String connective = rbc.getRulebase().getOperatorset().and
						.getFunctionName();
				flc.setconnective(connective);

				Variable[] var_inputs = rbc.getRulebase().getInputs();

				double min, max;
				int total_inputs = var_inputs.length;
				flc.setinputs(total_inputs);

				LinguisticLabel mf, mf2;
				int nmf;

				// Crea la estructura inOrderMfInput[i] (donde el índice i
				// direcciona las distintas variables de entrada) que contiene
				// una lista con las funciones de pertenencia ordenadas
				inOrderMfInput = new XfsgInOrderMemFunc[total_inputs];

				for (int i = 0; i < total_inputs; i++) {
					inOrderMfInput[i] = new XfsgInOrderMemFunc(var_inputs[i]);
					inOrderMfInput[i].sort();
				}

				/* Inicializa los MFCs */
				for (int i = 0; i < total_inputs; i++) {
					String tipo_input = tipo_input(inOrderMfInput[i]);
					

					nmf = inOrderMfInput[i].getSize();
					// Número de funciones de pertenencia
					num_mf = new Integer(nmf).toString();
					// Las pendientes que se calculan son las crecientes

					// Familia de tipo "triangular"
					if (tipo_input.equals("triangular")) {
						puntos_corte = "[ ";
						listaPuntos=new ArrayList<Double>();
						for (int j = 0; j < nmf; j++) {
							mf = inOrderMfInput[i].getMemFunc(j);
							double x1 = roundNum((mf.center() - mf.min())
									/ (mf.max() - mf.min()));
							listaPuntos.add(x1);
							puntos_corte += x1 + "   ";
						}
						puntos_corte += "];\n";
						pendientes = "[ ";
						listaPendientes=new ArrayList<Double>();
						for (int j = 0; j < nmf - 1; j++) {
							mf = inOrderMfInput[i].getMemFunc(j);
							mf2 = inOrderMfInput[i].getMemFunc(j + 1);
							double pendiente = roundNum((mf.max() - mf
									.min())
									/ (mf2.center() - mf.center()));
							listaPendientes.add(pendiente);
							pendientes += pendiente + "   ";
						}
						pendientes += "];\n";

						// Familia de tipo "sh_triangular"
					} else if (tipo_input.equals("sh_triangular")) {
						puntos_corte = "[ ";
						listaPuntos=new ArrayList<Double>();
						for (int j = 0; j < nmf; j++) {
							mf = inOrderMfInput[i].getMemFunc(j);
							double x1 = 0.0;
							x1 = roundNum((mf.get()[j] - mf.min())
									/ (mf.max() - mf.min()));
							listaPuntos.add(x1);
							puntos_corte += x1 + "   ";
						}
						puntos_corte += "];\n";
						pendientes = "[ ";
						listaPendientes=new ArrayList<Double>();
						for (int j = 0; j < nmf - 1; j++) {
							mf = inOrderMfInput[i].getMemFunc(j);
							double pendiente;
							pendiente = roundNum((mf.max() - mf.min())
									/ (mf.get()[j + 1] - mf.get()[j]));
							listaPendientes.add(pendiente);
							pendientes += pendiente + "   ";
						}
						pendientes += "];\n";

						// MFCs de tipo "triangle"
					} else if (tipo_input.equals("triangle")) {
						puntos_corte = "[ ";
						listaPuntos=new ArrayList<Double>();
						for (int j = 0; j < nmf; j++) {
							mf = inOrderMfInput[i].getMemFunc(j);
							double x1 = roundNum((mf.center() - mf.min())
									/ (mf.max() - mf.min()));
							listaPuntos.add(x1);
							puntos_corte += x1 + "   ";
						}
						puntos_corte += "];\n";
						pendientes = "[ ";
						listaPendientes=new ArrayList<Double>();
						for (int j = 0; j < nmf - 1; j++) {
							mf = inOrderMfInput[i].getMemFunc(j);
							mf2 = inOrderMfInput[i].getMemFunc(j + 1);
							double pendiente = roundNum((mf.max() - mf
									.min())
									/ (mf2.center() - mf.center()));
							listaPendientes.add(pendiente);
							pendientes += pendiente + "   ";
						}
						pendientes += "];\n";

						// MFCs de tipo "trapezoid"
					} else if (tipo_input.equals("sh_triangle")) {
						/* Puntos de Corte */
						puntos_corte = "[ ";
						listaPuntos=new ArrayList<Double>();
						for (int j = 0; j < nmf; j++) {
							mf = inOrderMfInput[i].getMemFunc(j);
							double x1 = 0.0;
							if (j == 0) {
								if (mf.getClass().toString().contains(
										"trapezoid"))
									x1 = roundNum((mf.get()[2] - mf.min())
											/ (mf.max() - mf.min()));
								else if (mf.getClass().toString().contains(
										"triangle"))
									x1 = roundNum((mf.center() - mf.min())
											/ (mf.max() - mf.min()));
							} else if (j == nmf - 1) {
								if (mf.getClass().toString().contains(
										"trapezoid"))
									x1 = roundNum((mf.get()[1] - mf.min())
											/ (mf.max() - mf.min()));
								else if (mf.getClass().toString().contains(
										"triangle"))
									x1 = roundNum((mf.center() - mf.min())
											/ (mf.max() - mf.min()));
							} else {
								x1 = roundNum((mf.center() - mf.min())
										/ (mf.max() - mf.min()));
							}
							listaPuntos.add(x1);
							puntos_corte += x1 + "   ";
						}
						puntos_corte += "];\n";
						pendientes = "[ ";
						listaPendientes=new ArrayList<Double>();
						for (int j = 0; j < nmf - 1; j++) {
							mf = inOrderMfInput[i].getMemFunc(j);
							mf2 = inOrderMfInput[i].getMemFunc(j + 1);
							double pendiente = 0.0;
							if (j == 0) {
								if (mf.getClass().toString().contains(
										"trapezoid"))
									pendiente = roundNum((mf.max() - mf
											.min())
											/ (mf2.center() - mf.get()[2]));
								else if (mf.getClass().toString().contains(
										"triangle"))
									pendiente = roundNum((mf.max() - mf
											.min())
											/ (mf2.center() - mf.center()));
							} else if (j == nmf - 2) {
								if (mf2.getClass().toString().contains(
										"trapezoid"))
									pendiente = roundNum((mf.max() - mf
											.min())
											/ (mf2.get()[1] - mf.center()));
								else if (mf.getClass().toString().contains(
										"triangle"))
									pendiente = roundNum((mf.max() - mf
											.min())
											/ (mf2.center() - mf.center()));
							} else
								pendiente = roundNum((mf.max() - mf.min())
										/ (mf2.center() - mf.center()));
							listaPendientes.add(pendiente);
							pendientes += pendiente + "   ";
						}
						pendientes += "];\n";
					}else if (tipo_input.equals("not found")) {
						
						puntos_corte = "N/A\n";
						listaPuntos=new ArrayList<Double>();
						pendientes = "N/A\n";
						listaPendientes=new ArrayList<Double>();
						flc.setEntradasNoNormalizadasMenor1(true);
					}
					flc.create_addMFC(num_mf, puntos_corte, pendientes, listaPuntos, listaPendientes);
						
				}
				
				Variable[] var_outputs = rbc.getRulebase().getOutputs();

				int total_outputs = var_outputs.length;
				flc.setoutputs(total_outputs);
				// Crea la estructura inOrderMfOutput[i] (donde el índice i
				// direcciona las distintas variables de salida) que contiene
				// una lista con las funciones de pertenencia ordenadas
				inOrderMfOutput = new XfsgInOrderMemFunc[total_outputs];
				for (int i = 0; i < total_outputs; i++) {
					inOrderMfOutput[i] = new XfsgInOrderMemFunc(var_outputs[i]);
					inOrderMfOutput[i].sort();
				}

				int maxn_fp = XfsgStaticMethods.CalculaMaximon_fp(rbc);
				int bitsn_fp = XfsgStaticMethods.ceillog2(maxn_fp);

				XfsgRulesMemData contenido = new XfsgRulesMemData(rbc
						.getNumberOfInputs()
						* bitsn_fp, rbc.getRulebase(), defuzzy);

				for (int i = 0; i < total_outputs; i++) {
					// code += "\n% RB" + (i+1) + "\n";
					/* Nombre de las funciones y Puntos */

					nmf = inOrderMfOutput[i].getSize();
					XfsgOutData outdata = new XfsgOutData(nmf, defuzzy);

					// Obtiene las etiquetas
					String[] labels = new String[nmf];

					// Calcula los valores para cada etiqueta según el
					// método de defuzzificación.
					if (defuzzy.equals("FuzzyMean")
							|| defuzzy.equals("MaxLabel")) {
						double[] values = new double[nmf];
						for (int j = 0; j < nmf; j++) {
							mf = inOrderMfOutput[i].getMemFunc2(j);
							labels[j] = mf.getLabel();
							min = mf.min();
							max = mf.max();
							values[j] = roundNum((mf.center() - min)
									/ (max - min));
						}
						outdata.setvalues(values);
					} else if (defuzzy.equals("WeightedFuzzyMean")
							|| defuzzy.equals("Quality")
							|| defuzzy.equals("GammaQuality")) {
						double[][] values2 = new double[nmf][2];
						for (int j = 0; j < nmf; j++) {
							mf = inOrderMfOutput[i].getMemFunc2(j);
							labels[j] = mf.getLabel();
							min = mf.min();
							max = mf.max();
							values2[j][0] = roundNum((mf.center() - min)
									/ (max - min));
							double gamma = 0;
							// Si el método de defuzzificación es GammaQuality
							// hay que calcular el valor de gamma. En los otros
							// casos no hace falta.
							if (defuzzy.equals("GammaQuality")) {
								gamma = rbc.getRulebase().getOperatorset().defuz
										.getSingleParameters()[0].value;
							}

							String clase = mf.getClass().toString();
							if (clase.contains("singleton")) {
								XfvhdlError err = new XfvhdlError();
								err.newWarning(37, "Defuzzification method: "
										+ defuzzy + ". Membership function: "
										+ mf.getClass() + ".");
							} else
								values2[j][1] = roundNum(calculateWeight2(mf,
										defuzzy, gamma));
						}
						outdata.setvalues2(values2);
					} else if (defuzzy.equals("TakagiSugeno")) {
						double[][] values2 = new double[nmf][3];
						for (int j = 0; j < nmf; j++) {
							mf = inOrderMfOutput[i].getMemFunc2(j);
							labels[j] = mf.getLabel();
							min = mf.min();
							max = mf.max();
							Parameter[] params = ((ParamMemFunc) mf)
									.getParameters();
							values2[j][0] = roundNum(params[0].value);
							values2[j][1] = roundNum(params[1].value);
							values2[j][2] = roundNum(params[2].value);
						}
						outdata.setvalues2(values2);
					}
					outdata.setlabels(labels);

					if (total_outputs != 1)
						outdata.setnombre("_" + var_outputs[i].getName());

					XfsgPrintRulesFactory factory = new XfsgPrintRulesFactory(
							total_inputs);
					printer = factory.create(contenido, defuzzy, name,
							bitsn_fp, outdata, inOrderMfInput, i);
					printer.printRules();

					outdata.setRB(printer.getRB());
					outdata.setpesos(printer.getPesos());
					if (i == 0) {
						flc.setgrados(printer.getGrados());
						flc.setcomplete(printer.getComplete());
					}
					flc.addRB(outdata);
				}
				// flc.generaComponente();
				listaflc.add(flc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**Método encargado de inicializar toda la información relativa a los 
	 * módulos crisp, capturándola de la especificación XFL.*/
	public void inicializarCrispBlocks() {

		int count = 0;

		SystemModule sistema = spec.getSystemModule();
		ModuleCall[] call = sistema.getModuleCalls();

		for (int i = 0; i < call.length; i++)
			if (call[i] instanceof CrispBlockCall)
				count++;

		CrispBlockCall cbcarray[] = new CrispBlockCall[count];

		for (int i = 0, j = 0; i < call.length; i++)
			if (call[i] instanceof CrispBlockCall) {
				cbcarray[j] = (CrispBlockCall) call[i];
				j++;
			}
		for (int i = 0; i < count; i++) {
			CrispBlockCall cbc = cbcarray[i];
			XfvhdlCrisp c = new XfvhdlCrisp(cbc.getName(), cbc);
			c.setinputs(cbc.getNumberOfInputs());
			c.setoutputs(cbc.getNumberOfOutputs());
			// c.generaComponente();
			listacrisp.add(c);
		}
	}

	/**Método de apoyo que redondea un double en su 4ª cifra decimal.*/
	private static double roundNum(double num) throws Exception {
		double valor = 0;
		valor = num;

		valor = valor * 10000;
		valor = java.lang.Math.round(valor);
		valor = valor / 10000;
		return valor;
	}

	/**
	 * 
	 * @param p Representa una función de pertenencia.
	 * @param defuzzy Cadena que indica el método de defuzzificación.
	 * @param gamma Valor para la variable gamma en caso de que el metodo de
	 *            defuzzificación sea GammaQuality, en otro caso, vale 0.
	 * @return Valor del parámetro de peso para ponderar la salida de una regla.
	 */
	private double calculateWeight2(LinguisticLabel p, String defuzzy,
			double gamma) {

		double universeRange = 0, w = 0;

		universeRange = p.max() - p.min();

		try {
			if (defuzzy.equals("WeightedFuzzyMean")) {
				w = p.basis() / universeRange;
			} else if (defuzzy.equals("GammaQuality")) {
				double aux = Math.pow(p.basis(), gamma);
				w = (1 / aux) / universeRange;
			} else if (defuzzy.equals("Quality")) {
				w = (1 / p.basis()) / universeRange;
			}
		} catch (Exception e) {
			new XfvhdlError(36, defuzzy);
			// err.show();
			return 0;
		}
		return w;
	}

	/**@param input Funciones de pertenencia de una entrada.
	 * @return Cadena representativa del tipo de las funciones 
	 * de pertenencia de una entrada en concreto.*/
	public String tipo_input(XfsgInOrderMemFunc input) {
		String res;
		// !!! CUIDADO: El orden es importante ya que, como está definido
		// actualmente, si sh_triang_fam es true también es true triang_fam
		if (sh_triang_fam(input))
			res = "sh_triangular";
		else if (triang_fam(input))
			res = "triangular";
		else if (triang_libres_norm_trap(input))
			res = "sh_triangle";
		else if (triang_libres_norm(input))
			res = "triangle";
		else
			res = "not found";
		return res;
	}

	/**@param input Funciones de pertenencia de una entrada.
	 * @return Indica si el tipo de las funciones 
	 * de pertenencia de la entrada son triangulos libres normalizados.*/
	private boolean triang_libres_norm(XfsgInOrderMemFunc input) {
		try {
			boolean res = true;
			int size = input.getSize();
			// comprueba primero que todos son triangulos libres
			for (int i = 0; i < size && res; i++) {
				LinguisticLabel mf = input.getMemFunc(i);
				if (mf instanceof ParamMemFunc) {
					String clase = mf.getClass().toString();
					if (!clase.contains("triangle"))
						res = false;
				} else
					res = false;
			}
			if (res) {
				// comprueba si están normalizados
				for (int i = 1; i < size && res; i++) {
					if (roundNum(input.getMemFunc(i).get()[0]) != roundNum(input
							.getMemFunc(i - 1).get()[1]))
						res = false;

					if (i == size - 1) {
						if (roundNum(input.getMemFunc(i).get()[1]) != roundNum(input
								.getMemFunc(i - 1).get()[2]))
							res = false;
						/*
						 * if (roundNum(input.getMemFunc(i).get()[0]) !=
						 * roundNum(input .getMemFunc(i - 1).get()[1])) res =
						 * false;
						 */
					}

				}
			}
			return res;
		} catch (Exception e) {
			return false;
		}
	}


	/**@param input Funciones de pertenencia de una entrada.
	 * @return Indica si el tipo de las funciones 
	 * de pertenencia de la entrada son triangulos libres normalizados 
	 * con saturación en los extremos.*/
	private boolean triang_libres_norm_trap(XfsgInOrderMemFunc input) {
		try {
			boolean res = true;
			boolean uno = false, dos = false, ambos = false;
			int size = input.getSize();
			// comprueba que los extremos son trapecios y el restos triangulos
			// libres
			for (int i = 0; i < size && res; i++) {
				LinguisticLabel mf = input.getMemFunc(i);
				if (mf instanceof ParamMemFunc) {
					String clase = mf.getClass().toString();
					if (i == 0) {
						if (clase.contains("trapezoid"))
							uno = true;
						else if (clase.contains("triangle"))
							uno = false;
						else
							res = false;
					} else if (i == size - 1) {
						if (clase.contains("trapezoid")) {
							dos = true;
							if (uno)
								ambos = true;
						} else if (clase.contains("triangle")) {
							dos = false;
							if (!uno)
								res = false;
						} else
							res = false;
					} else {
						if (!clase.contains("triangle"))
							res = false;
					}
				} else
					res = false;
			}

			if (res) {
				// comprueba si están normalizados (ambos extremos son
				// trapecios)
				if (ambos) {
					for (int i = 1; i < size && res; i++) {
						if (i == 1) {
							if (roundNum(input.getMemFunc(i).get()[0]) != roundNum(input
									.getMemFunc(i - 1).get()[2]))
								res = false;
						} else if (i == size - 1) {
							if (roundNum(input.getMemFunc(i).get()[0]) != roundNum(input
									.getMemFunc(i - 1).get()[1]))
								res = false;
							if (roundNum(input.getMemFunc(i).get()[1]) != roundNum(input
									.getMemFunc(i - 1).get()[2]))
								res = false;
						} else {
							if (roundNum(input.getMemFunc(i).get()[0]) != roundNum(input
									.getMemFunc(i - 1).get()[1]))
								res = false;
						}
					}
				}
				// comprueba si están normalizados (sólo el primer extremo es
				// trapecio)
				else if (uno) {
					for (int i = 1; i < size && res; i++) {
						if (i == 1) {
							if (roundNum(input.getMemFunc(i).get()[0]) != roundNum(input
									.getMemFunc(i - 1).get()[2]))
								res = false;
						} else if (i == size - 1) {
							if (roundNum(input.getMemFunc(i).get()[0]) != roundNum(input
									.getMemFunc(i - 1).get()[1]))
								res = false;
							if (roundNum(input.getMemFunc(i).get()[1]) != roundNum(input
									.getMemFunc(i - 1).get()[2]))
								res = false;
						} else {
							if (roundNum(input.getMemFunc(i).get()[0]) != roundNum(input
									.getMemFunc(i - 1).get()[1]))
								res = false;
						}
					}
				}
				// comprueba si están normalizados (sólo el segundo extremo es
				// trapecio)
				else if (dos) {
					for (int i = 1; i < size && res; i++) {
						if (i == 1) {
							if (roundNum(input.getMemFunc(i).get()[0]) != roundNum(input
									.getMemFunc(i - 1).get()[1]))
								res = false;
						} else if (i == size - 1) {
							if (roundNum(input.getMemFunc(i).get()[0]) != roundNum(input
									.getMemFunc(i - 1).get()[1]))
								res = false;
							if (roundNum(input.getMemFunc(i).get()[1]) != roundNum(input
									.getMemFunc(i - 1).get()[2]))
								res = false;
						} else {
							if (roundNum(input.getMemFunc(i).get()[0]) != roundNum(input
									.getMemFunc(i - 1).get()[1]))
								res = false;
						}
					}
				}

			}
			return res;
		} catch (Exception e) {
			return false;
		}
	}

	/**@param input Funciones de pertenencia de una entrada.
	 * @return Indica si el tipo de las funciones 
	 * de pertenencia de la entrada es una familia de triángulos.*/
	private boolean triang_fam(XfsgInOrderMemFunc input) {
		try {
			boolean res = true;
			int size = input.getSize();
			// comprueba que todos son de tipo triangular, es decir,
			// triangulos pertenecientes a una familia, y no libres
			for (int i = 0; i < size && res; i++) {
				LinguisticLabel mf = input.getMemFunc(i);
				if (mf instanceof FamiliarMemFunc) {
					String clase = ((FamiliarMemFunc) mf).getFamily()
							.getFunctionName();
					if (!clase.contains("triangular"))
						res = false;
				} else
					res = false;
			}
			return res;
		} catch (Exception e) {
			return false;
		}
	}

	/**@param input Funciones de pertenencia de una entrada.
	 * @return Indica si el tipo de las funciones 
	 * de pertenencia de la entrada es una familia de triángulos 
	 * con saturación en los extremos.*/
	private boolean sh_triang_fam(XfsgInOrderMemFunc input) {
		try {
			boolean res = true;
			int size = input.getSize();
			// comprueba que todos son de tipo triangular, es decir,
			// triangulos pertenecientes a una familia, y no libres,
			// excepto los extremos, que deben ser trapecios.
			for (int i = 0; i < size && res; i++) {
				LinguisticLabel mf = input.getMemFunc(i);
				if (mf instanceof FamiliarMemFunc) {
					String clase = ((FamiliarMemFunc) mf).getFamily()
							.getFunctionName();
					if (!clase.contains("sh_triangular"))
						res = false;
				} else
					res = false;
			}
			return res;
		} catch (Exception e) {
			return false;
		}
	}
	
	//MÉTODOS DE GENERACIÓN DE FICHERO DE JERARQUÍA
	
	/**
	 * @param listaGlobales Lista de variables globales.
	 * @param var Variable del sistema jerárquico.
	 * @return Indica si var es una variable interna, es decir, no es global.*/
	public boolean variableInterna(ArrayList<String> listaGlobales, String var){
		boolean interna=true;
		for(int i=0;i<listaGlobales.size();i++){
			if(listaGlobales.get(i).equals(var)){
				interna=false;
				i=listaGlobales.size();
			}
		}
		return interna;
	}
	
	/**@return Tamaño de var a la salida del módulo al que esté conectada.*/
	public String tamSalidaModuloConectada(String var,ModuleCall[] llamadas, ArrayList<XfvhdlFLC> listaFlc, ArrayList<XfvhdlCrisp> listaCrisp){
		String No="";
		int icrisp=0;
		int iflc=0;
		for(int i=0; i<llamadas.length;i++){
			if(llamadas[i] instanceof RulebaseCall){//Modulo de inferencia
				RulebaseCall rb=(RulebaseCall) llamadas[i];
				if(rb.getOutputVariables()[0].getName().equals(var)){
					No=listaFlc.get(iflc).getname()+"_No";
				}
				iflc++;
			}else{//Modulo crisp
				CrispBlockCall crisp=(CrispBlockCall) llamadas[i];
				if(crisp.getOutputVariables()[0].getName().equals(var)){
					No=listaCrisp.get(icrisp).getname()+"_No";
				}
				icrisp++;
			}
		}
		
		
		return No;
	}
	/**@return Mínimo nº de bits entre los de la salida de un módulo y 
	 * los de la entrada del siguiente, cuando var es interna.*/
	public int minimoBitsNoN(String var,ModuleCall[] llamadas, ArrayList<XfvhdlFLC> listaFlc, ArrayList<XfvhdlCrisp> listaCrisp, int bitsEntradaActual){
		int minBits=bitsEntradaActual;
		int icrisp=0;
		int iflc=0;
		for(int i=0; i<llamadas.length;i++){
			if(llamadas[i] instanceof RulebaseCall){//Modulo de inferencia
				RulebaseCall rb=(RulebaseCall) llamadas[i];
				if(rb.getOutputVariables()[0].getName().equals(var)){
					minBits=Math.min(minBits, listaFlc.get(iflc).getNo());
				}
				iflc++;
			}else{//Modulo crisp
				CrispBlockCall crisp=(CrispBlockCall) llamadas[i];
				if(crisp.getOutputVariables()[0].getName().equals(var)){
					minBits=Math.min(minBits, listaCrisp.get(icrisp).getNo());
				}
				icrisp++;
			}
		}
		
		return minBits;
		
	}
	/**@return Máximo nº de bits entre los de la entrada a un módulo, y la 
	 * entrada global del sistema con la que se conecta.*/
	public static int maximoBitsEntradaDeSistema(String var,ModuleCall[] llamadas, ArrayList<XfvhdlFLC> listaFlc, ArrayList<XfvhdlCrisp> listaCrisp){
		int maxBits=0;
		int icrisp=0;
		int iflc=0;
		for(int i=0; i<llamadas.length;i++){
			if(llamadas[i] instanceof RulebaseCall){//Modulo de inferencia
				RulebaseCall rb=(RulebaseCall) llamadas[i];
				for(int j=0;j<rb.getInputVariables().length;j++){
					if(rb.getInputVariables()[j].getName().equals(var)){
						maxBits=Math.max(listaFlc.get(iflc).getN(), maxBits);
					}
				}
				iflc++;
			}else{//Modulo crisp
				CrispBlockCall crisp=(CrispBlockCall) llamadas[i];
				for(int j=0;j<crisp.getInputVariables().length;j++){
					if(crisp.getInputVariables()[j].getName().equals(var)){
						maxBits=Math.max(listaCrisp.get(icrisp).getNo(), maxBits);
					}
				}
				icrisp++;
			}
		}
		return maxBits;
		
	}
	
	/**@return Bits adecuados para la salida del sistema.*/
	public static int bitsSalidaSistema(String var,ModuleCall[] llamadas, ArrayList<XfvhdlFLC> listaFlc, ArrayList<XfvhdlCrisp> listaCrisp){
		int bits=0;
		int icrisp=0;
		int iflc=0;
		for(int i=0; i<llamadas.length&&bits==0;i++){
			if(llamadas[i] instanceof RulebaseCall){//Modulo de inferencia
				RulebaseCall rb=(RulebaseCall) llamadas[i];
				for(int j=0;j<rb.getOutputVariables().length;j++){
					if(rb.getOutputVariables()[j].getName().equals(var)){
						bits=listaFlc.get(iflc).getNo();
					}
				}
				iflc++;
			}else{//Modulo crisp
				CrispBlockCall crisp=(CrispBlockCall) llamadas[i];
				for(int j=0;j<crisp.getOutputVariables().length;j++){
					if(crisp.getOutputVariables()[j].getName().equals(var)){
						bits=listaCrisp.get(icrisp).getNo();
					}
				}
				icrisp++;
			}
		}
		return bits;
	}
	
	/**@return Nombre de variable o señal que se conecta con una entrada 
	 * concreta de un módulo de inferencia.*/
	public String conectarAEntrada(XfvhdlFLC flc, int entrada, ModuleCall[] llamadas, 
			ArrayList<String> listaEntradasSistema,ArrayList<XfvhdlFLC> listaFlc,ArrayList<XfvhdlCrisp> listaCrisp){
		String res="", nombreModulo=flc.getname(),
			nombreEntrada="";
		boolean interna=true, señalCrisp=false;
		int icrisp=0,iflc=0,iseñal=0;
		int N=60,No=60;
		
		for(int i=0; i<llamadas.length;i++){
			if(llamadas[i] instanceof RulebaseCall){//Modulo de inferencia
				RulebaseCall rb=(RulebaseCall) llamadas[i];
				if(rb.getName().equals(nombreModulo)){
					nombreEntrada=rb.getInputVariables()[entrada].getName();
				}
				iflc++;
			}
		}
		for(int i=0;i<listaEntradasSistema.size()&&interna;i++){
			if(nombreEntrada.equals(listaEntradasSistema.get(i)))
				interna=false;
		}
		if(!interna){
			res+=nombreEntrada+"(";
		}
		iflc=0;
		icrisp=0;
		for(int i=0; i<llamadas.length&&interna;i++){
			
			if(llamadas[i] instanceof CrispBlockCall){//Bloque crisp
				CrispBlockCall crispB= (CrispBlockCall) llamadas[i];
				//prjFile.addFile("vhdl work \""+XfvhdlProperties.name_outputfiles+".vhd\"");
				if(crispB.getName().equals(nombreModulo)){
					
					nombreEntrada=crispB.getInputVariables()[entrada].getName();
					N=listaCrisp.get(icrisp).getNo();
					
				}
				if(crispB.getOutputVariable().getName().equals(nombreEntrada)){
					No=listaCrisp.get(icrisp).getNo();
					if(!señalesjerarquia.contains("signal s_"+listaCrisp.get(icrisp).getname()+"_out"))
						señalesjerarquia+="	signal s_"+listaCrisp.get(icrisp).getname()+"_out : std_logic_vector("+listaCrisp.get(icrisp).getname()+"_No);\n";
					señalCrisp=true;
					iseñal=icrisp;
					
				}
				
				icrisp++;
			}else{//Modulo de inferencia
				RulebaseCall rb=(RulebaseCall)llamadas[i];
				if(rb.getOutputVariables()[0].getName().equals(nombreEntrada)){
					No=listaFlc.get(iflc).getNo();
					if(!señalesjerarquia.contains("signal s_"+listaFlc.get(iflc).getname()+"_out"))
						señalesjerarquia+="	signal s_"+listaFlc.get(iflc).getname()+"_out : std_logic_vector("+listaFlc.get(iflc).getname()+"_No);\n";
					señalCrisp=false;
					iseñal=iflc;
				}
				iflc++;
			}
			//nombreEntrada="s_"+llamadas[i].getName()+"_in" + (i + 1);
		}
		
		
		
		if(señalCrisp&&interna){
			//listaCrisp.get(iseñal).getNo() es la salida de un módulo crip
			//flc.getN()                  es la entrada del siguiente módulo de inferencia
			if(listaCrisp.get(iseñal).getNo()==flc.getN()){
				asignaEntrada+="\ts_" + flc.getname()+"_in" + (entrada + 1)+" <= s_"+listaCrisp.get(iseñal).getname()+"_out;\n";
			}else if(listaCrisp.get(iseñal).getNo()>flc.getN()){
				asignaEntrada+="\ts_" + flc.getname()+"_in" + (entrada + 1)+" <= s_"+listaCrisp.get(iseñal).getname()+"_out" +
						"("+listaCrisp.get(iseñal).getNo()+" downto "+listaCrisp.get(iseñal).getNo()+"-"+flc.getname()+"_N+1);\n";
			}else if(listaCrisp.get(iseñal).getNo()<flc.getN()){
				asignaEntrada+="\ts_" + flc.getname()+"_in" + (entrada + 1)+" <= s_"+listaCrisp.get(iseñal).getname()+"_out &\"";
				for(int i=1; i <= (flc.getN()-listaCrisp.get(iseñal).getNo()) ;i++)
					asignaEntrada+="0";
				asignaEntrada+="\";\n";
			}
			
			
		}else if(!señalCrisp&&interna){
			//listaFlc.get(iseñal).getNo() es la salida de un módulo de inferencia
			//flc.getN()                   es la entrada del siguiente módulo de inferencia
			if(listaFlc.get(iseñal).getNo()==flc.getN()){
				asignaEntrada+="\ts_" + flc.getname()+"_in" + (entrada + 1)+" <= s_"+listaFlc.get(iseñal).getname()+"_out;\n";
			}else if(listaFlc.get(iseñal).getNo()>flc.getN()){
				asignaEntrada+="\ts_" + flc.getname()+"_in" + (entrada + 1)+" <= s_"+listaFlc.get(iseñal).getname()+"_out" +
						"("+listaFlc.get(iseñal).getNo()+" downto "+listaFlc.get(iseñal).getNo()+"-"+flc.getname()+"_N+1);\n";
			}else if(listaFlc.get(iseñal).getNo()<flc.getN()){
				asignaEntrada+="\ts_" + flc.getname()+"_in" + (entrada + 1)+" <= s_"+listaFlc.get(iseñal).getname()+"_out &\"";
				for(int i=1; i <= (flc.getN()-listaFlc.get(iseñal).getNo()) ;i++)
					asignaEntrada+="0";
				asignaEntrada+="\";\n";
			}
		}
		
		
		if(interna){
			res+="s_" + flc.getname()+"_in" + (entrada + 1);
			if(!señalesjerarquia.contains("signal s_"+flc.getname()+"_in" + (entrada + 1)))
				señalesjerarquia+="	signal s_"+flc.getname()+"_in" + (entrada + 1) + " : std_logic_vector("+flc.getname()+"_N downto 1);\n";
			
		}
		return res;
	}
	
	/**@return Nombre de variable o señal que se conecta con una entrada 
	 * concreta de un módulo crisp.*/
	public String conectarAEntrada(XfvhdlCrisp crisp, int entrada, ModuleCall[] llamadas, 
			ArrayList<String> listaEntradasSistema,ArrayList<XfvhdlFLC> listaFlc,ArrayList<XfvhdlCrisp> listaCrisp){
		
		String res="", nombreModulo=crisp.getname(),
			nombreEntrada=crisp.cbc.getInputVariables()[entrada].getName();
		boolean interna=true, señalCrisp=false;
		String funcion_crisp="";
		int icrisp=0,iflc=0,iseñal=0;
		int N=60,No=60;
		for(int i=0;i<listaEntradasSistema.size()&&interna;i++){
			if(crisp.cbc.getInputVariables()[entrada].getName().equals(listaEntradasSistema.get(i)))
				interna=false;
		}
		if(!interna){
			res+=nombreEntrada+"(";
		}
		
		icrisp=0;
		iflc=0;
		for(int i=0; i<llamadas.length&&interna;i++){
			
			if(llamadas[i] instanceof CrispBlockCall){//Bloque crisp
				CrispBlockCall crispB= (CrispBlockCall) llamadas[i];
				if(crispB.getName().equals(nombreModulo)){
					
					nombreEntrada=crispB.getInputVariables()[entrada].getName();
					N=listaCrisp.get(icrisp).getNo();
					
				}
				if(crispB.getOutputVariable().getName().equals(nombreEntrada)){
					No=listaCrisp.get(icrisp).getNo();
					if(!señalesjerarquia.contains("signal s_"+listaCrisp.get(icrisp).getname()+"_out"))
						señalesjerarquia+="	signal s_"+listaCrisp.get(icrisp).getname()+"_out : std_logic_vector("+listaCrisp.get(icrisp).getname()+"_No);\n";
					señalCrisp=true;
					iseñal=icrisp;
					
				}
				
				icrisp++;
			}else{//Modulo de inferencia
				RulebaseCall rb=(RulebaseCall)llamadas[i];
				if(rb.getOutputVariables()[0].getName().equals(nombreEntrada)){
					No=listaFlc.get(iflc).getNo();
					if(!señalesjerarquia.contains("signal s_"+listaFlc.get(iflc).getname()+"_out"))
						señalesjerarquia+="	signal s_"+listaFlc.get(iflc).getname()+"_out : std_logic_vector("+listaFlc.get(iflc).getname()+"_No);\n";
					señalCrisp=false;
					iseñal=iflc;
				}
				iflc++;
			}
		}
		
		
		for(int z=0;z<set_de_crisps.getBlocks().length;z++){
			String aux=set_de_crisps.getBlocks()[z].getLabel();
			if(aux.equalsIgnoreCase(crisp.getname())){
				funcion_crisp= set_de_crisps.getBlocks()[z].getFunctionName();}
		}
		
		
		
		if(señalCrisp&&interna){
			//listaCrisp.get(iseñal).getNo() es la salida de un módulo
			//crisp.getNo()                  es la entrada del siguiente módulo
			if(funcion_crisp.equalsIgnoreCase("select")&& entrada==0){
				asignaEntrada+="\ts_" + crisp.getname()+"_in" + (entrada + 1)+" <= s_"+listaCrisp.get(iseñal).getname()+"_out" +
					"("+listaCrisp.get(iseñal).getname()+"_No);\n";
			}else{
				if(listaCrisp.get(iseñal).getNo()==crisp.getNo()){
					asignaEntrada+="\ts_" + crisp.getname()+"_in" + (entrada + 1)+" <= s_"+listaCrisp.get(iseñal).getname()+"_out;\n";
				}else if(listaCrisp.get(iseñal).getNo()>crisp.getNo()){
					asignaEntrada+="\ts_" + crisp.getname()+"_in" + (entrada + 1)+" <= s_"+listaCrisp.get(iseñal).getname()+"_out" +
							"("+listaCrisp.get(iseñal).getNo()+" downto "+listaCrisp.get(iseñal).getNo()+"-"+crisp.getname()+"_No+1);\n";
				}else if(listaCrisp.get(iseñal).getNo()<crisp.getNo()){
					asignaEntrada+="\ts_" + crisp.getname()+"_in" + (entrada + 1)+" <= s_"+listaCrisp.get(iseñal).getname()+"_out &\"";
					for(int i=1; i <= (crisp.getNo()-listaCrisp.get(iseñal).getNo()) ;i++)
						asignaEntrada+="0";
					asignaEntrada+="\";\n";
				}
			}
			
			
		}else if(!señalCrisp&&interna){
			//listaFlc.get(iseñal).getNo() es la salida de un módulo
			//crisp.getNo()                  es la entrada del siguiente módulo
			if(funcion_crisp.equalsIgnoreCase("select")&& entrada==0){
				asignaEntrada+="\ts_" + crisp.getname()+"_in" + (entrada + 1)+" <= s_"+listaFlc.get(iseñal).getname()+"_out" +
					"("+listaFlc.get(iseñal).getname()+"_No);\n";
			}else{
				if(listaFlc.get(iseñal).getNo()==crisp.getNo()){
					asignaEntrada+="\ts_" + crisp.getname()+"_in" + (entrada + 1)+" <= s_"+listaFlc.get(iseñal).getname()+"_out;\n";
				}else if(listaFlc.get(iseñal).getNo()>crisp.getNo()){
					asignaEntrada+="\ts_" + crisp.getname()+"_in" + (entrada + 1)+" <= s_"+listaFlc.get(iseñal).getname()+"_out" +
							"("+listaFlc.get(iseñal).getNo()+" downto "+listaFlc.get(iseñal).getNo()+"-"+crisp.getname()+"_No+1);\n";
				}else if(listaFlc.get(iseñal).getNo()<crisp.getNo()){
					asignaEntrada+="\ts_" + crisp.getname()+"_in" + (entrada + 1)+" <= s_"+listaFlc.get(iseñal).getname()+"_out &\"";
					for(int i=1; i <= (crisp.getNo()-listaFlc.get(iseñal).getNo()) ;i++)
						asignaEntrada+="0";
					asignaEntrada+="\";\n";
				}
			}
		}
		
		if(interna){
			res+="s_" + crisp.getname()+"_in" + (entrada + 1);
			if(!señalesjerarquia.contains("signal s_"+crisp.getname()+"_in" + (entrada + 1))){
				if(funcion_crisp.equalsIgnoreCase("select")&& entrada==0){
					señalesjerarquia+="	signal s_"+crisp.getname()+"_in1 : std_logic;\n";
				}else
					señalesjerarquia+="	signal s_"+crisp.getname()+"_in" + (entrada + 1) + 
						" : std_logic_vector("+crisp.getname()+"_No downto 1);\n";
			}
			
		}
		return res;
	}
	
	/**@param listaFlc lista de todos los módulos de inferencia del 
	 * sistema jerárquico.
	 * @param listaCrisp lista de todos los módulos crisp del 
	 * sistema jerárquico.
	 * @return Código VHDL del nivel de la jerarquía del sistema jerárquico.*/
	public String generaJerarquia(ArrayList<XfvhdlFLC> listaFlc, ArrayList<XfvhdlCrisp> listaCrisp){
		ArrayList<String> listaVblesSistema=new ArrayList<String>(), listaEntradasSistema=new ArrayList<String>();
		ArrayList<String> listaSeñalesInternas=new ArrayList<String>();
		String conectaAsalida;
		int entradasSistema=spec.getSystemModule().getInputs().length;
		for (int i=0;i<entradasSistema;i++){
			listaVblesSistema.add(spec.getSystemModule().getInputs()[i].getName());
			listaEntradasSistema.add(spec.getSystemModule().getInputs()[i].getName());
		}
		for (int i=0;i<spec.getSystemModule().getOutputs().length;i++){
			listaVblesSistema.add(spec.getSystemModule().getOutputs()[i].getName());
		}		
		
		ArrayList<XfvhdlFLC> flcs=listaflc;
		String jerarquia="";
		String cadena_WORK="";
		String constantes_crisp="";
		String inputs="",outputs="",componentes="",
			entradasComponent, modulosPortMap="", inputsPortMap;
		//Esta lista contendrá todas las salidas del sistema para 
		//poder conectarla a la señal interna que le corresponda
		String[] outsSystem=new String[listaVblesSistema.size()-entradasSistema];;
		
		for (int i=0;i<listaflc.size();i++){
			cadena_WORK+="use WORK."+listaflc.get(i).getname()+"_Constants.all;\n";
		}
		for(int i=0;listaCrisp!=null && i<listaCrisp.size();i++){
			constantes_crisp+="\tconstant "+listaCrisp.get(i).getname()+"_No : integer := "+listaCrisp.get(i).getNo()+";\n";
		}
		
		if((listaflc.size()+listacrisp.size())==1)
			return null;
		
		for(int i=0;i<listaVblesSistema.size();i++){
			if(i<entradasSistema)
				inputs+="\t\t"+listaVblesSistema.get(i)+"	: in std_logic_vector("+maximoBitsEntradaDeSistema(listaVblesSistema.get(i),spec.getSystemModule().getModuleCalls(),listaFlc,listaCrisp)+" downto 1);		-- Input "+(i+1)+" signal.\n";
			else{
				outputs+="\t\t"+listaVblesSistema.get(i)+" : out std_logic_vector("+bitsSalidaSistema(listaVblesSistema.get(i),spec.getSystemModule().getModuleCalls(),listaFlc,listaCrisp)+" downto 1);	-- Output "+(i-entradasSistema+1)+" signal.\n";
				outsSystem[i-entradasSistema]=listaVblesSistema.get(i);
			}
		}
		//recorremos los modulescall
		ModuleCall[] llamadas=spec.getSystemModule().getModuleCalls();
		int iflc=0,icrisp=0;
		String variable;
		
		
		String funcion_crisp="";
		boolean pipelineConectado=false;
		String conexionPipeline="		pipeline => pipeline);\n\n";
		for (int i=0;i<llamadas.length;i++){
			entradasComponent="";
			inputsPortMap="";
			if(llamadas[i] instanceof RulebaseCall){//Modulo de inferencia
				for(int j=0;j<llamadas[i].getNumberOfInputs();j++){
					boolean interna=true;
					//Ayoma
					for(int k=0;k<listaVblesSistema.size();k++){//¿Es una variable interna?
						if(llamadas[i].getInputVariables()[j].getName().equals(listaVblesSistema.get(k)))
							interna=false;
					}
					XfvhdlFLC flc=listaFlc.get(iflc);
					entradasComponent+="		in"+(j+1)+"	: in std_logic_vector("+flcs.get(iflc).getname()+"_N downto 1);\n";
					variable=llamadas[i].getInputVariables()[j].getName();
					String tamDownto="", tam="";
					if(interna){
						//tam=tamSalidaModuloConectada(variable,llamadas,listaFlc,listaCrisp);
						//tamDownto+=minimoBitsNoN(variable,llamadas,listaFlc,listaCrisp,listaFlc.get(iflc).getN())+")";
					}else{
						tam=String.valueOf(maximoBitsEntradaDeSistema(variable,llamadas,listaFlc,listaCrisp));
						tamDownto+=" downto ("+maximoBitsEntradaDeSistema(variable,llamadas,listaFlc,listaCrisp)
						+"-"+flcs.get(iflc).getname()+"_N+1))";
					}
					//flcs.get(iflc)
					String conectaAentrada=conectarAEntrada(flc,j,llamadas,listaEntradasSistema,listaFlc,listaCrisp);
					inputsPortMap+="		in"+(j+1)+" => "
						+ conectaAentrada
						+(!conectaAentrada.startsWith("s_")?(tam+tamDownto):"")
						+",\n";
					
					//inputsPortMap+="		in"+(j+1)+" => "
					//	+conectarAEntrada(flcs.get(iflc),j,llamadas,listaEntradasSistema,listaFlc,listaCrisp)
					//	+tam
					//	+tamDownto+",\n";
				}
				iflc++;
			}else{//Bloque crisp
				
				for(int j=0;j<llamadas[i].getNumberOfInputs();j++){
					boolean interna=true;
					for(int k=0;k<listaVblesSistema.size();k++){//¿Es una variable interna?
						if(llamadas[i].getInputVariables()[j].getName().equals(listaVblesSistema.get(k)))
							interna=false;
					}
					XfvhdlCrisp crisp=listaCrisp.get(icrisp);
					entradasComponent+="		in"+(j+1)+"	: in std_logic_vector("+llamadas[i].getName()+"_No downto 1);\n";
					variable=llamadas[i].getInputVariables()[j].getName();
					String tamDownto="", tam="";
					if(interna){
						//tam=tamSalidaModuloConectada(variable,llamadas,listaFlc,listaCrisp);
						//tamDownto+=minimoBitsNoN(variable,llamadas,listaFlc,listaCrisp,listaCrisp.get(icrisp).getNo())+")";
					}else{
						tam=String.valueOf(maximoBitsEntradaDeSistema(variable,llamadas,listaFlc,listaCrisp));
						tamDownto+=" downto (" + maximoBitsEntradaDeSistema(variable,llamadas,listaFlc,listaCrisp)
						+"-"+crisp.getname()+"_No+1))";
					}
					String conectaAentrada=conectarAEntrada(crisp,j,llamadas,listaEntradasSistema,listaFlc,listaCrisp);
					inputsPortMap+="		in"+(j+1)+" => "
						+ conectaAentrada
						+(!conectaAentrada.startsWith("s_")?(tam+tamDownto):"")
						+",\n";
				}
					for(int z=0;z<set_de_crisps.getBlocks().length;z++){
						String aux=set_de_crisps.getBlocks()[z].getLabel();
						if(aux.equalsIgnoreCase(llamadas[i].getName())){
							funcion_crisp= set_de_crisps.getBlocks()[z].getFunctionName();}
					}
					
					if(funcion_crisp.equalsIgnoreCase("select")){
						funcion_crisp="selector";//select es una palabra reservada en VHDL
					}
					//add2, diff2, prod
					
					//Comprobamos si está o no conectado a la salida
					conectaAsalida=null;
					for(int l=0;l<outsSystem.length;l++){
						if(llamadas[i].getOutputVariables()[0].equals(outsSystem[l]))
							conectaAsalida=outsSystem[l];
					}
					modulosPortMap+="	"+XfvhdlProperties.name_outputfiles+"_" + 
									llamadas[i].getName()+" : "+funcion_crisp+"\n" +
									"	generic map (\n" +
									"		No => "+llamadas[i].getName()+"_No)\n" +
									"	port map (\n" +
									inputsPortMap+
						  			"		out1 => s_"+llamadas[i].getName()+"_out);\n\n" +
						  			(conectaAsalida!=null
						  					? "		" + conectaAsalida + " <= s_"+llamadas[i].getName()+"_out;\n\n"
						  					:"");
					if(!señalesjerarquia.contains("s_"+llamadas[i].getName()+"_out"))
						señalesjerarquia+="	signal s_"+llamadas[i].getName()+"_out : " +
										  "std_logic_vector("+llamadas[i].getName()+"_No downto 1);\n";	
					
					
				
				
				;
				icrisp++;
			}
			String cadenapipebits="";
			if(llamadas[i] instanceof RulebaseCall){
				cadenapipebits="	generic(\n"+
					"		"+llamadas[i].getName()+"_pipe: integer;\n"+
					"		"+llamadas[i].getName()+"_bits: integer);\n";
				componentes+="	component "+llamadas[i].getName()+"\n\n"+
						cadenapipebits+
						"	port (\n"+
						"		clk	: in std_logic;\n"+
						"		reset : in std_logic;\n"+
						entradasComponent+
						"		out1 : out std_logic_vector("+llamadas[i].getName()+"_No downto 1);\n"+
						"		pipeline : out std_logic);\n\n"+
						"	end component;\n\n"
						;
			}
			
			String auxpipebits="";
			if(llamadas[i] instanceof RulebaseCall){
				//Comprobamos si está o no conectado a la salida
				conectaAsalida=null;
				if(pipelineConectado)
					conexionPipeline="		pipeline => s_pipeline);\n\n";
				for(int l=0;l<outsSystem.length;l++){
					if(llamadas[i].getOutputVariables()[0].equals(outsSystem[l]))
						conectaAsalida=outsSystem[l];
				}
				auxpipebits+="	generic map(\n"+
					"		"+llamadas[i].getName()+"_pipe => max_pipe,\n"+
					"		"+llamadas[i].getName()+"_bits => max_bits)\n";
				modulosPortMap+="	"+XfvhdlProperties.name_outputfiles+"_"+llamadas[i].getName()+": "+llamadas[i].getName()+"\n"+
					auxpipebits+
					"	port map(\n"+
					"		clk => clk,\n"+
					"		reset => reset,\n"+
					inputsPortMap+
					"		out1 => s_"+llamadas[i].getName()+"_out,\n"+
					conexionPipeline +
					(conectaAsalida!=null
		  					? "		" + conectaAsalida + " <= s_"+llamadas[i].getName()+"_out;\n\n"
		  					:"");
				if(!señalesjerarquia.contains("s_pipeline"))
					señalesjerarquia+="	signal s_pipeline : " + "std_logic;\n";
				if(!señalesjerarquia.contains("s_"+llamadas[i].getName()+"_out"))
					señalesjerarquia+="	signal s_"+llamadas[i].getName()+"_out : " +
							"std_logic_vector("+llamadas[i].getName()+"_No downto 1);\n";
				pipelineConectado=true;
			}
			
			
		}
		componentes+="	constant max_pipe: integer:="+XfvhdlProperties.pipemax+";\n"+
				"	constant max_bits: integer:="+XfvhdlProperties.bitsmax+";\n";
		//recorremos los modulescall
		
		
		jerarquia+=
		"---------------------------------------------------------------------------\n"+
		"--                           Entity description                          --\n"+
		"---------------------------------------------------------------------------\n\n"+
		"library IEEE;\n"+
		"use IEEE.std_logic_1164.all;\n"+
		"use IEEE.std_logic_arith.all;\n"+
		"use IEEE.std_logic_unsigned.all;\n\n" +
		"library XfuzzyLib;\n" +
		"use XfuzzyLib.Entities.all;\n\n" +
		cadena_WORK+
		"\n" +
		"entity "+XfvhdlProperties.name_outputfiles+" is\n\n" +
		"	port(\n" +
		"		clk	: in std_logic;									-- Clock signal.\n" +
		"		reset : in std_logic;									-- Reset signal.\n" +
		inputs+
		outputs+
		"		pipeline : out std_logic);								-- Pipeline signal\n\n" +
		"end "+XfvhdlProperties.name_outputfiles+";\n\n" +
		"---------------------------------------------------------------------------\n"+
		"--                       Architecture description                        --\n"+
		"---------------------------------------------------------------------------\n\n"+
		"architecture FPGA of "+XfvhdlProperties.name_outputfiles+" is\n\n" +
		constantes_crisp+
		componentes+
		señalesjerarquia+"\n" +
		"begin\n\n" +
		asignaEntrada +
		"\n" +
		modulosPortMap+
		"\n\n"+
		"end FPGA;\n"
		;
		
		return jerarquia;
	}
	public void addToPrjCrispFunction(XfvhdlPrjFile prjFile){
		ModuleCall[] llamadas=spec.getSystemModule().getModuleCalls();
		for(int i=0; i<llamadas.length;i++){
			if(llamadas[i] instanceof CrispBlockCall){//Bloque crisp
				CrispBlockCall crispB= (CrispBlockCall) llamadas[i];
				prjFile.addFile("vhdl XfuzzyLib \""
			    	  	  + XfvhdlProperties.libraryDirectory
			    	  	  + XfvhdlProperties.fileSeparator+crispB.getCrispBlock().getFunctionName()+".vhd\"");
			}
		}
	}
}
