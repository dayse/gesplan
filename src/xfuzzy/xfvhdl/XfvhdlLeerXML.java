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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import xfuzzy.util.XComboBox;
import xfuzzy.util.XTextForm;

/**
 * Clase que se utiliza para trabajar con los ficheros de configuración de la
 * herramienta, basados en la utilización del lenguaje XML.
 * 
 * @author Lidia Delgado Carretero
 */
public class XfvhdlLeerXML {

	/**Árbol que representa al sistema*/
	private DefaultMutableTreeNode top;

	/**Mapa que contiene como clave el nombre del 
	 * módulo de inferencia y como valor una lista de los parámetros 
	 * que lo definen. */
	Map<String, String[]> mapa_rulebases;
	
	/**Mapa que contiene como clave el nombre del 
	 * módulo crisp y como valor una lista de los parámetros 
	 * que lo definen. */
	Map<String, String[]> mapa_crisps;

	
	String system = null;
	/**Opciones globales del sistema: "Generate complementary files" 
	 * y "Use simplified components".*/
	boolean[] options_system;
	
	/**Opciones globales del sistema con desplegables: 
	 * "RAM to be used", "ROM to be used", "Tool", "Optimization" y 
	 * "Effort". 
	 */
	int[] options_system2;
	
	/**Opciones globales del sistema, cuadros de texto:
	 * "FPGA Family" y "Device." 
	 */
	String[] options_system3;
	
	/**Opciones globales para gestionar el directorio de salida y 
	 * el prefijo de los ficheros.*/
	String[] options_system4;

	
	private JCheckBox simple, complementaryFiles;
	private XComboBox otherInformation[], otherInformation2[], 
		optimization[], mapEffort[];
	private XTextForm family, device;
	private XTextForm[] filesInformation;

	/**Constructor de la clase.*/
	public XfvhdlLeerXML(DefaultMutableTreeNode top,
			JCheckBox complementaryFiles, JCheckBox simple,
			XComboBox[] otherInformation, XTextForm family, XTextForm device, 
			XComboBox[] otherInformation2, XComboBox[] optimization, 
			XComboBox[] mapEffort, XTextForm[] filesInformation) {
		
		this.top = top;
		mapa_rulebases = new TreeMap<String, String[]>();
		mapa_crisps = new TreeMap<String, String[]>();
		system = (String) top.getUserObject();
		//options_system nos sirve a la hora de capturar la info del fichero de config
		options_system = new boolean[2];
		options_system2=new int[5];
		options_system3 = new String[2];
		options_system4 = new String[2];
		//this.include_confid = include_confid;
		this.complementaryFiles = complementaryFiles;
		this.simple = simple;
		this.otherInformation=otherInformation;
		this.family=family;
		this.device=device;
		this.otherInformation2=otherInformation2;
		this.optimization=optimization;
		this.mapEffort=mapEffort;
		this.filesInformation=filesInformation;
		
	}
	

	@SuppressWarnings("unchecked")
	
	/**Método que lee una configuración XML.*/
	public void leer_xml() {

		int i = 0;

		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new FileInputStream(
					XfvhdlProperties.fichero_config));

