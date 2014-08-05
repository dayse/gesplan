 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
/**
 * package relativo as classes de negocio
 * Com as classes bean
 * 
 */
package modelo;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Classe relativa ao Bean para o cadastro de Familias de modelos
 * 
 * @author daysemou
 *
 */

// A NamedQuerie tem que ter exatamente o nome do método que a emprega
@NamedQueries(
		{	
			@NamedQuery
			(	name = "Familia.recuperaUmaFamiliaEModelos",
				query = "select f from Familia f " +
						"left outer join fetch f.modelos where f.id = ? " +
						"group by f.codFamilia "
			),
			@NamedQuery
			(	name = "Familia.recuperaUmaFamiliaPeloCodigo",
					query = "select f from Familia f where f.codFamilia = ?"
			),
			@NamedQuery(name = "Familia.recuperaListaDeFamiliasPeloCodigoLike", 
					query = "select distinct(f) from Familia f " + 
							"left outer join fetch f.modelos " + 
							"where f.codFamilia like '%' || upper(?) || '%' "
		    ),
		    @NamedQuery(name = "Familia.recuperaListaDeFamiliasPorDescricao", 
					query = "select distinct(f) from Familia f " + 
							"left outer join fetch f.modelos " + 
							"where upper(f.descrFamilia) like '%' || upper(?) || '%' " + 
							"order by f.codFamilia "
		    ),
			@NamedQuery
			(	name = "Familia.recuperaListaDeFamilias",
				query = "select f from Familia f order by f.codFamilia"
			),
			@NamedQuery
			(	name = "Familia.recuperaListaDeFamiliasComModelos",
				query = "select distinct f from Familia f " + 
					    "left outer join fetch f.modelos " + 
					    "order by f.codFamilia"
				
			),
			@NamedQuery
			(	name = "Familia.recuperaListaDeFamiliasComDeFamPers",
				query = "select distinct f from Familia f " + 
					    "left outer join fetch f.deFamPers " +
					    "order by f.codFamilia"
				
			),
			@NamedQuery
			(	name = "Familia.recuperaListaPaginadaDeFamilias",
				query = "select distinct f from Familia f left outer join fetch f.modelos order by f.codFamilia"
			),
			@NamedQuery
			(	name = "Familia.recuperaListaPaginadaDeFamiliasCount",
				query = "select count (distinct f) from Familia f"
			)
		})

@Entity
@Table(name="FAMILIA")
      
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_FAMILIA",
		           allocationSize=1)
		           

public class Familia implements Serializable {

		private static final long serialVersionUID = 1L;

		/** identificador da familia de modelos */		 
		private Long id;
	
		/** codigo da familia de modelos */		 
		private String codFamilia;

		/** descricao da familia de modelos */		 
		private String descrFamilia;

		/** cobertura percentual de estoques da familia de modelos */		 
		private double cobertura;
		 
		/** estoque inicial (em peças) para a familia de modelos */		 
		private double estqInicFam;
		
		/** Tempo médio unitario de costura da Familia */		 
		private double tmuc;
		
		//  Uma  familia possui modelos

		// Devemos usar a interface Set para modelos ao invés de HashSet, pois no JPA às vezes vai
		// usar uma classe que implementa Set. Mas, é específica do JPA (PersistenceSet)
		
		
		private List<DeFamPer> deFamPers;
		
		private List<Modelo> modelos = new ArrayList<Modelo>();
		/* A   interface  Set   representa  uma   coleção  SEM  elementos 
		 * duplicados.  Para que um elemento  seja armazenado em um Set é
		 * utilizado  o  método  HashCode()  que  irá retornar um  número 
		 * inteiro que será  convertido na posição do elemento dentro  do
		 * Set. Um  Set  não  garante a ordem de  iteração dos  elementos 
		 * contidos no Set; em particular, não garante  que a  ordem  irá
		 * permanecer  constante  ao  longo  do  tempo.  Um Set permite a 
		 * existência delementos null.
		 */
		
		public Familia() {
			
		}
	

		// ********* Métodos do Tipo Get e Set *********

		@Id
		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
		@Column(name="ID")
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		@Column(nullable = false, length=15, unique=true)
		public String getCodFamilia() {
			return codFamilia;
		}

		public void setCodFamilia(String codFamilia) {
			this.codFamilia = codFamilia;
		}

		@Column(length=200)
		public String getDescrFamilia() {
			return descrFamilia;
		}

		public void setDescrFamilia(String descrFamilia) {
			this.descrFamilia = descrFamilia;
		}

		@Column(length=8)
		public double getCobertura() {
			return cobertura;
		}

		public void setCobertura(double cobertura) {
			this.cobertura = cobertura;
		}

		@Column(length=11)
		public double getEstqInicFam() {
			return estqInicFam;
		}

		public void setEstqInicFam(double estqInicFam) {
			this.estqInicFam = estqInicFam;
		}
		
		@Column(length=7)
		public void setTmuc(double tmuc) {
			this.tmuc = tmuc;
		}

		public double getTmuc() {
			return tmuc;
		}

		public void setCoberturaStr(String coberturaStr) {
    		try { this.cobertura = Double.parseDouble(coberturaStr); }
    		catch(NumberFormatException e) { this.cobertura=-999999999;};
			
		}
		
	
		// ********* Métodos para Associações *********
		
		@OneToMany(mappedBy = "familia")
		@OrderBy
		public List<Modelo> getModelos()
		{	return modelos;
		}
		
		public void setModelos(List<Modelo> modelos)
		{	this.modelos = modelos;	
		}
		
		// O atributo 'mappedBy' do JPA faz referencia ao atributo "familia" da classe DeFamPer
		// e por default, esta lista é inicializada como LAZY.
		@OneToMany(mappedBy="familia",  fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
		public List<DeFamPer> getDeFamPers() {
			return deFamPers;
		}
	
		
		public void setDeFamPers(List<DeFamPer> deFamPers) {
			this.deFamPers = deFamPers;
		}

        
		@Override
		public String toString(){
			return this.codFamilia + " - " + this.descrFamilia;
		}
		
		/**
		 * 
		 * Este método pode ser gerado AUTOMATICAMENTE pelo Java , juntamente com o método 
		 * "equals(Object obj)". Estes metodos são necessários para se determinar um critério principal
		 * de igualdade entre 2 objetos. 
		 * 
		 *  @return boolean
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((codFamilia == null) ? 0 : codFamilia.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		/**
		 * 
		 * Este método pode ser gerado AUTOMATICAMENTE pelo Java , juntamente com o método 
		 * "hashCode()". Estes metodos são necessários para se determinar um critério principal
		 * de igualdade entre 2 objetos. 
		 * 
		 *  @param Object
		 *  @return boolean
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Familia)) {
				return false;
			}
			Familia other = (Familia) obj;
			if (codFamilia == null) {
				if (other.codFamilia != null) {
					return false;
				}
			} else if (!codFamilia.equals(other.codFamilia)) {
				return false;
			}
			if (id == null) {
				if (other.id != null) {
					return false;
				}
			} else if (!id.equals(other.id)) {
				return false;
			}
			return true;
		}
		
}
