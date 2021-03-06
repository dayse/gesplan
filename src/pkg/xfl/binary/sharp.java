//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.binary;

import xfuzzy.lang.*;

public class sharp extends Binary {
 public sharp() {
   super("xfl","sharp");
   Parameter single[] = new Parameter[0];
   setSingleParameters(single);
  }

 public double compute(double a, double b) {
 return (a<=b? 1 : 0); 
  }

 public boolean test () {
   return true;
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (a<=b? 1 : 0); "+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (a<=b? 1 : 0); "+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (a<=b? 1 : 0); "+eol;
   return code;
  }
}
