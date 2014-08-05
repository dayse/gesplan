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

import java.util.Date;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.security.acl.Group;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import xfuzzy.lang.RulebaseCall;
import xfuzzy.lang.Specification;
import xfuzzy.lang.Variable;
import xfuzzy.util.XConstants;
import xfuzzy.util.XLabel;
import xfuzzy.util.XTextForm;
import xfuzzy.xfsg.XfsgOutData;

/**
 * Clase que implementa la interfaz XvhdlBlock. Se utiliza para almacenar la
 * información relativa a una base de reglas.
 * 
 * @author Lidia Delgado Carretero
 */

public class XfvhdlFLC implements XfvhdlBlock {

	private static final long serialVersionUID = -4981662902071901357L;

	/**Nombre del FLC.*/ 
	private String name = "";
	
	/**Grupo para arithmetic o memory.*/
	ButtonGroup group1;
	
	/**RadioButton para arithmetic.*/
	JRadioButton arithmetic;
	
	/**RadioButton para memory.*/
	JRadioButton memory;

	/**Nº bits entrada.*/ // Bitsize of input 
	private int N;

	/**Nº bits grado de pertenencia.*/ // Bitsize of membership degree / 
	private int grad;

	/**Nº bits para la pendiente.*/ // Bitsize of MF slope / 
	private int P;	

	/**Nº bits salida.*/ // Bitsize of output
	private int No;

	/**Nº bits para el peso de defuzzificación.*/ // Bits for defuzzification weight
	private int W;

	/**Array de mfcs*/ 
	private ArrayList<MFC> mfcs;

	/**Lista de enteros de bits asociados a las reglas de cada entrada.*/ 
	private ArrayList<Integer> bits_var;
	
	/**Base de reglas.*/ // Rule Base
	private ArrayList<XfsgOutData> RB;

	/**Conectivo de antecedentes.*/ 
	private String connective;

	/**Atributo que indica si el usuario nos ha proporcionado todos los 
	 * datos relativos al módulo de inferencia.*/
	private boolean todo_relleno;

	/**Método de defuzzificación.*/ 
	private String defuzzy;

	/**Almacena la sección "Bitsize information"*/
	XTextForm[] numBitsInformation;
	
	/**Número de entradas.*/ 
	private int inputs;

	/**Número de salidas.*/ 
	private int outputs;

	/**Grados de certeza de las reglas.*/
	String grados;

	/**Contiene la llamada a la base de reglas.*/ 
	RulebaseCall rbc;
	
	/**Cadena que almacena el código VHDL asociado al FLC.*/
	String vhdl;
	
	/**Botón para señalar si la MFC se implementa con ROM.*/
	JRadioButton MFC_ROM;
	
	/**Botón para señalar si la MFC se implementa con RAM.*/
	JRadioButton MFC_RAM;
	
	/**Botón para señalar si la MFC se implementa con bloque lógico.*/
	JRadioButton MFC_logicBlock;
	
	/**Botón para señalar si la RB se implementa con ROM.*/
	JRadioButton RB_ROM;
	
	/**Botón para señalar si la RB se implementa con RAM.*/
	JRadioButton RB_RAM;
	
	/**Botón para señalar si la RB se implementa con bloque lógico.*/
	JRadioButton RB_logicBlock;

	/**Booleano que indica si la base de reglas es completa.*/
	boolean complete;
	
	/**Booleano que nos ayuda a bloquear Bits for Membership Function Slope.*/
	boolean flag_arith;
	
	/**XfvhdlUniverse que sirve para saber si las entradas están o no normalizadas.*/
	XfvhdlUniverse u;
	/**XfvhdlDiscretizeUniverse que sirve para saber si las entradas están o no normalizadas.*/
	XfvhdlDiscretizeUniverse disc;
    
	/**Especificación XFL.*/
	Specification spec;
	
	/**Booleano que indica si se han de calcular pesos.*/
	boolean calculateWeights;
    
	/**Booleano que indica si las entradas no están normalizadas porque el 
	 * solapamiento es menor que 1.*/
	boolean entradasNoNormalizadasMenor1;
	
	///**Booleano que indica si el método de defuzzificación es o no simplificado.*/
	boolean simplificado;//se inicializa cuando empezamos a generar codigo vhdl
    
	private boolean init;
	
	
	// ----------------------------------------------------------------------------//
	// CONSTRUCTOR //
	// ----------------------------------------------------------------------------//

	/**Constructor de la clase.*/
	public XfvhdlFLC(String name, RulebaseCall rbc, Specification spec) {
		this.name = name;
		this.spec=spec;
		N = 0;
		grad = 0;
		P = 0;
		No = 0;
		W = 0;
		mfcs = new ArrayList<MFC>();
		bits_var=new ArrayList<Integer>();
		RB = new ArrayList<XfsgOutData>();
		connective = "";
		grados = "";
		defuzzy = "";
		todo_relleno = false;
		inputs = 0;
		outputs = 0;
		this.rbc = rbc;
		//botones que se agruparán en group 1
		//Si las entradas no están normalizadas,
		//memory estará marcado y además no te dejará marcar arithmetic
		arithmetic = new JRadioButton("Arithmetic");
		arithmetic.setSelected(true);
		arithmetic.setActionCommand("arithmetic");
		memory = new JRadioButton("Memory");
		memory.setActionCommand("memory");
		flag_arith=true;
		//botones que se agruparán en group 2
		MFC_ROM = new JRadioButton("ROM");
		MFC_ROM.setSelected(true);
		MFC_ROM.setActionCommand("ROM");
		MFC_RAM = new JRadioButton("RAM");
		MFC_RAM.setActionCommand("RAM");
		MFC_logicBlock = new JRadioButton("Logic Block");
		MFC_logicBlock.setActionCommand("logic");
		//botones que se agruparán en group 2
		RB_ROM = new JRadioButton("ROM");
		RB_ROM.setActionCommand("ROM");
		RB_ROM.setSelected(true);
		RB_RAM = new JRadioButton("RAM");
		RB_RAM.setActionCommand("RAM");
		RB_logicBlock = new JRadioButton("Logic Block");
		RB_logicBlock.setActionCommand("logic");
		RB = new ArrayList<XfsgOutData>();
		
		//Inicializaciones por defecto de atributos de ayuda
		u = new XfvhdlUniverse();
		disc = new XfvhdlDiscretizeUniverse();
		calculateWeights=false;
		entradasNoNormalizadasMenor1=false;
		
		
	}

	// MÉTODOS GET
	
	/**
	 * 
	 * @return calcula pesos o no.
	 */
	public boolean getCalculateWeights(){
		return calculateWeights;
	}

	/**
	 * 
	 * @return Nº bits entrada.
	 *///Bitsize of input
	public int getN() {

		return N;
	}

	/**
	 * 
	 * @return Nº bits grado de pertenencia.
	 */ //Bitsize of membership degree
	public int getgrad() {
		return grad;
	}

	/**
	 * 
	 * @return Nº bits de la pendiente.
	 */ //Bitsize of MF slope
	public int getP() {
		return P;
	}

	/**
	 * 
	 * @return Nº bits salida.
	 */ // Bitsize of output
	public int getNo() {
		return No;
	}

	/**
	 * 
	 * @return Indica si es arithmetic o memory.
	 */
	public boolean getMFC_arithmetic() {
		return arithmetic.isSelected();
	}
	/**
	 * 
	 * @return MFC con ROM, RAM o bloque lógico.
	 */
	public String getMFC_memory() {
		String ret;
		if(MFC_ROM.isSelected())
			ret="ROM";
		else if(MFC_RAM.isSelected())
			ret="RAM";
		else
			ret="logic";
		return ret;
	}
	/**
	 * 
	 * @return RB con ROM, RAM o bloque lógico.
	 */
	public String getRB_memory() {
		String ret;
		if(RB_ROM.isSelected())
			ret="ROM";
		else if(RB_RAM.isSelected())
			ret="RAM";
		else
			ret="logic";
		return ret;
	}
	
	/**
	 * 
	 * @return El nombre del FLC.
	 */
	public String getname() {
		return name;
	}

	/**
	 * 
	 * @return Bits para el peso de defuzzification.
	 */
	public int getW() {
		return W;
	}

	/**
	 * 
	 * @return El array de mfcs del flc.
	 */
	public ArrayList<MFC> getmfcs() {
		return mfcs;
	}

	/**
	 * 
	 * @return Base de reglas
	 */
	public ArrayList<XfsgOutData> getRB() {
		return RB;
	}

	/**
	 * @return Conectivo de antecedentes.
	 */
	public String getconnective() {
		return connective;
	}

	/**
	 * @return todo_relleno.
	 */
	public boolean gettodo_relleno() {
		return todo_relleno;
	}

	/**
	 * @return Método de defuzzificación.
	 */
	public String getdeffuzy() {
		return defuzzy;
	}

	/**
	 * @return El número de entradas.
	 */
	public int getinputs() {
		return inputs;
	}

	/**
	 * 
	 * @return El número de salidas.
	 */
	public int getoutputs() {
		return outputs;
	}

	/**
	 * @return Grados de certeza de las reglas.*/
	public String getgrados() {
		return grados;
	}
	
	/**@return Indica si el método de defuzzificación es simplificado.*/
	public boolean getSimplificado(){
		return simplificado;
	}

	/**@return Código VHDL del flc.*/
	public String getVHDL(){
		return vhdl;
	}

