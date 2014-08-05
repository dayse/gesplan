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
//			FUNCION DE ERROR			//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.lang.*;

public class XfslErrorFunction implements Cloneable {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int MEAN_SQUARE_ERROR = 0;
 public static final int W_MEAN_SQUARE_ERROR = 1;
 public static final int MEAN_ABS_ERROR = 2;
 public static final int W_MEAN_ABS_ERROR = 3;
 public static final int CLASIF_ERROR = 4;
 public static final int ADV_CLASIF_ERROR = 5;
 public static final int NEFCLASS = 6;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int code;
 private double[] weight;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			  CONSTRUCTORES				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Constructor utilizado en la interfaz grafica		//
 //-------------------------------------------------------------//

 public XfslErrorFunction(int code) throws XflException {
  if(code < 0) throw new XflException(25);
  this.code = code;
  this.weight = new double[0];
 }

 //-------------------------------------------------------------//
 // Constructor usado en la configuracion			//
 //-------------------------------------------------------------//

 public XfslErrorFunction(String name) throws XflException {
  this(nameToCode(name));
 }

 //-------------------------------------------------------------//
 // Constructor con pesos usado en la configuracion		//
 //-------------------------------------------------------------//

 public XfslErrorFunction(String name, double[] weights) throws XflException {
  this(nameToCode(name));
  setWeights(weights);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //		METODOS ESTATICOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Parser de los identificadores de las funciones		//
 //-------------------------------------------------------------//

 private static int nameToCode(String name) {
  if(name.equals("mean_square_error")) return MEAN_SQUARE_ERROR;
  if(name.equals("weighted_mean_square_error")) return W_MEAN_SQUARE_ERROR;
  if(name.equals("mean_absolute_error")) return MEAN_ABS_ERROR;
  if(name.equals("weighted_mean_absolute_error")) return W_MEAN_ABS_ERROR;
  if(name.equals("classification_error")) return CLASIF_ERROR;
  if(name.equals("advanced_classification_error")) return ADV_CLASIF_ERROR;
  if(name.equals("classification_square_error")) return NEFCLASS;
  if(name.equals("MSE")) return MEAN_SQUARE_ERROR;
  if(name.equals("WMSE")) return W_MEAN_SQUARE_ERROR;
  if(name.equals("MAE")) return MEAN_ABS_ERROR;
  if(name.equals("WMAE")) return W_MEAN_ABS_ERROR;
  if(name.equals("CE")) return CLASIF_ERROR;
  if(name.equals("ACE")) return ADV_CLASIF_ERROR;
  if(name.equals("CSE")) return NEFCLASS;
  return -1;
 }

 //-------------------------------------------------------------//
 // Identificador asociado a cada funcion			//
 //-------------------------------------------------------------//

 private static String codeToName(int code) {
  switch(code) {
   case MEAN_SQUARE_ERROR: return "MSE";
   case W_MEAN_SQUARE_ERROR: return "WMSE";
   case MEAN_ABS_ERROR: return "MAE";
   case W_MEAN_ABS_ERROR: return "WMAE";
   case CLASIF_ERROR: return "CE";
   case ADV_CLASIF_ERROR: return "ACE";
   case NEFCLASS: return "CSE";
  }
  return null;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Devuelve una copia del objeto				//
 //-------------------------------------------------------------//

 public Object clone() {
  try {
   XfslErrorFunction clone = new XfslErrorFunction(this.code);
   double wclone[] = new double[weight.length];
   System.arraycopy(weight,0,wclone,0,weight.length);
   clone.setWeights(wclone);
   return clone;
  } catch(Exception ex) { return null; }
 }

 //-------------------------------------------------------------//
 // Obtiene el codigo de la funcion				//
 //-------------------------------------------------------------//

 public int getCode() { return code; }

 //-------------------------------------------------------------//
 // Obtiene el nombre de la funcion (para la ventana de xfsl)	//
 //-------------------------------------------------------------//

 public String getName() {
  switch(code) {
   case MEAN_SQUARE_ERROR: return "Mean Square Error";
   case W_MEAN_SQUARE_ERROR: return "Weighted Mean Square Error";
   case MEAN_ABS_ERROR: return "Mean Absolute Error";
   case W_MEAN_ABS_ERROR: return "Weighted Mean Absolute Error";
   case CLASIF_ERROR: return "Classification Error";
   case ADV_CLASIF_ERROR: return "Advanced Classification Error";
   case NEFCLASS: return "Classification Square Error";
  }
  return null;
 }

 //-------------------------------------------------------------//
 // Analiza si es una funcion de error de clasificacion		//
 //-------------------------------------------------------------//

 public boolean isClassification() {
  switch(code) {
   case MEAN_SQUARE_ERROR:
   case W_MEAN_SQUARE_ERROR:
   case MEAN_ABS_ERROR:
   case W_MEAN_ABS_ERROR: return false;
   case CLASIF_ERROR:
   case ADV_CLASIF_ERROR:
   case NEFCLASS: return true;
   default: return false;
  }
 }

 //-------------------------------------------------------------//
 // Analiza si la funcion utiliza pesos				//
 //-------------------------------------------------------------//

 public boolean isWeighted() {
  return (code == W_MEAN_SQUARE_ERROR || code == W_MEAN_ABS_ERROR);
 }

 //-------------------------------------------------------------//
 // Codigo del fichero de configuracion				//
 //-------------------------------------------------------------//

 public String toCode() {
  String eol = System.getProperty("line.separator", "\n");
  String src = "xfsl_errorfunction("+codeToName(code);
  for(int i=0; i<weight.length; i++) src += ", "+weight[i];
  src += ")"+eol;
  return src;
 }

 //-------------------------------------------------------------//
 // Asigna los valores de los pesos				//
 //-------------------------------------------------------------//

 public void setWeights(double[] weight) throws XflException {
  if(!isWeighted() && weight.length>0) throw new XflException(25);
  this.weight = weight;
 }

 //-------------------------------------------------------------//
 // Obtiene los valores de los pesos				//
 //-------------------------------------------------------------//

 public double[] getWeights() {
  return this.weight;
 }

 //-------------------------------------------------------------//
 // Normaliza el valor de los pesos				//
 //-------------------------------------------------------------//

 public void normalizeWeights(int numoutputs) {
  if(!isWeighted()) return;
  double aw[] = new double[numoutputs];
  double norm = 0;
  for(int i=0; i<weight.length && i<numoutputs; i++) norm += weight[i];
  for(int i=weight.length; i<numoutputs; i++) norm += 1;
  for(int i=0; i<weight.length && i<numoutputs; i++) aw[i] = weight[i]/norm;
  for(int i=weight.length; i<numoutputs; i++) aw[i] = 1/norm;
  this.weight = aw;
 }

 //-------------------------------------------------------------//
 // Evalua la funcion de error para un conjunto de patrones	//
 //-------------------------------------------------------------//

 public XfslEvaluation evaluate(Specification s, XfslPattern p, double le) {
  switch(code) {
   case MEAN_SQUARE_ERROR: return MSE(s,p,le);
   case W_MEAN_SQUARE_ERROR: return WMSE(s,p,le);
   case MEAN_ABS_ERROR: return MAE(s,p,le);
   case W_MEAN_ABS_ERROR: return WMAE(s,p,le);
   case CLASIF_ERROR: return CE(s,p,le);
   case ADV_CLASIF_ERROR: return ACE(s,p,le);
   case NEFCLASS: return NCE(s,p,le);
  }
  return null;
 }

 //-------------------------------------------------------------//
 // Calcula las derivadas del sistema 				//
 //-------------------------------------------------------------//

 public XfslEvaluation compute_derivatives(Specification s, XfslPattern p)
 throws XflException{
  switch(code) {
   case MEAN_SQUARE_ERROR: return dMSE(s,p);
   case W_MEAN_SQUARE_ERROR: return dWMSE(s,p);
   case MEAN_ABS_ERROR: return dMAE(s,p);
   case W_MEAN_ABS_ERROR: return dWMAE(s,p);
   case CLASIF_ERROR: return dCE(s,p);
   case ADV_CLASIF_ERROR: return dACE(s,p);
   case NEFCLASS: return dNCE(s,p);
  }
  throw new XflException(19);
 }

 //-------------------------------------------------------------//
 // Estima las derivadas del sistema mediante la tangente	//
 //-------------------------------------------------------------//

 public XfslEvaluation estimate_derivatives(Specification spec,
 XfslPattern pattern, double perturb) {

  Parameter[] adjustable = spec.getAdjustable();
  XfslEvaluation ev1 = evaluate(spec,pattern,1.0);
  for(int i=0; i<adjustable.length; i++) {
   double prev = adjustable[i].value;
   double sign = (Math.random() <0.5? 1.0 : -1.0);
   adjustable[i].setDesp(sign*perturb);
   spec.update();
   XfslEvaluation ev2 = evaluate(spec,pattern,1.0);
   if((adjustable[i].value - prev)/(sign*perturb) >0.001 ) {
    double deriv = (ev2.error - ev1.error)/(adjustable[i].value - prev);
    adjustable[i].addDeriv( deriv );
   }
   adjustable[i].value = prev;
  }
  return ev1;
 }

 //-------------------------------------------------------------//
 // Estima las derivadas del sistema de forma grosera		//
 //-------------------------------------------------------------//

 public XfslEvaluation stochastic_derivatives(Specification spec,
 XfslPattern pattern, double perturb) {

  Parameter[] adjustable = spec.getAdjustable();
  double prev[] = new double[adjustable.length];
  double val1[] = new double[adjustable.length];
  double sign[] = new double[adjustable.length];
  for(int i=0; i<adjustable.length; i++) {
   prev[i] = adjustable[i].value;
   sign[i] = (Math.random() <0.5? 1.0 : -1.0);
   adjustable[i].setDesp(sign[i]*perturb);
  }
  spec.update();
  XfslEvaluation ev1 = evaluate(spec,pattern,1.0);

  for(int i=0; i<adjustable.length; i++) {
   val1[i] = adjustable[i].value;
   adjustable[i].value = prev[i];
   adjustable[i].setDesp(-sign[i]*perturb);
  }
  spec.update();
  XfslEvaluation ev2 = evaluate(spec,pattern,1.0);

  for(int i=0; i<adjustable.length; i++) {
   double val2 = adjustable[i].value;
   adjustable[i].value = prev[i];
   if( (val1[i]-val2)/(sign[i]*perturb) >0.001 )
    adjustable[i].addDeriv((ev2.error - ev1.error)/(val2 - val1[i]));
  }
  return evaluate(spec,pattern,1.0);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Calculo de Mean Square Error				//
 //-------------------------------------------------------------//

 private XfslEvaluation MSE(Specification spec,XfslPattern pattern,double le) {
  SystemModule system = spec.getSystemModule();
  double mxae=0, mse=0, error=0;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   for(int i=0; i<output.length; i++) {
    double dev = (output[i]-pattern.output[p][i])/pattern.range[i];
    if(dev<0) dev = -dev;
    error += dev*dev/output.length;
    mse += dev*dev/output.length;
    if(dev>mxae) mxae = dev;
   }
  }
  return new XfslEvaluation(error,mse,mxae,le,pattern.input.length);
 }

 //-------------------------------------------------------------//
 // Calculo de Weighted Mean Square Error			//
 //-------------------------------------------------------------//

 private XfslEvaluation WMSE(Specification spec,XfslPattern pattern,double le) {
  SystemModule system = spec.getSystemModule();
  double mxae=0, mse=0, error=0;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   for(int i=0; i<output.length; i++) {
    double dev = (output[i]-pattern.output[p][i])/pattern.range[i];
    if(dev<0) dev = -dev;
    error += weight[i]*dev*dev;
    mse += dev*dev/output.length;
    if(dev>mxae) mxae = dev;
   }
  }
  return new XfslEvaluation(error,mse,mxae,le,pattern.input.length);
 }

 //-------------------------------------------------------------//
 // Calculo de Mean Absolute Error				//
 //-------------------------------------------------------------//

 private XfslEvaluation MAE(Specification spec,XfslPattern pattern,double le) {
  SystemModule system = spec.getSystemModule();
  double mxae=0, mse=0, error=0;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   for(int i=0; i<output.length; i++) {
    double dev = (output[i]-pattern.output[p][i])/pattern.range[i];
    if(dev<0) dev = -dev;
    error += dev/output.length;
    mse += dev*dev/output.length;
    if(dev>mxae) mxae = dev;
   }
  }
  return new XfslEvaluation(error,mse,mxae,le,pattern.input.length);
 }

