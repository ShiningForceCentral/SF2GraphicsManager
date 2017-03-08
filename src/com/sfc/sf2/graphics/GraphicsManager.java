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
import java.awt.Color;

/**
 *
 * @author wiz
 */
public class GraphicsManager {
    
    public static final int COMPRESSION_NONE = 0;
    public static final int COMPRESSION_BASIC = 1;
    public static final int COMPRESSION_STACK = 2;    
       
    private PaletteManager paletteManager = new PaletteManager();
    private Tile[] tiles;

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
       
    public void importDisassembly(String paletteFilePath, String graphicsFilePath, int compression){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importDisassembly() - Importing disassembly ...");
        paletteManager.importDisassembly(paletteFilePath);
        Color[] palette = paletteManager.getPalette();
        palette[0] = new Color(255, 255, 255, 0);
        tiles = DisassemblyManager.importDisassembly(graphicsFilePath, palette, compression);
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String graphicsFilePath, int compression){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(tiles, graphicsFilePath, compression);
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength, int compression){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importOriginalRom() - Importing original ROM ...");
        paletteManager.importRom(romFilePath, paletteOffset, paletteLength);
        Color[] palette = paletteManager.getPalette();
        palette[0] = new Color(255, 255, 255, 0);
        tiles = RomManager.importRom(romFilePath, graphicsOffset, graphicsLength, compression, palette);
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String graphicsOffset, int compression){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.exportOriginalRom() - Exporting original ROM ...");
        RomManager.exportRom(tiles, originalRomFilePath, graphicsOffset, compression);
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.exportOriginalRom() - Original ROM exported.");        
    }      
    
    public int importPng(String filepath){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importPng() - Importing PNG ...");
        tiles = PngManager.importPng(filepath);
        paletteManager.setPalette(tiles[0].getPalette());
        int tileWidth = PngManager.getImportedPngTileWidth();
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.importPng() - PNG imported.");
        return tileWidth;
    }
    
    public void exportPng(String filepath, String tilesPerRow){
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(tiles, filepath, tilesPerRow);
        System.out.println("com.sfc.sf2.graphics.GraphicsManager.exportPng() - PNG exported.");       
    }
}