	/**
	 * @return Un booleano indicando si la base de reglas es completa o no.
	 */
	public boolean getcomplete() {
		return complete;
	}
	
	/**@return Array de nº de bits necesarios para la parte entera, 
	 * necesario para cuando método de defuzzificación es Takagi-Sugeno.*/
	public int[] getBitsParteEnteraTakSug(){
		int[] bitsPE=new int[mfcs.size()+1];
		int longitud=RB.get(0).getvalues2().length;
		//double mayor1=0, mayor2=0, mayor3=0;
		double[] mayor=new double[mfcs.size()+1];
		boolean[] hayNegativo=new boolean[mfcs.size()+1];
		//boolean hayNegativo1=false,hayNegativo2=false,hayNegativo3=false;
		for(int i=0;i<mfcs.size()+1;i++){
			mayor[i]=0;
			hayNegativo[i]=false;
		}
		for (int i=0;i<longitud;i++){
			for(int j=0;j<mfcs.size()+1;j++){
				if(RB.get(0).getvalues2()[i][j]<0&&!hayNegativo[j])
					hayNegativo[j]=true;
				if(mayor[j]<Math.abs(RB.get(0).getvalues2()[i][j]))
					mayor[j]=Math.abs(RB.get(0).getvalues2()[i][j]);
			}
		}
		for(int i=0;i<mfcs.size()+1;i++){
			bitsPE[i]=(int)Math.ceil(Math.log(Math.ceil(mayor[i]))/Math.log(2));
			if(hayNegativo[i])
				bitsPE[i]++;
			if(bitsPE[i]<2)
				bitsPE[i]=2;
		}
		return bitsPE;
	}

	/**@param bitsTotales Nº de bits para codificar el parámetro del método 
	 * de defuzzificación Takagi-Sugeno.
	 * @return Lista de cadenas que representan los parámetros de Takagi-Sugeno.*/
	public String[][] getTakSugBinario(int bitsTotales) throws IOException{
		int longitud=RB.get(0).getvalues2().length;
		String[][] res=new String[longitud][mfcs.size()+1];
		int[] BitsParteEntera=new int[mfcs.size()+1];
		int[] BitsParteDecimal=new int[mfcs.size()+1];
		
		BitsParteEntera= getBitsParteEnteraTakSug();
		for(int i=0;i<mfcs.size()+1;i++){
			BitsParteDecimal[i]=bitsTotales-BitsParteEntera[i];
		}

		XfvhdlBinaryDecimal converter=new XfvhdlBinaryDecimal();
		RB.get(0).getvalues2();
		for (int i=0;i<longitud;i++){
			for(int j=0;j<mfcs.size()+1;j++){
				res[i][j]=converter.toCA2(RB.get(0).getvalues2()[i][j], 
						bitsTotales,BitsParteDecimal[j]);
			}
		}
		return res;
	}
	
	/**@param spec Especificación XFL.
	 * @return Código del fichero de test bench del módulo de inferencia.*/
	public String getTestBench(Specification spec){
		String res="";
		XfvhdlTestBenchFile testBench=new XfvhdlTestBenchFile();
		res=testBench.createTestBenchSource(spec, this,null,null);
		return res;
	}
	// MÉTODOS SET
	
	
	
	/**@param calculateWeights true si queremos que calcule los pesos.*/
	public void setCalculateWeights(boolean calculateWeights){
		this.calculateWeights=calculateWeights;
	}

	/**
	 * 
	 * @param N Nº bits entrada
	 */ //Bitsize of input
	public void setN(int N) {
		this.N = N;
	}

	/**
	 * 
	 * @param grad Nº bits grado del pertenencia
	 */ //Bitsize of membership degree
	public void setgrad(int grad) {
		this.grad = grad;
	}

	/**
	 * 
	 * @param P Nº bits de las pendientes
	 */ //Bitsize of MF slope
	public void setP(int P) {
		this.P = P;
	}

	/**
	 * 
	 * @param No Nº bits salida
	 */ //Bitsize of output
	public void setNo(int No) {
		this.No = No;
	}

	/**
	 * 
	 * @param arith Opción MFC memory (arithmetic or memory).
	 */
	public void setMFC_arithmetic(boolean arith) {
		if (arith){
			flag_arith=true;
			memory.setSelected(false);
			arithmetic.setSelected(true);
		}
		else{
			flag_arith=false;
			arithmetic.setSelected(false);
			memory.setSelected(true);
		}
	}
	
	/**
	 * 
	 * @param memory Opción MFC memory (ROM, RAM or logic block).
	 */
	public void setMFC_memory(String mem) {
		if (mem.equals("ROM")){
			MFC_RAM.setSelected(false);
			MFC_logicBlock.setSelected(false);
			MFC_ROM.setSelected(true);
		}
		else if(mem.equals("RAM")){
			MFC_logicBlock.setSelected(false);
			MFC_ROM.setSelected(false);
			MFC_RAM.setSelected(true);
		}else{
			MFC_ROM.setSelected(false);
			MFC_RAM.setSelected(false);
			MFC_logicBlock.setSelected(true);
			
		}
			
	}
	
	/**
	 * 
	 * @param memory Opción RB memory (ROM, RAM or logic block).
	 */
	public void setRB_memory(String mem) {
		if (mem.equals("ROM")){
			RB_RAM.setSelected(false);
			RB_logicBlock.setSelected(false);
			RB_ROM.setSelected(true);
		}
		else if(mem.equals("RAM")){
			RB_logicBlock.setSelected(false);
			RB_ROM.setSelected(false);
			RB_RAM.setSelected(true);
		}else{
			RB_ROM.setSelected(false);
			RB_RAM.setSelected(false);
			RB_logicBlock.setSelected(true);
			
		}
			
	}
	
