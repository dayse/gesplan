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

package xfuzzy.xfsg;

import java.util.ArrayList;
import xfuzzy.lang.CrispBlockCall;
import xfuzzy.lang.FamiliarMemFunc;
import xfuzzy.lang.LinguisticLabel;
import xfuzzy.lang.ModuleCall;
import xfuzzy.lang.ParamMemFunc;
import xfuzzy.lang.Parameter;
import xfuzzy.lang.RulebaseCall;
import xfuzzy.lang.Specification;
import xfuzzy.lang.SystemModule;
import xfuzzy.lang.Variable;

/**
 * Clase que incluye los métodos "inicializarFLCs" e "inicializarCrispBlocks"
 * encargados de construir los objetos de tipo XfsgFLC y XfsgCrisp de una
 * especificación XFL. También incluye métodos para determinar los tipos de
 * funciones de pertenencia y para calcular el segundo parámetro de ponderación
 * usado en determinados métodos de defuzzificacción.
 * 
 * @author Jesús Izquierdo Tena
 * @author Santiago Sánchez-Solano
 */

public class XfsgLeerXfl {

	// ATRIBUTOS
	private ArrayList<XfsgFLC> listaflc;

	private ArrayList<XfsgCrisp> listacrisp;

	private Specification spec;

	// El siguiente atributo es para las funciones de pertenencia de las
	// variables de entrada ordenadas
	private XfsgInOrderMemFunc[] inOrderMfInput;

	// El siguiente atributo es para las funciones de pertenencia de las
	// variables de salida ordenadas
	private XfsgInOrderMemFunc[] inOrderMfOutput;

	private XfsgSTPrintRules printer;

	// CONSTRUCTOR
	public XfsgLeerXfl(Specification spec) {
		this.spec = spec;
		listaflc = new ArrayList<XfsgFLC>();
		listacrisp = new ArrayList<XfsgCrisp>();
	}

	// MÉTODOS GET

	public ArrayList<XfsgFLC> getlistaflc() {
		return listaflc;
	}

	public ArrayList<XfsgCrisp> getlistacrisp() {
		return listacrisp;
	}

	public Specification getspec() {
		return spec;
	}

	// MÉTODOS SET

	public void setlistaflc(ArrayList<XfsgFLC> listaflc) {
		this.listaflc = listaflc;
	}

	public void setlistacrisp(ArrayList<XfsgCrisp> listacrisp) {
		this.listacrisp = listacrisp;
	}

	public void setspec(Specification spec) {
		this.spec = spec;
	}