 //-------------------------------------------------------------//
 // Calculo de Weighted Mean Absolute Error			//
 //-------------------------------------------------------------//

 private XfslEvaluation WMAE(Specification spec,XfslPattern pattern,double le) {
  SystemModule system = spec.getSystemModule();
  double mxae=0, mse=0, error=0;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   for(int i=0; i<output.length; i++) {
    double dev = (output[i]-pattern.output[p][i])/pattern.range[i];
    if(dev<0) dev = -dev;
    error += weight[i]*dev;
    mse += dev*dev/output.length;
    if(dev>mxae) mxae = dev;
   }
  }
  return new XfslEvaluation(error,mse,mxae,le,pattern.input.length);
 }

 //-------------------------------------------------------------//
 // Calculo de Classification Error				//
 //-------------------------------------------------------------//

 private XfslEvaluation CE(Specification spec, XfslPattern pattern, double le) {
  SystemModule system = spec.getSystemModule();
  double misscr=0, misscn=0, error=0;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   double norm = output.length * pattern.input.length;
   double err = 0;
   for(int i=0; i<output.length; i++)
    if(output[i] != pattern.output[p][i]) err++;
   error += err/norm;
   misscn += err;
   misscr += err/norm;
  }
  return new XfslEvaluation(error,misscr,misscn,le);
 }

 //-------------------------------------------------------------//
 // Calculo de Advanced Classification Error			//
 //-------------------------------------------------------------//

 private XfslEvaluation ACE(Specification spec,XfslPattern pattern,double le) {
  SystemModule system = spec.getSystemModule();
  double misscr=0, misscn=0, error=0;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   double norm = output.length * pattern.input.length;
   AggregateMemFunc[] fuzzyvalue = system.getFuzzyValues();
   double err1=0, err2=0;
   for(int i=0; i<output.length; i++) {
    if(output[i] != pattern.output[p][i]) {
     double degree1 = fuzzyvalue[i].getActivationDegree(output[i]);
     double degree2 = fuzzyvalue[i].getActivationDegree(pattern.output[p][i]);
     err1 += 1 + ( degree1 - degree2 )/output.length; 
     err2++;
    }
   }
   error += err1/norm;
   misscr += err2/norm;
   misscn += err2;
  }
  return new XfslEvaluation(error,misscr,misscn,le);
 }

 //-------------------------------------------------------------//
 // Calculo de Square Classification Error			//
 //-------------------------------------------------------------//

 private XfslEvaluation NCE(Specification spec,XfslPattern pattern,double le) {
  SystemModule system = spec.getSystemModule();
  double misscr=0, misscn=0, error=0;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   double norm = output.length * pattern.input.length;
   AggregateMemFunc[] fuzzyvalue = system.getFuzzyValues();
   Variable[] outvar = system.getOutputs();
   double err1 = 0, err2 = 0;
   for(int i=0; i<output.length; i++) {
    LinguisticLabel[] pmf = outvar[i].getType().getAllMembershipFunctions();
    for(int j=0; j<pmf.length; j++) {
     double degree = fuzzyvalue[i].getActivationDegree(pmf[j].center());
     if(pmf[j].center() == pattern.output[p][i])
      err1 += (1-degree)*(1-degree)/pmf.length;
     else err1 += degree*degree/pmf.length;
    }
    if(output[i] != pattern.output[p][i]) err2++;
   }
   error += err1/norm;
   misscr += err2/norm;
   misscn += err2;
  }
  return new XfslEvaluation(error,misscr,misscn,le);
 }

 //-------------------------------------------------------------//
 // Derivada de Mean Square Error				//
 //-------------------------------------------------------------//

 private XfslEvaluation dMSE(Specification spec, XfslPattern pattern) 
 throws XflException {
  SystemModule system = spec.getSystemModule();
  double mxae=0, mse=0, error=0;
  double[] range = pattern.range;
  int numpatterns = pattern.input.length;
  for(int p=0; p<numpatterns; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   double[] target = pattern.output[p];
   double[] deriv = new double[output.length];
   for(int i=0; i<output.length; i++) {
    double dev = (output[i]-target[i])/range[i];
    deriv[i] = 2*dev/(range[i]*output.length*numpatterns);
    if(dev<0) dev = -dev;
    error += dev*dev/output.length;
    mse += dev*dev/output.length;
    if(dev>mxae) mxae = dev;
   }
   system.derivative(deriv);
  }
  return new XfslEvaluation(error,mse,mxae,numpatterns);
 }

 //-------------------------------------------------------------//
 // Derivada de Weighted Mean Square Error			//
 //-------------------------------------------------------------//

 private XfslEvaluation dWMSE(Specification spec, XfslPattern pattern)
 throws XflException {
  SystemModule system = spec.getSystemModule();
  double[] range = pattern.range;
  double mxae=0, mse=0, error=0;
  int numpatterns = pattern.input.length;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   double[] target = pattern.output[p];
   double[] deriv = new double[output.length];
   for(int i=0; i<output.length; i++) {
    double dev = (output[i]-target[i])/range[i];
    deriv[i] = 2*weight[i]*dev/(range[i]*numpatterns);
    if(dev<0) dev = -dev;
    error += weight[i]*dev*dev;
    mse += dev*dev/output.length;
    if(dev>mxae) mxae = dev;
   }
   system.derivative(deriv);
  }
  return new XfslEvaluation(error,mse,mxae,numpatterns);
 }

 //-------------------------------------------------------------//
 // Derivada de Mean Absolute Error				//
 //-------------------------------------------------------------//

 private XfslEvaluation dMAE(Specification spec, XfslPattern pattern)
 throws XflException {
  SystemModule system = spec.getSystemModule();
  double[] range = pattern.range;
  double mxae=0, mse=0, error=0;
  int numpatterns = pattern.input.length;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   double[] target = pattern.output[p];
   double[] deriv = new double[output.length];
   for(int i=0; i<output.length; i++) {
    double dev = (output[i]-target[i])/range[i];
    if(dev>0) deriv[i] = 1.0;
    else if(dev<0) deriv[i] = -1.0;
    else deriv[i] = 0;
    deriv[i] /= (range[i]*output.length*numpatterns);
    if(dev<0) dev = -dev;
    error += dev/output.length;
    mse += dev*dev/output.length;
    if(dev>mxae) mxae = dev;
   }
   system.derivative(deriv);
  }
  return new XfslEvaluation(error,mse,mxae,numpatterns);
 }

 //-------------------------------------------------------------//
 // Derivada de Weighted Mean Absolute Error			//
 //-------------------------------------------------------------//

 private XfslEvaluation dWMAE(Specification spec, XfslPattern pattern)
 throws XflException {
  SystemModule system = spec.getSystemModule();
  double[] range = pattern.range;
  double mxae=0, mse=0, error=0;
  int numpatterns = pattern.input.length;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   double[] target = pattern.output[p];
   double[] deriv = new double[output.length];
   for(int i=0; i<output.length; i++) {
    double dev = (output[i]-target[i])/range[i];
    if(dev>0) deriv[i] = weight[i];
    else if(dev<0) deriv[i] = -weight[i];
    else deriv[i] = 0;
    deriv[i] /= (range[i]*numpatterns);
    if(dev<0) dev = -dev;
    error += weight[i]*dev;
    mse += dev*dev/output.length;
    if(dev>mxae) mxae = dev;
   }
   system.derivative(deriv);
  }
  return new XfslEvaluation(error,mse,mxae,numpatterns);
 }

 //-------------------------------------------------------------//
 // Derivada de Classification Error				//
 //-------------------------------------------------------------//

 private XfslEvaluation dCE(Specification spec, XfslPattern pattern)
 throws XflException {
  throw new XflException(19);
 }

 //-------------------------------------------------------------//
 // Derivada de Advanced Classification Error			//
 //-------------------------------------------------------------//

 private XfslEvaluation dACE(Specification spec, XfslPattern pattern)
 throws XflException {
  throw new XflException(19);
 }

 //-------------------------------------------------------------//
 // Derivada de Square Classification Error			//
 //-------------------------------------------------------------//

 private XfslEvaluation dNCE(Specification spec, XfslPattern pattern)
 throws XflException {
  SystemModule system = spec.getSystemModule();
  double misscr=0, misscn=0, error=0;
  for(int p=0; p<pattern.input.length; p++) {
   double[] output = system.crispInference(pattern.input[p]);
   double normalize = output.length * pattern.input.length;
   AggregateMemFunc[] fuzzyvalue = system.getFuzzyValues();
   Variable[] outvar = system.getOutputs();
   double err1 = 0, err2 = 0;
   for(int i=0; i<output.length; i++) {
    LinguisticLabel[] pmf = outvar[i].getType().getAllMembershipFunctions();
    for(int j=0; j<fuzzyvalue[i].conc.length; j++) {
     double center = fuzzyvalue[i].conc[j].center();
     double degree = fuzzyvalue[i].getActivationDegree(center);
     double norm = pattern.input.length*output.length*pmf.length;
     if(fuzzyvalue[i].conc[j].degree() == degree) 
      if(center == pattern.output[p][i])
       fuzzyvalue[i].conc[j].setDegreeDeriv(-2*(1-degree)/norm);
      else fuzzyvalue[i].conc[j].setDegreeDeriv(2*degree/norm);
    }
    for(int j=0; j<pmf.length; j++) {
     double degree = fuzzyvalue[i].getActivationDegree(pmf[j].center());
     if(pmf[j].center() == pattern.output[p][i])
      err1 += (1-degree)*(1-degree)/pmf.length;
     else err1 += degree*degree/pmf.length; 
    }
    if(output[i] != pattern.output[p][i]) err2++;
   }
   error += err1/normalize;
   misscr += err2/normalize;
   misscn += err2;
   system.derivative();
  }
  return new XfslEvaluation(error,misscr,misscn);
 }
}
