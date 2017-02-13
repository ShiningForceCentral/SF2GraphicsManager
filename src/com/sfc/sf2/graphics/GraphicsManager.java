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
    private Tile[] tiles;

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
       
    public void importDisassembly(String paletteFilePath, String graphicsFilePath, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerimportDisassembly() - Importing disassembly ...");
        paletteManager.importDisassembly(paletteFilePath);
        tiles = DisassemblyManager.importDisassembly(graphicsFilePath, paletteManager.getPalette(), compressed);
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerimportDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String paletteFilePath, String graphicsFilePath, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerimportDisassembly() - Exporting disassembly ...");
        paletteManager.exportDisassembly(paletteFilePath, paletteManager.getPalette());
        DisassemblyManager.exportDisassembly(tiles, graphicsFilePath);
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerimportDisassembly() - Disassembly exported.");        
    }   
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerimportOriginalRom() - Importing original ROM ...");
        paletteManager.importRom(romFilePath, paletteOffset, paletteLength);
        tiles = RomManager.importRom(RomManager.ORIGINAL_ROM_TYPE,romFilePath, graphicsOffset, graphicsLength, compressed);
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerimportOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerexportOriginalRom() - Exporting original ROM ...");
        RomManager.exportRom(RomManager.ORIGINAL_ROM_TYPE, tiles, originalRomFilePath, graphicsOffset, compressed);
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerexportOriginalRom() - Original ROM exported.");        
    }      
    
    public void importPng(String filepath){
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerimportPng() - Importing PNG ...");
        tiles = PngManager.importPng(filepath);
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerimportPng() - PNG imported.");
    }
    
    public void exportPng(String filepath){
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerexportPng() - Exporting PNG ...");
        PngManager.exportPng(tiles, filepath);
        System.out.println("com.sfc.sf2.graphics.GraphicsManagerexportPng() - PNG exported.");       
    }
}
