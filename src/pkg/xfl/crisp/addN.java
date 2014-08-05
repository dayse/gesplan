//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.crisp;

import xfuzzy.lang.*;

public class addN extends CrispBlock {
 public addN() {
   super("xfl","addN");
   Parameter single[] = new Parameter[1];
   single[0] = new Parameter("N");
   setSingleParameters(single);
  }

 public double compute(double[] x) {
   double N = singleparam[0].value;
  double a = 0;
  for(int i=0; i<N; i++) a+=x[i];
  return a;
  }

 public int inputs() {
   double N = singleparam[0].value;
 return (int) N; 
  }

 public boolean test () {
   double N = singleparam[0].value;
   return ( N>0 );
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double a = 0;"+eol;
   code += "     for(int i=0; i<N; i++) a+=x[i];"+eol;
   code += "     return a;"+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     int i;"+eol;
   code += "     double a = 0;"+eol;
   code += "     for(i=0; i<N; i++) a+=x[i];"+eol;
   code += "     return a;"+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double a = 0;"+eol;
   code += "     for(int i=0; i<N; i++) a+=x[i];"+eol;
   code += "     return a;"+eol;
   return code;
  }
}