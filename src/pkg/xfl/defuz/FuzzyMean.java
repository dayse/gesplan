//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.defuz;

import xfuzzy.lang.*;

public class FuzzyMean extends DefuzMethod {
 public FuzzyMean() {
   super("xfl","FuzzyMean");
   Parameter single[] = new Parameter[0];
   setSingleParameters(single);
  }

 public double compute(AggregateMemFunc mf) {
   double min = mf.min();
   double max = mf.max();
  double num=0, denom=0;
  for(int i=0; i<mf.conc.length; i++) {
   num += mf.conc[i].degree() * mf.conc[i].center();
   denom += mf.conc[i].degree();
  }
  if(denom==0) return (min+max)/2;
  return num/denom;
  }

 public boolean test () {
   return true;
  }

 public boolean test(AggregateMemFunc mf) {
   for(int i=0; i<mf.conc.length; i++) {
     LinguisticLabel pmf = mf.conc[i].getMF();
     if(!(pmf instanceof pkg.xfl.mfunc.triangle)
       && !(pmf instanceof pkg.xfl.mfunc.isosceles)
       && !(pmf instanceof pkg.xfl.mfunc.trapezoid)
       && !(pmf instanceof pkg.xfl.mfunc.bell)
       && !(pmf instanceof pkg.xfl.mfunc.rectangle)
       && !(pmf instanceof pkg.xfl.mfunc.singleton)
       ) return false;
    }
   return true;
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double num=0, denom=0;"+eol;
   code += "     for(int i=0; i<mf.conc.length; i++) {"+eol;
   code += "      num += mf.conc[i].degree() * mf.conc[i].center();"+eol;
   code += "      denom += mf.conc[i].degree();"+eol;
   code += "     }"+eol;
   code += "     if(denom==0) return (min+max)/2;"+eol;
   code += "     return num/denom;"+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double num=0, denom=0;"+eol;
   code += "     int i;"+eol;
   code += "     for(i=0; i<mf.length; i++) {"+eol;
   code += "      num += mf.degree[i] * mf.conc[i].center();"+eol;
   code += "      denom += mf.degree[i];"+eol;
   code += "     }"+eol;
   code += "     if(denom==0) return (min+max)/2;"+eol;
   code += "     return num/denom;"+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double num=0, denom=0;"+eol;
   code += "     for(int i=0; i<mf.length; i++) {"+eol;
   code += "      num += mf.conc[i]->degree() * mf.conc[i]->center();"+eol;
   code += "      denom += mf.conc[i]->degree();"+eol;
   code += "     }"+eol;
   code += "     if(denom==0) return (min+max)/2;"+eol;
   code += "     return num/denom;"+eol;
   return code;
  }

//+++++++++++++++++++
 public void derivative(AggregateMemFunc mf, double derror) {
   double num=0, denom=0;
   for(int i=0; i<mf.conc.length; i++) {
     num += mf.conc[i].degree() * mf.conc[i].center();
     denom += mf.conc[i].degree();
    }
   double y = num/denom;
   for(int i=0; i<mf.conc.length; i++) {
     mf.conc[i].setDegreeDeriv(derror*(mf.conc[i].center()-y)/denom);
     mf.conc[i].setCenterDeriv(derror*mf.conc[i].degree()/denom);
    }
  }
//+++++++++++++++++++

}
