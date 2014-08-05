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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import xfuzzy.lang.CrispBlockCall;
import xfuzzy.lang.ModuleCall;
import xfuzzy.lang.RulebaseCall;
import xfuzzy.lang.Specification;
import xfuzzy.lang.SystemModule;
import xfuzzy.lang.Variable;

/**
 * Clase que mantiene un directorio de los componentes disponibles en la
 * librería XfuzzyLib y la información utilizada para generar los ficheros .txt
 * y .mdl
 * 
 * @author Jesús Izquierdo Tena
 */
public class XfsgArchitecturesSimulink {

	// Almacena el módulo de la librería XfuzzyLib que implementa cada bloque de
	// un sistema difuso (base de reglas o bloque crisp)
	private static Map<String, String> dir_architectures = new TreeMap<String, String>();

	// Almacena las coordenadas de cada bloque dentro del modelo Simulink
	private static Map<String, int[]> coorden_comp = new TreeMap<String, int[]>();

	private static Map<String, Collection<int[]>> conexiones = new TreeMap<String, Collection<int[]>>();

	// Almacena las coordenadas de cada entrada dentro del modelo Simulink
	private static Map<String, int[]> coorden_inputs = new TreeMap<String, int[]>();

	// Almacena las coordenadas de cada salida dentro del modelo Simulink
	private static Map<String, int[]> coorden_outputs = new TreeMap<String, int[]>();

	
	// ----------------------------------------------------------------------------//
	// CONSTRUCTORES //
	// ----------------------------------------------------------------------------//

	public XfsgArchitecturesSimulink() {
		// Directorio de los diferentes componentes de XfuzzyLib
		// FLCs
		dir_architectures.put("1-1-prod-FuzzyMeans", "XfuzzyLib/FLCs/FLC1_FMs");
		dir_architectures.put("2-1-prod-FuzzyMeans", "XfuzzyLib/FLCs/FLC2_P_FMs");
		dir_architectures.put("2-2-prod-FuzzyMeans", "XfuzzyLib/FLCs/FLC2_P_FMs_2");
		dir_architectures.put("2-1-prod-FuzzyMean", "XfuzzyLib/FLCs/FLC2_P_FM");
		dir_architectures.put("2-1-min-FuzzyMean_extD", "XfuzzyLib/FLCs/FLC2_M_FM_extD");
		dir_architectures.put("2-1-min-FuzzyMean", "XfuzzyLib/FLCs/FLC2_M_FM");
		dir_architectures.put("2-1-prod-MaxLabel", "XfuzzyLib/FLCs/FLC2_P_ML");
		dir_architectures.put("2-1-min-MaxLabel", "XfuzzyLib/FLCs/FLC2_M_ML");
		dir_architectures.put("2-1-prod-WeightedFuzzyMean", "XfuzzyLib/FLCs/FLC2_P_WFM");
		dir_architectures.put("2-1-prod-WeightedFuzzyMean_extD", "XfuzzyLib/FLCs/FLC2_P_WFM_extD");
		dir_architectures.put("2-1-min-WeightedFuzzyMean", "XfuzzyLib/FLCs/FLC2_M_WFM");
		dir_architectures.put("2-1-min-WeightedFuzzyMean_extD", "XfuzzyLib/FLCs/FLC2_M_WFM_extD");
		dir_architectures.put("2-1-prod-Quality", "XfuzzyLib/FLCs/FLC2_P_WFM");
		dir_architectures.put("2-1-prod-GammaQuality", "XfuzzyLib/FLCs/FLC2_P_WFM");
		dir_architectures.put("2-1-prod-TakagiSugenos", "XfuzzyLib/FLCs/FLC2_P_TS1s");
		dir_architectures.put("2-1-prod-TakagiSugeno_extD", "XfuzzyLib/FLCs/FLC2_P_TS1_extD");
		dir_architectures.put("2-1-prod-TakagiSugeno", "XfuzzyLib/FLCs/FLC2_P_TS1");
		dir_architectures.put("2-1-min-TakagiSugeno_extD", "XfuzzyLib/FLCs/FLC2_M_TS1_extD");
		dir_architectures.put("2-1-min-TakagiSugeno", "XfuzzyLib/FLCs/FLC2_M_TS1");
		// Bloques Crisp
		dir_architectures.put("select", "XfuzzyLib/Crisp/select");
		dir_architectures.put("prod", "XfuzzyLib/Crisp/prod");
		dir_architectures.put("div", "XfuzzyLib/Crisp/div");
		dir_architectures.put("add2", "XfuzzyLib/Crisp/add2");
		dir_architectures.put("addN3", "XfuzzyLib/Crisp/add3");
		dir_architectures.put("addN4", "XfuzzyLib/Crisp/add4");
		dir_architectures.put("diff2", "XfuzzyLib/Crisp/diff2");
	}

