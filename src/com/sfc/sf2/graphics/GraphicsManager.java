/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics;

import com.sfc.sf2.graphics.io.DisassemblyManager;
import com.sfc.sf2.graphics.io.PngManager;
import com.sfc.sf2.graphics.io.RomManager;
import com.sfc.sf2.palette.PaletteManager;

/**
 *
 * @author wiz
 */
public class GraphicsManager {
       
    private PaletteManager paletteManager = new PaletteManager();
    private byte[] graphics;

    public byte[] getGraphics() {
        return graphics;
    }

    public void setGraphics(byte[] graphics) {
        this.graphics = graphics;
    }
       
    public void importDisassembly(String paletteFilePath, String graphicsFilePath, boolean compressed){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Importing disassembly ...");
        paletteManager.importDisassembly(paletteFilePath);
        graphics = DisassemblyManager.importDisassembly(graphicsFilePath, compressed);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String paletteFilePath, String graphicsFilePath, boolean compressed){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Exporting disassembly ...");
        paletteManager.exportDisassembly(paletteFilePath, paletteManager.getPalette());
        DisassemblyManager.exportDisassembly(graphics, graphicsFilePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength, boolean compressed){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importOriginalRom() - Importing original ROM ...");
        paletteManager.importRom(romFilePath, paletteOffset, paletteLength);
        graphics = RomManager.importRom(RomManager.ORIGINAL_ROM_TYPE,romFilePath, compressed);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength, boolean compressed){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportOriginalRom() - Exporting original ROM ...");
        RomManager.exportRom(RomManager.ORIGINAL_ROM_TYPE, graphics, originalRomFilePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportOriginalRom() - Original ROM exported.");        
    }      
    
    public void importPng(String filepath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importPng() - Importing PNG ...");
        graphics = PngManager.importPng(filepath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String filepath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(graphics, filepath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportPng() - PNG exported.");       
    }
}
