/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.graphics.Tile;
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
    
    
    public static Tile[] importDisassembly(String filePath, Color[] palette, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        Tile[] tiles = DisassemblyManager.parseGraphics(filePath, palette, compressed);        
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return tiles;
    }
    
    public static void exportDisassembly(Tile[] tiles, String filePath){
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        DisassemblyManager.produceGraphics(tiles);
        DisassemblyManager.writeFiles(filePath);
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }    
    
    private static Tile[] parseGraphics(String filePath, Color[] palette, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.parseGraphics() - Parsing graphics ...");
        Tile[] tiles = null;       
        try{
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            tiles = UncompressedGraphicsDecoder.decodeUncompressedGraphics(data, palette);
        }catch(Exception e){
             System.err.println("com.sfc.sf2.graphics.io.DisassemblyManager.parseGraphics() - Error while parsing graphics data : "+e);
        } 
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.parseGraphics() - Graphics parsed.");
        return tiles;
    }

    private static void produceGraphics(Tile[] tiles) {
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.produceGraphics() - Producing graphics ...");
        UncompressedGraphicsEncoder.produceGraphics(tiles);
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.produceGraphics() - Graphics produced.");
    }    
  
    private static void writeFiles(String filePath){
        try {
            System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.writeFiles() - Writing file ...");
            Path graphicsFilePath = Paths.get(filePath);
            byte[] newVWFontFileBytes = UncompressedGraphicsEncoder.getNewGraphicsFileBytes();
            Files.write(graphicsFilePath,newVWFontFileBytes);
            System.out.println(newVWFontFileBytes.length + " bytes into " + graphicsFilePath);
            System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.writeFiles() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    
}
