//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.mfunc;

import xfuzzy.lang.*;

public class slope extends ParamMemFunc {
 public slope() {
   super("xfl","slope");
   Parameter single[] = new Parameter[2];
   single[0] = new Parameter("a");
   single[1] = new Parameter("m");
   setSingleParameters(single);
  }

 public double compute(double x) {
   double a = singleparam[0].value;
   double m = singleparam[1].value;
   double out = m*(x-a);
   return (out>1 ? 1 : (out<0 ? 0 : out));
  }

 public double greatereq(double x) {
   double min = this.u.min();
   double a = singleparam[0].value;
   double m = singleparam[1].value;
   double out;
   if(m>0) out = m*(x-a); else out = m*(min-a);
   return (out>1 ? 1 : (out<0 ? 0 : out));
  }

 public double smallereq(double x) {
   double max = this.u.max();
   double a = singleparam[0].value;
   double m = singleparam[1].value;
   double out;
   if(m>0) out = m*(max-a); else out = m*(x-a);
   return (out>1 ? 1 : (out<0 ? 0 : out));
  }

 public double[] deriv_eq(double x) {
   double[] deriv = new double[getNumberOfParameters()];
   double a = singleparam[0].value;
   double m = singleparam[1].value;
   if(x<a && x>a+1/m) { deriv[0] = -m; deriv[1] = (x-a); }
   else if(x>a && x<a+1/m) { deriv[0] = -m; deriv[1] = (x-a); }
   else if(x==a) { deriv[0] = -m/2; deriv[1] = 0; }
   else if(x==a+1/m) { deriv[0] = -m/2; deriv[1] = 0.5/m; }
   else {  deriv[0] = 0; deriv[1] = 0; }
   return deriv;
  }

 public double[] deriv_greq(double x) {
   double[] deriv = new double[getNumberOfParameters()];
   double min = this.u.min();
   double a = singleparam[0].value;
   double m = singleparam[1].value;
   if(m>0) {
    if(x>a && x<a+1/m) { deriv[0] = -m; deriv[1] = (x-a); }
    else if(x==a) { deriv[0] = -m/2; deriv[1] = 0; }
    else if(x==a+1/m) { deriv[0] = -m/2; deriv[1] = 0.5/m; }
    else { deriv[0] = 0; deriv[1] = 0; }
   }
   else if( m*(min-a) <1 ) { deriv[0] = -m; deriv[1] = min-a; }
   else { deriv[0] = 0; deriv[1] = 0; }
   return deriv;
  }

 public double[] deriv_smeq(double x) {
   double[] deriv = new double[getNumberOfParameters()];
   double max = this.u.max();
   double a = singleparam[0].value;
   double m = singleparam[1].value;
   if(m<0) {
    if(x<a && x>a+1/m) { deriv[0] = -m; deriv[1] = (x-a); }
    else if(x==a) { deriv[0] = -m/2; deriv[1] = 0; }
    else if(x==a+1/m) { deriv[0] = -m/2; deriv[1] = 0.5/m; }
    else { deriv[0] = 0; deriv[1] = 0; }
   }
   else if( m*(max-a) <1 ) { deriv[0] = -m; deriv[1] = max-a; }
   else { deriv[0] = 0; deriv[1] = 0; }
   return deriv;
  }

 public boolean test () {
   double min = this.u.min();
   double max = this.u.max();
   double a = singleparam[0].value;
   double m = singleparam[1].value;
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
   double m = singleparam[1].value;
  a += desp[0];
  m += desp[1];
  if(a<min) a = min;
  if(a>max) a = max;
  if(m/pos[1]<=0) m = pos[1]/2;
  pos[0] = a;
  pos[1] = m;
   updateValues(pos);
  }

 public String getEqualJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double out = m*(x-a);"+eol;
   code += "      return (out>1 ? 1 : (out<0 ? 0 : out));"+eol;
   return code;
  }

 public String getGreqJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double out;"+eol;
   code += "      if(m>0) out = m*(x-a); else out = m*(min-a);"+eol;
   code += "      return (out>1 ? 1 : (out<0 ? 0 : out));"+eol;
   return code;
  }

 public String getSmeqJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double out;"+eol;
   code += "      if(m>0) out = m*(max-a); else out = m*(x-a);"+eol;
   code += "      return (out>1 ? 1 : (out<0 ? 0 : out));"+eol;
   return code;
  }


 public String getEqualCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double out = m*(x-a);"+eol;
   code += "      return (out>1 ? 1 : (out<0 ? 0 : out));"+eol;
   return code;
  }

 public String getGreqCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double out;"+eol;
   code += "      if(m>0) out = m*(x-a); else out = m*(min-a);"+eol;
   code += "      return (out>1 ? 1 : (out<0 ? 0 : out));"+eol;
   return code;
  }

 public String getSmeqCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double out;"+eol;
   code += "      if(m>0) out = m*(max-a); else out = m*(x-a);"+eol;
   code += "      return (out>1 ? 1 : (out<0 ? 0 : out));"+eol;
   return code;
  }


 public String getEqualCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double out = m*(x-a);"+eol;
   code += "      return (out>1 ? 1 : (out<0 ? 0 : out));"+eol;
   return code;
  }

 public String getGreqCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double out;"+eol;
   code += "      if(m>0) out = m*(x-a); else out = m*(min-a);"+eol;
   code += "      return (out>1 ? 1 : (out<0 ? 0 : out));"+eol;
   return code;
  }

 public String getSmeqCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "      double out;"+eol;
   code += "      if(m>0) out = m*(max-a); else out = m*(x-a);"+eol;
   code += "      return (out>1 ? 1 : (out<0 ? 0 : out));"+eol;
   return code;
  }

}