//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.crisp;

import xfuzzy.lang.*;

public class diff2 extends CrispBlock {
 public diff2() {
   super("xfl","diff2");
   Parameter single[] = new Parameter[0];
   setSingleParameters(single);
  }

 public double compute(double[] x) {
 return x[0] - x[1]; 
  }

 public int inputs() {
 return 2; 
  }

 public boolean test () {
   return true;
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return x[0] - x[1]; "+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return x[0] - x[1]; "+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return x[0] - x[1]; "+eol;
   return code;
  }
}