	public XfsgArchitecturesSimulink(String name, int x, int y, int n) {
		int[] c = { x, y };
		if (n == 0)
			coorden_comp.put(name, c);
		else if (n == 1)
			coorden_inputs.put(name, c);
		else if (n == 2)
			coorden_outputs.put(name, c);
	}

	public XfsgArchitecturesSimulink(String conexion, Collection<int[]> puntos) {
		conexiones.put(conexion, puntos);
		// System.out.println("Conexión: "+conexion+" Puntos: "+puntos.size());
	}

	/**
	 * Función que genera la descripción txt de un componente: 
	 * FLC o bloque crisp
	 */
	public String getDescriptionTxt(XfsgBlock block, String propiedades) {
		String txt = "";
		String propiedades2 = propiedades;
		String architecture1;
		if (block instanceof XfsgFLC) {
			XfsgFLC aux_flc = (XfsgFLC) block;
			if (aux_flc.getconnective().equals("prod")
					&& XfsgProperties.use_components_simplified
					&& (aux_flc.getdeffuzy().equals("FuzzyMean") || aux_flc
							.getdeffuzy().equals("TakagiSugeno"))) {
				propiedades2 = propiedades2 + "s";

				if (!aux_flc.getcomplete()) {
					XfsgError err = new XfsgError();
					err.newWarning(9, aux_flc.getname() + "  in .txt file");
				}
			}

			if (XfsgProperties.use_components_simplified) {
				if (!aux_flc.getconnective().equals("prod")
						|| !(aux_flc.getdeffuzy().equals("FuzzyMean") || aux_flc
								.getdeffuzy().equals("TakagiSugeno"))) {
					XfsgError err = new XfsgError();
					err.newWarning(13, aux_flc.getname() + "  in .txt file");
				}
			}

			architecture1 = dir_architectures.get(propiedades2);
			RulebaseCall rbc = (RulebaseCall) aux_flc.rbc;
			txt += "\nRuleBase: " + rbc.getName() + "\n{\n";
			txt += "Inputs: \n";
			Variable[] var_inputs = rbc.getInputVariables();
			for (int i = 0; i < rbc.getNumberOfInputs(); i++) {
				txt += "	" + var_inputs[i].getName() + "\n";
			}
			txt += "Outputs: \n";
			Variable[] var_outputs = rbc.getOutputVariables();
			for (int i = 0; i < rbc.getNumberOfOutputs(); i++) {
				txt += "	" + var_outputs[i].getName() + "\n";
			}
			txt += "Component: " + "\n	" + architecture1 + "\n}\n";
		}

		else if (block instanceof XfsgCrisp) {
			XfsgCrisp aux_crisp = (XfsgCrisp) block;
			architecture1 = dir_architectures.get(propiedades2);
			CrispBlockCall cbc = (CrispBlockCall) aux_crisp.cbc;
			txt += "\nCrispBlock: " + cbc.getName() + "\n{\n";
			txt += "Inputs: \n";
			Variable[] var_inputs = cbc.getInputVariables();
			for (int i = 0; i < cbc.getNumberOfInputs(); i++) {
				txt += "	" + var_inputs[i].getName() + "\n";
			}
			txt += "Outputs: \n";
			Variable[] var_outputs = cbc.getOutputVariables();
			for (int i = 0; i < cbc.getNumberOfOutputs(); i++) {
				txt += "	" + var_outputs[i].getName() + "\n";
			}
			txt += "Component: " + "\n	" + architecture1 + "\n}\n";
		}
		return txt;
	}

