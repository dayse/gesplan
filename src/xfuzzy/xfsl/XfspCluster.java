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
//	Agrupamiento de un conjunto de datos en N clusters	//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package xfuzzy.xfsl;

public class XfspCluster {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			MIEMBROS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public double[][] data;
 public double[][] cluster;
 public int[] assign;

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			   CONSTRUCTOR				//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public XfspCluster(double[][] data, int C) {
  this.data = data;
  this.cluster = new double[C][data[0].length];
  this.assign = new int[data.length];
  for(int i=0; i<C; i++) assign[i] = i;
  for(int i=C; i<assign.length; i++) assign[i] = -1;
  for(int i=C; i<assign.length; i++) insert(i);
  do { set_cluster(); } while (reassign());
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PUBLICOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Funcion de evaluacion del agrupamiento			//
 //-------------------------------------------------------------//

 public double evaluation() {
  double max=0, min=Double.MAX_VALUE;
  for(int i=0; i<cluster.length; i++)
   for(int j=i+1; j<cluster.length; j++){
    double dist = dist1_cl_cl(i,j);
    if(dist<min) min = dist;
   }
  for(int i=0; i<cluster.length; i++) {
   double dist = diameter(i);
   if(dist>max) max = dist;
  }
  return min/max;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //			METODOS PRIVADOS			//
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

 //-------------------------------------------------------------//
 // Distancia entre dos datos					//
 //-------------------------------------------------------------//

 private double distance_mf_mf(int i, int j) {
  double dist = 0;
  for(int k=0; k<data[i].length; k++) {
   double diff = (data[i][k]-data[j][k]);
   dist += diff*diff;
  }
  return dist;
 }

 //-------------------------------------------------------------//
 // Distancia entre un dato y un cluster			//
 //-------------------------------------------------------------//

 private double distance_mf_cl(int i, int j) {
  double dist = 0;
  for(int k=0; k<data[i].length; k++) {
   double diff = (data[i][k]-cluster[j][k]);
   dist += diff*diff;
  }
  return dist;
 }

 //-------------------------------------------------------------//
 // Distancia entre dos clusters				//
 //-------------------------------------------------------------//

 private double distance_cl_cl(int i, int j) {
  double dist = 0;
  for(int k=0; k<cluster[i].length; k++) {
   double diff = (cluster[i][k]-cluster[j][k]);
   dist += diff*diff;
  }
  return dist;
 }

 //-------------------------------------------------------------//
 // Distancia minima entre los puntos de dos clusters		//
 //-------------------------------------------------------------//

 private double dist1_cl_cl(int cl1, int cl2) {
  double min=Double.MAX_VALUE;
  for(int i=0; i<data.length; i++) {
   if(assign[i] != cl1) continue;
   for(int j=0; j<data.length; j++) {
    if(assign[j] != cl2) continue;
    double dist = distance_mf_mf(i,j);
    if(dist<min) min = dist;
   }
  }
  return min;
 }

 //-------------------------------------------------------------//
 // Distancia maxima entre los puntos de un cluster		//
 //-------------------------------------------------------------//

 private double diameter(int cl) {
  double max=0;
  for(int i=0; i<data.length; i++) {
   if(assign[i] != cl) continue;
   for(int j=i+1; j<data.length; j++) {
    if(assign[j] != cl) continue;
    double dist = distance_mf_mf(i,j);
    if(dist>max) max = dist;
   }
  }
  return max;
 }

 //-------------------------------------------------------------//
 // Calcula el centro del cluster a partir de sus puntos	//
 //-------------------------------------------------------------//

 private void set_cluster() {
  for(int i=0;i<cluster.length; i++){
   int n=0;
   for(int k=0;k<cluster[i].length;k++) cluster[i][k] = 0;
   for(int j=0;j<data.length;j++) if(assign[j] == i) {
    for(int k=0;k<cluster[i].length;k++) cluster[i][k] += data[j][k];
    n++;
   }
   if(n>0) for(int k=0;k<cluster[i].length;k++) cluster[i][k] /= n;
  }
 }

 //-------------------------------------------------------------//
 // Asigna el punto i-esimo al cluster mejor o a uno nuevo	//
 //-------------------------------------------------------------//

 private void insert(int n) {
  double dist_new, dist_cl, min;
  int sel_new=0, sel_cl1=0, sel_cl2=0;

  set_cluster();
  min=Double.MAX_VALUE;
  for(int i=0; i<cluster.length; i++){
   double dist = distance_mf_cl(n, i);
   if(dist<min) { min = dist; sel_new = i; }
  }
  dist_new = min;

  min=Double.MAX_VALUE;
  for(int i=0; i<cluster.length ; i++)
   for(int j=i+1; j<cluster.length ; j++) {
    double dist = distance_cl_cl(i, j);
    if(dist<min) {min = dist; sel_cl1 = i; sel_cl2 = j;}
   }
  dist_cl = min;
  if(dist_new < dist_cl) assign[n] = sel_new;
  else {
   for(int i=0; i<n; i++) if(assign[i] == sel_cl2) assign[i] = sel_cl1;
   assign[n] = sel_cl2;
  }
 }

 //-------------------------------------------------------------//
 // Reasigna los puntos a los clusters				//
 //-------------------------------------------------------------//

 private boolean reassign(){
  boolean change=false;
  for(int i=0; i<data.length; i++){
   int sel = 0;
   double min = distance_mf_cl(i,0);
   for(int j=1; j<cluster.length; j++){
    double dist = distance_mf_cl(i, j);
    if(min > dist) { min = dist; sel = j; }
   }
   if(assign[i] != sel) { assign[i] = sel; change = true; }
  }
  return change;
 }
}

