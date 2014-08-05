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


package xfuzzy.util;

import javax.swing.*;

/**
 * Iconos de la ventana de selección de ficheros
 * 
 * @author Francisco José Moreno Velo
 *
 */
public class XFileIcons {

	//----------------------------------------------------------------------------//
	//                              MIEMBROS PRIVADOS                             //
	//----------------------------------------------------------------------------//

	/**
	 * Codificacion GIF del icono "xfuzzy"
	 */
	private static final byte[] xfuzzy_image() { 
		byte data[] = {
				71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -95, 0, 0, -1, -1,
				-1, -82, 69, 12, -1, -1, -1, -1, -1, -1, 33, -2, 14, 77, 97,
				100, 101, 32, 119, 105, 116, 104, 32, 71, 73, 77, 80, 0, 33, -7,
				4, 1, 10, 0, 0, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0,
				0, 2, 36, -124, -113, -87, -101, -31, -33, 32, 120, 97, 57, 68, -93,
				-54, -26, 90, -24, 49, 84, -59, 116, -100, 88, -99, -102, 25, 30, -19,
				36, -103, -97, -89, -62, 117, -119, 51, 5, 0, 59 };
		return data;
	}

	/**
	 * Codificacion GIF del icono "pkg"
	 */
	private static final byte[] pkg_image() { 
		byte data[] = {
				71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -62, 0, 0, -1, -6, 
				-51, 0, 0, 0, -91, 42, 42, -128, -128, -128, -1, -1, -1, -1, -1, 
				-1, -1, -1, -1, -1, -1, -1, 33, -2, 14, 77, 97, 100, 101, 32, 
				119, 105, 116, 104, 32, 71, 73, 77, 80, 0, 33, -7, 4, 1, 10, 
				0, 0, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 3, 64, 
				8, -86, -79, -48, 110, -123, 32, 32, -99, 110, -118, 93, 41, -57, -34, 
				54, 13, 83, 88, 61, -35, -96, 6, -21, 37, -87, 48, 27, 75, -23, 
				-38, -98, -26, 88, 114, 120, -18, 75, -102, 96, 7, -62, 16, 85, -116, 
				39, -38, -121, -105, 44, 46, 121, 68, -122, 39, -124, -119, 72, 47, 81, 
				-85, 84, -101, 0, 0, 59 };
		return data;
	}

	/**
	 * Codificacion GIF del icono "doc"
	 */
	private static final byte[] doc_image() { 
		byte data[] = {
				71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -95, 0, 0, 0, 0, 
				0, -1, -1, -1, -128, -128, -128, -1, -1, -1, 33, -2, 14, 77, 97, 
				100, 101, 32, 119, 105, 116, 104, 32, 71, 73, 77, 80, 0, 33, -7, 
				4, 1, 10, 0, 3, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 
				0, 2, 57, -100, -113, -119, -64, 13, 122, 64, -104, -109, -63, 33, -123, 
				14, -14, 41, 73, 113, -51, 23, 104, -101, 6, 8, 9, 24, 86, -22, 
				82, -102, 91, -70, -74, 20, 13, -53, -13, 27, -39, 110, -83, 19, 112, 
				120, 6, 86, 11, 23, 113, 40, -119, -122, -96, -20, 2, 29, 20, 0, 
				0, 59 };
		return data;
	}

	/**
	 * Codificacion GIF del icono "folder"
	 */
	private static final byte[] folder_image() { 
		byte data[] = {
				71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -62, 0, 0, -128, -128, 
				-128, -64, -64, -64, -1, -1, 0, -1, -1, -1, 0, 0, 0, -1, -1, 
				-1, -1, -1, -1, -1, -1, -1, 33, -2, 14, 77, 97, 100, 101, 32, 
				119, 105, 116, 104, 32, 71, 73, 77, 80, 0, 33, -7, 4, 1, 10, 
				0, 5, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 3, 59, 
				88, -70, -36, 11, 16, 58, 6, -126, -80, 1, -52, 82, -81, -49, -111, 
				-60, 13, 100, 105, -110, 0, 49, 126, -84, -107, -114, 88, 123, -67, -64, 
				32, -57, -12, 16, -17, -77, 90, -33, -98, 28, 111, -105, 3, -70, 124, 
				-95, 100, 72, 85, 32, 56, -97, 80, -24, 102, 74, 117, 36, 0, 0, 
				59 };
		return data;
	}