	/**
	 * Función que genera la descripción mdl de un componente: 
	 * FLC o bloque crisp
	 */
	public String getDescriptionMdl(XfsgBlock block, String propiedades) {

		String mdl = "";
		String propiedades2 = propiedades;
		int[] coord = coorden_comp.get(block.getname());

		// Coordenadas del nuevo componente
		int x1 = coord[0];
		int y1 = coord[1];
		int x2 = x1 + 100;
		int y2 = y1 + 100;

		String architecture;
		String name = block.getname();
		if (block instanceof XfsgFLC) {
			XfsgFLC aux_flc = (XfsgFLC) block;
			if (aux_flc.getconnective().equals("prod")
					&& XfsgProperties.use_components_simplified
					&& (aux_flc.getdeffuzy().equals("FuzzyMean") || aux_flc
							.getdeffuzy().equals("TakagiSugeno"))) {
				propiedades2 = propiedades2 + "s";

				if (!aux_flc.getcomplete()) {
					XfsgError err = new XfsgError();
					err.newWarning(9, aux_flc.getname() + "  in .mdl file");
				}
			}

			if (XfsgProperties.use_components_simplified) {
				if (!aux_flc.getconnective().equals("prod")
						|| !(aux_flc.getdeffuzy().equals("FuzzyMean") || aux_flc
								.getdeffuzy().equals("TakagiSugeno"))) {
					XfsgError err = new XfsgError();
					err.newWarning(13, aux_flc.getname() + "  in .mdl file");
				}

			}

			architecture = dir_architectures.get(propiedades2);
			if (architecture == null) {
				XfsgError err = new XfsgError();
				err.newWarning(11, name);
			}
			mdl = "		Block {\n" + "			BlockType		      Reference\n"
					+ "			Name		      \"" + name + "\"\n" + "			Ports		      ["
					+ block.getinputs() + " , " + block.getoutputs() + "]\n"
					+ "			Position		      [" + x1 + "," + y1 + "," + x2 + ","
					+ y2 + "]\n" + "			SourceBlock	      \"" + architecture
					+ "\"\n" + "			SourceType	      \"Subsystem\"\n"
					+ "			ShowPortLabels	      \"FromPortIcon\"\n"
					+ "			SystemSampleTime	      \"-1\"\n"
					+ "			FunctionWithSeparateData off\n"
					+ "			RTWMemSecFuncInitTerm   \"Inherit from model\"\n"
					+ "			RTWMemSecFuncExecute    \"Inherit from model\"\n"
					+ "			RTWMemSecDataConstants  \"Inherit from model\"\n"
					+ "			RTWMemSecDataInternal   \"Inherit from model\"\n"
					+ "			RTWMemSecDataParameters \"Inherit from model\"\n"
					+ "			N				\"" + name + ".N\"\n" + "			grad			\"" + name
					+ ".grad\"\n" + "			P				\"" + name + ".P\"\n"
					+ "			No				\"" + name + ".No\"\n" + "			Pb				\"" + name
					+ ".Pb\"\n";
			int n_inputs = aux_flc.getinputs();
			int n_outputs = aux_flc.getoutputs();
			if (n_inputs == 1) {
				mdl += "			n_fp				\"" + name + ".n_fp\"\n" + "			MFC_a			\""
						+ name + ".MFC_a\"\n" + "			MFC_m				\"" + name
						+ ".MFC_m\"\n";
			} else if (n_inputs > 1) {
				for (int i = 0; i < n_inputs; i++) {
					mdl += "			n_fp" + (i + 1) + "				\"" + name + ".MFC"
							+ (i + 1) + ".n_fp\"\n" + "			MFC_a" + (i + 1)
							+ "			\"" + name + ".MFC" + (i + 1) + ".MFC_a\"\n"
							+ "			MFC_m" + (i + 1) + "				\"" + name + ".MFC"
							+ (i + 1) + ".MFC_m\"\n";
				}
			}
			if (n_outputs == 1) {
				mdl += "			RB				\"" + name + ".RB"
						+ aux_flc.getRB().get(0).getnombre() + "\"\n";
				mdl += "		}\n";
			} else if (n_outputs > 1) {
				for (int i1 = 0; i1 < n_outputs; i1++) {
					mdl += "			RB" + (i1 + 1) + "				\"" + name + ".RB"
							+ aux_flc.getRB().get(i1).getnombre() + "\"\n";
				}
				mdl += "		}\n";
			}

		} else if (block instanceof XfsgCrisp) {
			architecture = dir_architectures.get(propiedades2);
			if (architecture == null) {
				XfsgError err = new XfsgError();
				err.newWarning(14, name);
			}

			mdl = "		Block {\n" + "			BlockType		      Reference\n"
					+ "			Name		      \"" + name + "\"\n" + "			Ports		      ["
					+ block.getinputs() + " , " + block.getoutputs() + "]\n"
					+ "			Position		      [" + x1 + "," + y1 + "," + x2 + ","
					+ y2 + "]\n" + "			SourceBlock	      \"" + architecture
					+ "\"\n" + "			SourceType	      \"Subsystem\"\n"
					+ "			ShowPortLabels	      \"FromPortIcon\"\n"
					+ "			SystemSampleTime	      \"-1\"\n"
					+ "			FunctionWithSeparateData off\n"
					+ "			RTWMemSecFuncInitTerm   \"Inherit from model\"\n"
					+ "			RTWMemSecFuncExecute    \"Inherit from model\"\n"
					+ "			RTWMemSecDataConstants  \"Inherit from model\"\n"
					+ "			RTWMemSecDataInternal   \"Inherit from model\"\n"
					+ "			RTWMemSecDataParameters \"Inherit from model\"\n"
					+ "			No				\"" + name + ".No\"\n" + "		}\n";
		}

		return mdl;
	}

