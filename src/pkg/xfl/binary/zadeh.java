//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.binary;

import xfuzzy.lang.*;

public class zadeh extends Binary {
 public zadeh() {
   super("xfl","zadeh");
   Parameter single[] = new Parameter[0];
   setSingleParameters(single);
  }

 public double compute(double a, double b) {
 return (a<0.5 || 1-a>b? 1-a : (a<b? a : b)); 
  }

 public boolean test () {
   return true;
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (a<0.5 || 1-a>b? 1-a : (a<b? a : b)); "+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (a<0.5 || 1-a>b? 1-a : (a<b? a : b)); "+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (a<0.5 || 1-a>b? 1-a : (a<b? a : b)); "+eol;
   return code;
  }
}
