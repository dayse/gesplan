//--------------------------------------------------------------------------------//
//                               COPYRIGHT NOTICE                                 //
//--------------------------------------------------------------------------------//
// Copyright (c) 2012, Instituto de Microelectronica de Sevilla (IMSE-CNM)        //
//                                                                                //
// All rights reserved.                                                           //
//                                                                                //
// Redistribution and use in source and binary forms, with or without             //
// modification, are permitted provided that the following  conditions are met:   //
//                                                                                //
//     * Redistributions of source code must retain the above copyright notice,   //
//       this list of conditions and the following disclaimer.                    // 
//                                                                                //
//     * Redistributions in binary form must reproduce the above copyright        // 
//       notice, this list of conditions and the following disclaimer in the      //
//       documentation and/or other materials provided with the distribution.     //
//                                                                                //
//     * Neither the name of the IMSE-CNM nor the names of its contributors may   //
//       be used to endorse or promote products derived from this software        //
//       without specific prior written permission.                               //
//                                                                                //
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"    //
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE      //
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE // 
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE  //
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL     //
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR     //
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER     //
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,  //
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  //
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.           //
//--------------------------------------------------------------------------------//


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//	GENERADOR DEL FICHERO "InputMembershipFunction.java"	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfj;

import java.io.*;

