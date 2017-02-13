/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.uncompressed.UncompressedGraphicsDecoder;
import com.sfc.sf2.graphics.uncompressed.UncompressedGraphicsEncoder;
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
    
    public static final int ORIGINAL_ROM_TYPE = 0;
    public static final int CARAVAN_ROM_TYPE = 1;
    
    private static final int[][] VWFONT_OFFSETS = {   {0x29002,0x29A02},
                                                            {0x29002,0x29A02}
                                                        };
    
    private static File romFile;  
    private static byte[] romData;
    
    public static Tile[] importRom(int romType, String romFilePath, String graphicsOffset, String graphicsLength, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.io.RomManager.importRom() - Importing ROM ...");
        RomManager.openFile(romFilePath);
        Tile[] tiles = RomManager.parseGraphics(romType);        
        System.out.println("com.sfc.sf2.graphics.io.RomManager.importRom() - ROM imported.");
        return tiles;
    }
    
    public static void exportRom(int romType, Tile[] tiles, String romFilePath, String graphicsOffset, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.io.RomManager.exportRom() - Exporting ROM ...");
        RomManager.produceGraphics(tiles);
        RomManager.writeFile(romType, romFilePath);
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
    
    private static Tile[] parseGraphics(int romType){
        System.out.println("com.sfc.sf2.graphics.io.RomManager.parseGraphics() - Parsing Graphics ...");
        Tile[] tiles = null;
        //byte[] data = Arrays.copyOfRange(romData,VWFONT_OFFSETS[romType][0],VWFONT_OFFSETS[romType][1]);        
        //byte[][] graphicsChars = UncompressedGraphicsDecoder.parseVWFont(data);
        System.out.println("com.sfc.sf2.graphics.io.RomManager.parseGraphics() - Graphics parsed.");
        return tiles;
    }

    private static void produceGraphics(Tile[] tiles) {
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.produceGraphics() - Producing Graphics ...");
        UncompressedGraphicsEncoder.produceGraphics(tiles);
        System.out.println("com.sfc.sf2.graphics.io.DisassemblyManager.produceGraphics() - Graphics produced.");
    }    
  
    private static void writeFile(int romType, String romFilePath){
        try {
            System.out.println("com.sfc.sf2.graphics.io.RomManager.writeFile() - Writing file ...");
            romFile = new File(romFilePath);
            Path romPath = Paths.get(romFile.getAbsolutePath());
            romData = Files.readAllBytes(romPath);
            byte[] newVWFontFileBytes = UncompressedGraphicsEncoder.getNewGraphicsFileBytes();
            System.arraycopy(newVWFontFileBytes, 0, romData, VWFONT_OFFSETS[romType][0], newVWFontFileBytes.length);
            Files.write(romPath,romData);
            System.out.println(romData.length + " bytes into " + romFilePath);  
            System.out.println("com.sfc.sf2.graphics.io.RomManager.writeFile() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(RomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
