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

package xfuzzy.xfsim;

import xfuzzy.lang.*;
import xfuzzy.util.*;

/**
 * Hilo del proceso de simulación
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XfsimProcess extends Thread {
	
	//----------------------------------------------------------------------------//
	//                            MIEMBROS PRIVADOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Ventana principal de la aplicación
	 */
	private Xfsim xfsim;
	
	//----------------------------------------------------------------------------//
	//                                CONSTRUCTOR                                 //
	//----------------------------------------------------------------------------//

	/**
	 * Constructor
	 */
	XfsimProcess(Xfsim xfsim) {
		super("xfsim thread 1");
		this.xfsim = xfsim;
		start();
	}
	
	//----------------------------------------------------------------------------//
	//                             MÉTODOS PÚBLICOS                               //
	//----------------------------------------------------------------------------//

	/**
	 * Ejecución del hilo
	 */
	public void run() {
		XfsimConfig config = xfsim.getConfiguration();
		SystemModule system = xfsim.getSpecification().getSystemModule();
		
		if(!config.isConfigured()) return;
		try {
			if(config.init == null) config.plant.init();
			else config.plant.init(config.init);
		} catch(Exception ex) {
			XDialog.showMessage(null,ex.toString());
			return;
		}
		double[] plantstate = config.plant.state();
		long begin = System.currentTimeMillis();
		double[] fuzzystate = system.crispInference(plantstate);
		plantstate = config.plant.compute(fuzzystate);
		long time = System.currentTimeMillis();
		int iteration = 1;
		xfsim.refreshStatus(iteration,time-begin,fuzzystate,plantstate);
		config.open();
		config.iter(iteration,time-begin,fuzzystate,plantstate);
		while(config.limit.test(iteration,time-begin,fuzzystate,plantstate)) {
			while(xfsim.getStatus()==2)
				try{ Thread.sleep(100); } catch(InterruptedException e) {}
				if(xfsim.getStatus() == 3) break;
				fuzzystate = system.crispInference(plantstate);
				plantstate = config.plant.compute(fuzzystate);
				time = System.currentTimeMillis();
				iteration++;
				xfsim.refreshStatus(iteration,time-begin,fuzzystate,plantstate);
				config.iter(iteration,time-begin,fuzzystate,plantstate);
		}
		config.end();
		xfsim.setSensitive(0);
	}
}