public class XfjInputMemFunc {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private String eol = System.getProperty("line.separator", "\n");
 private File dir;
 private String pkgname;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS CONSTANTES			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final String create(File dir, String pkgname) {
  XfjInputMemFunc creator = new XfjInputMemFunc(dir,pkgname);
  creator.createFile();
  return creator.getMessage();
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfjInputMemFunc(File dir, String pkgname) {
  this.dir = dir;
  this.pkgname = pkgname;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Obtiene el nombre del fichero creado			//
 //-------------------------------------------------------------//

 private String getMessage() {
  File file = new File(dir,"InputMembershipFunction.java");
  return file.getAbsolutePath();
 }

 //-------------------------------------------------------------//
 // Genera el fichero "InputMembershipFunction.java"		//
 //-------------------------------------------------------------//

 public void createFile() {
  File file = new File(dir,"InputMembershipFunction.java");

  String heading[] = getHeading();
  String source[] = getSource();

  String code = "";
  for(int i=0; i<heading.length; i++) code += heading[i]+eol;
  code += getPackage()+eol+eol;
  for(int i=0; i<source.length; i++) code += source[i]+eol;

  byte[] buf = code.getBytes();
  try {
   OutputStream stream = new FileOutputStream(file);
   stream.write(buf);
   stream.close();
  }
  catch (IOException e) {}
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Genera el codigo de cabecera				//
 //-------------------------------------------------------------//

 private String[] getHeading() {
  String source[] = {
   "//++++++++++++++++++++++++++++++++++++++++++++++++++++++//",
   "//                                                      //",
   "// Class:  InputMembershipFunction                      //",
   "//                                                      //",
   "// Author: Automatically generated by Xfuzzy            //",
   "//                                                      //",
   "// Description: Membership function of an input         //",
   "//              variable                                //",
   "//                                                      //",
   "//++++++++++++++++++++++++++++++++++++++++++++++++++++++//",
   "" };

  return source;
 }

 //-------------------------------------------------------------//
 // Genera el codigo del paquete				//
 //-------------------------------------------------------------//

 private String getPackage() {
  if(pkgname != null && pkgname.length()>0) return "package "+this.pkgname+";";
  return "";
 }

 //-------------------------------------------------------------//
 // Genera el codigo de la clase "InputMembershipFunction"	//
 //-------------------------------------------------------------//

 private String[] getSource() {
  String source[] = {
"public abstract class InputMembershipFunction {",
" double min;",
" double max;",
" double step;",
"",
" public double center() {",
"  return 0;",
" }",
"",
" public double basis() {",
"  return 0;",
" }",
"",
" public abstract double param(int i);",
"",
" public abstract double isEqual(double x);",
"",
" public double isSmallerOrEqual(double x) {",
"  double degree=0, mu;",
"  for(double y=max; y>=x ; y-=step) if((mu = isEqual(y))>degree) degree=mu;",
"  return degree;",
" }",
"",
" public double isGreaterOrEqual(double x) {",
"   double degree=0, mu;",
"   for(double y=min; y<=x ; y+=step) if((mu = isEqual(y))>degree) degree=mu;",
"   return degree;",
"  }",
"",
" public double isEqual(MembershipFunction mf) {",
"  if(mf instanceof FuzzySingleton)",
"   { return isEqual( ((FuzzySingleton) mf).getValue()); }",
"  if((mf instanceof OutputMembershipFunction) &&",
"     ((OutputMembershipFunction) mf).isDiscrete() ) {",
"   double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();",
"   double deg = 0;",
"   for(int i=0; i<val.length; i++){",
"    double mu = isEqual(val[i][0]);",
"    double minmu = (mu<val[i][1] ? mu : val[i][1]);",
"    if( deg<minmu ) deg = minmu;",
"   }",
"   return deg;",
"  }",
"  double mu1,mu2,minmu,degree=0;",
"  for(double x=min; x<=max; x+=step){",
"   mu1 = mf.compute(x);",
"   mu2 = isEqual(x);",
"   minmu = (mu1<mu2 ? mu1 : mu2);",
"   if( degree<minmu ) degree = minmu;",
"  }",
"  return degree;",
"  }",
"",
" public double isGreaterOrEqual(MembershipFunction mf) {",
"  if(mf instanceof FuzzySingleton)",
"   { return isGreaterOrEqual( ((FuzzySingleton) mf).getValue()); }",
"  if((mf instanceof OutputMembershipFunction) &&",
"     ((OutputMembershipFunction) mf).isDiscrete() ) {",
"   double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();",
"   double deg = 0;",
"   for(int i=0; i<val.length; i++){",
"    double mu = isGreaterOrEqual(val[i][0]);",
"    double minmu = (mu<val[i][1] ? mu : val[i][1]);",
"    if( deg<minmu ) deg = minmu;",
"   }",
"   return deg;",
"  }",
"  double mu1,mu2,minmu,degree=0,greq=0;",
"  for(double x=min; x<=max; x+=step){",
"   mu1 = mf.compute(x);",
"   mu2 = isEqual(x);",
"   if( mu2>greq ) greq = mu2;",
"   if( mu1<greq ) minmu = mu1; else minmu = greq;",
"   if( degree<minmu ) degree = minmu;",
"  }",
"  return degree;",
" }",
"",
" public double isSmallerOrEqual(MembershipFunction mf) {",
"  if(mf instanceof FuzzySingleton)",
"   { return isSmallerOrEqual( ((FuzzySingleton) mf).getValue()); }",
"  if((mf instanceof OutputMembershipFunction) &&",
"     ((OutputMembershipFunction) mf).isDiscrete() ) {",
"   double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();",
"   double deg = 0;",
"   for(int i=0; i<val.length; i++){",
"    double mu = isSmallerOrEqual(val[i][0]);",
"    double minmu = (mu<val[i][1] ? mu : val[i][1]);",
"    if( deg<minmu ) deg = minmu;",
"   }",
"   return deg;",
"  }",
"  double mu1,mu2,minmu,degree=0,smeq=0;",
"  for(double x=max; x>=min; x-=step){",
"   mu1 = mf.compute(x);",
"   mu2 = isEqual(x);",
"   if( mu2>smeq ) smeq = mu2;",
"   if( mu1<smeq ) minmu = mu1; else minmu = smeq;",
"   if( degree<minmu ) degree = minmu;",
"  }",
"  return degree;",
" }",
"",
" public double isGreater(MembershipFunction mf, OperatorSet op) {",
"  if(mf instanceof FuzzySingleton)",
"   { return op.not(isSmallerOrEqual( ((FuzzySingleton) mf).getValue())); }",
"  if((mf instanceof OutputMembershipFunction) &&",
"     ((OutputMembershipFunction) mf).isDiscrete() ) {",
"   double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();",
"   double deg = 0;",
"   for(int i=0; i<val.length; i++){",
"    double mu = op.not(isSmallerOrEqual(val[i][0]));",
"    double minmu = (mu<val[i][1] ? mu : val[i][1]);",
"    if( deg<minmu ) deg = minmu;",
"   }",
"   return deg;",
"  }",
"  double mu1,mu2,minmu,gr,degree=0,smeq=0;",
"  for(double x=max; x>=min; x-=step){",
"   mu1 = mf.compute(x);",
"   mu2 = isEqual(x);",
"   if( mu2>smeq ) smeq = mu2;",
"   gr = op.not(smeq);",
"   minmu = ( mu1<gr ? mu1 : gr);",
"   if( degree<minmu ) degree = minmu;",
"  }",
"  return degree;",
" }",
"",
" public double isSmaller(MembershipFunction mf, OperatorSet op) {",
"  if(mf instanceof FuzzySingleton)",
"   { return op.not(isGreaterOrEqual( ((FuzzySingleton) mf).getValue())); }",
"  if((mf instanceof OutputMembershipFunction) &&",
"     ((OutputMembershipFunction) mf).isDiscrete() ) {",
"   double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();",
"   double deg = 0;",
"   for(int i=0; i<val.length; i++){",
"    double mu = op.not(isGreaterOrEqual(val[i][0]));",
"    double minmu = (mu<val[i][1] ? mu : val[i][1]);",
"    if( deg<minmu ) deg = minmu;",
"   }",
"   return deg;",
"  }",
"  double mu1,mu2,minmu,sm,degree=0,greq=0;",
"  for(double x=min; x<=max; x+=step){",
"   mu1 = mf.compute(x);",
"   mu2 = isEqual(x);",
"   if( mu2>greq ) greq = mu2;",
"   sm = op.not(greq);",
"   minmu = ( mu1<sm ? mu1 : sm);",
"   if( degree<minmu ) degree = minmu;",
"  }",
"  return degree;",
" }",
"",
" public double isNotEqual(MembershipFunction mf, OperatorSet op) {",
"  if(mf instanceof FuzzySingleton)",
"   { return op.not(isEqual( ((FuzzySingleton) mf).getValue())); }",
"  if((mf instanceof OutputMembershipFunction) &&",
"     ((OutputMembershipFunction) mf).isDiscrete() ) {",
"   double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();",
"   double deg = 0;",
"   for(int i=0; i<val.length; i++){",
"    double mu = op.not(isEqual(val[i][0]));",
"    double minmu = (mu<val[i][1] ? mu : val[i][1]);",
"    if( deg<minmu ) deg = minmu;",
"   }",
"   return deg;",
"  }",
"  double mu1,mu2,minmu,degree=0;",
"  for(double x=min; x<=max; x+=step){",
"   mu1 = mf.compute(x);",
"   mu2 = op.not(isEqual(x));",
"   minmu = (mu1<mu2 ? mu1 : mu2);",
"   if( degree<minmu ) degree = minmu;",
"  }",
" return degree;",
" }",
"",
" public double isApproxEqual(MembershipFunction mf, OperatorSet op) {",
"  if(mf instanceof FuzzySingleton)",
"   { return op.moreorless(isEqual( ((FuzzySingleton) mf).getValue())); }",
"  if((mf instanceof OutputMembershipFunction) &&",
"     ((OutputMembershipFunction) mf).isDiscrete() ) {",
"   double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();",
"   double deg = 0;",
"   for(int i=0; i<val.length; i++){",
"    double mu = op.moreorless(isEqual(val[i][0]));",
"    double minmu = (mu<val[i][1] ? mu : val[i][1]);",
"    if( deg<minmu ) deg = minmu;",
"   }",
"   return deg;",
"  }",
"  double mu1,mu2,minmu,degree=0;",
"  for(double x=min; x<=max; x+=step){",
"   mu1 = mf.compute(x);",
"   mu2 = op.moreorless(isEqual(x));",
"   minmu = (mu1<mu2 ? mu1 : mu2);",
"   if( degree<minmu ) degree = minmu;",
"  }",
"  return degree;",
" }",
"",
" public double isVeryEqual(MembershipFunction mf, OperatorSet op) {",
"  if(mf instanceof FuzzySingleton)",
"   { return op.very(isEqual( ((FuzzySingleton) mf).getValue())); }",
"  if((mf instanceof OutputMembershipFunction) &&",
"     ((OutputMembershipFunction) mf).isDiscrete() ) {",
"   double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();",
"   double deg = 0;",
"   for(int i=0; i<val.length; i++){",
"    double mu = op.very(isEqual(val[i][0]));",
"    double minmu = (mu<val[i][1] ? mu : val[i][1]);",
"    if( deg<minmu ) deg = minmu;",
"   }",
"   return deg;",
"  }",
"  double mu1,mu2,minmu,degree=0;",
"  for(double x=min; x<=max; x+=step){",
"   mu1 = mf.compute(x);",
"   mu2 = op.very(isEqual(x));",
"   minmu = (mu1<mu2 ? mu1 : mu2);",
"   if( degree<minmu ) degree = minmu;",
"  }",
"  return degree;",
" }",
"",
" public double isSlightlyEqual(MembershipFunction mf, OperatorSet op) {",
"  if(mf instanceof FuzzySingleton)",
"   { return op.slightly(isEqual( ((FuzzySingleton) mf).getValue())); }",
"  if((mf instanceof OutputMembershipFunction) &&",
"     ((OutputMembershipFunction) mf).isDiscrete() ) {",
"   double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();",
"   double deg = 0;",
"   for(int i=0; i<val.length; i++){",
"    double mu = op.slightly(isEqual(val[i][0]));",
"    double minmu = (mu<val[i][1] ? mu : val[i][1]);",
"    if( deg<minmu ) deg = minmu;",
"   }",
"   return deg;",
"  }",
"  double mu1,mu2,minmu,degree=0;",
"  for(double x=min; x<=max; x+=step){",
"   mu1 = mf.compute(x);",
"   mu2 = op.slightly(isEqual(x));",
"   minmu = (mu1<mu2 ? mu1 : mu2);",
"   if( degree<minmu ) degree = minmu;",
"  }",
"  return degree;",
" }",
"}" };

  return source;
 }
}
