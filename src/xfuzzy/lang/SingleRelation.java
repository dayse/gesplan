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

package xfuzzy.lang;

/**
 * Clase que describe una proposición difusa simple, es decir, una
 * proposición que relaciona una variable con una de sus etiquetas
 * lingüísticas
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class SingleRelation extends Relation {

	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                              //
	//----------------------------------------------------------------------------//

	/**
	 * Tipo de proposición (IGUAL, MAYOR, MENOR, ...)
	 */
	private int kind;
	
	/**
	 * Variable de la proposición simple
	 */
	private Variable var;
	
	/**
	 * Etiqueta lingüística de la proposición simple
	 */
	private LinguisticLabel mf;
	
	/**
	 * Referencia a la base de reglas para poder utilizar su conjunto de operadores
	 */
	private Rulebase rb;
	
	/**
	 * Grado de activación de la proposición en un proceso de inferencia
	 */
	private double degree;

	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public SingleRelation(int kind, Variable var, LinguisticLabel mf, Rulebase rb) {
		this.kind = kind;
		this.var = var;
		this.var.link();
		this.mf = mf;
		this.mf.link();
		this.rb = rb;
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	//----------------------------------------------------------------------------//
	// Métodos de acceso de los campos                                            //
	//----------------------------------------------------------------------------//

	/**
	 * Obtiene el tipo de proposición
	 */
	public int getKind() {
		return this.kind;
	}

	/**
	 * Asigna el tipo de proposición
	 */
	public void setKind(int kind) {
		this.kind = kind;
	}

	/**
	 * Obtiene la referencia a la variable de la proposición
	 */
	public Variable getVariable() {
		return this.var;
	}

	/**
	 * Asigna la referencia a la variable de la proposición
	 */
	public void setVariable(Variable var) {
		this.var = var;
	}

	/**
	 * Obtiene la referencia a la MF de la proposición
	 */
	public LinguisticLabel getMembershipFunction() {
		return this.mf;
	}

	/**
	 * Asigna la referencia a la MF de la proposición
	 */
	public void setMembershipFunction(LinguisticLabel mf) {
		this.mf = mf;
	}

	/**
	 * Obtiene la referencia a la componente izquierda
	 */
	public Relation getLeftRelation() {
		return null;
	}

	/**
	 * Asigna la referencia a la componente izquierda
	 */
	public void setLeftRelation(Relation rel) {
	}

	/**
	 * Obtiene la referencia a la componente derecha
	 */
	public Relation getRightRelation() {
		return null;
	}

	/**
	 * Obtiene la referencia a la componente derecha
	 */
	public void setRightRelation(Relation rel) {
	}

	//----------------------------------------------------------------------------//
	// Métodos de cálculo de la proposición                                       //
	//----------------------------------------------------------------------------//

	/**
	 * Calcula el grado de activación de la proposición
	 */
	public double compute() throws XflException {
		switch(kind) {
		case IS: return compute_eq();
		case GREATER: return compute_gr();
		case GR_EQ: return compute_greq();
		case SMALLER: return compute_sm();
		case SM_EQ: return compute_smeq();
		case APP_EQ: return compute_appeq();
		case ISNOT: return compute_noteq();
		case SL_EQ: return compute_sleq();
		case VERY_EQ: return compute_vreq();
		default: return 0;
		}
	}

	/**
	 * Calcula la derivada de la proposición
	 */
	public void derivative(double de) throws XflException {
		switch(kind) {
		case IS: derivative_eq(de); break;
		case GREATER: derivative_gr(de); break;
		case GR_EQ: derivative_greq(de); break;
		case SMALLER: derivative_sm(de); break;
		case SM_EQ: derivative_smeq(de); break;
		case APP_EQ: derivative_appeq(de); break;
		case ISNOT: derivative_noteq(de); break;
		case SL_EQ: derivative_sleq(de); break;
		case VERY_EQ: derivative_vreq(de); break;
		}
	}

	//----------------------------------------------------------------------------//
	// Otros métodos                                                              //
	//----------------------------------------------------------------------------//

	/**
	 * Genera la descripcion XFL3 de la proposición
	 */
	public String toXfl() {
		switch(kind) {
		case IS:      return this.var.getName()+" == "+this.mf.getLabel();
		case GREATER: return this.var.getName()+" > "+ this.mf.getLabel();
		case GR_EQ:   return this.var.getName()+" >= "+this.mf.getLabel();
		case SMALLER: return this.var.getName()+" < "+ this.mf.getLabel();
		case SM_EQ:   return this.var.getName()+" <= "+this.mf.getLabel(); 
		case APP_EQ:  return this.var.getName()+" ~= "+this.mf.getLabel();
		case ISNOT:   return this.var.getName()+" != "+this.mf.getLabel();
		case SL_EQ:   return this.var.getName()+" %= "+this.mf.getLabel();
		case VERY_EQ: return this.var.getName()+" += "+this.mf.getLabel();
		default: return "";
		}
	}

	/**
	 * Obtiene un duplicado del objeto
	 */
	public Object clone(Rulebase rbcl) {
		return new SingleRelation(this.kind,this.var,this.mf,rbcl);
	}

	/**
	 * Verifica que la proposición sea ajustable
	 */
	public boolean isAdjustable() {
		return mf.isAdjustable();
	}

	/**
	 * Elimina los enlaces para poder eliminar la proposición
	 */
	public void dispose() {
		if(this.mf != null) this.mf.unlink();
		if(this.var != null) this.var.unlink();
	}

	/**
	 * Sustituye una función de pertenencia por otra
	 */
	public void exchange(LinguisticLabel oldmf, LinguisticLabel newmf) {
		if(this.mf == oldmf) {
			this.mf.unlink();
			this.mf = newmf;
			this.mf.link();
		}
	}

	/**
	 * Sustituye una variable por otra
	 */
	public void exchange(Variable oldvar, Variable newvar) {
		if(this.var == oldvar) {
			this.var.unlink();
			this.var = newvar;
			this.var.link();
		}
	}

 	//----------------------------------------------------------------------------//
	//                             MÉTODOS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	//----------------------------------------------------------------------------//
	// Métodos de cálculo del grado de activación                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Calcula el grado de activación de la proposición "=="
	 */
	private double compute_eq() throws XflException {
		MemFunc value = this.var.getValue();
		if(value == null) throw new XflException(18);
		if(value instanceof pkg.xfl.mfunc.singleton) {
			return this.mf.compute( ((ParamMemFunc) value).get()[0] );
		}

		if((value instanceof AggregateMemFunc) &&
				((AggregateMemFunc) value).isDiscrete() ) {
			double[][] val = ((AggregateMemFunc) value).getDiscreteValues();
			double deg = 0;
			for(int i=0; i<val.length; i++){
				double mu = this.mf.compute(val[i][0]);
				double minmu = (mu<val[i][1] ? mu : val[i][1]);
				if( deg<minmu ) deg = minmu;
			}
			return deg;
		}

		double min = this.mf.min();
		double max = this.mf.max();
		double step = this.mf.step();
		double deg = 0;

		for(double x=min; x<=max; x+=step){
			double mu1 = this.mf.compute(x);
			double mu2 = value.compute(x);
			double minmu = (mu1<mu2 ? mu1 : mu2);
			if( deg<minmu ) deg = minmu;
		}
		return deg;
	}

	/**
	 * Calcula el grado de activación de la proposición ">"
	 */
	private double compute_gr() throws XflException {
		MemFunc value = this.var.getValue();
		if(value == null) throw new XflException(18);
		if(value instanceof pkg.xfl.mfunc.singleton) {
			degree = this.mf.smallereq( ((ParamMemFunc) value).get()[0] );
			return rb.operation.not.compute(degree);
		}

		if((value instanceof AggregateMemFunc) &&
				((AggregateMemFunc) value).isDiscrete() ) {
			double[][] val = ((AggregateMemFunc) value).getDiscreteValues();
			double deg = 0;
			for(int i=0; i<val.length; i++){
				double mu = rb.operation.not.compute( this.mf.smallereq(val[i][0]) );
				double minmu = (mu<val[i][1] ? mu : val[i][1]);
				if( deg<minmu ) deg = minmu;
			}
			return deg;
		}

		double min = this.mf.min();
		double max = this.mf.max();
		double step = this.mf.step();
		double deg = 0, smeq = 0;

		for(double x=max; x>=min; x-=step){
			double mu1 = value.compute(x);
			double mu2 = this.mf.compute(x);
			if( mu2>smeq ) smeq = mu2;
			double gr = rb.operation.not.compute(smeq);
			double minmu = (mu1<gr ? mu1 : gr);
			if( deg<minmu ) deg = minmu;
		}
		return deg;
	}

	/**
	 * Calcula el grado de activación de la proposición ">="
	 */
	private double compute_greq() throws XflException {
		MemFunc value = this.var.getValue();
		if(value == null) throw new XflException(18);
		if(value instanceof pkg.xfl.mfunc.singleton) {
			return this.mf.greatereq( ((ParamMemFunc) value).get()[0] );
		}

		if((value instanceof AggregateMemFunc) &&
				((AggregateMemFunc) value).isDiscrete() ) {
			double[][] val = ((AggregateMemFunc) value).getDiscreteValues();
			double deg = 0;
			for(int i=0; i<val.length; i++){
				double mu = this.mf.greatereq(val[i][0]);
				double minmu = (mu<val[i][1] ? mu : val[i][1]);
				if( deg<minmu ) deg = minmu;
			}
			return deg;
		}

		double min = this.mf.min();
		double max = this.mf.max();
		double step = this.mf.step();
		double deg=0, greq=0;

		for(double x=min; x<=max; x+=step){
			double mu1 = value.compute(x);
			double mu2 = this.mf.compute(x);
			if( mu2>greq ) greq = mu2;
			double minmu = (mu1<greq ? mu1 : greq);
			if( deg<minmu ) deg = minmu;
		}
		return deg;
	}

	/**
	 * Calcula el grado de activación de la proposición "<"
	 */
	private double compute_sm() throws XflException {
		MemFunc value = this.var.getValue();
		if(value == null) throw new XflException(18);
		if(value instanceof pkg.xfl.mfunc.singleton) {
			degree = this.mf.greatereq( ((ParamMemFunc) value).get()[0] );
			return rb.operation.not.compute(degree);
		}

		if((value instanceof AggregateMemFunc) &&
				((AggregateMemFunc) value).isDiscrete() ) {
			double[][] val = ((AggregateMemFunc) value).getDiscreteValues();
			double deg = 0;
			for(int i=0; i<val.length; i++){
				double mu = rb.operation.not.compute( this.mf.greatereq(val[i][0]) );
				double minmu = (mu<val[i][1] ? mu : val[i][1]);
				if( deg<minmu ) deg = minmu;
			}
			return deg;
		}

		double min = this.mf.min();
		double max = this.mf.max();
		double step = this.mf.step();
		double deg = 0, greq = 0;

		for(double x=min; x<=max; x+=step){
			double mu1 = value.compute(x);
			double mu2 = this.mf.compute(x);
			if( mu2>greq ) greq = mu2;
			double sm = rb.operation.not.compute(greq);
			double minmu = (mu1<sm ? mu1 : sm);
			if( deg<minmu ) deg = minmu;
		}
		return deg;
	}

	/**
	 * Calcula el grado de activación de la proposición "<="
	 */
	private double compute_smeq() throws XflException {
		MemFunc value = this.var.getValue();
		if(value == null) throw new XflException(18);
		if(value instanceof pkg.xfl.mfunc.singleton) {
			return this.mf.smallereq( ((ParamMemFunc) value).get()[0] );
		}

		if((value instanceof AggregateMemFunc) &&
				((AggregateMemFunc) value).isDiscrete() ) {
			double[][] val = ((AggregateMemFunc) value).getDiscreteValues();
			double deg = 0;
			for(int i=0; i<val.length; i++){
				double mu = this.mf.smallereq(val[i][0]);
				double minmu = (mu<val[i][1] ? mu : val[i][1]);
				if( deg<minmu ) deg = minmu;
			}
			return deg;
		}

		double min = this.mf.min();
		double max = this.mf.max();
		double step = this.mf.step();
		double deg=0, smeq=0;

		for(double x=max; x>=min; x-=step){
			double mu1 = value.compute(x);
			double mu2 = this.mf.compute(x);
			if( mu2>smeq ) smeq = mu2;
			double minmu = (mu1<smeq ? mu1 : smeq);
			if( deg<minmu ) deg = minmu;
		}
		return deg;
	}

	/**
	 * Calcula el grado de activación de la proposición "~="
	 */
	private double compute_appeq() throws XflException {
		MemFunc value = this.var.getValue();
		if(value == null) throw new XflException(18);
		if(value instanceof pkg.xfl.mfunc.singleton) {
			degree = this.mf.compute( ((ParamMemFunc) value).get()[0] );
			return rb.operation.moreorless.compute(degree);
		}

		if((value instanceof AggregateMemFunc) &&
				((AggregateMemFunc) value).isDiscrete() ) {
			double[][] val = ((AggregateMemFunc) value).getDiscreteValues();
			double deg = 0;
			for(int i=0; i<val.length; i++){
				double mu = rb.operation.moreorless.compute( this.mf.compute(val[i][0]) );
				double minmu = (mu<val[i][1] ? mu : val[i][1]);
				if( deg<minmu ) deg = minmu;
			}
			return deg;
		}

		double min = this.mf.min();
		double max = this.mf.max();
		double step = this.mf.step();
		double deg = 0;
		for(double x=min; x<=max; x+=step){
			double mu1 = rb.operation.moreorless.compute( this.mf.compute(x) );
			double mu2 = value.compute(x);
			double minmu = (mu1<mu2 ? mu1 : mu2);
			if( deg<minmu ) deg = minmu;
		}
		return deg;
	}

	/**
	 * Calcula el grado de activación de la proposición "!="
	 */
	private double compute_noteq() throws XflException {
		MemFunc value = this.var.getValue();
		if(value == null) throw new XflException(18);
		if(value instanceof pkg.xfl.mfunc.singleton) {
			degree = this.mf.compute( ((ParamMemFunc) value).get()[0] );
			return rb.operation.not.compute(degree);
		}

		if((value instanceof AggregateMemFunc) &&
				((AggregateMemFunc) value).isDiscrete() ) {
			double[][] val = ((AggregateMemFunc) value).getDiscreteValues();
			double deg = 0;
			for(int i=0; i<val.length; i++){
				double mu = rb.operation.not.compute( this.mf.compute(val[i][0]) );
				double minmu = (mu<val[i][1] ? mu : val[i][1]);
				if( deg<minmu ) deg = minmu;
			}
			return deg;
		}

		double min = this.mf.min();
		double max = this.mf.max();
		double step = this.mf.step();
		double deg = 0;

		for(double x=min; x<=max; x+=step){
			double mu1 = rb.operation.not.compute( this.mf.compute(x) );
			double mu2 = value.compute(x);
			double minmu = (mu1<mu2 ? mu1 : mu2);
			if( deg<minmu ) deg = minmu;
		}
		return deg;
	}

	/**
	 * Calcula el grado de activación de la proposición "%="
	 */
	private double compute_sleq() throws XflException {
		MemFunc value = this.var.getValue();
		if(value == null) throw new XflException(18);
		if(value instanceof pkg.xfl.mfunc.singleton) {
			degree = this.mf.compute( ((ParamMemFunc) value).get()[0] );
			return rb.operation.slightly.compute(degree);
		}

		if((value instanceof AggregateMemFunc) &&
				((AggregateMemFunc) value).isDiscrete() ) {
			double[][] val = ((AggregateMemFunc) value).getDiscreteValues();
			double deg = 0;
			for(int i=0; i<val.length; i++){
				double mu = rb.operation.slightly.compute( this.mf.compute(val[i][0]) );
				double minmu = (mu<val[i][1] ? mu : val[i][1]);
				if( deg<minmu ) deg = minmu;
			}
			return deg;
		}

		double min = this.mf.min();
		double max = this.mf.max();
		double step = this.mf.step();
		double deg = 0;
		for(double x=min; x<=max; x+=step){
			double mu1 = rb.operation.slightly.compute( this.mf.compute(x) );
			double mu2 = value.compute(x);
			double minmu = (mu1<mu2 ? mu1 : mu2);
			if( deg<minmu ) deg = minmu;
		}
		return deg;
	}

	/**
	 * Calcula el grado de activación de la proposición "+="
	 */
	private double compute_vreq() throws XflException {
		MemFunc value = this.var.getValue();
		if(value == null) throw new XflException(18);
		if(value instanceof pkg.xfl.mfunc.singleton) {
			degree = this.mf.compute( ((ParamMemFunc) value).get()[0] );
			return rb.operation.very.compute(degree);
		}

		if((value instanceof AggregateMemFunc) &&
				((AggregateMemFunc) value).isDiscrete() ) {
			double[][] val = ((AggregateMemFunc) value).getDiscreteValues();
			double deg = 0;
			for(int i=0; i<val.length; i++){
				double mu = rb.operation.very.compute( this.mf.compute(val[i][0]) );
				double minmu = (mu<val[i][1] ? mu : val[i][1]);
				if( deg<minmu ) deg = minmu;
			}
			return deg;
		}

		double min = this.mf.min();
		double max = this.mf.max();
		double step = this.mf.step();
		double deg = 0;
		for(double x=min; x<=max; x+=step){
			double mu1 = rb.operation.very.compute( this.mf.compute(x) );
			double mu2 = value.compute(x);
			double minmu = (mu1<mu2 ? mu1 : mu2);
			if( deg<minmu ) deg = minmu;
		}
		return deg;
	}

	//----------------------------------------------------------------------------//
	//		Metodos de calculo de la derivada		//
	//----------------------------------------------------------------------------//

	/**
	 * Calcula la derivada de la proposición "=="
	 */
	private void derivative_eq(double de) throws XflException {
		if(!this.mf.isAdjustable()) return;
		double deriv[] = this.mf.deriv_eq(this.var.getCrispValue());
		for(int i=0; i<deriv.length; i++) this.mf.addDeriv(i, de*deriv[i] );
	}

	/**
	 * Calcula la derivada de la proposición ">"
	 */
	private void derivative_gr(double de) throws XflException {
		if(!this.mf.isAdjustable()) return;
		double dnot = rb.operation.not.derivative(degree);
		double deriv[] = this.mf.deriv_smeq(this.var.getCrispValue());
		for(int i=0; i<deriv.length; i++) this.mf.addDeriv(i, de*dnot*deriv[i] );
	}

	/**
	 * Calcula la derivada de la proposición ">="
	 */
	private void derivative_greq(double de) throws XflException {
		if(!this.mf.isAdjustable()) return;
		double deriv[] = this.mf.deriv_greq(this.var.getCrispValue());
		for(int i=0; i<deriv.length; i++) this.mf.addDeriv(i, de*deriv[i] );
	}

	/**
	 * Calcula la derivada de la proposición "<"
	 */
	private void derivative_sm(double de) throws XflException {
		if(!this.mf.isAdjustable()) return;
		double dnot = rb.operation.not.derivative(degree);
		double deriv[] = this.mf.deriv_greq(this.var.getCrispValue());
		for(int i=0; i<deriv.length; i++) this.mf.addDeriv(i, de*dnot*deriv[i] );
	}

	/**
	 * Calcula la derivada de la proposición "<="
	 */
	private void derivative_smeq(double de) throws XflException {
		if(!this.mf.isAdjustable()) return;
		double deriv[] = this.mf.deriv_smeq(this.var.getCrispValue());
		for(int i=0; i<deriv.length; i++) this.mf.addDeriv(i, de*deriv[i] );
	}

	/**
	 * Calcula la derivada de la proposición "~="
	 */
	private void derivative_appeq(double de) throws XflException {
		if(!this.mf.isAdjustable()) return;
		double dnot = rb.operation.moreorless.derivative(degree);
		double deriv[] = this.mf.deriv_eq(this.var.getCrispValue());
		for(int i=0; i<deriv.length; i++) this.mf.addDeriv(i, de*dnot*deriv[i] );
	}

	/**
	 * Calcula la derivada de la proposición "!="
	 */
	private void derivative_noteq(double de) throws XflException {
		if(!this.mf.isAdjustable()) return;
		double dnot = rb.operation.not.derivative(degree);
		double deriv[] = this.mf.deriv_eq(this.var.getCrispValue());
		for(int i=0; i<deriv.length; i++) this.mf.addDeriv(i, de*dnot*deriv[i] );
	}

	/**
	 * Calcula la derivada de la proposición "%="
	 */
	private void derivative_sleq(double de) throws XflException {
		if(!this.mf.isAdjustable()) return;
		double dnot = rb.operation.slightly.derivative(degree);
		double deriv[] = this.mf.deriv_eq(this.var.getCrispValue());
		for(int i=0; i<deriv.length; i++) this.mf.addDeriv(i, de*dnot*deriv[i] );
	}

	/**
	 * Calcula la derivada de la proposición "+="
	 */
	private void derivative_vreq(double de) throws XflException {
		if(!this.mf.isAdjustable()) return;
		double dnot = rb.operation.very.derivative(degree);
		double deriv[] = this.mf.deriv_eq(this.var.getCrispValue());
		for(int i=0; i<deriv.length; i++) this.mf.addDeriv(i, de*dnot*deriv[i] );
	}

}

