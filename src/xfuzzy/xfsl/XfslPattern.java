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

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//		   PATRONES DE ENTRENAMIENTO			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.lang.*;
import java.io.*;
import java.util.Vector;

public class XfslPattern {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public double[][] input;
 public double[][] output;
 public double[] range;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfslPattern(File file, int numinputs, int numoutputs)
 throws XflException {
  Vector pattern = readPatternFile(file);
  int numpatterns = pattern.size()/(numinputs+numoutputs);
  this.input = new double[numpatterns][numinputs];
  this.output = new double[numpatterns][numoutputs];
  for(int p=0, j=0; p<numpatterns ; p++) {
   for(int i=0; i<numinputs; i++, j++)
    input[p][i] = ((Double) pattern.elementAt(j)).doubleValue();
   for(int o=0; o<numoutputs; o++, j++)
    output[p][o] = ((Double) pattern.elementAt(j)).doubleValue();
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Introduce en el objeto los valores de los rangos de las	//
 // variables de salida						//
 //-------------------------------------------------------------//

 public void setRanges(Specification spec) {
  Variable[] output = spec.getSystemModule().getOutputs();
  this.range = new double[output.length];
  for(int i=0; i<output.length; i++) this.range[i] = output[i].range();
 }

 //-------------------------------------------------------------//
 // Calcula el universo de discurso de la variable i-esima	//
 //-------------------------------------------------------------//

 public Universe getUniverse(int var, boolean inputvar) {
  double min,max;
  if(inputvar) {
   min = max = input[0][var];
   for(int i=0; i<input.length; i++) {
    if(input[i][var] < min) min = input[i][var];
    if(input[i][var] > max) max = input[i][var];
   }
  } else {
   min = max = output[0][var];
   for(int i=0; i<output.length; i++) {
    if(output[i][var] < min) min = output[i][var];
    if(output[i][var] > max) max = output[i][var];
   }
  }
  try { return new Universe(min,max); } catch(Exception  ex) { return null; }
 }

 //-------------------------------------------------------------//
 // Obtiene el conjunto de valores de la variable de salida	//
 //-------------------------------------------------------------//

 public double[] getValues(int ovar) {
  double val[] = new double[0];
  for(int i=0;i<output.length; i++) {
   boolean included = false;
   for(int j=0;j<val.length;j++) if(val[j] == output[i][ovar]) included = true;
   if(!included) {
    double aux[] = new double[val.length+1];
    System.arraycopy(val,0,aux,0,val.length);
    aux[val.length] = output[i][ovar];
    val = aux;
   }
  }
  return val;
 }

 //-------------------------------------------------------------//
 // Cambia el orden de los patrones de forma aleatoria		//
 //-------------------------------------------------------------//

 public void shufflePattern() {
  for(int i=0; i<this.input.length; i++)
   for(int j=i+1; j<this.input.length; j++)
    if(Math.random()<0.5) {
     double[] aux = this.input[i];
     this.input[i] = this.input[j];
     this.input[j] = aux;
     aux = this.output[i];
     this.output[i] = this.output[j];
     this.output[j] = aux;
    }
 }

 //-------------------------------------------------------------//
 // Obtiene un objeto con un unico patron de datos		//
 //-------------------------------------------------------------//

 public XfslPattern getSingle(int p) {
  XfslPattern single = new XfslPattern();
  single.input = new double[1][]; single.input[0] = this.input[p];
  single.output = new double[1][]; single.output[0] = this.output[p];
  single.range = this.range;
  return single;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Constructor utilizado internamente				//
 //-------------------------------------------------------------//

 private XfslPattern() {
 }

 //-------------------------------------------------------------//
 // Lee los patrones de un fichero de datos			//
 //-------------------------------------------------------------//

 private Vector readPatternFile(File file) throws XflException {
  FileInputStream stream;
  Vector v = new Vector();
  if(file == null) return v;
  StringBuffer data = new StringBuffer();
  try {
   stream = new FileInputStream(file);
   for(int item = stream.read(); item != -1; item = stream.read()) {
    if(!Character.isWhitespace((char) item)) data.append((char) item);
    else if(data.length()>0) {
     try { v.addElement(Double.valueOf(data.toString())); }
     catch(NumberFormatException e) {}
     data = new StringBuffer();
    }
   }
   stream.close();
  }
  catch(IOException e) { throw new XflException(33); }
  return v;
 }
}
