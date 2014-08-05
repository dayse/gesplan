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

/**
 * clase que se utiliza para trabajar con los ficheros de configuración de la
 * herramienta, basados en la utilización del lenguaje XML.
 * 
 * @author Jesús Izquierdo Tena
 */
public class XfsgLeerXML {

	private DefaultMutableTreeNode top;

	Map<String, String[]> mapa_rulebases, mapa_crisps;

	String system = null;

	boolean[] options_system;

	private JCheckBox include_confid, gen_txtfile, gen_simmodel, simple;

	public XfsgLeerXML(DefaultMutableTreeNode top, JCheckBox include_confid,
			JCheckBox gen_txtfile, JCheckBox gen_simmodel, JCheckBox simple) {
		this.top = top;
		mapa_rulebases = new TreeMap<String, String[]>();
		mapa_crisps = new TreeMap<String, String[]>();
		system = (String) top.getUserObject();
		options_system = new boolean[4];
		this.include_confid = include_confid;
		this.gen_txtfile = gen_txtfile;
		this.gen_simmodel = gen_simmodel;
		this.simple = simple;
	}

	@SuppressWarnings("unchecked")
	
	public void leer_xml() {

		int i = 0;

		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new FileInputStream(
					XfsgProperties.fichero_config));

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
					String[] valores_aux = new String[4];
					for (Element valor : valores) {
						String valor_aux = valor.getValue();
						if (!valor_aux.isEmpty())
							valores_aux[i] = valor.getValue();
						else
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

				for (Element option : hijosoptions) {
					String valor_aux = option.getValue();
					options_system[i] = Boolean.valueOf(valor_aux);
					i++;
				}
				actualiza_valores();
				marca_opciones();
			} else {
				new XfsgError(1, raiz.getAttributeValue("name"));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

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

	private void actualiza_flc(DefaultMutableTreeNode dmtn) {
		XfsgFLC flc = (XfsgFLC) dmtn.getUserObject();
		boolean completa = true;
		String name = flc.getname();
		String[] aux = (String[]) mapa_rulebases.get(name);
		if (aux != null) {
			for (int i = 0; i < aux.length; i++) {
				int m = 0;
				if (i == 0) {
					m = Integer.parseInt(aux[0]);
					flc.setN(m);
				} else if (i == 1) {
					m = Integer.parseInt(aux[1]);
					flc.setNo(m);
				} else if (i == 2) {
					m = Integer.parseInt(aux[2]);
					flc.setgrad(m);
				} else if (i == 3) {
					m = Integer.parseInt(aux[3]);
					flc.setP(m);
				}

				if (m == 0)
					completa = false;
			}
		} else {
			new XfsgError(2, name);
		}
		flc.settodo_relleno((completa & aux != null)
				| (aux == null & flc.gettodo_relleno()));
	}

	private void actualiza_crisp(DefaultMutableTreeNode dmtn) {
		XfsgCrisp crisp = (XfsgCrisp) dmtn.getUserObject();
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
			new XfsgError(3, name);
		}
		crisp.settodo_relleno((completa & aux != null)
				| (aux == null & crisp.gettodo_relleno()));
	}

	private void marca_opciones() {
		for (int i = 0; i < options_system.length; i++) {
			if (i == 0) {
				if (options_system[i])
					include_confid.setSelected(true);
				else
					include_confid.setSelected(false);
			} else if (i == 1) {
				if (options_system[i])
					gen_txtfile.setSelected(true);
				else
					gen_txtfile.setSelected(false);
			} else if (i == 2) {
				if (options_system[i])
					gen_simmodel.setSelected(true);
				else
					gen_simmodel.setSelected(false);
			} else if (i == 3) {
				if (options_system[i])
					simple.setSelected(true);
				else
					simple.setSelected(false);
			}
		}
	}

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
					DefaultMutableTreeNode dmtn2 = (DefaultMutableTreeNode) dmtn
							.getChildAt(j);
					XfsgFLC flc = (XfsgFLC) dmtn2.getUserObject();
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
					bits_input.setText(String.valueOf(flc.getN()));
					bits_output.setText(String.valueOf(flc.getNo()));
					bits_membership_degree.setText(String
							.valueOf(flc.getgrad()));
					bits_MF_slopes.setText(String.valueOf(flc.getP()));
					rulebase.addContent(bits_input);
					rulebase.addContent(bits_output);
					rulebase.addContent(bits_membership_degree);
					rulebase.addContent(bits_MF_slopes);
					rulebases.addContent(rulebase);
				}
				if (i == 1) {
					DefaultMutableTreeNode dmtn2 = (DefaultMutableTreeNode) dmtn
							.getChildAt(j);
					XfsgCrisp crisp = (XfsgCrisp) dmtn2.getUserObject();
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

		Element include_rule_confidence_factor_mfile = new Element(
				"include_rule_confidence_factor_mfile");
		include_rule_confidence_factor_mfile.setText(String
				.valueOf(include_confid.isSelected()));
		Element gen_txtfile1 = new Element("gen_txtfile");
		gen_txtfile1.setText(String.valueOf(gen_txtfile.isSelected()));
		Element gen_simmodel1 = new Element("gen_simmodel");
		gen_simmodel1.setText(String.valueOf(gen_simmodel.isSelected()));
		Element use_simp_components = new Element("use_simp_components");
		use_simp_components.setText(String.valueOf(simple.isSelected()));
		options.addContent(include_rule_confidence_factor_mfile);
		options.addContent(gen_txtfile1);
		options.addContent(gen_simmodel1);
		options.addContent(use_simp_components);

		eRaiz.addContent(rulebases);
		eRaiz.addContent(crisps);
		eRaiz.addContent(options);

		Format format = Format.getPrettyFormat();
		XMLOutputter xmloutputter = new XMLOutputter(format);
		String xml = xmloutputter.outputString(docNuevo);
		return xml;
	}
}
