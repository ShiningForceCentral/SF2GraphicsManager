/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

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
    
    public static byte[][] importRom(int romType, String romFilePath){
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.importRom() - Importing ROM ...");
        RomManager.openFile(romFilePath);
        byte[][] vwfontChars = RomManager.parseVWFont(romType);        
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.importRom() - ROM imported.");
        return vwfontChars;
    }
    
    public static void exportRom(int romType, byte[][] vwfontChars, String romFilePath){
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.exportRom() - Exporting ROM ...");
        RomManager.produceVWFont(vwfontChars);
        RomManager.writeFile(romType, romFilePath);
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.exportRom() - ROM exported.");        
    }    
    
    private static void openFile(String romFilePath){
        try {
            System.out.println("com.sfc.sf2.vwfont.io.RomManager.openFile() - ROM file path : " + romFilePath);
            romFile = new File(romFilePath);
            romData = Files.readAllBytes(Paths.get(romFile.getAbsolutePath()));
            System.out.println("com.sfc.sf2.vwfont.io.RomManager.openFile() - File opened.");
        } catch (IOException ex) {
            Logger.getLogger(RomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static byte[][] parseVWFont(int romType){
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.parseVWFont() - Parsing VW Font ...");
        byte[] data = Arrays.copyOfRange(romData,VWFONT_OFFSETS[romType][0],VWFONT_OFFSETS[romType][1]);        
        byte[][] vwfontChars = UncompressedGraphicsDecoder.parseVWFont(data);
        System.out.println("com.sfc.sf2.vwfont.io.RomManager.parseVWFont() - VW Font parsed.");
        return vwfontChars;
    }

    private static void produceVWFont(byte[][] vwfontChars) {
        System.out.println("com.sfc.sf2.vwfont.io.DisassemblyManager.produceVWFont() - Producing VW Font ...");
        UncompressedGraphicsEncoder.produceVWFont(vwfontChars);
        System.out.println("com.sfc.sf2.vwfont.io.DisassemblyManager.produceVWFont() - VW Font produced.");
    }    
  
    private static void writeFile(int romType, String romFilePath){
        try {
            System.out.println("com.sfc.sf2.vwfont.io.RomManager.writeFile() - Writing file ...");
            romFile = new File(romFilePath);
            Path romPath = Paths.get(romFile.getAbsolutePath());
            romData = Files.readAllBytes(romPath);
            byte[] newVWFontFileBytes = UncompressedGraphicsEncoder.getNewVWFontFileBytes();
            System.arraycopy(newVWFontFileBytes, 0, romData, VWFONT_OFFSETS[romType][0], newVWFontFileBytes.length);
            Files.write(romPath,romData);
            System.out.println(romData.length + " bytes into " + romFilePath);  
            System.out.println("com.sfc.sf2.vwfont.io.RomManager.writeFile() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(RomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
