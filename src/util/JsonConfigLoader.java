package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.*;

/**
 * A class to load resources from a json file using 
 * the Google's Gson lib.
 * 
 *  This is a good way to get for example users data to be used
 *  as inital data, so that they can be left outside the code.
 *  
 * @author felipe.pontes
 *
 */
public class JsonConfigLoader {
	
	
	/**
	 * Return a given json string for the filepath
	 * @param filepath
	 * @return
	 */
	public static String getJson(String filepath){

		String json = "";
		try {
			json = readFile(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return json;
	}

	/**
	 * Retorna uma grande string com o conteudo do arquivo desejado.
	 * Pode jogar uma IOException caso de erro ao tentar ler o arquivo.
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	private static String readFile( String filepath ) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (filepath));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}
	
}