	/**
	 * generaConexiones
	 */
	public static String generaConexiones(Specification spec) {
		String res = "";
		SystemModule sm = spec.getSystemModule();
		ModuleCall[] mc = sm.getModuleCalls();
		// Mapas para almacenar las coordenadas de las entradas y las salidas.
		Map<String, int[]> coord_inputs = new TreeMap<String, int[]>();
		Map<String, int[]> coord_outputs = new TreeMap<String, int[]>();

		// Coloca las entradas
		Variable[] inputs = sm.getInputs();
		if (inputs.length >= 1) {
			for (int i = 0; i < inputs.length; i++) {
				int[] coord_aux = coorden_inputs.get(inputs[i].getName());
				// Coordenadas de la entrada
				int x1 = coord_aux[0];
				int y1 = coord_aux[1];
				int x2 = x1 + 20;
				int y2 = y1 + 20;
				int coord[] = { x1, y1, x2, y2 };
				coord_inputs.put(inputs[i].getName(), coord);
				res += "		Block {\n" + "			BlockType		      Inport\n"
						+ "			Name		      \"" + inputs[i].getName() + "\"\n"
						+ "			Position		      [" + x1 + "," + y1 + "," + x2
						+ "," + y2 + "]\n"
						+ "			IconDisplay	      \"Port number\"\n"
						+ "			OutDataType	      \"sfix(16)\"\n"
						+ "			OutScaling	      \"2^0\"\n" + "		}\n";
			}
		}
		// Coloca las salidas.
		Variable[] outputs = sm.getOutputs();
		if (outputs.length >= 1) {
			for (int i = 0; i < outputs.length; i++) {
				int[] coord_aux = coorden_outputs.get(outputs[i].getName());
				// Coordenadas de la salida
				int x1 = coord_aux[0];
				int y1 = coord_aux[1];
				int x2 = x1 + 20;
				int y2 = y1 + 20;
				int coord[] = { x1, y1, x2, y2 };
				coord_outputs.put(outputs[i].getName(), coord);
				res += "		Block {\n" + "			BlockType		      Outport\n"
						+ "			Name		      \"" + outputs[i].getName() + "\"\n"
						+ "			Position		      [" + x1 + "," + y1 + "," + x2
						+ "," + y2 + "]\n"
						+ "			IconDisplay	      \"Port number\"\n"
						+ "			OutDataType	      \"sfix(16)\"\n"
						+ "			OutScaling	      \"2^0\"\n" + "		}\n";
			}
		}
		// Coloca las conexiones internas
		for (int i = 0; i < mc.length; i++) {
			ModuleCall mc1 = mc[i];
			Variable[] salidas = mc1.getOutputVariables();
			for (int k = 0; k < salidas.length; k++) {
				res += "		Line {\n" + "			SrcBlock		      \"" + mc1.getName()
						+ "\"\n" + "			SrcPort		      " + (k + 1) + "\n"
						+ "			Points		      [0, 0]\n";
				for (int j = 0; j < mc.length; j++) {
					ModuleCall mc2 = mc[j];
					Variable[] entradas = mc2.getInputVariables();
					for (int l = 0; l < entradas.length; l++) {
						if (entradas[l].equals(salidas[k])) {
							String aux6 = genera_cable2(entradas[l].getName()
									+ mc1.getName() + mc2.getName(), mc1, mc2,
									l + 1, k + 1);
							// String
							// aux6=genera_cable(entradas[l].getName()+mc1.getName()+mc2.getName());
							res += "			Branch {\n" + "				Points			" + aux6
									+ "\n" + "				DstBlock		\"" + mc2.getName()
									+ "\"\n" + "				DstPort			" + (l + 1)
									+ "\n" + "			}\n";
						}
					}
				}
				// Comprueba las salidas hacia fuera
				for (int i2 = 0; i2 < outputs.length; i2++) {
					if (outputs[i2].equals(salidas[k])) {
						String aux6 = genera_cable3(outputs[i2].getName()
								+ mc1.getName() + "null",
								outputs[i2].getName(), mc1, k + 1);
						// String
						// aux6=genera_cable(outputs[i2].getName()+mc1.getName()+"null");
						res += "			Branch {\n" + "				Points			" + aux6 + "\n"
								+ "				DstBlock		\"" + outputs[i2].getName()
								+ "\"\n" + "				DstPort			1\n" + "			}\n";
					}
				}
				res += "		}\n";
			}
		}
		// Crea el código para las conexiones de las variables de entrada con
		// los FLCs
		for (int i = 0; i < inputs.length; i++) {
			res += " 		Line {\n" + "			SrcBlock		      \""
					+ inputs[i].getName() + "\"\n" + "			SrcPort		      1\n"
					+ "			Points		      [0, 0]\n";

			for (int k = 0; k < mc.length; k++) {
				ModuleCall mc1 = mc[k];
				Variable[] entradas = mc1.getInputVariables();
				for (int j = 0; j < entradas.length; j++) {
					if (entradas[j].equals(inputs[i])) {
						String aux6 = genera_cable1(entradas[j].getName()
								+ "null" + mc1.getName(), inputs[i].getName(),
								mc1, j + 1);
						// String
						// aux6=genera_cable(entradas[j].getName()+"null"+mc1.getName());
						res += "			Branch {\n" + "				Points			" + aux6 + "\n"
								+ "				DstBlock		\"" + mc1.getName() + "\"\n"
								+ "				DstPort			" + (j + 1) + "\n" + "			}\n";
					}
				}
			}
			res += "		}\n";
		}
		return res;
	}

