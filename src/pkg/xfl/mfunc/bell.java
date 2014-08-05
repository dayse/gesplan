//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.mfunc;

import xfuzzy.lang.*;

public class bell extends ParamMemFunc {
 public bell() {
   super("xfl","bell");
   Parameter single[] = new Parameter[2];
   single[0] = new Parameter("a");
   single[1] = new Parameter("b");
   setSingleParameters(single);
  }

 public double compute(double x) {
   double a = singleparam[0].value;
   double b = singleparam[1].value;
 return Math.exp( -(a-x)*(a-x)/(b*b) ); 
  }

 public double greatereq(double x) {
   double a = singleparam[0].value;
   double b = singleparam[1].value;
 if(x>a) return 1; return Math.exp( - (x-a)*(x-a)/(b*b) ); 
  }

 public double smallereq(double x) {
   double a = singleparam[0].value;
   double b = singleparam[1].value;
 if(x<a) return 1; return Math.exp( - (x-a)*(x-a)/(b*b) ); 
  }

 public double center() {
   double a = singleparam[0].value;
   double b = singleparam[1].value;
 return a; 
  }

 public double basis() {
   double a = singleparam[0].value;
   double b = singleparam[1].value;
 return b; 
  }

 public double[] deriv_eq(double x) {
   double[] deriv = new double[getNumberOfParameters()];
   double a = singleparam[0].value;
   double b = singleparam[1].value;
   double aux = (x-a)/b;
   deriv[0] = 2*aux*Math.exp(-aux*aux)/b;
   deriv[1] = 2*aux*aux*Math.exp(-aux*aux)/b;
   return deriv;
  }

 public double[] deriv_greq(double x) {
   double[] deriv = new double[getNumberOfParameters()];
   double a = singleparam[0].value;
   double b = singleparam[1].value;
   if(x>a) { deriv[0] = 0; deriv[1] = 0; }
   else {
    double aux = (x-a)/b;
    deriv[0] = 2*aux*Math.exp(-aux*aux)/b;
    deriv[1] = 2*aux*aux*Math.exp(-aux*aux)/b;
   }
   return deriv;
  }

 public double[] deriv_smeq(double x) {
   double[] deriv = new double[getNumberOfParameters()];
   double a = singleparam[0].value;
   double b = singleparam[1].value;
   if(x<a) { deriv[0] = 0; deriv[1] = 0; }
   else {
    double aux = (x-a)/b;
    deriv[0] = 2*aux*Math.exp(-aux*aux)/b;
    deriv[1] = 2*aux*aux*Math.exp(-aux*aux)/b;
   }
   return deriv;
  }

 public double[] deriv_center() {
   double[] deriv = new double[getNumberOfParameters()];
   double a = singleparam[0].value;
   double b = singleparam[1].value;
 deriv[0] = 1; deriv[1] = 0; 
   return deriv;
  }

 public double[] deriv_basis() {
   double[] deriv = new double[getNumberOfParameters()];
   double a = singleparam[0].value;
   double b = singleparam[1].value;
 deriv[0] = 0; deriv[1] = 1; 
   return deriv;
  }

 public boolean test () {
   double min = this.u.min();
   double max = this.u.max();
   double a = singleparam[0].value;
   double b = singleparam[1].value;
   return ( a>=min && a<=max && b>0 && b<=(max-min)/2 );
  }

 public void update() {
   if(!isAdjustable()) return;
   double[] pos = get();
   double[] desp = getDesp();
   boolean[] adj = getAdjustable();
   double min = this.u.min();
   double max = this.u.max();
   double a = singleparam[0].value;
   double b = singleparam[1].value;
  double grid = 1.0e-10;
  a += desp[0];
  b += desp[1];
  if(a<min) a = min;
  if(a>max) a = max;
  if(b<=0) b = pos[1]/2;
  if(b<=grid) b = grid;
  if(b>(max-min)/2) b = (max-min)/2;
  pos[0] = a;
  pos[1] = b;
   updateValues(pos);
  }

 public String getEqualJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return Math.exp( -(a-x)*(a-x)/(b*b) ); "+eol;
   return code;
  }

 public String getGreqJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    if(x>a) return 1; return Math.exp( - (x-a)*(x-a)/(b*b) ); "+eol;
   return code;
  }

 public String getSmeqJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    if(x<a) return 1; return Math.exp( - (x-a)*(x-a)/(b*b) ); "+eol;
   return code;
  }

 public String getCenterJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return a; "+eol;
   return code;
  }

 public String getBasisJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return b; "+eol;
   return code;
  }


 public String getEqualCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return exp( -(a-x)*(a-x)/(b*b) ); "+eol;
   return code;
  }

 public String getGreqCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    if(x>a) return 1; return exp( - (x-a)*(x-a)/(b*b) ); "+eol;
   return code;
  }

 public String getSmeqCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    if(x<a) return 1; return exp( - (x-a)*(x-a)/(b*b) ); "+eol;
   return code;
  }

 public String getCenterCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return a; "+eol;
   return code;
  }

 public String getBasisCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return b; "+eol;
   return code;
  }


 public String getEqualCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return exp( -(a-x)*(a-x)/(b*b) ); "+eol;
   return code;
  }

 public String getGreqCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    if(x>a) return 1; return exp( - (x-a)*(x-a)/(b*b) ); "+eol;
   return code;
  }

 public String getSmeqCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    if(x<a) return 1; return exp( - (x-a)*(x-a)/(b*b) ); "+eol;
   return code;
  }

 public String getCenterCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return a; "+eol;
   return code;
  }

 public String getBasisCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "    return b; "+eol;
   return code;
  }

}
