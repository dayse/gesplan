//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.binary;

import xfuzzy.lang.*;

public class prod extends Binary {
 public prod() {
   super("xfl","prod");
   Parameter single[] = new Parameter[0];
   setSingleParameters(single);
  }

 public double compute(double a, double b) {
 return a*b; 
  }

 public double[] derivative(double a, double b) {
   double[] deriv = new double[2];
  deriv[0] = b;
  deriv[1] = a;
   return deriv;
  }

 public boolean test () {
   return true;
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return a*b; "+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return a*b; "+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return a*b; "+eol;
   return code;
  }
}
