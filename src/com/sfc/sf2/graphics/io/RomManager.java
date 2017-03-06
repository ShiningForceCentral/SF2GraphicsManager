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
    
    private static final int COMPRESSION_NONE = 0;
    private static final int COMPRESSION_BASIC = 1;
    private static final int COMPRESSION_STACK = 2;
    
    private static File romFile;  
    private static byte[] romData;
    
    public static Tile[] importRom(String romFilePath, String graphicsOffset, String graphicsLength, int compression, Color[] palette){
        System.out.println("com.sfc.sf2.graphics.io.RomManager.importRom() - Importing ROM ...");
        RomManager.openFile(romFilePath);
        int offset = Integer.parseInt(graphicsOffset,16);
        int length = Integer.parseInt(graphicsLength);
        Tile[] tiles = RomManager.parseGraphics(offset,length,compression, palette);        
        System.out.println("com.sfc.sf2.graphics.io.RomManager.importRom() - ROM imported.");
        return tiles;
    }
    
    public static void exportRom(Tile[] tiles, String romFilePath, String graphicsOffset, int compression){
        System.out.println("com.sfc.sf2.graphics.io.RomManager.exportRom() - Exporting ROM ...");
        RomManager.produceGraphics(tiles, compression);
        int offset = Integer.parseInt(graphicsOffset,16);
        RomManager.writeFile(romFilePath, offset, compression);
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
    
    private static Tile[] parseGraphics(int graphicsOffset, int graphicsLength, int compression, Color[] palette){
        System.out.println("com.sfc.sf2.graphics.io.RomManager.parseGraphics() - Parsing Graphics ...");
        byte[] data = Arrays.copyOfRange(romData,graphicsOffset,graphicsOffset+graphicsLength);        
        Tile[] tiles = null;
        switch(compression){
            case COMPRESSION_NONE:
                tiles = UncompressedGraphicsDecoder.decodeUncompressedGraphics(data, palette);
                break;
            case COMPRESSION_BASIC:
                tiles = BasicGraphicsDecoder.decodeBasicGraphics(data, palette);
                break;

        }
        System.out.println("com.sfc.sf2.graphics.io.RomManager.parseGraphics() - Graphics parsed.");
        return tiles;
    }

    private static void produceGraphics(Tile[] tiles, int compression) {
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.produceGraphics() - Producing Graphics ...");
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
  
    private static void writeFile(String romFilePath, int offset, int compression){
        try {
            System.out.println("com.sfc.sf2.graphics.io.RomManager.writeFile() - Writing file ...");
            romFile = new File(romFilePath);
            Path romPath = Paths.get(romFile.getAbsolutePath());
            romData = Files.readAllBytes(romPath);
            byte[] newGraphicsFileBytes = null;
            switch(compression){
                case COMPRESSION_NONE:
                    newGraphicsFileBytes = UncompressedGraphicsEncoder.getNewGraphicsFileBytes(); 
                    break;
                case COMPRESSION_BASIC:
                    newGraphicsFileBytes = BasicGraphicsEncoder.getNewGraphicsFileBytes(); 
                    break;
            }
            System.arraycopy(newGraphicsFileBytes, 0, romData, offset, newGraphicsFileBytes.length);
            Files.write(romPath,romData);
            System.out.println(romData.length + " bytes into " + romFilePath);  
            System.out.println("com.sfc.sf2.graphics.io.RomManager.writeFile() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(RomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
