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
//		SELECCION DE PARAMETROS A AJUSTAR		//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

import xfuzzy.lang.*;

public class XfslSetting {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			CONSTANTES PUBLICAS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public static final int DISABLE = 0;
 public static final int ENABLE = 1;
 public static final int NOT_SELECTED = -1;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private int code;
 private String type;
 private String mf;
 private int param;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			  CONSTRUCTORES				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Constructor a partir de objetos				//
 //-------------------------------------------------------------//

 public XfslSetting(Type type,ParametricFunction mf,int param,boolean enable) {
  if(type != null) this.type = type.getName(); else this.type = null;
  if(mf != null) this.mf = mf.toString(); else this.mf = null;
  this.param = param;
  if(enable) this.code = ENABLE; else this.code = DISABLE;
 }

 //-------------------------------------------------------------//
 // Constructor a partir de codigo				//
 //-------------------------------------------------------------//

 public XfslSetting(String def, boolean enable) {
  String tname = def.substring(0, def.indexOf("."));
  String mfname = def.substring(def.indexOf(".")+1, def.lastIndexOf("."));
  String pname = def.substring(def.lastIndexOf(".")+1);

  if(tname.equals("ANY")) this.type = null; else this.type = tname;
  if(mfname.equals("ANY")) this.mf = null; else this.mf = mfname;
  if(pname.equals("ANY")) this.param = NOT_SELECTED;
  else this.param = Integer.parseInt(pname);
  if(enable) this.code = ENABLE; else this.code = DISABLE;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Descripcion que aparece en la ventana			//
 //-------------------------------------------------------------//

 public String toString() {
  String tname = (type == null? "ANY": type);
  String mfname = (mf == null? "ANY": mf);
  String pname = (param == NOT_SELECTED? "ANY": ""+param);

  if(code == ENABLE) return "enable: "+tname+"."+mfname+"."+pname;
  else return "disable: "+tname+"."+mfname+"."+pname;
 }

 //-------------------------------------------------------------//
 // Descripcion que aparece en el fichero de configuracion	//
 //-------------------------------------------------------------//

 public String toCode() {
  String tname = (type == null? "ANY": type);
  String mfname = (mf == null? "ANY": mf);
  String pname = (param == NOT_SELECTED? "ANY": ""+param);

  if(code == ENABLE) return "xfsl_enable("+tname+"."+mfname+"."+pname+")";
  else return "xfsl_disable("+tname+"."+mfname+"."+pname+")";
 }

 //-------------------------------------------------------------//
 // Aplicar la seleccion a los tipos de una especificacion	//
 //-------------------------------------------------------------//

 public void set(Type[] tp) {
  for(int i=0; i<tp.length; i++) {
   if(type != null && !type.equals(tp[i].getName())) continue;
   Family[] fam = tp[i].getFamilies();
   for(int j=0; j<fam.length; j++) {
    if(mf != null && !mf.equals(fam[j].toString())) continue;
    Parameter[] pm = fam[j].getParameters();
    if(param == NOT_SELECTED)
     for(int k=0;k<pm.length;k++) pm[k].setAdjustable( (code == ENABLE) );
    else if(param<=pm.length) pm[param-1].setAdjustable( (code == ENABLE));
   }
   ParamMemFunc[] pmf = tp[i].getParamMembershipFunctions();
   for(int j=0; j<pmf.length; j++) {
    if(mf != null && !mf.equals(pmf[j].toString())) continue;
    Parameter[] pm = pmf[j].getParameters();
    if(param == NOT_SELECTED)
     for(int k=0;k<pm.length;k++) pm[k].setAdjustable( (code == ENABLE) );
    else if(param<=pm.length) pm[param-1].setAdjustable( (code == ENABLE));
   }
  }
 }
}