	/**
	 * Codificacion GIF del icono "hdisk"
	 */
	private static final byte[] hdisk_image() { 
		byte data[] = {
				71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -62, 0, 0, -128, -128, 
				-128, -64, -64, -64, 0, 0, 0, -1, -1, -1, 0, -128, 0, -1, -1, 
				-1, -1, -1, -1, -1, -1, -1, 33, -2, 14, 77, 97, 100, 101, 32, 
				119, 105, 116, 104, 32, 71, 73, 77, 80, 0, 33, -7, 4, 1, 10, 
				0, 5, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 3, 46, 
				88, -70, -36, -2, 48, -54, 9, -86, -67, 86, -127, -64, -69, -25, -128, 
				0, 12, 100, 105, -110, -107, -8, 125, 68, -112, 110, -21, -9, -70, -40, 
				69, -85, -25, 121, -41, 124, -88, 8, -64, -96, 48, 56, 41, 26, -113, 
				9, 0, 59 };
		return data;
	}

	/**
	 * Codificacion GIF del icono "home"
	 */
	private static final byte[] home_image() { 
		byte data[] = {
				71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -62, 0, 0, 0, 0, 
				0, -128, 0, 0, -126, -126, -126, -64, -64, -64, -1, -1, -1, 0, 0, 
				-117, -1, -1, -1, -1, -1, -1, 33, -2, 14, 77, 97, 100, 101, 32, 
				119, 105, 116, 104, 32, 71, 73, 77, 80, 0, 33, -7, 4, 1, 10, 
				0, 6, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 3, 68, 
				104, -70, -36, -2, 112, 1, -96, 102, 4, 33, 80, -5, 112, -42, 92, 
				-29, 9, 66, 22, 74, -103, 48, -84, -90, -104, 14, 4, 1, -73, 21, 
				38, -60, 120, 12, 74, 121, 79, 80, -68, 88, 97, 88, -64, 1, 43, 
				-72, 2, -87, 24, 59, 26, 0, -55, -91, -111, 1, 21, 74, -101, -44, 
				9, 81, 80, -104, -100, 34, -115, 4, 0, 59 };
		return data;
	}

	/**
	 * Codificacion GIF del icono "ofolder"
	 */
	private static final byte[] ofolder_image() { 
		byte data[] = {
				71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -62, 0, 0, -49, -49, 
				-49, 0, 0, 0, -128, -128, -128, -1, -1, -1, -64, -64, -64, -1, -1, 
				0, -1, -1, -1, -1, -1, -1, 33, -2, 14, 77, 97, 100, 101, 32, 
				119, 105, 116, 104, 32, 71, 73, 77, 80, 0, 33, -7, 4, 1, 10, 
				0, 0, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 3, 66, 
				8, -70, -36, 44, 16, -70, 55, 106, 21, 19, -120, 65, 74, 39, 67, 
				20, 41, -101, 103, -126, 86, 24, 104, -36, -23, 18, 66, 32, -50, 98, 
				17, 111, 105, 30, -61, 114, -5, -71, -78, 24, -21, 71, -68, -83, 74, 
				-60, -109, 108, -59, 122, -103, 98, 76, 26, 45, -64, 4, 80, -81, 88, 
				108, 102, -53, 109, 36, 0, 0, 59 };
		return data;
	}

