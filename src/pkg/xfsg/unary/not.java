//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfsg.unary;

import xfuzzy.lang.*;

public class not extends Unary {
 public not() {
   super("xfsg","not");
   Parameter single[] = new Parameter[0];
   setSingleParameters(single);
  }

 public double compute(double a) {
 return 1-a; 
  }

 public double derivative(double a) {
   double deriv;
 deriv = -1; 
   return deriv;
  }

 public boolean test () {
   return true;
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return 1-a; "+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return 1-a; "+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return 1-a; "+eol;
   return code;
  }
}
