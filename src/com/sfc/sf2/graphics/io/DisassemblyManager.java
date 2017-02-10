/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.graphics.uncompressed.UncompressedGraphicsDecoder;
import com.sfc.sf2.graphics.uncompressed.UncompressedGraphicsEncoder;
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
    
    
    public static byte[][] importDisassembly(String filePath){
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.importDisassembly() - Importing disassembly ...");
        byte[][] vwfontChars = DisassemblyManager.parseVWFont(filePath);        
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.importDisassembly() - Disassembly imported.");
        return vwfontChars;
    }
    
    public static void exportDisassembly(byte[][] vwfontChars, String filePath){
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        DisassemblyManager.produceVWFont(vwfontChars);
        DisassemblyManager.writeFiles(filePath);
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }    
    
    private static byte[][] parseVWFont(String filePath){
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.parseTextbank() - Parsing VW Font ...");
        byte[][] vwfontChars = null;       
        try{
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            vwfontChars = UncompressedGraphicsDecoder.parseVWFont(data);
        }catch(Exception e){
             System.err.println("com.sfc.sf2.vwfont.ioDisassemblyManager.parseTextbank() - Error while parsing VW Font data : "+e);
        } 
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.parseTextbank() - VW Font parsed.");
        return vwfontChars;
    }

    private static void produceVWFont(byte[][] vwfontChars) {
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.produceTextbanks() - Producing text banks ...");
        UncompressedGraphicsEncoder.produceVWFont(vwfontChars);
        System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.produceTextbanks() - Text banks produced.");
    }    
  
    private static void writeFiles(String filePath){
        try {
            System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.writeFiles() - Writing file ...");
            Path vwfontFilePath = Paths.get(filePath);
            byte[] newVWFontFileBytes = UncompressedGraphicsEncoder.getNewVWFontFileBytes();
            Files.write(vwfontFilePath,newVWFontFileBytes);
            System.out.println(newVWFontFileBytes.length + " bytes into " + vwfontFilePath);
            System.out.println("com.sfc.sf2.vwfont.ioDisassemblyManager.writeFiles() - File written.");
        } catch (IOException ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    
}