	/**
	 * Codificacion GIF del icono "upfolder"
	 */
	private static final byte[] upfolder_image() { 
		byte data[] = {
				71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -62, 0, 0, -49, -49, 
				-49, 0, 0, 0, -1, -1, -1, -1, -1, 0, -64, -64, -64, -1, -1, 
				-1, -1, -1, -1, -1, -1, -1, 33, -2, 14, 77, 97, 100, 101, 32, 
				119, 105, 116, 104, 32, 71, 73, 77, 80, 0, 33, -7, 4, 1, 10, 
				0, 0, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 3, 58, 
				8, -70, 28, -2, 44, 6, 65, 105, 0, 47, -49, 65, -8, 112, 85, 
				40, 120, 29, 1, -110, 104, -7, 77, 106, 75, -98, -86, -29, -126, -15, 
				-45, -62, 107, 86, 11, -99, 102, 115, -72, 94, 96, -58, 122, 5, 82, 
				-63, 21, -47, -57, 116, 68, 20, -50, -89, 52, 26, 73, 0, 0, 59 };
		return data;
	}

	/**
	 * Codificacion GIF del icono "xfconfig"
	 */
	private static final byte[] xfconfig_image() { 
		byte data[] = {
				71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -124, 0, 0, -128, -128, 
				0, -1, -1, 0, -1, -1, -1, 0, 0, 0, -61, 109, 40, -53, 125, 
				48, -21, -61, -126, -57, -90, 113, -37, -94, 69, -70, 105, 40, -53, 117, 
				56, -61, 89, 4, -94, 121, 60, -21, -78, 93, -70, 89, 24, -82, 69, 
				12, -66, 117, 24, -29, -94, 117, -49, 109, 40, -82, 93, 8, -29, -114, 
				56, -86, 101, 36, -5, -70, 97, -45, 125, 40, -49, -122, 60, -57, 125, 
				40, -61, 105, 28, -29, -102, 81, -37, -122, 65, -13, -78, 97, -37, -94, 
				81, -1, -1, -1, 33, -2, 14, 77, 97, 100, 101, 32, 119, 105, 116, 
				104, 32, 71, 73, 77, 80, 0, 33, -7, 4, 1, 10, 0, 26, 0, 
				44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 5, 110, 32, 81, 24, 
				7, -94, -99, -119, -78, 16, 103, -53, 52, 75, 123, 58, -113, -36, 54, 
				80, -44, 74, -75, -67, 59, 19, 13, 37, -24, -109, 85, 38, -106, 75, 
				-47, 118, -63, 12, 53, -128, -91, -26, 66, -47, 100, 40, -128, 64, -44, 
				-73, -103, 0, -94, 28, -128, 96, 112, -6, -98, 58, -84, -127, 86, 28, 
				16, 124, 3, 100, -49, 37, 17, 28, 8, -32, 3, -11, -40, -106, -67, 
				-109, 79, 122, 90, 45, 121, 108, 121, 122, 0, 121, 124, 90, 119, 110, 
				-126, -118, -120, 98, -112, -114, 45, -120, 100, 89, 100, -124, 82, 95, 127, 
				45, 33, 0, 59 };
		return data;
	}

	//----------------------------------------------------------------------------//
	//                              MIEMBROS PUBLICOS                             //
	//----------------------------------------------------------------------------//

	/**
	 * Icono de los ficheros con extensión ".xfl"
	 */
	public static final ImageIcon xfuzzy = new ImageIcon(xfuzzy_image());
	
	/**
	 * Icono de los ficheros con extensión ".pkg"
	 */
	public static final ImageIcon pkg = new ImageIcon(pkg_image());
	
	/**
	 * Icono del resto de ficheros
	 */
	public static final ImageIcon doc = new ImageIcon(doc_image());
	
	/**
	 * Icono de los directorios
	 */
	public static final ImageIcon folder = new ImageIcon(folder_image());
	
	/**
	 * Icono de los discos
	 */
	public static final ImageIcon hdisk = new ImageIcon(hdisk_image());
	
	/**
	 * Icono del directorio de usuario
	 */
	public static final ImageIcon home = new ImageIcon(home_image());
	
	/**
	 * Icono de directorio abierto
	 */
	public static final ImageIcon ofolder = new ImageIcon(ofolder_image());
	
	/**
	 * Icono de subir un directorio
	 */
	public static final ImageIcon upfolder = new ImageIcon(upfolder_image());
	
	/**
	 * Icono de un fichero de configuración ".xfc"
	 */
	public static final ImageIcon xfconfig = new ImageIcon(xfconfig_image());
}
