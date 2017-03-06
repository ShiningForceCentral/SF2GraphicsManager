/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compressed.BasicGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.BasicGraphicsEncoder;
import com.sfc.sf2.graphics.uncompressed.UncompressedGraphicsDecoder;
import com.sfc.sf2.graphics.uncompressed.UncompressedGraphicsEncoder;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class DisassemblyManager {
    
    private static final int COMPRESSION_NONE = 0;
    private static final int COMPRESSION_BASIC = 1;
    private static final int COMPRESSION_STACK = 2;
    
    public static Tile[] importDisassembly(String filePath, Color[] palette, int compression){
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        Tile[] tiles = DisassemblyManager.parseGraphics(filePath, palette, compression);        
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return tiles;
    }
    
    public static void exportDisassembly(Tile[] tiles, String filePath, int compression){
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        DisassemblyManager.produceGraphics(tiles, compression);
        DisassemblyManager.writeFiles(filePath, compression);
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }    
    
    private static Tile[] parseGraphics(String filePath, Color[] palette, int compression){
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.parseGraphics() - Parsing graphics ...");
        Tile[] tiles = null;       
        try{
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            switch(compression){
                case COMPRESSION_NONE:
                    tiles = UncompressedGraphicsDecoder.decodeUncompressedGraphics(data, palette);
                    break;
                case COMPRESSION_BASIC:
                    tiles = BasicGraphicsDecoder.decodeBasicGraphics(data, palette);
                    break;
                
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.graphics.io.DisassemblyManager.parseGraphics() - Error while parsing graphics data : "+e);
        } 
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.parseGraphics() - Graphics parsed.");
        return tiles;
    }

    private static void produceGraphics(Tile[] tiles, int compression) {
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.produceGraphics() - Producing graphics ...");
        switch(compression){
            case COMPRESSION_NONE:
                UncompressedGraphicsEncoder.produceGraphics(tiles);
                break;
            case COMPRESSION_BASIC:
                BasicGraphicsEncoder.produceGraphics(tiles);
                break;
        } 
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.produceGraphics() - Graphics produced.");
    }    
  
    private static void writeFiles(String filePath, int compression){
        try {
            System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.writeFiles() - Writing file ...");
            Path graphicsFilePath = Paths.get(filePath);
            byte[] newGraphicsFileBytes = null;
            switch(compression){
                case COMPRESSION_NONE:
                    newGraphicsFileBytes = UncompressedGraphicsEncoder.getNewGraphicsFileBytes(); 
                    break;
                case COMPRESSION_BASIC:
                    newGraphicsFileBytes = BasicGraphicsEncoder.getNewGraphicsFileBytes(); 
                    break;
            }
            Files.write(graphicsFilePath,newGraphicsFileBytes);
            System.out.println(newGraphicsFileBytes.length + " bytes into " + graphicsFilePath);
            System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.writeFiles() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    
}