	/**
	 * 
	 * @param name Nombre del FLC
	 */
	public void setname(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param K Bits para el peso de defuzzification.
	 */
	public void setW(int W) {
		this.W = W;
	}


	/**
	 * 
	 * @param RB Base de reglas.
	 */
	public void setRB(ArrayList<XfsgOutData> RB) {
		this.RB = RB;
	}

	/**
	 * 
	 * @param connective Conectivo de antecedentes.
	 */
	public void setconnective(String connective) {
		this.connective = connective;
	}
	
	/**@param todo_relleno Indicador de completitud en configuración.
	 * */
	public void settodo_relleno(boolean todo_relleno) {
		this.todo_relleno = todo_relleno;
	}

	/**
	 * 
	 * @param deffuzy Método de defuzzificación.
	 */
	public void setdefuzzy(String deffuzy) {
		this.defuzzy = deffuzy;
	}

	/**
	 * 
	 * @param inputs Número de entradas.
	 */
	public void setinputs(int inputs) {
		this.inputs = inputs;
	}

	/**
	 * 
	 * @param outputs Número de salidas.
	 */
	public void setoutputs(int outputs) {
		this.outputs = outputs;
	}

	/**@param grados Grados de certeza de la regla.*/
	public void setgrados(String grados) {
		this.grados = grados;
	}

	/**@param complete Indica si la base de reglas es completa,*/
	public void setcomplete(boolean complete) {
		this.complete = complete;	
	}
	/**@paramn b  Indica si las entradas no están normalizadas 
	 * porque el solapamiento es menor que 1 */
	public void setEntradasNoNormalizadasMenor1(boolean b){
		entradasNoNormalizadasMenor1=b;
	}
	
	/**Método que le da el valor que le corresponde al atributo simplificado, 
	 * a partir de la especificación XFL.
	 * @param Especificación XFL.
	 * */
	public void setSimplificado(Specification spec){
		
		if(getdeffuzy().equals("MaxLabel")||
				
				((getdeffuzy().equals("TakagiSugeno")
						||getdeffuzy().equals("FuzzyMean"))&&
						!entradasNoNormalizadasMenor1&&
						XfvhdlProperties.simplifiedComponents==true
						  
				)){
			if(getdeffuzy().equals("MaxLabel")||
					(getinputs()>1&&getconnective().equalsIgnoreCase("prod")))//simplificado
			{
				simplificado=true;
			}else if (getinputs()>1&&!getconnective().equalsIgnoreCase("prod")){//no simpl
				simplificado=false;
			}else{//simplificado, caso de 1 entrada
				simplificado=true;
			}

		}else{//no simplificado
			simplificado=false;
		}
	}

	// MÉTODOS PROPIOS DE LA CLASE

	/**@param a Nº de bits asociados a las reglas de la siguiente entrada.
	 */
	public void addBits_var(int a){
		bits_var.add(a);
	}
	
	

	/**Añade un nuevo MFC a la lista mfcs.
	 * @param n_fp Número de funciones de pertenencia.
	 * @param _a Cadena que representa los puntos del MFC.
	 * @param slopes Cadena que se representa con las pendientes del MFC.
	 * @param listaPuntos Lista de puntos del MFC.
	 * @param listaPendientes Lista de pendientes del MFC.*/
	public void create_addMFC(String n_fp, String _a, String slopes, ArrayList<Double> listaPuntos, ArrayList<Double> listaPendientes) {
		MFC m = new MFC(n_fp, _a, slopes, listaPuntos, listaPendientes);
		mfcs.add(m);
	}
	
	/**Añade una nueva base de reglas  al atributo RB.
	 * @param rb Base de reglas.*/
	public void addRB(XfsgOutData rb) {
		this.RB.add(rb);
	}
	
	/**@return Nombre del flc.*/
	public String toString() {
		return name;
	}
	
	/**Define la interfaz gráfica de usuario asociada al 
	 * módulo de inferencia.*/
	public Box gui() {

		if(entradasNoNormalizadasMayor1(spec)){
			return null;
		}else if(this.getoutputs()>1){
			new XfvhdlError(14, name);
			return null;
		}else if (this.defuzzy.equalsIgnoreCase("TakagiSugeno")
			      && this.getinputs()>2){
			new XfvhdlError(9, name);
			return null;
		}
		// Define los botones asociados para la sección "Bitsize
		// information"
		String numBitsInformationLabels[] = { "Bits for Input",
				"Bits for Output", "Bits for membership degree",
				"Bits for membership function slope ",
				"Bits for defuzzification weight" };

		// Crea la sección "Bitsize information"
		numBitsInformation = new XTextForm[5];
		for (int i = 0; i < numBitsInformation.length; i++) {
			numBitsInformation[i] = new XTextForm(numBitsInformationLabels[i]);
			numBitsInformation[i].setEditable(true);
			numBitsInformation[i].setFieldWidth(5);
		}
		XTextForm.setWidth(numBitsInformation);

		// Escribe los valores por defecto
		Integer intTmp = new Integer(N);
		numBitsInformation[0].setText(intTmp.toString());
		intTmp = new Integer(No);
		numBitsInformation[1].setText(intTmp.toString());
		intTmp = new Integer(grad);
		numBitsInformation[2].setText(intTmp.toString());
		intTmp = new Integer(P);
		numBitsInformation[3].setText(intTmp.toString());
		intTmp = new Integer(W);
		numBitsInformation[4].setText(intTmp.toString());
		if(!flag_arith){
			numBitsInformation[3].setEditable(false);
			numBitsInformation[3].setFieldEnabled(false);
			numBitsInformation[3].setLabelEnabled(false);
		}
			

		Box evolbox = new Box(BoxLayout.Y_AXIS);

		//evolbox.add(Box.createVerticalStrut(10));
		
		Box aux = new Box(BoxLayout.X_AXIS);
		
		Box bitinfo = new Box(BoxLayout.Y_AXIS);
		
		bitinfo.add(new XLabel("Variables for Rulebase: " + name));
		bitinfo.add(new XLabel("Bitsize information"));
		for (int i = 0; i < numBitsInformation.length; i++)
			bitinfo.add(numBitsInformation[i]);
		aux.add(bitinfo);
		// Define los botones asociados para la sección "Kinds of
		// memory"
		Box retbox = new Box(BoxLayout.Y_AXIS);
		retbox.setPreferredSize(new Dimension (250,50));
		retbox.add(new XLabel("Kinds of memory"));
				
		
		
		group1 = new ButtonGroup();
		group1.add(arithmetic);
		group1.add(memory);
		
		ActionListener al = new VoteActionListener();
	    arithmetic.addActionListener(al);
	    memory.addActionListener(al);
	    
		
		ButtonGroup group2 = new ButtonGroup();
		group2.add(MFC_ROM);
		group2.add(MFC_RAM);
		group2.add(MFC_logicBlock);
		
		
		ButtonGroup group3 = new ButtonGroup();
		group3.add(RB_ROM);
		group3.add(RB_RAM);
		group3.add(RB_logicBlock);
		
		
		
		Box subBox1 = new Box(BoxLayout.X_AXIS);
		subBox1.add(arithmetic);
		subBox1.add(memory);

		Box subBox2 = new Box(BoxLayout.X_AXIS);
		subBox2.add(MFC_ROM);
		subBox2.add(MFC_RAM);
		subBox2.add(MFC_logicBlock);
		
		Box subBoxMFC= new Box(BoxLayout.Y_AXIS);
		subBoxMFC.add(new XLabel("MFC's"));
		subBoxMFC.add(subBox1);
		subBoxMFC.add(subBox2);
		
		Box subRB=new Box(BoxLayout.X_AXIS);
		subRB.add(RB_ROM);
		subRB.add(RB_RAM);
		subRB.add(RB_logicBlock);
		
		Box RB=new  Box(BoxLayout.Y_AXIS);
		RB.add(new XLabel("Rule Base"));
		RB.add(subRB);
		
		retbox.add(subBoxMFC);
		retbox.add(RB);
		aux.add(retbox);
		evolbox.add(aux);
		//MFC'S
		entradasNoNormalizadasMenor1(spec);
		for (int k = 0; k < mfcs.size(); k++) {
			evolbox.add(Box.createVerticalStrut(5));
			evolbox.add(new XLabel("MFC" + (k + 1)));
			String MFCInformationLabels[] = { "N. Membership Functions",
					"Breakpoints", "Slopes" };

			// Crea la sección "MFCj Information"
			XTextForm[] MFCInformation = new XTextForm[3];
			for (int j = 0; j < MFCInformation.length; j++) {
				MFCInformation[j] = new XTextForm(MFCInformationLabels[j]);
				MFCInformation[j].setEditable(false);
				MFCInformation[j].setFieldWidth(100);
			}
			MFC mfc_aux = mfcs.get(k);
			MFCInformation[0].setText(mfc_aux.getn_fp());
			MFCInformation[1].setText(mfc_aux.get_a());
			MFCInformation[2].setText(mfc_aux.getslopes());

			XTextForm.setWidth(MFCInformation);
			for (int l = 0; l < MFCInformation.length; l++) {
				evolbox.add(MFCInformation[l]);
			}
			
		}
		if(entradasNoNormalizadasMenor1){
			setMFC_arithmetic(false);
			numBitsInformation[3].setEditable(false);
			numBitsInformation[3].setFieldEnabled(false);
			numBitsInformation[3].setLabelEnabled(false);
			memory.setEnabled(false);
			arithmetic.setEnabled(false);
		}
		
		// Crea la sección "Rulebase Information"
		
		
		for (int i = 0; i < this.RB.size(); i++) {
			evolbox.add(Box.createVerticalStrut(5));
			evolbox.add(new XLabel("RB" + (i + 1)));
			JTextArea jta = new JTextArea();
			jta.append(this.RB.get(i).getRB());
			jta.setEditable(false);
			jta.setBackground(XConstants.textbackground);
			evolbox.add(jta);
		}

		

		evolbox.setVisible(true);
		
		return evolbox;
	}
	
	/**Pb, es la posición de la coma, si esta es < 0, error, 
	 * necesitamos más bits para representar el dato.*/
	public boolean compruebaPbAdecuado(){
		
		for(int i=0;i<mfcs.size();i++){
			if(P-mfcs.get(i).pbi()<0 && arithmetic.isSelected()){
				return false;
			}
		}
		return true;
	}
	
	/**Genera la cabecera del fichero VHDL asociado al FLC.*/
	public String generaCabecera(){
		String user="";
	    String so="";
	    String vmachine="";
	    String separator="";
		Date now = new Date();
	    String date = now.toString();
	    if(init!=true){
	    	try {
	            so = System.getProperty("os.name", "not specified");
	            so += " ver. ";
	            so += System.getProperty("os.version", "not specified");
	            vmachine = System.getProperty("java.version", "not specified");
	            user = System.getProperty("user.name", "not specified");
	            separator = System.getProperty("file.separator", "\\");
	         } catch (Exception e) {
	         }
	         init = true;
	    }
		String res="--\n" +
		"-- Automatically generated by Xfvhdl\n" +
		"--\n" +
		"-- output:: "+name+".vhd\n" +
		"-- source:: "+XfvhdlProperties.ficheroXFL+"\n" +
		"-- for "+user+", "+date+"\n" +
		"-- in "+so+ " with VM "+ vmachine+ "\n" +
		"--\n" +
		"\n" +
		"---------------------------------------------------------------------------\n" +
		"--                        "+name+"_Constants                       --\n" +
		"---------------------------------------------------------------------------\n" +
		"\n" +
		"library IEEE;\n" +
		"use IEEE.std_logic_1164.all;\n" +
		"use IEEE.std_logic_arith.all;\n" +
		"use IEEE.std_logic_unsigned.all;\n" +
		"\n";;
		return res;
	}
	/**Genera el código VHDL de la zona de constants del fichero VHDL 
	 * asociado al FLC.*/
	public String generaConstantes(Specification spec){
		String MFCi="";
		String auxRBRAM=name+"_MFC1_bits";
	    int mfci_bits;
	    int maxFuncPert=0;
	    for(int i=0;i<mfcs.size();i++){
	    	if(Integer.valueOf(mfcs.get(i).getn_fp())>maxFuncPert)
	    		maxFuncPert=(int)(Integer.valueOf(mfcs.get(i).getn_fp()));	    		
	    }	    	
	    int pipe=(int) Math.pow((double) 2,(double) mfcs.size());
		if(arithmetic.isSelected())
			pipe=Math.max(pipe, maxFuncPert);
	    if (!simplificado)
	    	pipe=Math.max(pipe, No+1);	    	
	    
	    int bits=(int)Math.ceil((Math.log(pipe)/Math.log(2)));
	    if(XfvhdlProperties.pipemax<pipe){
	    	XfvhdlProperties.pipemax=pipe;
	    	XfvhdlProperties.bitsmax=bits;
	    }
		String res="package "+name+"_Constants is\n\n" +
		"	constant "+name+"_inputs : integer := "+inputs+"; -- Inputs to the fuzzy system\n" +
		"	constant "+name+"_N : integer := "+N+"; -- Bitsize of input\n" +
		"	constant "+name+"_grad : integer := "+grad+"; -- Bitsize of membership degree\n" +
		"	constant "+name+"_W : integer := "+W+"; -- Bitsize of deffuz. parameter\n" +
		"	constant "+name+"_P : integer := "+P+"; -- Bitsize of MF slope\n" +
		"	constant "+name+"_No : integer := "+No+"; -- Bitsize of output\n"
		;
		for(int i=0;i<mfcs.size();i++){
			auxRBRAM+=i<(mfcs.size()-1)?"+"+name+"_MFC"+(i+2)+"_bits":"";
			mfci_bits=(int) Math.ceil(
		            Math.log(Double.valueOf(mfcs.get(i).getn_fp()))
		               / Math.log((double) 2));
			
			MFCi="	--\n" +
				"	constant "+name+"_MFC"+(i+1)+"_nfp : integer := "+mfcs.get(i).getn_fp()+"; -- Number of membership functions\n" +
				(arithmetic.isSelected()?"	constant "+name+"_MFC"+(i+1)+"_Pb : integer := "+(P-mfcs.get(i).pbi())+"; -- Binary point for MF slope\n":"") +
				"	constant "+name+"_MFC"+(i+1)+"_bits : integer := "+mfci_bits+"; -- Bitsize of label\n";
			res+=MFCi;
		}
		if(RB_RAM.isSelected()){
			res+="	--\n" +
			"	constant "+name+"_a_bits : integer := "+auxRBRAM+";  -- Bitsize of address bus rules memory\n" +
			"	constant "+name+"_d_bits : integer := "
				+ (defuzzy.equalsIgnoreCase("TakagiSugeno")?""+(mfcs.size()+1)+"*":
				((defuzzy.equalsIgnoreCase("WeightedFuzzyMean")||defuzzy.equalsIgnoreCase("Quality")||defuzzy.equalsIgnoreCase("GammaQuality"))?name+"_W + ":""))
				+name+"_No;  -- Bitsize of data bus rules memory -- Rows for rules memory\n" +
			"	--\n"
			;
		}
		res+="	--\n" +
		"	constant pipe_"+name+" : integer := "+pipe+"; -- Pipeline cycles\n" +
		"	constant bits_"+name+" : integer := "+bits+"; -- ceil(log2(j_pipe))\n" +
		"	--\n"
		;
		if(defuzzy.equalsIgnoreCase("TakagiSugeno")){
			int[] partesEnteras=getBitsParteEnteraTakSug();
			res+="	constant "+name+"_Pc : integer := "+(No-partesEnteras[0])+"; -- Binary point for Takagi Sugeno \"c\" parameter (decimal)\n"
				;
			for(int i=1;i<=mfcs.size();i++){
				res+="	constant "+name+"_Pa"+i+" : integer := "+(No-partesEnteras[i])+"; -- Binary point for Takagi Sugeno \"a"+i+"\" parameter (decimal)\n";
			}
			res+="	--\n";
		}
		if(!simplificado)
			res+="	constant "+name+"_bitsn : integer := "+name+"_No+"+name+"_grad+"+name+"_inputs"+
			((defuzzy.equalsIgnoreCase("WeightedFuzzyMean")||defuzzy.equalsIgnoreCase("Quality")||defuzzy.equalsIgnoreCase("GammaQuality"))?"+"+name+"_W":"")+
			"; -- Bitsize of accumulator numerator\n\n" ;
		
		res+="end "+name+"_Constants;\n\n";
		return res;
	}
	
	/**Genera el código VHDL dependiente de que las entradas sean 
	 * de tipo arithmetic, del fichero VHDL 
	 * asociado al FLC.*/
	public String generaEntradasAritmeticas(){
		String str_aux = "";
	    String others;
	    String tipoAntecedent="ArithMem";
	    //if(!arithmetic.isSelected())
	    //	tipoAntecedent="AntecedentMem";
		String librerias="library IEEE;\n" +
			"use IEEE.std_logic_1164.all;\n" +
			"use IEEE.std_logic_arith.all;\n" +
			"use IEEE.std_logic_unsigned.all;\n\n" +
			"use WORK."+name+"_Constants.all;\n\n";
		String res="";
		for(int i=0;i<mfcs.size();i++){
			res+="---------------------------------------------------------------------------\n" +
			"--                       "+name+"_"+tipoAntecedent+(i+1)+"                       --\n" +
			"---------------------------------------------------------------------------\n\n" +
			librerias +
			"entity "+name+"_"+tipoAntecedent+(i+1)+" is\n\n" +
			"	generic(\n" +
			"	  "+name+"_bits : integer);\n" +
			"	port(\n" +
			"	  addr : in std_logic_vector("+name+"_bits-1 downto 0); --Address buss\n" +
			"	  MFC_a : out std_logic_vector("+name+"_N-1 downto 0); -- Breakpoint\n" +
			"	  MFC_m : out std_logic_vector("+name+"_P-1 downto 0)); -- Slope\n\n" +
			"end "+name+"_"+tipoAntecedent+(i+1)+";\n\n" +
			"architecture FPGA of "+name+"_"+tipoAntecedent+(i+1)+" is\n\n" +
			"	signal s_addr : std_logic_vector("+name+"_MFC"+(i+1)+"_bits-1 downto 0);\n" +
			"	signal s_data : std_logic_vector(("+name+"_N+"+name+"_P) downto 1);\n\n\n";

			if(MFC_logicBlock.isSelected()){
				res+="begin\n\n" +
				"	s_addr <= addr("+name+"_MFC"+(i+1)+"_bits-1 downto 0) when (addr <= "+name+"_MFC"+(i+1)+"_nfp-1) else\n" +							    
				"             std_logic_vector(conv_unsigned("+name+"_MFC"+(i+1)+"_nfp-1, "+name+"_MFC"+(i+1)+"_bits));\n\n" +
				"	Read_mem : process(s_addr)\n" +
				"	begin\n" +
				"		case s_addr is\n";
				XfvhdlBinaryDecimal converter=new XfvhdlBinaryDecimal();
				str_aux="";
				double punto;
				double pendiente;
				for (int j=0;j<Integer.valueOf(mfcs.get(i).getn_fp());j++){
					String punto_pendiente="";
					String antecedente=converter.toBinary(j, bits_var.get(i));
					if(j==Integer.valueOf(mfcs.get(i).getn_fp())-1){
						pendiente=0;
					}else{
						pendiente=mfcs.get(i).getPendiente(j);
					}
					punto=mfcs.get(i).getPunto(j);
					punto_pendiente+=converter.toBinaryInRange(punto, 0, 1, N);
					punto_pendiente+=converter.toBinaryInRange(pendiente, 0, Math.pow(2, mfcs.get(i).pbi()), P);
					str_aux+="			when \""+antecedente+"\" => s_data <= \""+punto_pendiente+"\";\n" ;//Aqui hay que llamar a alguna funcion		
				}
				others="";
				for(int j=0;j<N+P;j++){
					others+="-";
				}
				res+= str_aux +
				"			when others => s_data <= \""+others+"\";\n" +
				//Hay que meter el numero exacto de rayas-----------
				"		end case;\n" +
				"	end process Read_mem;\n\n" ;
			}
			else if(MFC_ROM.isSelected()){
				res+="	subtype ROM_WORD is std_logic_vector(("+name+"_N+"+name+"_P-1) downto 0);\n" +
				"	type ROM_TABLE is array (0 to "+name+"_MFC"+(i+1)+"_nfp-1) of ROM_WORD;\n" +
				"	constant ROM : ROM_TABLE := ROM_TABLE'(\n";
				XfvhdlBinaryDecimal converter=new XfvhdlBinaryDecimal();
				str_aux="";
				double punto;
				double pendiente;
				for (int j=0;j<Integer.valueOf(mfcs.get(i).getn_fp());j++){
					String punto_pendiente="";
					String antecedente=converter.toBinary(j, bits_var.get(i));
					if(j==Integer.valueOf(mfcs.get(i).getn_fp())-1){
						pendiente=0;
					}else{
						pendiente=mfcs.get(i).getPendiente(j);
					}
					punto=mfcs.get(i).getPunto(j);
					punto_pendiente+=converter.toBinaryInRange(punto, 0, 1, N);
					punto_pendiente+=converter.toBinaryInRange(pendiente, 0, Math.pow(2, mfcs.get(i).pbi()), P);
					str_aux+="		    ROM_WORD'(\""+punto_pendiente+"\")" ;//Aqui hay que llamar a alguna funcion		
					 if (j<Integer.valueOf(mfcs.get(i).getn_fp())-1) {
						 str_aux+=",\n";
					 }
					 else {
						 str_aux+=");\n\n";
					 }
				}
				res+= str_aux +
				"begin\n\n" +
				"	s_addr <= addr("+name+"_MFC"+(i+1)+"_bits-1 downto 0) when (addr <= "+name+"_MFC"+(i+1)+"_nfp-1) else\n" +
				"			  std_logic_vector(conv_unsigned("+name+"_MFC"+(i+1)+"_nfp-1, "+name+"_MFC"+(i+1)+"_bits));\n\n" +
				"	s_data <= ROM(conv_integer(s_addr));\n\n";
			}
			res+=
			"	MFC_a <= s_data(("+name+"_N+"+name+"_P) downto ("+name+"_P+1));\n" +
			"	MFC_m <= s_data("+name+"_P downto 1);\n\n" +
			"end FPGA;\n\n\n";
		}
		return res;
	}
	/**Genera el código VHDL dependiente de que la base de reglas sea 
	 * de tipo Logic Block, del fichero VHDL 
	 * asociado al FLC.*/
	public String generaRulesMemLB(Specification spec, String nombre_i) throws IOException{
		String res="";
		String others;
		String librerias="library IEEE;\n" +
			"use IEEE.std_logic_1164.all;\n" +
			"use IEEE.std_logic_arith.all;\n" +
			"use IEEE.std_logic_unsigned.all;\n\n" +
			"use WORK."+name+"_Constants.all;\n\n";
		String suma=name+"_MFC1_bits ";
		for(int i=1;i<mfcs.size();i++)
			suma+="+ "+name+"_MFC"+(i+1)+"_bits";
		XfvhdlRulesMemLB calcular=new XfvhdlRulesMemLB(); 
		String whenrules;
		if(defuzzy!="TakagiSugeno")
			whenrules=calcular.createRulesMemSource(spec, nombre_i, bits_var,null, calculateWeights, No, W);
		else
			whenrules=calcular.createRulesMemSource(spec, nombre_i, bits_var,getTakSugBinario(No), calculateWeights, No, W);
		others="";
		for(int j=0;j<No;j++){
			if(defuzzy=="TakagiSugeno")
				others+="---";
			else
				others+="-";
		}
		if(defuzzy=="WeightedFuzzyMean"||defuzzy=="Quality"||defuzzy=="GammaQuality"){
			for(int i=0;i<W;i++)
				others+="-";
		}
			
		
		res+="---------------------------------------------------------------------------\n" +
		"--                        "+name+"_RulesMem                        --\n" +
		"---------------------------------------------------------------------------\n\n" +
		librerias +
		"entity "+name+"_RulesMem is\n\n" +
		"	port(\n" +
		"	  addr : in std_logic_vector(("+suma+")-1 downto 0); -- Address bus\n" +
		"	  do : out std_logic_vector("+ (defuzzy.equalsIgnoreCase("TakagiSugeno")?""+(mfcs.size()+1)+"*":
			((defuzzy.equalsIgnoreCase("WeightedFuzzyMean")||defuzzy.equalsIgnoreCase("Quality")||defuzzy.equalsIgnoreCase("GammaQuality"))?(name+"_W + "):""))
			+name+"_No downto 1)); -- Data bus\n\n" +
		"end "+name+"_RulesMem;\n\n\n" +
		"architecture FPGA of "+name+"_RulesMem is\n\n" +
		"	signal s_data : std_logic_vector("+ (defuzzy.equalsIgnoreCase("TakagiSugeno")?""+(mfcs.size()+1)+"*":
			((defuzzy.equalsIgnoreCase("WeightedFuzzyMean")||defuzzy.equalsIgnoreCase("Quality")||defuzzy.equalsIgnoreCase("GammaQuality"))?name+"_W + ":""))
			+name+"_No downto 1);\n\n" +
		"begin\n\n" +
		"	Read_mem : process(addr)\n" +
		"	begin\n" +
		"		case addr is\n" +
		whenrules +
		"			when others => s_data <= \""+others+"\";\n"+
		"		end case;\n" +
		"	end process Read_mem;\n\n" +
		"	do <= s_data;\n\n" +
		"end FPGA;\n\n";
		return res;
	}
	
	/**Genera el código VHDL dependiente de que la base de reglas sea 
	 * de tipo ROM, del fichero VHDL 
	 * asociado al FLC.*/
	public String generaRulesMemROM(Specification spec, String nombre_i) throws IOException{

		String res="";
		String librerias="library IEEE;\n" +
			"use IEEE.std_logic_1164.all;\n" +
			"use IEEE.std_logic_arith.all;\n" +
			"use IEEE.std_logic_unsigned.all;\n\n" +
			"use WORK."+name+"_Constants.all;\n\n";
		String suma=name+"_MFC1_bits ";
		
		for(int i=1;i<mfcs.size();i++)
			suma+="+ "+name+"_MFC"+(i+1)+"_bits";
		
		XfvhdlRulesMemROM calcular=new XfvhdlRulesMemROM();
		
		String whenrules="";
		if(defuzzy!="TakagiSugeno")
			whenrules=calcular.createRulesMemSource(spec, nombre_i, bits_var,null, calculateWeights, No, W);
		else
			whenrules=calcular.createRulesMemSource(spec, nombre_i, bits_var,getTakSugBinario(No), calculateWeights, No, W);
			
		res+="---------------------------------------------------------------------------\n"
		+ "--                        "+name+"_RulesMem                        --\n"
		+ "---------------------------------------------------------------------------\n\n"
		+ librerias 
		+ "entity "+name+"_RulesMem is\n\n"
		+ "	port(\n"
		+ "		addr : in std_logic_vector(("+suma+")-1 downto 0); -- Address bus\n"
		+ "		do : out std_logic_vector("
		+ (defuzzy.equalsIgnoreCase("TakagiSugeno")?""
		+ (mfcs.size()+1)+"*":
			((defuzzy.equalsIgnoreCase("WeightedFuzzyMean")
			||defuzzy.equalsIgnoreCase("Quality")
			||defuzzy.equalsIgnoreCase("GammaQuality"))?(name
			+"_W + "):"")) +name+"_No downto 1)); -- Data bus\n\n"
		+ "end "+name+"_RulesMem;\n\n\n"
		+ "architecture FPGA of "+name+"_RulesMem is\n\n"
		+ "	subtype ROM_WORD is std_logic_vector(" 
		+ (defuzzy.equalsIgnoreCase("TakagiSugeno")?""
		+ (mfcs.size()+1)+ "*": 
			((defuzzy.equalsIgnoreCase("WeightedFuzzyMean")
			|| defuzzy.equalsIgnoreCase("Quality") 
			|| defuzzy.equalsIgnoreCase("GammaQuality"))?name 
			+ "_W + " : "")) + name + "_No -1 downto 0);\n"
		+ "	type ROM_TABLE is array (0 to 2 ** (" + suma+ ")-1) of ROM_WORD;\n"
		+ "	constant ROM : ROM_TABLE := ROM_TABLE'(\n" 
		+ whenrules
		+ "end FPGA;\n\n"; 

		return res;
	}
	/**Genera el tramo final del código VHDL.
	 * @param spec Especificación XFL.*/
	private String generaFinFLC(Specification spec){
		String res="";
		String puertosin="";
		String componentesin="";
		String suma_MFC_bits=name+"_MFC1_bits";
		String[] MFC_bits=new String[mfcs.size()];
		String arithmetic_signal;
		String arithmetic_map="";
		String tipoAntecedent;
		String componentRulesMem="";
		String BloqueRulMem;
		String[] BloqueMemoriaAntecedente=new String[mfcs.size()];
		String BloqueArithAntecedente="";
	    if(!arithmetic.isSelected())
	    	tipoAntecedent="AntecedentMem";
	    else{
	    	if(MFC_RAM.isSelected())
	    		tipoAntecedent="ArithMem_RAM";
	    	else
	    		tipoAntecedent="ArithMem";
	    }
		String librerias="library IEEE;\n" +
		"use IEEE.std_logic_1164.all;\n" +
		"use IEEE.std_logic_arith.all;\n" +
		"use IEEE.std_logic_unsigned.all;\n\n" +
		"use WORK."+name+"_Constants.all;\n\n";
		if(arithmetic.isSelected()){
			if(MFC_RAM.isSelected())
				arithmetic_signal="-- Internal signals (ArithMem_RAM)\n\n";
			else
				arithmetic_signal="-- Internal signals (Arithmetic)\n\n";
		}else{
			arithmetic_signal="-- Internal signals (AntecedentMem)\n\n";
		}
		for(int i=0;i<mfcs.size();i++){
			puertosin+="		in"+(i+1)+" : in std_logic_vector("+name+"_N downto 1); -- Input "+(i+1)+" signal.\n";
			if(!MFC_RAM.isSelected()){
				if(arithmetic.isSelected())
					componentesin+="	component "+name+"_"+tipoAntecedent+(i+1)+"\n" +
						"		generic(\n" +
						"			"+name+"_bits : integer);\n" +
						"		port(\n" +
						"			addr : in std_logic_vector("+name+"_bits-1 downto 0);\n" +
						"			MFC_a : out std_logic_vector("+name+"_N-1 downto 0);\n" +
						"			MFC_m : out std_logic_vector("+name+"_P-1 downto 0));\n" +
						"	end component;\n\n"
						;
				else
					componentesin+="	component "+name+"_"+tipoAntecedent+(i+1)+"\n" +
					"		port(\n" +
					"			pipe : in std_logic;\n" +
					"			addr : in std_logic_vector("+name+"_N-1 downto 0);\n" +
					"			alpha_1 : out std_logic_vector("+name+"_grad downto 1);\n" +
					"			alpha_2 : out std_logic_vector("+name+"_grad downto 1);\n" +
					"			L_1     : out std_logic_vector("+name+"_MFC"+(i+1)+"_bits downto 1);\n" +
					"			L_2     : out std_logic_vector("+name+"_MFC"+(i+1)+"_bits downto 1));\n" +
					"	end component;\n\n"
					;
					
			}
			
			if(arithmetic.isSelected()){
				arithmetic_signal+="	signal s_MFC_a"+(i+1)+" : std_logic_vector("+name+"_N downto 1);\n" +
							"	signal s_MFC_m"+(i+1)+" : std_logic_vector("+name+"_P downto 1);\n" +
							"	signal s_alpha_"+(i+1)+"1 : std_logic_vector("+name+"_grad downto 1);\n" +
							"	signal s_alpha_"+(i+1)+"2 : std_logic_vector("+name+"_grad downto 1);\n" +
							"	signal s_L_"+(i+1)+"1 : std_logic_vector("+name+"_MFC"+(i+1)+"_bits downto 1);\n" +
							"	signal s_L_"+(i+1)+"2 : std_logic_vector("+name+"_MFC"+(i+1)+"_bits downto 1);\n";
			}else{
				arithmetic_signal+="	signal s_alpha_"+(i+1)+"1 : std_logic_vector("+name+"_grad downto 1);\n" +
				"	signal s_alpha_"+(i+1)+"2 : std_logic_vector("+name+"_grad downto 1);\n" +
				"	signal s_L_"+(i+1)+"1 : std_logic_vector("+name+"_MFC"+(i+1)+"_bits downto 1);\n" +
				"	signal s_L_"+(i+1)+"2 : std_logic_vector("+name+"_MFC"+(i+1)+"_bits downto 1);\n"
				;
			}
				
			if(arithmetic.isSelected()){
				if(MFC_RAM.isSelected()){
					arithmetic_signal+="	signal di"+(i+1)+" : std_logic_vector("+name+"_N+"+name+"_P downto 1);\n";
					addLibrary(tipoAntecedent);//Añadimos bloque de biblioteca
					BloqueMemoriaAntecedente[i]="	Mem"+(i+1)+" : "+tipoAntecedent+"\n" +
						"		generic map (\n" +
						"			bits => "+name+"_bits,\n" +
						"			nfp  => "+name+"_MFC"+(i+1)+"_nfp\n" +
						"			bitl => "+name+"_MFC"+(i+1)+"_bits,\n" +
						"			N => "+name+"_N,\n" +
						"			P => "+name+"_P)\n" +
						"		port map(\n" +
						"			clk => clk,\n" +
						"			we => clk,\n" +
						"			addr => s_addr,\n" +
						"			di    => di"+(i+1)+",\n" +
						"			MFC_a => s_MFC_a"+(i+1)+",\n" +
						"			MFC_m => s_MFC_m"+(i+1)+");\n\n"
						;
				}else{
					
					BloqueMemoriaAntecedente[i]="	Mem"+(i+1)+" : "+name+"_"+tipoAntecedent+(i+1)+"\n" +
						"		generic map(\n" +
						"			"+name+"_bits => "+name+"_bits)\n" +
						"		port map(\n" +
						"			addr => s_addr,\n" +
						"			MFC_a => s_MFC_a"+(i+1)+",\n" +
						"			MFC_m => s_MFC_m"+(i+1)+");\n\n";
				}
			}else{//En memoria
				if(MFC_RAM.isSelected()){
					arithmetic_signal+="	signal di"+(i+1)+" : std_logic_vector(2*"+name+"_grad+"+name+"_MFC"+(i+1)+"_bits downto 1);\n"
					;
					addLibrary("Antecedent_RAM");
					BloqueMemoriaAntecedente[i]="	Mem"+(i+1)+" : Antecedent_RAM\n" +
					"		generic map(\n" +
					"			N    => "+name+"_N,\n" +
					"			grad => "+name+"_grad,\n" +
					"			bitl => "+name+"_MFC"+(i+1)+"_bits)\n" +
					"		port map(\n" +
					"		clk     => clk,\n" +
					"		we      => s_pipe,\n" +
					"		addr    => in"+(i+1)+",\n" +
					"		di      => di"+(i+1)+",\n" +
					"		alpha_1 => s_alpha_"+(i+1)+"1,\n" +
					"		alpha_2 => s_alpha_"+(i+1)+"2,\n" +
					"		L_1     => s_L_"+(i+1)+"1,\n" +
					"		L_2     => s_L_"+(i+1)+"2);\n\n"
					;
				}else
					BloqueMemoriaAntecedente[i]="	Mem"+(i+1)+" : "+name+"_"+tipoAntecedent+(i+1)+"\n" +
						"		port map(\n"+
						"			pipe    => s_pipe,\n" +
						"			addr    => in"+(i+1)+",\n"+
						"			alpha_1 => s_alpha_"+(i+1)+"1,\n"+
						"			alpha_2 => s_alpha_"+(i+1)+"2,\n"+
						"			L_1     => s_L_"+(i+1)+"1,\n"+
						"			L_2     => s_L_"+(i+1)+"2);\n\n"
						;
			}
			MFC_bits[i]=name+"_MFC"+(i+1)+"_bits";
			if(i>0)
				suma_MFC_bits+=" + "+name+"_MFC"+(i+1)+"_bits";
			
		}
		arithmetic_signal+="\n";
		for(int i=0;i<mfcs.size();i++){
			String subsuma1=name+"_MFC"+mfcs.size()+"_bits";
			String subsuma2="1";
			int restar_en_S=mfcs.size()-i-1;
			for (int l=mfcs.size()-1;l>i;l--){
				subsuma1+=" + "+MFC_bits[l-1];
			}
			for (int l=mfcs.size()-i-1;l>0;l--){
				subsuma2=MFC_bits[mfcs.size()-l]+" + "+subsuma2;
			}
			addLibrary("Arithmetic");
			BloqueArithAntecedente="	Arith"+(i+1)+" : Arithmetic\n" +
			"		generic map(\n" +
			"			N => "+name+"_N,\n" +
			"			P => "+name+"_P,\n" +
			(arithmetic.isSelected()?"			Pb => "+name+"_MFC"+(i+1)+"_Pb,\n":"") +
			"			n_fp => "+name+"_MFC"+(i+1)+"_nfp,\n" +
			"			bitl => "+name+"_MFC"+(i+1)+"_bits,\n" +
			"			bitc => "+name+"_bits,\n" +
			"			grad => "+name+"_grad)\n" +
			"		port map(\n" +
			"			clk => clk,\n" +
			"			pipe => s_pipe,\n" +
			"			x => in"+(i+1)+",\n" +
			"			addr => s_addr,\n" +
			"			MFC_a => s_MFC_a"+(i+1)+",\n" +
			"			MFC_m => s_MFC_m"+(i+1)+",\n" +
			"			alpha_1 => s_alpha_"+(i+1)+"1,\n" +
			"			alpha_2 => s_alpha_"+(i+1)+"2,\n" +
			"			L_1 => s_L_"+(i+1)+"1,\n" +
			"			L_2 => s_L_"+(i+1)+"2);\n\n";
			addLibrary("RuleSelect");
			arithmetic_map+=(arithmetic.isSelected()?BloqueArithAntecedente:"")+
			(BloqueMemoriaAntecedente[i]!=null?BloqueMemoriaAntecedente[i]:"") +
			"	RulSel"+(i+1)+" : RuleSelect\n" +
			"		generic map(\n" +
			"			grad => "+name+"_grad,\n" +
			"			bitl => "+name+"_MFC"+(i+1)+"_bits)\n" +
			"		port map(\n" +
			"			alpha_1 => s_alpha_"+(i+1)+"1,\n" +
			"			alpha_2 => s_alpha_"+(i+1)+"2,\n" +
			"			L_1 => s_L_"+(i+1)+"1,\n" +
			"			L_2 => s_L_"+(i+1)+"2,\n" +
			"			S => s_addr("+name+"_inputs-"+restar_en_S+"),\n" +
			"			L => s_addr_R(("+subsuma1+") downto ("+subsuma2+")),\n" +
			"			alpha => s_alphas(("+(i+1)+" * "+name+"_grad) downto (("+i+" * "+name+"_grad) + 1)));\n\n"
			;
		}
		String signalsDivision="-- Internal signals (Division)\n" +
		"	signal s_Num : std_logic_vector("+name+"_bitsn downto 1);\n" +
		"	signal s_Den : std_logic_vector("+name+"_bitsn-"+name+"_No downto 1);\n\n";
		if(simplificado)
			signalsDivision="";
		
		if(!RB_RAM.isSelected()){
			componentRulesMem="	component "+name+"_RulesMem\n" +
				"		port(\n" +
				"			addr : in std_logic_vector(("+suma_MFC_bits+")-1 downto 0); -- Address bus.\n" +
				"			do : out std_logic_vector("+ (defuzzy.equalsIgnoreCase("TakagiSugeno")?""+(mfcs.size()+1)+"*":
				((defuzzy.equalsIgnoreCase("WeightedFuzzyMean")||defuzzy.equalsIgnoreCase("Quality")||defuzzy.equalsIgnoreCase("GammaQuality"))?name+"_W + ":""))+name+"_No downto 1)); -- Data bus.\n" +
				"	end component;\n\n";
			BloqueRulMem="	RulMem : "+name+"_RulesMem\n" +
				"		port map(\n" +
				"			addr => s_addr_R,\n" +
				"			do => s_output_mem_R);\n\n";
		}else{//RB_RAM selected
			addLibrary("RulesMem_RAM");
			BloqueRulMem="	RulMem : RulesMem_RAM\n" +
				"		generic map(\n" +
				"			a_bits => "+name+"_a_bits,\n" +
				"			d_bits => "+name+"_d_bits)\n\n" +
				"		port map(\n" +
				"			clk  => clk,\n" +
				"			we   => clk,\n" +
				"			addr => s_addr_R,\n" +
				"			di   => s_output_mem_R,\n" +
				"			do   => s_output_mem_R);\n"
				;
		}
		addLibrary("Control");
		res="---------------------------------------------------------------------------\n" +
		"  --                               "+name+"                               --\n" +
		"---------------------------------------------------------------------------\n\n" +
		librerias+
		"library XfuzzyLib;\n" +
		"use XfuzzyLib.Entities.all;\n\n" +
		"entity "+name+" is\n\n" +
		"	generic(\n" +
		"		"+name+"_pipe : integer := pipe_"+name+";\n" +
		"		"+name+"_bits : integer := bits_"+name+");\n" +
		"	port(\n" +
		"		clk : in std_logic; -- Clock signal.\n" +
		"		reset : in std_logic; -- Reset signal.\n" +
		puertosin +//para cada entrada una linea aqui
		"		out1 : out std_logic_vector("+name+"_No downto 1); -- Output signal.\n" +
		"		pipeline : out std_logic); -- Pipeline signal.\n\n" +
		"end "+name+";\n\n\n" +
		"architecture FPGA of "+name+" is\n\n" +
		(componentesin.equalsIgnoreCase("")&&
				componentRulesMem.equalsIgnoreCase("")
				?"":"-- Components declaration\n\n") +
		componentesin +//para cada entrada un bloque component
		componentRulesMem +
		"-- Internal signals (Control)\n\n" +
		"	signal s_pipe : std_logic;\n" +
		"	signal s_acc : std_logic;\n" +
		"	signal s_addr : std_logic_vector("+name+"_bits downto 1);\n\n" +
		
		arithmetic_signal +
		"-- Internal signals (RuleSelect)\n" +
		"	signal s_alphas : std_logic_vector(("+name+"_inputs * "+name+"_grad) downto 1);\n" +
		"-- Internal signals (RulMem)\n" +
		"	signal s_addr_R : std_logic_vector(" +suma_MFC_bits+" downto 1);\n"+
		"	signal s_output_mem_R : std_logic_vector("+ (defuzzy.equalsIgnoreCase("TakagiSugeno")?""+(mfcs.size()+1)+"*":
			((defuzzy.equalsIgnoreCase("WeightedFuzzyMean")||defuzzy.equalsIgnoreCase("Quality")||defuzzy.equalsIgnoreCase("GammaQuality"))?name+"_W + ":""))+name+"_No downto 1);\n" +
		"-- Internal signals ("+(connective.equalsIgnoreCase("min")?"Minimum":"Product")+")\n" +
		"	signal s_output_"+connective+" : std_logic_vector("+name+"_grad downto 1);\n" +
		signalsDivision+
		"begin\n\n" +
		"	pipeline <= s_pipe;\n\n" +
		"	Controller : Control\n" +
		"		generic map(\n" +
		"			inputs => "+name+"_inputs,\n" +
		"			bitc => "+name+"_bits,\n" +
		"			ncp => "+name+"_pipe)\n" +
		"		port map(\n" +
		"			clk => clk,\n" +
		"			reset => reset,\n" +
		"			addr => s_addr,\n" +
		"			pipe => s_pipe,\n" +
		"			acc => s_acc);\n\n" +
		arithmetic_map +
		BloqueRulMem +
		generaConectivoYDefuzificadorFLC(spec)+
		"end FPGA;\n"
		;
				
		return res;
	}
	/**
	 * Genera el código VHDL asociado al FLC y lo almacena en el atributo vhdl.
	 * @param spec Especificación XFL.
	 * @param nombre_i Número de orden del modulo de inferencia 
	 *            dentro del arbol de la especificacion.
	 */
	public void generaVHDL(Specification spec, String nombre_i) throws IOException{
		
		int bitswhen;
		String entradas="";
		String rulesmem="";
		String flc="";
	    XfvhdlProperties.dir_regl=0;
	    setSimplificado(spec);
	    
		String cabecera=generaCabecera();
		String constantes=generaConstantes(spec) ;
		
		for(int i=0;i<mfcs.size();i++){
			bitswhen=(int)Math.ceil(Math.log(Integer.valueOf(mfcs.get(i).getn_fp()))/Math.log(2));
			bits_var.add(bitswhen);
			XfvhdlProperties.dir_regl+=bitswhen;
		}
		
		if(arithmetic.isSelected()){
			if(MFC_ROM.isSelected()||MFC_logicBlock.isSelected()){
				entradas+=generaEntradasAritmeticas();					
			}else if(MFC_RAM.isSelected()){
				//no se generan las entradas
			}
		}	
		else	// MFCs en memoria
		{
			if(MFC_ROM.isSelected()){
				XfvhdlAntecedentMemFiles antecedentes=new XfvhdlAntecedentMemFiles();
				entradas+=antecedentes.createMemorySourceROM(spec, this);				
			}else if(MFC_logicBlock.isSelected()){
				XfvhdlAntecedentMemFiles antecedentes=new XfvhdlAntecedentMemFiles();
				entradas+=antecedentes.createMemorySourceLB(spec, this);							
			}else if(MFC_RAM.isSelected()){
				//no se generan las entradas
			}
		}
		
		if(RB_ROM.isSelected()){
			rulesmem=generaRulesMemROM(spec,nombre_i);
		}else if(RB_logicBlock.isSelected()){
			rulesmem=generaRulesMemLB(spec,nombre_i);
		}else if(RB_RAM.isSelected()){
			//No se genera la rulebase
		}
			
		flc+=generaFinFLC(spec);
		vhdl=cabecera+constantes+entradas+rulesmem+flc;
	
	}
	/**Genera los ficheros complementarios si el usuario lo ha elegido así.
	 * @param nombre_i Número de orden del modulo de inferencia 
	 * dentro del arbol de la especificacion.*/
	public void generaComplementarios(String nombre_i) throws IOException{

		XfvhdlAntecedentMemComplementaryFiles amcf =
            new XfvhdlAntecedentMemComplementaryFiles();
         amcf.createCF(spec,this);
		
         
         XfvhdlRulesMemComplementaryFile rmcf =
	            new XfvhdlRulesMemComplementaryFile();
         if(defuzzy!="TakagiSugeno")	
			rmcf.createCF(spec, nombre_i, bits_var, null, calculateWeights, No, W, this);
         else
        	 rmcf.createCF(spec, nombre_i, bits_var, getTakSugBinario(No), calculateWeights, No, W, this);
         
	}
		
		
       
    
	
	/**Este método actualiza el atributo estático XfvhdlProperties.listaBloquesDeBiblioteca, 
	 * añadiéndole el bloque de librería que se le pasa por parámetro.
	 * @param l Nombre del bloque de librería.*/
	private void addLibrary(String l){
		if(!XfvhdlProperties.listaBloquesDeBiblioteca.contains(l))
			XfvhdlProperties.listaBloquesDeBiblioteca.add(l);
	}
	
	/**Método que a partir de una especificación XFL indica si sus entradas no 
	 * están normalizadas porque el solapamiento de las funciones de pertenencia 
	 * de alguna de ellas es mayor que 1.
	 * @param spec Especificación XFL.
	 * @return No normalizadas por solapamiento > 1.*/
	public boolean entradasNoNormalizadasMayor1(Specification spec){
		boolean res=false;
		Variable[] var = spec.getSystemModule().getInputs();
		for (int i=0;i<mfcs.size()&&!res;i++){
			u = disc.discretizeUniverse(var[i],this,i);
			res=disc.getEntradasNoNormalizadasMayor1();
			
		}
		return res;
	}
	/**Método que a partir de una especificación XFL indica si sus entradas no 
	 * están normalizadas porque el solapamiento de las funciones de pertenencia 
	 * de alguna de ellas es menor que 1.
	 * @param spec Especificación XFL.
	 * @return No normalizadas por solapamiento < 1.*/
	public boolean entradasNoNormalizadasMenor1(Specification spec){
		boolean res=false;
		boolean aux=false;
		Variable[] var = spec.getSystemModule().getInputs();
		for (int i=0;i<mfcs.size();i++){
			u = disc.discretizeUniverse(var[i],this,i);
			aux=disc.getEntradasNoNormalizadasMenor1();
			if(aux){
				mfcs.get(i)._a="[ ];\n";
				mfcs.get(i).slopes="[ ];\n";
				mfcs.get(i).listaPendientes=new ArrayList<Double>();
				mfcs.get(i).listaPuntos=new ArrayList<Double>();
			}
			res=aux||res;
		}
		if(res)
			setEntradasNoNormalizadasMenor1(res);
		
		return res;
	}
	
	
	
	
	
	
	/**@param spec Especificación XFL.
	 * @return Código VHDL del conectivo y el defuzzidicador del FLC.*/
	private String generaConectivoYDefuzificadorFLC(Specification spec){
		String minprod="";
		String Mui="			Mui => s_alphas,\n";
		String def;
		if (mfcs.size()!=1){
			addLibrary(connective.equalsIgnoreCase("min")?"Minimum":"Product");
			minprod=(connective.equalsIgnoreCase("min")?"	min : Minimum\n":"	prod : Product\n") +
				"		generic map(\n" +
				"			grad => "+name+"_grad,\n" +
				"			inputs => "+name+"_inputs)\n" +
				"		port map(\n" +
				"			input => s_alphas,\n" +
				"			output => s_output_"+(connective.equalsIgnoreCase("min")?"min":"prod")+");\n\n"
				;
			if(connective.equalsIgnoreCase("min")){
				Mui="			Mui => s_output_min,\n";
			}else
				Mui="			Mui => s_output_prod,\n";
		}
		String div="	Div : Division\n" +
			"		generic map(\n" +
			"			No => "+name+"_No,\n" +
			"			bitsn => "+name+"_bitsn)\n" +
			"		port map(\n" +
			"			clk => clk,\n" +
			"			pipe => s_pipe,\n" +
			"			Num => s_Num,\n" +
			"			Den => s_Den,\n" +
			"			result => out1);\n\n\n";
		if(simplificado)
			div="\n\n\n";
		if(defuzzy.equalsIgnoreCase("FuzzyMean")){
			if(!simplificado){
				addLibrary("FuzzyMean");
				addLibrary("Division");
				def="	Defuzz : FuzzyMean\n" +
				"		generic map(\n" +
				"			No => "+name+"_No,\n" +
				"			grad => "+name+"_grad,\n" +
				"			inputs => "+name+"_inputs,\n" +
				"			bitsn => "+name+"_bitsn)\n" +
				"		port map(\n" +
				"			clk => clk,\n" +
				"			pipe => s_pipe,\n" +				
				"			acc => s_acc,\n" +
				"			ci => s_output_mem_R,\n" +
				Mui + //aqui hay diferencias
				"			Num => s_Num,\n" +
				"			Den => s_Den);\n\n"+
				div;
				
			}
			else{
				addLibrary("FuzzyMean_s");
				def="	Defuzz : FuzzyMean_s\n" +
					"		generic map(\n" +
					"			No => "+name+"_No,\n" +
					"			grad => "+name+"_grad)\n" +
					"		port map(\n" +
					"			clk => clk,\n" +
					"			pipe => s_pipe,\n" +
					"			acc => s_acc,\n" +
					"			ci => s_output_mem_R,\n" +
					Mui +
					"			result => out1);\n\n";
			}
		}else if(defuzzy.equalsIgnoreCase("MaxLabel")){//En este caso siempre es simplificado
			addLibrary("MaxLabel");
			def="	Defuzz : MaxLabel\n" +
				"		generic map(\n" +
				"			No => "+name+"_No,\n" +
				"			grad => "+name+"_grad)\n" +
				"		port map(\n" +
				"			clk => clk,\n" +
				"			pipe => s_pipe,\n" +
				"			acc => s_acc,\n" +
				"			ci => s_output_mem_R,\n" +
				Mui +
				"			result => out1);\n\n";
		}else if(defuzzy.equalsIgnoreCase("TakagiSugeno")){
			String ents="";
			String coefGeneric="\t\t\tPc  => "+name+"_Pc,\n";
			int coef1=mfcs.size()+1;
			int coef2=mfcs.size();
			String abc="\t\t\tc => s_output_mem_R ("+coef1+"*"+name+"_No downto "+coef2+"*"+name+"_No+1),\n";
			
			for(int i=0;i<mfcs.size();i++){
				ents+="\t\t\tx"+(i+1)+" => in"+(i+1)+",\n";
				coef1--;
				coef2--;
				abc+="\t\t\ta"+(i+1)+" => s_output_mem_R ("+coef1+"*"+name+"_No downto "+coef2+"*"+name+"_No+1),\n";
				coefGeneric+="\t\t\tPa"+(i+1)+"  => "+name+"_Pa"+(i+1);
				if(i<mfcs.size()-1)
					coefGeneric+=",\n";
				else
					coefGeneric+=")\n";
			}
			
			if(connective.equalsIgnoreCase("min")){
				Mui="			Mui => s_output_min,\n";
			}else
				Mui="			Mui => s_output_prod,\n";
			
			if(!simplificado){
				addLibrary("TakagiSugeno");
				addLibrary("Division");
				def="	Defuzz : TakagiSugeno\n" +
				"		generic map(\n" +
				"			No => "+name+"_No,\n" +
				"			N => "+name+"_N,\n" +
				"			grad => "+name+"_grad,\n" +
				"			bitsn => "+name+"_bitsn,\n" +
				coefGeneric +
				"		port map(\n" +
				"			clk => clk,\n" +
				"			pipe => s_pipe,\n" +
				"			acc => s_acc,\n" +
				ents +
				abc+
				Mui + 
				"			Num => s_Num,\n" +
				"			Den => s_Den);\n\n"+
				div;
				
			}
			else{
				addLibrary("TakagiSugeno_s");
				def="	Defuzz : TakagiSugeno_s\n" +
					"		generic map(\n" +
					"			No => "+name+"_No,\n"+
					"			N => "+name+"_N,\n"+
					"			grad => "+name+"_grad,\n"+
					coefGeneric +
					"		port map(\n" +
					"			clk => clk,\n" +
					"			pipe => s_pipe,\n" +
					"			acc => s_acc,\n" +
					ents +
					abc +
					Mui +
					"			result => out1);\n\n";
			}
		}else{//WeightedFuzzyMean, Quality o GammaQuality=>siempre con div
			addLibrary("WeightedFuzzyMean");
			addLibrary("Division");
			def="	Defuzz : WeightedFuzzyMean\n" +
			"		generic map(\n" +
			"			No => "+name+"_No,\n" +
			"			grad => "+name+"_grad,\n" +
			"			W => "+name+"_W,\n" +
			"			inputs => "+name+"_inputs,\n" +
			"			bitsn => "+name+"_bitsn)\n" +
			"		port map(\n" +
			"			clk => clk,\n" +
			"			acc => s_acc,\n" +
			"			pipe => s_pipe,\n" +
			"			ci => s_output_mem_R("+name+"_No+"+name+"_W downto "+name+"_W+1),\n" +
			Mui + 
			"			Gmi => s_output_mem_R("+name+"_W downto 1),\n" +
			"			Num => s_Num,\n" +
			"			Den => s_Den);\n\n"+
			div;
			
		}
		
		return (minprod+def);
	}
	
	
	/*double PasarDecimalBinario(double valor, double max, double min,
			int precision) {

		double max_bin;
		int resultado;

		max_bin = Math.pow(2.0, precision) - 1;

		resultado = (int) ((max_bin * (valor - min)) / (max - min));
		return (resultado);
	}*/

	// CLASE PRIVADA QUE REPRESENTA A LOS MFCs

	/**Subclase que representa a los MFCs.*/
	public class MFC {

		/**Número de funciones de pertenencia.*/
		public String n_fp;

		/**Puntos de corte.*/
		public String _a;

		/**Pendientes.*/
		public String slopes;
		
		/**Lista de pendientes.*/
		public ArrayList<Double> listaPendientes;
		
		/**Lista de puntos de corte.*/
		public ArrayList<Double> listaPuntos;
		
		/**Constructor de la subclase MFC.*/
		public MFC(String n_fp, String _a, String slopes, ArrayList<Double> listaPuntos, ArrayList<Double> listaPendientes) {
			this.listaPendientes=listaPendientes;
			this.n_fp = n_fp;
			this._a = _a;
			this.slopes = slopes;
			this.listaPuntos=listaPuntos;
			
		}
		
		//MÉTODOS GET
		
		/**@param pos Nº de la función de pertenencia de la que quiero 
		 * saber su pendiente.
		 * @return Pendiente.*/
		public double getPendiente(int pos){
			return listaPendientes.get(pos);
		}
		
		/**@return Techo del máximo de las pendientes del MFC.*/
		private int getPendienteMayor(){
			double pdte=0;
			for(int i=0;i<listaPendientes.size();i++){
				if (listaPendientes.get(i)>pdte)
					pdte=listaPendientes.get(i);
			}
			return (int)Math.ceil(pdte);
			
		}
		
		/**@param pos Nº de la orden del punto de corte.
		 * @return Punto de corte.*/
		public double getPunto (int pos){
			return listaPuntos.get(pos);
		}
		
		/**@return Nº de funciones de pertenencia.*/
		public String getn_fp() {
			return n_fp;
		}

		/**@return Puntos de corte*/
		public String get_a() {
			return _a;
		}

		/**@return Pendientes.*/
		public String getslopes() {
			return slopes;
		}
		
		
		//MÉTODOS DE LA CLASE
		/**@return Número de bits de la parte entera de P*/
		public int pbi(){
			int bits_pbi;
			int pdte_mayor=getPendienteMayor();
			bits_pbi=(int)Math.ceil(Math.log(pdte_mayor)/Math.log(2));
			return bits_pbi;
		}
		

	}
	/**Clase privada que gestiona el hecho de que cuando el usuario elige 
	 * generar código con antecedentes en memoria, se bloquee el campo 
	 * que captura el nº de bits para la pendiente, ya que no hace falta 
	 * indicarlo.*/
	private class VoteActionListener implements ActionListener {
	      public void actionPerformed(ActionEvent ex) {
	        String choice = group1.getSelection().getActionCommand();
	        if (choice=="memory"){
	        	numBitsInformation[3].setText("0");
	        	numBitsInformation[3].setEditable(false);
	        	numBitsInformation[3].setFieldEnabled(false);
	        	numBitsInformation[3].setLabelEnabled(false);
	        	
	        }else{
	        	numBitsInformation[3].setEditable(true);
	        	numBitsInformation[3].setFieldEnabled(true);
	        	numBitsInformation[3].setLabelEnabled(true);
	        	
	        }
	      }
	    }

	

}
