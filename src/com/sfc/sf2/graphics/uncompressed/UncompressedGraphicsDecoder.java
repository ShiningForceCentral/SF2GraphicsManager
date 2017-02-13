/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.uncompressed;

import com.sfc.sf2.graphics.Tile;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class UncompressedGraphicsDecoder {
    
    public static Tile[] decodeUncompressedGraphics(byte[] data, Color[] palette){
        System.out.println("com.sfc.sf2.graphics.UncompressedGraphicsDecoder.decodeUncompressedGraphics() - Decoding uncompressed graphics ...");
        System.out.println("com.sfc.sf2.graphics.UncompressedGraphicsDecoder.decodeUncompressedGraphics() - Data length = " + data.length + ", -> expecting " + data.length/32 + " tiles to parse.");
        Tile[] tiles = new Tile[data.length/32];
        for(int i=0;i<tiles.length;i++){
            Tile tile = new Tile();
            tile.setId(i);
            tile.setPalette(palette);
            for(int y=0;y<8;y++){
                for(int x=0;x<8;x+=2){
                    byte currentByte = data[i*32+(y*8+x)/2];
                    int firstPixel = (currentByte & 0xF0)/16;
                    int secondPixel = currentByte & 0x0F;
                    tile.setPixel(x, y, firstPixel);
                    tile.setPixel(x+1, y, secondPixel);
                }
            }
            System.out.println(tile);
            tiles[i] = tile;
        }
        System.out.println("com.sfc.sf2.graphics.UncompressedGraphicsDecoder.decodeUncompressedGraphics() - Uncompressed graphics decoded.");
        return tiles;
    }
    
}
