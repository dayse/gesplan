 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import service.exception.AplicacaoException;

import carga.*;

/**
 * Esse service é responsavel por chamar as cargas do sistema
 * Para isso é preciso:
 * 
 * Instanciar a lista de cargas como uma nova lista vazia;
 * Instanciar uma Carga;
 * Adiciona-la na lista cargas;
 * Retornar o método executarCargas;
 *  
 * Lembrando que é preciso jogar uma ApplicationExeption para o CargaAction.
 * Isso por que o diferentemente do TestNG, essa carga é executada pelo browser, logo tem uma
 * interface propria para o retorno de erros, e deve ser usada!
 * 
 * 
 * 
 * @author felipe.arruda
 * 
 */

public class CargaAppService {

	//lista das cargas que devem ser executadas
	private ArrayList<CargaBase> cargas;
	
	/**
	 * não cria nenhuma fabrica de nada, ja que são chamadas dentro das Cargas(Filhos de CargaBase)
	 */
	public CargaAppService() {
	}
			
	/**
	 * executa apenas a carga basica do sistema:
	 * CargaLimparUploads
	 * CargaUsuario
	 * CargaParametros
	 * 
	 * @return
	 * @throws AplicacaoException
	 */
	public boolean executarCargaBasica() throws AplicacaoException{
		//instancia a lista de cargas novamente, para ter certeza que
		//apenas as cargas a seguir serao executadas.
		cargas = new ArrayList<CargaBase>();

		cargas.add(new CargaLimparUploads());
		cargas.add(new CargaUsuario());
		cargas.add(new CargaParametros());
		return executarCargas();
	}

	/**
	 * executa a carga do estudo de caso Inicial:
	 * Inclui todos os dados básicos mas não chega a implementar o pmp
	 * Refere-se a primeira rodada
	 * 
	 * CargaBasica;
	 * CargaPerioPM
	 * CargaHP
	 * CargaCapacDia
	 * CargaFamilia
	 * CargaModelo
	 * CargaDataEstqInic
	 * CargaTecido
	 * CargaRecurso
	 * CargaCapacRec
	 * CargaTecModel
	 * CargaRecModel
	 * CargaDeModPer
	 * CargaCadPlan
	 * CargaExcecao
	 * 
	 * @return
	 * @throws AplicacaoException
	 */
	public boolean executarCargaEstudoDeCaso() throws AplicacaoException{
		//instancia a lista de cargas novamente, para ter certeza que
		//apenas as cargas a seguir serao executadas.
		cargas = new ArrayList<CargaBase>();
		//executa carga basica, se ela nao rodar, retorna logo falso.
		if(!this.executarCargaBasica())
			return false;
		//estabelece os periodos e HP para o primeiro ciclo de planejamento
		cargas.add(new CargaPerioPM());
		cargas.add(new CargaHP());
		//informa dados de capacidade diaria no setor de costura
		cargas.add(new CargaCapacDia());
		//informa dados originais para as familias de produto
		cargas.add(new CargaFamilia());
		//informa dados originais para os modelos
		cargas.add(new CargaModelo());
		//informa data relativa ao estoque inicial dos modelos
		cargas.add(new CargaDataEstqInic(new GregorianCalendar(2011,Calendar.JUNE,30)));
		//informa dados de tecidos
		cargas.add(new CargaTecido());
		//informa dados de recursos
		cargas.add(new CargaRecurso());
		//informa capacidade de recurso por período
		cargas.add(new CargaCapacRec());
		//informa consumo de tecido por modelo
		cargas.add(new CargaTecModel());
		//informa consumo de recurso por modelo
		cargas.add(new CargaRecModel());
		//informa demanda por periodo por modelo para os primeiros 10 periodos
		cargas.add(new CargaDeModPer());
		//inclui 1 plano mestre
		cargas.add(new CargaCadPlan());
		//inclui cada uma das exceções do sistema no status "ativa"
		cargas.add(new CargaExcecao());
		return executarCargas();
	}
	
	/**
	 * executa a carga das alteracoes do estudo de caso,
	 * Ou seja: executa a carga que preenche os dados básicos do estudo de caso relativos ao primeiro ciclo de planejamento
	 * e depois faz as alterações relativas ao início de um novo ciclo de planejamento, ou seja
	 * implementa o plano vigente, defasa período, define novo HP, altera os dados de demanda dos modelos relativos ao novo 
	 * periodo incluido, atualiza dados de estoque inicial dos modelos, atualiza capacidade de máquina e de recurso para o novo
	 * periodo.  Inclui plano com novo HP, inclui modelagem fuzzy  e inclui plano com fuzzy.
	 * Neste momento os planos cadastrados se referem ao segundo periodo, enquanto o plano vigente inicia no primeiro periodo.
	 *  
	 * CargaEstudoDeCaso;
	 * 
	 * CargaEstudoImplementaPlano
	 * CargaEstudoDefasaPeriodo
	 * CargaEstudoAgrupaPeriodo
	 * CargaEstudoDefineNovoHP
	 * CargaEstudoAlteraDemandaDePeriodo
	 * CargaEstudoAtualizaEstoque
	 * CargaEstudoAtualizaCapacDia
	 * CargaEstudoAtualizaCapacRec
	 * CargaEstudoIncluiPlanoComNovoHP
	 * CargaEstudoIncluiModelagemFuzzy
	 * CargaEstudoIncluiPlanoComFuzzy
	 * 
	 * @return
	 * @throws AplicacaoException
	 */
	public boolean executarCargaAlteraEstudoDeCaso() throws AplicacaoException{
		//instancia a lista de cargas novamente, para ter certeza que
		//apenas as cargas a seguir serao executadas.
		cargas = new ArrayList<CargaBase>();		
		//executa carga do caso de estudo, se ela nao rodar, retorna logo falso.
		if(!this.executarCargaEstudoDeCaso())
			return false;
		
		cargas.add(new CargaEstudoAlteraRecebimentoProducao());
		cargas.add(new CargaEstudoImplementaPlano());
		cargas.add(new CargaEstudoDefasaPeriodo());
		cargas.add(new CargaEstudoAgrupaPeriodo());
		cargas.add(new CargaEstudoDefineNovoHP());
		cargas.add(new CargaEstudoAlteraDemandaDePeriodo());
		cargas.add(new CargaEstudoAtualizaEstoque());
		//informa data relativa ao estoque inicial dos modelos
		cargas.add(new CargaDataEstqInic(new GregorianCalendar(2011,Calendar.JULY,16)));
		cargas.add(new CargaEstudoAtualizaCapacDia());
		cargas.add(new CargaEstudoAtualizaCapacRec());
		cargas.add(new CargaEstudoIncluiPlanoComNovoHP());
		cargas.add(new CargaEstudoIncluiModelagemFuzzy());
		cargas.add(new CargaEstudoIncluiPlanoComFuzzy());
		return executarCargas();
	}

	
	
	/**
	 * Esse método executa genericamente a lista de cargas
	 * percorre cada uma e chama o método 'executar' de cada.
	 * Se esse método retornar false, ele para de executar todas as outras de retorna falso.
	 * 
	 * Se terminar de executar todas as cargas na lista de cargas, então retorna true. 
	 * @return
	 * @throws AplicacaoException
	 */
	private boolean executarCargas()  throws AplicacaoException{
		for(CargaBase carga : cargas){
			System.out.println(">>>executando carga:"+carga.getClass());
			if(!carga.executar()){
				return false;
			}
			System.out.println(">>>Sucesso:"+carga.getClass());
		}
		cargas = new ArrayList<CargaBase>();
		return true;
	}
	
	
}
