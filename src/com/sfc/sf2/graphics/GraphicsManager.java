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
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importDisassembly() - Importing disassembly ...");
        paletteManager.importDisassembly(paletteFilePath);
        tiles = DisassemblyManager.importDisassembly(graphicsFilePath, paletteManager.getPalette(), compressed);
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String paletteFilePath, String graphicsFilePath, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(tiles, graphicsFilePath);
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importOriginalRom() - Importing original ROM ...");
        paletteManager.importRom(romFilePath, paletteOffset, paletteLength);
        tiles = RomManager.importRom(romFilePath, graphicsOffset, graphicsLength, compressed, paletteManager.getPalette());
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength, boolean compressed){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.exportOriginalRom() - Exporting original ROM ...");
        RomManager.exportRom(tiles, originalRomFilePath, graphicsOffset, compressed);
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.exportOriginalRom() - Original ROM exported.");        
    }      
    
    public void importPng(String filepath){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importPng() - Importing PNG ...");
        tiles = PngManager.importPng(filepath);
        paletteManager.setPalette(tiles[0].getPalette());
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String filepath, String tilesPerRow){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(tiles, filepath, tilesPerRow);
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.exportPng() - PNG exported.");       
    }
}
