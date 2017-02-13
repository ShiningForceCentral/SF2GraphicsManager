/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.uncompressed;

import com.sfc.sf2.graphics.Tile;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class UncompressedGraphicsEncoder {
    
    private static byte[] newGraphicsFileBytes;
    
    public static void produceGraphics(Tile[] tiles){
        System.out.println("com.sfc.sf2.graphics.UncompressedGraphicsEncoder.produceGraphics() - Producing Graphics ...");
        byte[] data = new byte[0];
        for(Tile tile : tiles){
            int previousLength = data.length;
            data = Arrays.copyOf(data,data.length+32);
            //System.arraycopy(vwfontChar, 0, data, previousLength, vwfontChar.length);
        }
        System.out.println("com.sfc.sf2.graphics.UncompressedGraphicsEncoder.produceGraphics() - Graphics produced.");
        newGraphicsFileBytes = data;
    }
    
    public static byte[] getNewGraphicsFileBytes(){
        return newGraphicsFileBytes;
    }
}
