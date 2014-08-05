//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfsg.defuz;

import xfuzzy.lang.*;

public class GammaQuality extends DefuzMethod {
 public GammaQuality() {
   super("xfsg","GammaQuality");
   Parameter single[] = new Parameter[1];
   single[0] = new Parameter("gamma");
   setSingleParameters(single);
  }

 public double compute(AggregateMemFunc mf) {
   double min = mf.min();
   double max = mf.max();
   double gamma = singleparam[0].value;
  double num=0, denom=0;
  for(int i=0; i<mf.conc.length; i++) {
   double w = Math.pow(mf.conc[i].basis(), gamma);
   num += mf.conc[i].degree() * mf.conc[i].center() / w;
   denom += mf.conc[i].degree() / w;
  }
  if(denom == 0) return (min+max)/2;
  return num/denom;
  }

 public boolean test () {
   double gamma = singleparam[0].value;
   return ( gamma>=0 );
  }

 public boolean test(AggregateMemFunc mf) {
   return true;
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double num=0, denom=0;"+eol;
   code += "     for(int i=0; i<mf.conc.length; i++) {"+eol;
   code += "      double w = Math.pow(mf.conc[i].basis(), gamma);"+eol;
   code += "      num += mf.conc[i].degree() * mf.conc[i].center() / w;"+eol;
   code += "      denom += mf.conc[i].degree() / w;"+eol;
   code += "     }"+eol;
   code += "     if(denom == 0) return (min+max)/2;"+eol;
   code += "     return num/denom;"+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double w, num=0, denom=0;"+eol;
   code += "     int i;"+eol;
   code += "     for(i=0; i<mf.length; i++) {"+eol;
   code += "      w = pow( mf.conc[i].basis(), gamma);"+eol;
   code += "      num += mf.degree[i] * mf.conc[i].center() / w;"+eol;
   code += "      denom += mf.degree[i] / w;"+eol;
   code += "     }"+eol;
   code += "     if(denom == 0) return (min+max)/2;"+eol;
   code += "     return num/denom;"+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double num=0, denom=0;"+eol;
   code += "     for(int i=0; i<mf.length; i++) {"+eol;
   code += "      double w = pow( mf.conc[i]->basis(), gamma);"+eol;
   code += "      num += mf.conc[i]->degree() * mf.conc[i]->center() / w;"+eol;
   code += "      denom += mf.conc[i]->degree() / w;"+eol;
   code += "     }"+eol;
   code += "     if(denom == 0) return (min+max)/2;"+eol;
   code += "     return num/denom;"+eol;
   return code;
  }
}