	public void inicializarFLCs() {

		String num_mf = "", puntos_corte = "", pendientes = "";
		SystemModule sistema = spec.getSystemModule();
		RulebaseCall rbcarray[] = sistema.getRulebaseCalls();
		try {
			for (int s = 0; s < rbcarray.length; s++) {

				RulebaseCall rbc = rbcarray[s];

				String name = rbc.getName();
				/* MFC */
				// Crea el FLC
				XfsgFLC flc = new XfsgFLC(name, rbc);
				String defuzzy = rbc.getRulebase().getOperatorset().defuz
						.getFunctionName();
				flc.setdefuzzy(defuzzy);
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

					// System.out.println(tipo_input);
					if (tipo_input.equals("not found")) {
						XfsgError err = new XfsgError();
						err.newWarning(12, inOrderMfInput[i].getVariable()
								.getName());
					} else {
						nmf = inOrderMfInput[i].getSize();
						// Número de funciones de pertenencia
						num_mf = new Integer(nmf).toString();
						// Las pendientes que se calculan son las crecientes

						// Familia de tipo "triangular"
						if (tipo_input.equals("triangular")) {
							puntos_corte = "[ ";
							for (int j = 0; j < nmf; j++) {
								mf = inOrderMfInput[i].getMemFunc(j);
								double x1 = roundNum((mf.center() - mf.min())
										/ (mf.max() - mf.min()));
								puntos_corte += x1 + "   ";
							}
							puntos_corte += "];\n";
							pendientes = "[ ";
							for (int j = 0; j < nmf - 1; j++) {
								mf = inOrderMfInput[i].getMemFunc(j);
								mf2 = inOrderMfInput[i].getMemFunc(j + 1);
								double pendiente = roundNum((mf.max() - mf
										.min())
										/ (mf2.center() - mf.center()));
								pendientes += pendiente + "   ";
							}
							pendientes += "];\n";

							// Familia de tipo "sh_triangular"
						} else if (tipo_input.equals("sh_triangular")) {
							puntos_corte = "[ ";
							for (int j = 0; j < nmf; j++) {
								mf = inOrderMfInput[i].getMemFunc(j);
								double x1 = 0.0;
								x1 = roundNum((mf.get()[j] - mf.min())
										/ (mf.max() - mf.min()));
								puntos_corte += x1 + "   ";
							}
							puntos_corte += "];\n";
							pendientes = "[ ";
							for (int j = 0; j < nmf - 1; j++) {
								mf = inOrderMfInput[i].getMemFunc(j);
								double pendiente;
								pendiente = roundNum((mf.max() - mf.min())
										/ (mf.get()[j + 1] - mf.get()[j]));
								pendientes += pendiente + "   ";
							}
							pendientes += "];\n";

							// MFCs de tipo "triangle"
						} else if (tipo_input.equals("triangle")) {
							puntos_corte = "[ ";
							for (int j = 0; j < nmf; j++) {
								mf = inOrderMfInput[i].getMemFunc(j);
								double x1 = roundNum((mf.center() - mf.min())
										/ (mf.max() - mf.min()));
								puntos_corte += x1 + "   ";
							}
							puntos_corte += "];\n";
							pendientes = "[ ";
							for (int j = 0; j < nmf - 1; j++) {
								mf = inOrderMfInput[i].getMemFunc(j);
								mf2 = inOrderMfInput[i].getMemFunc(j + 1);
								double pendiente = roundNum((mf.max() - mf
										.min())
										/ (mf2.center() - mf.center()));
								pendientes += pendiente + "   ";
							}
							pendientes += "];\n";

							// MFCs de tipo "trapezoid"
						} else if (tipo_input.equals("sh_triangle")) {
							/* Puntos de Corte */
							puntos_corte = "[ ";
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
								puntos_corte += x1 + "   ";
								// System.out.println(puntos_corte);
							}
							puntos_corte += "];\n";
							pendientes = "[ ";
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
								pendientes += pendiente + "   ";
							}
							pendientes += "];\n";
						}
						flc.crear_añadirMFC(num_mf, puntos_corte, pendientes);
					}
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
				// System.out.println(maxn_fp);
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
								XfsgError err = new XfsgError();
								err.newWarning(10, "Defuzzification method: "
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
					// System.out.println(printer.getPesos());
					if (i == 0) {
						flc.setgrados(printer.getGrados());
						flc.setcomplete(printer.getComplete());
					}
					flc.añadirRB(outdata);
				}
				// flc.generaComponente();
				listaflc.add(flc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inicializarCrispBlocks() {

		// String code="";
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
			XfsgCrisp c = new XfsgCrisp(cbc.getName(), cbc);
			c.setinputs(cbc.getNumberOfInputs());
			c.setoutputs(cbc.getNumberOfOutputs());
			// c.generaComponente();
			listacrisp.add(c);
		}
	}

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
	 * @param p
	 *            Representa una función de pertenencia
	 * @param defuzzy
	 *            Cadena que indica el método de defuzzificación
	 * @param gamma
	 *            Valor para la variable gamma en caso de que el metodo de
	 *            defuzzificación sea GammaQuality, en otro caso, vale 0
	 * @return el valor del parámetro w para ponderar la salida de una regla
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
			new XfsgError(5, defuzzy);
			// err.show();
			return 0;
		}
		return w;
	}

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
}