	/**
	 * genera_cable
	 */
	private static String genera_cable(String conex) {
		String cable = "[ ", cable2 = "";
		Collection<int[]> c = conexiones.get(conex);
		Iterator<int[]> it = c.iterator();
		while (it.hasNext()) {
			int[] aux = it.next();
			cable += (aux[2] - aux[0]) + " , " + (aux[3] - aux[1]) + " ; ";
		}
		cable += "]";
		cable = cable.replace(" ; ]", "]");
		// resto 30 a la última coordenada de fila
		int n = cable.lastIndexOf(";");
		cable2 = cable.substring(0, n + 1);
		int m = cable.lastIndexOf(",");
		String valor = cable.substring(n + 1, m - 1).trim();
		int valor1 = Integer.parseInt(valor);
		// String aux=cable.substring(n+1, m-1);
		cable2 += " " + (valor1 - 30) + cable.substring(m);

		return cable2;
	}

	/**
	 * genera_cable1
	 */
	private static String genera_cable1(String conex, String input,
			ModuleCall mc1, int ent) {
		String cable = "[ ";
		Collection<int[]> c = conexiones.get(conex);
		// int tam=c.size();
		// int i=1;
		boolean end = false;
		int[] coord1 = coorden_inputs.get(input);
		int salx = coord1[0] + 20;
		int saly = coord1[1] + 10;
		// System.out.println(salx+" "+saly);
		int[] coord2 = coorden_comp.get(mc1.getName());
		int metx = coord2[0] - 30;
		int mety = coord2[1]
				+ (int) calcula_coord(mc1.getNumberOfInputs(), ent);
		// System.out.println(metx+" "+mety);
		Iterator<int[]> it = c.iterator();
		while (it.hasNext() && !end) {
			int[] aux = it.next();
			if ((salx + (aux[2] - aux[0])) > metx) {
				cable += (metx - salx) + " , 0 ;";
				salx += metx - salx;
				end = true;
			} else {
				salx += aux[2] - aux[0];
				saly += aux[3] - aux[1];
				cable += (aux[2] - aux[0]) + " , " + (aux[3] - aux[1]) + " ; ";
			}
			// System.out.println(salx+" "+saly);
		}
		if (!end)
			cable += (metx - salx) + " , 0 ;";

		cable += " 0 , " + (mety - saly);
		cable += "]";
		return cable;
	}

