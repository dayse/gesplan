 
    /*
    *
    * Copyright (c) 2013 - 2014 INT - National Institute of Technology & COPPE - Alberto Luiz Coimbra Institute 
- Graduate School and Research in Engineering.
    * See the file license.txt for copyright permission.
    *
    */
        
     
package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

public class Digesto {
    
    private static String algoritmo = "MD5";

    private Digesto() {
    }
    
    public synchronized static String gerarDigesto( String chave ) {
        if (chave == null) throw new NullPointerException("chave nao pode ser nula no digesto");
        return stringHexa( gerarHash( chave.toString() ) );
    }
    
    public static String gerarDigestoSHA1(String chave) {
        return DigestUtils.shaHex(chave);
    }

    public static String gerarDigestoMD5Correto(String chave) {
        return DigestUtils.md5Hex(chave);
    }
    
    private synchronized static byte[] gerarHash( String frase ) {
        try {
            MessageDigest md = MessageDigest.getInstance( algoritmo );
            md.update( frase.getBytes() );
            return md.digest();
        } catch ( NoSuchAlgorithmException e ) {
            return null;
        }
    }
    
    private synchronized static String stringHexa( byte[] bytes ) {
        String s = new String();
        for (int i = 0; i < bytes.length; i++) {
            s += Integer.toHexString((((bytes[i] >> 4) & 0xf) << 4)
            | (bytes[i] & 0xf));
        }
        return s;
    }
}
