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
        byte[] data = new byte[tiles.length*32];
        for(int i=0;i<tiles.length;i++){
            int[][] pixels = tiles[i].getPixels();
            for(int y=0;y<8;y++){
                for(int x=0;x<8;x+=2){
                    byte first = (byte)pixels[x][y];
                    byte second = (byte)pixels[x+1][y];
                    data[(i*64+y*8+x)/2] = (byte)(first*16 | second);
                }
            }
        }
        System.out.println("com.sfc.sf2.graphics.UncompressedGraphicsEncoder.produceGraphics() - Graphics produced.");
        newGraphicsFileBytes = data;
    }
    
    public static byte[] getNewGraphicsFileBytes(){
        return newGraphicsFileBytes;
    }
}
