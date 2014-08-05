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

package xfuzzy.xfsp.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import xfuzzy.lang.LinguisticLabel;
import xfuzzy.lang.Type;

import xfuzzy.util.XConstants;

/**
 * <p> <b>Descripción:</b> panel para la representación gráfica de funciones
 * que permite la visualización de las funciones de pertenenencia del tipo
 * seleccionado por el usuario.
 * <p>
 * @author Jorge Agudo Praena
 * @version 2.1
 * @see XfspView
 *
 */
public class XfspGraphPanel
    extends JPanel {

	/**
	 * Código asociado a la clase serializable
	 */
	private static final long serialVersionUID = 95505666603075L;

  //gráfico para los ejes
  private Graphics2D gaxis;

  //gráfico para las funciones de pertenencia
  private Graphics2D gmf;

  //gráfico para la función de pertenencia seleccionada
  private Graphics2D gsel;

  //abcisa utilizada en los procesos de representación de funciones
  private int x0;

  //abcisa utilizada en los procesos de representación de funciones
  private int x1;

  //ordenada utilizada en los procesos de representación de funciones
  private int y0;

  //ordenada utilizada en los procesos de representación de funciones
  private int y1;

  //tipo elegido para su representación por el panel
  private Type selectedType = null;

  //función de pertenencia seleccionada para que tenga una representación
  //distinta
  private LinguisticLabel mfsel;

  /**
   * <p> <b>Descripción:</b> crea un panel para la representación gráfica de
   * funciones.
   * @param width Anchura que debe tener el panel.
   *
   */
  XfspGraphPanel(int width) {
    //llama al constructor de la superclase
    super();
    //establece el tamaño por defecto del panel...
    Dimension prefsize = new Dimension(width, getPreferredSize().height);
    setPreferredSize(prefsize);
    //...y el color de fondo del panel
    setBackground(XConstants.textbackground);
    setBorder(BorderFactory.createLoweredBevelBorder());
  }

  /**
   * <p> <b>Descripción:</b> establece el tipo seleccionado por el usuario, que
   * será el que deba representar el panel.
   * @param selectedType Tipo cuyas funciones de pertenencia debe representar
   * el panel.
   *
   */
  public void setSelection(Type selectedType) {
    this.selectedType = selectedType;
  }

  /**
   * <p> <b>Descripción:</b> dibuja el gráfico en la pantalla.
   * @param g Objeto Graphics a partir del cual se crearán el resto de objetos
   * Graphics necesarios en el panel.
   *
   */
  public void paint(Graphics g) {
    //llama al constructor de la superclase
    super.paint(g);
    //establece los objetos graphics de la clase
    setGraphics(g);
    //dibuja los ejes del panel
    paintAxis();
    //si se ha seleccionado algún tipo...
    if (selectedType != null) {
      //...dibuja sus funciones de pertenencia
      paintFunctions();
    }
  }

  /**
   * <p> <b>Descripción:</b> establece la función de pertenencia seleccionada
   * de entre las que forman el tipo representado.
   * @param mfsel Función de pertenencia seleccionada.
   *
   */
  public void setSelection(LinguisticLabel mfsel) {
    this.mfsel = mfsel;
  }

  /**
   * <p> <b>Descripción:</b> establece los objetos Graphics necesarios para el
   * panel a partir de uno dado.
   * @param gc Objeto a patir del cual se crearán los objetos Graphics
   * necesarios para el panel.
   *
   */
  private void setGraphics(Graphics gc) {
    gaxis = (Graphics2D) gc.create();
    gaxis.setColor(Color.black);
    gmf = (Graphics2D) gc.create();
    gmf.setColor(Color.blue);
    gsel = (Graphics2D) gc.create();
    gsel.setColor(Color.red);
    Dimension size = getSize();
    x0 = size.width / 8;
    x1 = (size.width * 7) / 8;
    y0 = (size.height * 7) / 8;
    y1 = size.height / 8;
  }

  /**
   * <p> <b>Descripción:</b> dibuja en el panel los ejes de coordenadas.
   *
   */
  private void paintAxis() {
    int xm = (x0 + x1) / 2;
    int ym = (y0 + y1 + 1) / 2;
    gaxis.drawLine(x0 - 1, y0 + 1, x0 - 1, y1);
    gaxis.drawLine(x0 - 1, y0 + 1, x1 + 1, y0 + 1);
    gaxis.drawLine(x1 + 1, y0 + 1, x1 + 1, y1);
    gaxis.drawLine(x0 - 6, y1, x0 - 1, y1);
    gaxis.drawLine(x0 - 6, ym, x0 - 1, ym);
    gaxis.drawLine(x1 + 1, y1, x1 + 6, y1);
    gaxis.drawLine(x1 + 1, ym, x1 + 6, ym);
    gaxis.drawLine(x0 - 1, y0 + 6, x0 - 1, y0 + 1);
    gaxis.drawLine(xm, y0 + 6, xm, y0 + 1);
    gaxis.drawLine(x1 + 1, y0 + 6, x1 + 1, y0 + 1);
    gaxis.drawString("1.0", x0 - 30, y1 + 5);
    gaxis.drawString("1.0", x1 + 7, y1 + 5);
    gaxis.drawString("0.5", x0 - 30, ym + 5);
    gaxis.drawString("0.5", x1 + 7, ym + 5);
    gaxis.drawString("Min.", x0 - 10, y0 + 20);
    gaxis.drawString("Max.", x1 - 10, y0 + 20);
  }

  /**
   * <p> <b>Descripción:</b> dibuja una función de pertenencia en el panel.
   * @param mf Función de pertenencia que debe representar.
   * @param gc Objeto a partir del cual se dibujará la función de pertenencia.
   *
   */
  private void paintFunction(LinguisticLabel mf, Graphics2D gc) {
    //obtiene los valores mínimo y máximo del universo de discurso del tipo
    double min = selectedType.getUniverse().min();
    double max = selectedType.getUniverse().max();
    double step = selectedType.getUniverse().step();
    //si la función de pertenencia es un singleton...
    if (mf instanceof pkg.xfl.mfunc.singleton) {
      //...se limita a dibujar una línea vertical sobre el punto donde la
      //función toma su valor
      double value = mf.get()[0];
      int x = (int) ( ( (value - min) * (x1 - x0)) / (max - min)) + x0;
      if ( (x >= x0) && (x <= x1)) {
        gc.drawLine(x, y0, x, y1);
      }
      return;
    }
    //para otro tipo de funciones, tiene que construir su representación gráfica
    //de forma iterativa tomando valores de muestra
    double next = min + step;
    int xp = x0;
    int yp = (int) (mf.compute(min) * (y1 - y0)) + y0;
    for (int xi = x0 + 1; xi <= x1; xi++) {
      double x = min + ( ( (xi - x0) * (max - min)) / (x1 - x0));

      while (x >= next) {
        int yi = (int) (mf.compute(next) * (y1 - y0)) + y0;
        gc.drawLine(xp, yp, xi, yi);
        xp = xi;
        yp = yi;
        next += step;
      }
      int yi = (int) (mf.compute(x) * (y1 - y0)) + y0;
      gc.drawLine(xp, yp, xi, yi);
      xp = xi;
      yp = yi;
    }
  }

  /**
   * <p> <b>Descripción:</b> dibuja todas las funciones de pertenencia del
   * tipo seleccionado.
   *
   */
  private void paintFunctions() {
    //obtiene las funciones de pertenencia del tipo seleccionado
    LinguisticLabel[] mf = selectedType.getAllMembershipFunctions();
    //para todas las funciones del tipo...
    for (int i = 0; i < mf.length; i++) {
      //...si la función actual no es la seleccionada...
      if (mf[i] != mfsel) {
        //la dibuja con color azul
        paintFunction(mf[i], gmf);
      }
      //en otro caso....
      else {
        //...la dibuja de color rojo
        paintFunction(mf[i], gsel);
      }
    }
  }
}
