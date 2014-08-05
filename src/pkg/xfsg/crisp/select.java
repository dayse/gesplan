//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfsg.crisp;

import xfuzzy.lang.*;

public class select extends CrispBlock {
 public select() {
   super("xfsg","select");
   Parameter single[] = new Parameter[1];
   single[0] = new Parameter("N");
   setSingleParameters(single);
  }

 public double compute(double[] x) {
   double N = singleparam[0].value;
 return x[ (int)x[0] + 1 ]; 
  }

 public int inputs() {
   double N = singleparam[0].value;
 return (int) N + 1; 
  }

 public boolean test () {
   double N = singleparam[0].value;
   return ( N>=1 );
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return x[ (int)x[0] + 1 ]; "+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return x[ (int)x[0] + 1 ]; "+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return x[ (int)x[0] + 1 ]; "+eol;
   return code;
  }
}
