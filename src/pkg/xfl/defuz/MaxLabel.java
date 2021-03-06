//+++++++++++++++++++++++++++++++++++++++++++++++++++++
// File automatically generated by Xfuzzy - DO NOT EDIT
//+++++++++++++++++++++++++++++++++++++++++++++++++++++

package pkg.xfl.defuz;

import xfuzzy.lang.*;

public class MaxLabel extends DefuzMethod {
 public MaxLabel() {
   super("xfl","MaxLabel");
   Parameter single[] = new Parameter[0];
   setSingleParameters(single);
  }

 public double compute(AggregateMemFunc mf) {
   double max = mf.max();
  double maxdegree=0, center=0;
  for(int i=0; i<mf.conc.length; i++)
   if(mf.conc[i].degree() >= maxdegree)
    { center = mf.conc[i].center(); maxdegree = mf.conc[i].degree(); }
  return center;
  }

 public boolean test () {
   return true;
  }

 public boolean test(AggregateMemFunc mf) {
   return true;
  }

 public String getJavaCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double maxdegree=0, center=0;"+eol;
   code += "     for(int i=0; i<mf.conc.length; i++)"+eol;
   code += "      if(mf.conc[i].degree() >= maxdegree)"+eol;
   code += "       { center = mf.conc[i].center(); maxdegree = mf.conc[i].degree(); }"+eol;
   code += "     return center;"+eol;
   return code;
  }

 public String getCCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double mx=0, c=0;"+eol;
   code += "     int i;"+eol;
   code += "     for(i=0; i<mf.length; i++)"+eol;
   code += "      if(mf.degree[i] >= mx) { c = mf.conc[i].center(); mx = mf.degree[i]; }"+eol;
   code += "     return c;"+eol;
   return code;
  }

 public String getCppCode() {
   String eol = System.getProperty("line.separator", "\n");
   String code = "";
   code += "     double maxdegree=0, center=0;"+eol;
   code += "     for(int i=0; i<mf.length; i++)"+eol;
   code += "      if(mf.conc[i]->degree() >= maxdegree)"+eol;
   code += "       { center = mf.conc[i]->center(); maxdegree = mf.conc[i]->degree(); }"+eol;
   code += "     return center;"+eol;
   return code;
  }
}