			Element raiz = doc.getRootElement();
			if (raiz.getAttributeValue("name").equals(system)) {

				Element rulebases = raiz.getChild("rulebases");
				List<Element> hijosrulebases = (List<Element>) rulebases
						.getChildren();

				Element crisps = raiz.getChild("crisps");
				List<Element> hijoscrisps = (List<Element>) crisps
						.getChildren();

				Element options = raiz.getChild("options");
				List<Element> hijosoptions = (List<Element>) options
						.getChildren();

				for (Element rulebase : hijosrulebases) {
					String name = rulebase.getAttributeValue("name");
					List<Element> valores = (List<Element>) rulebase
							.getChildren();
					String[] valores_aux = new String[8];
					for (Element valor : valores) {
						String valor_aux = valor.getValue();
						if (!valor_aux.isEmpty())
							valores_aux[i] = valor.getValue();
						else if(valor.getName().startsWith("bits_"))
							valores_aux[i] = "0";
						i++;
					}
					i = 0;
					mapa_rulebases.put(name, valores_aux);
				}

				for (Element crisp : hijoscrisps) {
					String name = crisp.getAttributeValue("name");
					List<Element> valores = (List<Element>) crisp.getChildren();
					String[] valores_aux = new String[1];
					for (Element valor : valores) {
						String valor_aux = valor.getValue();
						if (!valor_aux.isEmpty())
							valores_aux[i] = valor.getValue();
						else
							valores_aux[i] = "0";
						i++;
					}
					i = 0;
					mapa_crisps.put(name, valores_aux);
				}
				
				int j=0;
				for (Element option : hijosoptions) {
					String valor_aux = option.getValue();
					if(i<2)
						options_system[i] = Boolean.valueOf(valor_aux);
					else if(i==2||i==3||(i>5&&i<9)){
						options_system2[j] = Integer.valueOf(valor_aux);
						j++;
					}else if(i==4||i==5)//Cuando i es 5 o 6=>family y device
						options_system3[i-4] = valor_aux;
					else if (i==9||i==10)
						options_system4[i-9]=valor_aux;
					i++;
					
				}
				actualiza_valores();
				marca_opciones();
			} else {
				new XfvhdlError(33, raiz.getAttributeValue("name"));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch( Exception e){
			XfvhdlError err = new XfvhdlError(32);
            err.show();
            return;
		}
	}

