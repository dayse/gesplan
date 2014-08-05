//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.mfunc;

import xfuzzy.lang.*;

public class singleton extends ParamMemFunc {
 public singleton() {
   super("xfl","singleton");
   Parameter single[] = new Parameter[1];
   single[0] = new Parameter("a");
   setSingleParameters(single);
  }

 public double compute(double x) {
   double a = singleparam[0].value;
 return (x==a? 1 : 0); 
  }

 public double greatereq(double x) {
   double a = singleparam[0].value;
 return (x>=a? 1 : 0); 
  }

 public double smallereq(double x) {
   double a = singleparam[0].value;
 return (x<=a? 1 : 0); 
  }

 public double center() {
   double a = singleparam[0].value;
 return a; 
  }

 public double[] deriv_center() {
   double[] deriv = new double[getNumberOfParameters()];
   double a = singleparam[0].value;
 deriv[0] = 1; 
   return deriv;
  }

 public boolean test () {
   double min = this.u.min();
   double max = this.u.max();
   double a = singleparam[0].value;
   return ( a>=min && a<=max );
  }

 public void update() {
   if(!isAdjustable()) return;
   double[] pos = get();
   double[] desp = getDesp();
   boolean[] adj = getAdjustable();
   double min = this.u.min();
   double max = this.u.max();
   double a = singleparam[0].value;
  pos[0] += desp[0];
  if(pos[0]<min) pos[0] = min;
  if(pos[0]>max) pos[0] = max;
   updateValues(pos);
  }

 public String getEqualJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (x==a? 1 : 0); "+eol;
   return code;
  }

 public String getGreqJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (x>=a? 1 : 0); "+eol;
   return code;
  }

 public String getSmeqJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (x<=a? 1 : 0); "+eol;
   return code;
  }

 public String getCenterJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return a; "+eol;
   return code;
  }


 public String getEqualCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (x==a? 1 : 0); "+eol;
   return code;
  }

 public String getGreqCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (x>=a? 1 : 0); "+eol;
   return code;
  }

 public String getSmeqCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (x<=a? 1 : 0); "+eol;
   return code;
  }

 public String getCenterCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return a; "+eol;
   return code;
  }


 public String getEqualCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (x==a? 1 : 0); "+eol;
   return code;
  }

 public String getGreqCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (x>=a? 1 : 0); "+eol;
   return code;
  }

 public String getSmeqCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return (x<=a? 1 : 0); "+eol;
   return code;
  }

 public String getCenterCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return a; "+eol;
   return code;
  }

}
