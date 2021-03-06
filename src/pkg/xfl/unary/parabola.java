//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.unary;

import xfuzzy.lang.*;

public class parabola extends Unary {
 public parabola() {
   super("xfl","parabola");
   Parameter single[] = new Parameter[0];
   setSingleParameters(single);
  }

 public double compute(double a) {
 return 4*a*(1-a); 
  }

 public double derivative(double a) {
   double deriv;
 deriv = 4-8*a; 
   return deriv;
  }

 public boolean test () {
   return true;
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return 4*a*(1-a); "+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return 4*a*(1-a); "+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return 4*a*(1-a); "+eol;
   return code;
  }
}
