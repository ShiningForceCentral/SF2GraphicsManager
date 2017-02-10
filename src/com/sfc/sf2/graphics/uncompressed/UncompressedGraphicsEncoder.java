/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.uncompressed;

import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class UncompressedGraphicsEncoder {
    
    private static byte[] newVWFontFileBytes;
    
    public static void produceVWFont(byte[][] vwfontChars){
        System.out.println("com.sfc.sf2.vwfont.graphics.VWFontEncoder.produceVWFont() - Producing VWFont ...");
        byte[] data = new byte[0];
        for(byte[] vwfontChar : vwfontChars){
            int previousLength = data.length;
            data = Arrays.copyOf(data,data.length+32);
            System.arraycopy(vwfontChar, 0, data, previousLength, vwfontChar.length);
        }
        System.out.println("com.sfc.sf2.vwfont.graphics.VWFontEncoder.produceVWFont() - VWFont produced.");
        newVWFontFileBytes = data;
    }
    
    public static byte[] getNewVWFontFileBytes(){
        return newVWFontFileBytes;
    }
}