	/**Actualiza los valores de los campos cuando cargamos una configuración.*/
	private void actualiza_valores() {
		int n = top.getChildCount();

		for (int i = 0; i < n; i++) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) top
					.getChildAt(i);
			int m = dmtn.getChildCount();
			for (int j = 0; j < m; j++) {
				DefaultMutableTreeNode dmtn2 = (DefaultMutableTreeNode) dmtn
						.getChildAt(j);
				if (i == 0) {
					actualiza_flc(dmtn2);
				} else if (i == 1) {
					actualiza_crisp(dmtn2);
				}
			}

		}
	}

	/**Actualiza los parámetros de los módulos de inferencia cuando cargamos 
	 * una configuración XML.*/
	private void actualiza_flc(DefaultMutableTreeNode dmtn) {
		XfvhdlFLC flc = (XfvhdlFLC) dmtn.getUserObject();
		boolean completa = true;
		String name = flc.getname();
		String[] aux = (String[]) mapa_rulebases.get(name);
		if (aux != null) {
			int n = 0;
			for (int i = 0; i < aux.length; i++) {
				int m = 0;
				boolean b=true;
				if (i == 0) {//N
					m = Integer.parseInt(aux[0]);
					flc.setN(m);
				} else if (i == 1) {//No
					m = Integer.parseInt(aux[1]);
					flc.setNo(m);
				} else if (i == 2) {//grad
					m = Integer.parseInt(aux[2]);
					flc.setgrad(m);
				} else if (i == 3) {//P
					//En este caso, no afecta m porque este campo afecta 
					//a completa de manera conjunta con MFC_atrithmetic
					m=1;
					n = Integer.parseInt(aux[3]);
					flc.setP(n);
				} else if (i == 4) {//W
					m = Integer.parseInt(aux[4]);
					flc.setW(m);
				} else if(i==5){//MFC_arithmetic
					//m tiene que ser !=0 para no afectar a completa
					m=1;
					b = Boolean.parseBoolean(aux[5]);
					flc.setMFC_arithmetic(b);
					//Si el tipo de mem es aritmetica y el campo P es 0
					//el FLC no está completo
					if(n==0 && b)
						m=0;
				} else if(i==6){//MFC_memory
					m=1;
					flc.setMFC_memory(aux[6]);
				} else if(i==7){//RB_memory
					m=1;
					flc.setRB_memory(aux[7]);
				}

				if (m == 0)
					completa = false;
			}
		} else {
			new XfvhdlError(34, name);
		}
		flc.settodo_relleno((completa & aux != null)
				| (aux == null & flc.gettodo_relleno()));
	}

	/**Actualiza los parámetros de los módulos crisp cuando cargamos 
	 * una configuración XML.*/
	private void actualiza_crisp(DefaultMutableTreeNode dmtn) {
		XfvhdlCrisp crisp = (XfvhdlCrisp) dmtn.getUserObject();
		boolean completa = true;
		String name = crisp.getname();
		String[] aux = (String[]) mapa_crisps.get(name);
		if (aux != null) {
			for (int i = 0; i < aux.length; i++) {
				int m = 0;
				if (i == 0) {
					m = Integer.parseInt(aux[0]);
					crisp.setNo(m);
				}

				if (m == 0)
					completa = false;
			}
		} else {
			new XfvhdlError(35, name);
		}
		crisp.settodo_relleno((completa & aux != null)
				| (aux == null & crisp.gettodo_relleno()));
	}

	/**Método encargado de marcar las opciones globales cuando cargamos una 
	 * configuración XML.*/
	private void marca_opciones() {
		//opciones globales
		for (int i = 0; i < options_system.length; i++) {
			//if (i == 0) {
				//if (options_system[i])
				//	include_confid.setSelected(true);
				//else
				//	include_confid.setSelected(false);
			//} 
			if (i == 0) {
				if (options_system[i])
					complementaryFiles.setSelected(true);
				else
					complementaryFiles.setSelected(false);
			} else if (i == 1) {
				if (options_system[i])
					simple.setSelected(true);
				else
					simple.setSelected(false);
			}// else if (i==3){
			//	if (options_system[i])
			//		extractROM.setSelected(true);
			//	else
			//		extractROM.setSelected(false);
			//}
		}
		for(int i=0;i<options_system2.length;i++){
			if(i==0)
				otherInformation[0].setSelectedIndex(options_system2[i]);
			else if(i==1)
				otherInformation[1].setSelectedIndex(options_system2[i]);
			else if(i==2)
				otherInformation2[0].setSelectedIndex(options_system2[i]);
			else if(i==3)
				optimization[0].setSelectedIndex(options_system2[i]);
			else if(i==4)
				mapEffort[0].setSelectedIndex(options_system2[i]);
		}
		for(int i=0;i<options_system3.length;i++){
			if(i==0)
				family.setText(options_system3[i]);
			else if(i==1)
				device.setText(options_system3[i]);
		}
		for(int i=0;i<options_system4.length;i++){
				filesInformation[i+1].setText(options_system4[i]);
			
		}
	}

	/**Método encargado de escribir el fichero de configuración XML 
	 * correctamente a la hora de guardarlo.*/
	public String escribir_xml() {

		Document docNuevo = new Document();

		Element eRaiz = new Element("system");
		eRaiz.setAttribute("name", system);

		docNuevo.addContent(eRaiz);

		Element rulebases = new Element("rulebases");
		Element crisps = new Element("crisps");
		Element options = new Element("options");

		int n = top.getChildCount();

		for (int i = 0; i < n; i++) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) top
					.getChildAt(i);
			int m = dmtn.getChildCount();
			if (i == 0) {
				eRaiz.setAttribute("rulebases", String.valueOf(m));
			} else if (i == 1) {
				eRaiz.setAttribute("crisps", String.valueOf(m));
			}
			for (int j = 0; j < m; j++) {
				if (i == 0) {
					//Cada una de las RBs
					DefaultMutableTreeNode dmtn2 = (DefaultMutableTreeNode) dmtn
							.getChildAt(j);
					XfvhdlFLC flc = (XfvhdlFLC) dmtn2.getUserObject();
					Element rulebase = new Element("rulebase");
					rulebase.setAttribute("name", flc.getname());
					rulebase.setAttribute("inputs", String.valueOf(flc
							.getinputs()));
					rulebase.setAttribute("outputs", String.valueOf(flc
							.getoutputs()));
					Element bits_input = new Element("bits_input");
					Element bits_output = new Element("bits_output");
					Element bits_membership_degree = new Element(
							"bits_membership_degree");

					Element bits_MF_slopes = new Element("bits_MF_slopes");
					Element bits_def_weight=new Element("bits_def_weight");
					Element MFC_arithmetic=new Element("MFC_arithmetic");
					Element MFC_memory = new Element("MFC_memory");
					Element RB_memory = new Element("RB_memory");
					bits_input.setText(String.valueOf(flc.getN()));
					bits_output.setText(String.valueOf(flc.getNo()));
					bits_membership_degree.setText(String
							.valueOf(flc.getgrad()));
					bits_MF_slopes.setText(String.valueOf(flc.getP()));
					bits_def_weight.setText(String.valueOf(flc.getW()));
					MFC_arithmetic.setText(String.valueOf(flc.getMFC_arithmetic()));
					MFC_memory.setText(flc.getMFC_memory());
					RB_memory.setText(flc.getRB_memory());
					rulebase.addContent(bits_input);
					rulebase.addContent(bits_output);
					rulebase.addContent(bits_membership_degree);
					rulebase.addContent(bits_MF_slopes);
					rulebase.addContent(bits_def_weight);
					rulebase.addContent(MFC_arithmetic);
					rulebase.addContent(MFC_memory);
					rulebase.addContent(RB_memory);
					rulebases.addContent(rulebase);
				}
				if (i == 1) {
					DefaultMutableTreeNode dmtn2 = (DefaultMutableTreeNode) dmtn
							.getChildAt(j);
					XfvhdlCrisp crisp = (XfvhdlCrisp) dmtn2.getUserObject();
					Element crisp1 = new Element("crisp");
					crisp1.setAttribute("name", crisp.getname());
					crisp1.setAttribute("inputs", String.valueOf(crisp
							.getinputs()));
					crisp1.setAttribute("outputs", String.valueOf(crisp
							.getoutputs()));
					Element bitsize_input_output = new Element("bitsize_output");
					bitsize_input_output.setText(String.valueOf(crisp.getNo()));
					crisp1.addContent(bitsize_input_output);
					crisps.addContent(crisp1);
				}
			}
		}

		//Element include_rule_confidence_factor_mfile = new Element(
		//		"include_rule_confidence_factor_mfile");
		//include_rule_confidence_factor_mfile.setText(String
		//		.valueOf(include_confid.isSelected()));
		Element gen_complementaryFile1 = new Element("complementary_files");
		gen_complementaryFile1.setText(String.valueOf(complementaryFiles.isSelected()));
		Element use_simp_components = new Element("use_simp_components");
		use_simp_components.setText(String.valueOf(simple.isSelected()));
		//Element extract_rom = new Element("extract_rom");
		//extract_rom.setText(String.valueOf(extractROM.isSelected()));		
		Element FPGA_RAM = new Element("FPGA_RAM");
		FPGA_RAM.setText(String.valueOf(otherInformation[0].getSelectedIndex()));
		Element FPGA_ROM = new Element("FPGA_ROM");
		FPGA_ROM.setText(String.valueOf(otherInformation[1].getSelectedIndex()));
		
		
		Element FPGA_family=new Element("FPGA_family");
		FPGA_family.setText(String.valueOf(family.getText()));
		Element FPGA_device=new Element("FPGA_device");
		FPGA_device.setText(String.valueOf(device.getText()));
		Element CAD_tool=new Element("CAD_tool");
		CAD_tool.setText(String.valueOf(otherInformation2[0].getSelectedIndex()));
		Element CAD_optimization=new Element("CAD_optimization");
		CAD_optimization.setText(String.valueOf(optimization[0].getSelectedIndex()));
		Element CAD_effort=new Element("CAD_effort");
		CAD_effort.setText(String.valueOf(mapEffort[0].getSelectedIndex()));
		Element outputFile =new Element("outputFile");
		outputFile.setText(filesInformation[1].getText());
		Element outputDirectory =new Element("outputDirectory");
		outputDirectory.setText(filesInformation[2].getText());
		
		//options.addContent(include_rule_confidence_factor_mfile);
		options.addContent(gen_complementaryFile1);
		options.addContent(use_simp_components);
		options.addContent(FPGA_RAM);
		options.addContent(FPGA_ROM);
		options.addContent(FPGA_family);
		options.addContent(FPGA_device);
		options.addContent(CAD_tool);
		options.addContent(CAD_optimization);
		options.addContent(CAD_effort);
		options.addContent(outputFile);
		options.addContent(outputDirectory);

		eRaiz.addContent(rulebases);
		eRaiz.addContent(crisps);
		eRaiz.addContent(options);

		Format format = Format.getPrettyFormat();
		XMLOutputter xmloutputter = new XMLOutputter(format);
		String xml = xmloutputter.outputString(docNuevo);
		return xml;
	}
}
