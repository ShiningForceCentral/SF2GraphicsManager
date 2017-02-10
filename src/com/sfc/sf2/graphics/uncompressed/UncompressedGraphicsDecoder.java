/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.uncompressed;

/**
 *
 * @author wiz
 */
public class UncompressedGraphicsDecoder {
    
    public static byte[][] parseVWFont(byte[] data){
        System.out.println("com.sfc.sf2.vwfont.graphics.VWFontDecoder.parseVWFont() - Parsing VW Font ...");
        byte[][] vwfontChars = new byte[data.length/32][];
        for(int i=0;i<(data.length/32);i++){
            byte[] character = new byte[32];
            System.arraycopy(data, i*32, character, 0, 32);
            vwfontChars[i] = character;
        }
        System.out.println("com.sfc.sf2.vwfont.graphics.VWFontDecoder.parseVWFont() - VW Font parsed.");
        return vwfontChars;
    }
    
}
