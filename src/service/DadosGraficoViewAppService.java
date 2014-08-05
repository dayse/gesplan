 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package service;

import java.util.ArrayList;

import br.blog.arruda.plot.Plot;

import br.blog.arruda.plot.data.PlotData;
import br.blog.arruda.plot.opt.PlotOptions;
import br.blog.arruda.plot.opt.tipo.PlotBars;
import br.blog.arruda.plot.opt.tipo.PlotLines;
import br.blog.arruda.plot.opt.tipo.PlotPoints;



import com.google.gson.Gson;


public class DadosGraficoViewAppService {

	
	@SuppressWarnings("unchecked")
	public DadosGraficoViewAppService() {
	}
	
	

	
	/**
	 * Gera um PlotData.
	 * 
	 * Por default o Flot usa lines como o tipo.
	 * 
	 * @param xAxis
	 * @param yAxis
	 * @return
	 */
	public PlotData gerarPlotData(ArrayList<Double> xAxis, ArrayList<Double> yAxis){
		
		return Plot.generatePlotData(xAxis, yAxis);
	}



	/**
	 * Retorna um Plot passando um grafico e as labels que se deseja para os eixos X e Y.
	 * Quando quiser fazer uma configuracao especifica, pode fazer nesse metodo. Incluindo as caracteristicas
	 * que deseja no grafico.
	 * @param listaDados
	 * @param opcoes
	 * @return
	 */
	public Plot gerarPlotComLabels(ArrayList<PlotData> datas, String xLabel, String yLabel){
		
		return Plot.generatePlot(datas, xLabel, yLabel);
	}
	

	/**
	 * Gera um PlotData de barra cm barWidth de 0.5 por default.
	 * 
	 * @param xAxis
	 * @param yAxis
	 * @return
	 */
	public PlotData gerarPlotDataEmBarras(ArrayList<Double> xAxis, ArrayList<Double> yAxis){
		
		return Plot.generatePlotDataBars(xAxis, yAxis, 0.5);
	}

	/**
	 * Gera um PlotData de linha, pondo steps como falso.
	 * 
	 * @param xAxis
	 * @param yAxis
	 * @return
	 */
	public PlotData gerarPlotDataEmLinhas(ArrayList<Double> xAxis, ArrayList<Double> yAxis){		
		return Plot.generatePlotDataLines(xAxis, yAxis, false);
	}

	/**
	 * Gera um PlotData de pontos. Radius(Raio do ponto) de 1 por padrao.
	 * Ainda nao foi testando, vale a pena conferir o valor do Radius(Radio do ponto).
	 * 
	 * @param xAxis
	 * @param yAxis
	 * @return
	 */
	public PlotData gerarPlotDataEmPontos(ArrayList<Double> xAxis, ArrayList<Double> yAxis){		
		return Plot.generatePlotDataPoints(xAxis, yAxis, 1.0);
	}
	

		
}