	/**
	 * genera_cable2 (conexiones internas)
	 */
	private static String genera_cable2(String conex, ModuleCall mc1,
			ModuleCall mc2, int ent, int sal) {
		String cable = "[ ";
		Collection<int[]> c = conexiones.get(conex);
		// int tam=c.size();
		// int i=1;
		// int v=0;
		boolean end = false;
		int[] coord1 = coorden_comp.get(mc1.getName());
		int salx = coord1[0] + 100;
		int saly = coord1[1]
				+ (int) calcula_coord(mc1.getNumberOfOutputs(), sal);
		int[] coord2 = coorden_comp.get(mc2.getName());
		int metx = coord2[0] - 30;
		int mety = coord2[1]
				+ (int) calcula_coord(mc2.getNumberOfInputs(), ent);
		Iterator<int[]> it = c.iterator();
		while (it.hasNext() && !end) {
			int[] aux = it.next();
			if ((salx + (aux[2] - aux[0])) > metx) {
				cable += (metx - salx) + " , 0 ;";
				salx += metx - salx;
				end = true;
			} else {
				salx += aux[2] - aux[0];
				saly += aux[3] - aux[1];
				cable += (aux[2] - aux[0]) + " , " + (aux[3] - aux[1]) + " ; ";
			}
		}
		if (!end)
			cable += (metx - salx) + " , 0 ;";

		cable += " 0 , " + (mety - saly);
		cable += "]";
		return cable;
	}

	// cables hacia las salidas de fuera
	private static String genera_cable3(String conex, String out,
			ModuleCall mc1, int sal) {
		String cable = "[ ";
		Collection<int[]> c = conexiones.get(conex);
		// int tam=c.size();
		// int i=1;
		boolean end = false;
		int[] coord1 = coorden_comp.get(mc1.getName());
		int salx = coord1[0] + 100;
		int saly = coord1[1]
				+ (int) calcula_coord(mc1.getNumberOfOutputs(), sal);
		int[] coord2 = coorden_outputs.get(out);
		int metx = coord2[0] - 30;
		int mety = coord2[1] + 10;
		Iterator<int[]> it = c.iterator();
		// System.out.println(c.size());
		while (it.hasNext() && !end) {
			int[] aux = it.next();
			if ((salx + (aux[2] - aux[0])) > metx) {
				cable += (metx - salx) + " , 0 ;";
				salx += metx - salx;
				end = true;
				// System.out.println("entro");
			} else {
				salx += aux[2] - aux[0];
				saly += aux[3] - aux[1];
				cable += (aux[2] - aux[0]) + " , " + (aux[3] - aux[1]) + " ; ";
			}
		}
		if (!end)
			cable += (metx - salx) + " , 0 ;";

		cable += " 0 , " + (mety - saly);
		cable += "]";
		return cable;
	}

	/**
	 * calcula_coord
	 */
	private static double calcula_coord(int n_inputs, int input) {
		double res = 0;
		if (n_inputs == 1)
			res = 50;
		else if (n_inputs == 2) {
			if (input == 1)
				res = 25;
			else
				res = 75;
		} else if (n_inputs == 3) {
			if (input == 1)
				res = 15;
			else if (input == 2)
				res = 50;
			else
				res = 85;
		}
		return res;
	}
}
