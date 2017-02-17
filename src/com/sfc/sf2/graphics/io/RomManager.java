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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class RomManager {
    
    private static File romFile;  
    private static byte[] romData;
    
    public static Tile[] importRom(String romFilePath, String graphicsOffset, String graphicsLength, boolean compressed, Color[] palette){
        System.out.println("com.sfc.sf2.graphics.io.RomManager.importRom() - Importing ROM ...");
        RomManager.openFile(romFilePath);
        int offset = Integer.parseInt(graphicsOffset,16);
        int length = Integer.parseInt(graphicsLength);
        Tile[] tiles = RomManager.parseGraphics(offset,length,compressed, palette);        
        System.out.println("com.sfc.sf2.graphics.io.RomManager.importRom() - ROM imported.");
        return tiles;
    }
    
    public static void exportRom(Tile[] tiles, String romFilePath, String graphicsOffset, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.io.RomManager.exportRom() - Exporting ROM ...");
        RomManager.produceGraphics(tiles);
        int offset = Integer.parseInt(graphicsOffset,16);
        RomManager.writeFile(romFilePath, offset);
        System.out.println("com.sfc.sf2.graphics.io.RomManager.exportRom() - ROM exported.");        
    }    
    
    private static void openFile(String romFilePath){
        try {
            System.out.println("com.sfc.sf2.graphics.io.RomManager.openFile() - ROM file path : " + romFilePath);
            romFile = new File(romFilePath);
            romData = Files.readAllBytes(Paths.get(romFile.getAbsolutePath()));
            System.out.println("com.sfc.sf2.graphics.io.RomManager.openFile() - File opened.");
        } catch (IOException ex) {
            Logger.getLogger(RomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Tile[] parseGraphics(int graphicsOffset, int graphicsLength, boolean compressed, Color[] palette){
        System.out.println("com.sfc.sf2.graphics.io.RomManager.parseGraphics() - Parsing Graphics ...");
        byte[] data = Arrays.copyOfRange(romData,graphicsOffset,graphicsOffset+graphicsLength);        
        Tile[] tiles = UncompressedGraphicsDecoder.decodeUncompressedGraphics(data, palette);
        System.out.println("com.sfc.sf2.graphics.io.RomManager.parseGraphics() - Graphics parsed.");
        return tiles;
    }

    private static void produceGraphics(Tile[] tiles) {
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.produceGraphics() - Producing Graphics ...");
        UncompressedGraphicsEncoder.produceGraphics(tiles);
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.produceGraphics() - Graphics produced.");
    }    
  
    private static void writeFile(String romFilePath, int offset){
        try {
            System.out.println("com.sfc.sf2.graphics.io.RomManager.writeFile() - Writing file ...");
            romFile = new File(romFilePath);
            Path romPath = Paths.get(romFile.getAbsolutePath());
            romData = Files.readAllBytes(romPath);
            byte[] newGraphicsFileBytes = UncompressedGraphicsEncoder.getNewGraphicsFileBytes();
            System.arraycopy(newGraphicsFileBytes, 0, romData, offset, newGraphicsFileBytes.length);
            Files.write(romPath,romData);
            System.out.println(romData.length + " bytes into " + romFilePath);  
            System.out.println("com.sfc.sf2.graphics.io.RomManager.writeFile() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(RomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
