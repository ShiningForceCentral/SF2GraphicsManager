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
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class GraphicsManager {
    
    private static final Logger LOG = Logger.getLogger(GraphicsManager.class.getName());
    
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
        LOG.entering(LOG.getName(),"importDisassembly");
        LOG.info("info");
        LOG.fine("fine");
        LOG.finer("finer");
        LOG.finest("finest");
        paletteManager.importDisassembly(paletteFilePath);
        Color[] palette = paletteManager.getPalette();
        palette[0] = new Color(255, 255, 255, 0);
        tiles = DisassemblyManager.importDisassembly(graphicsFilePath, palette, compression);
        LOG.exiting(LOG.getName(),"importDisassembly");
    }
    
    public void exportDisassembly(String graphicsFilePath, int compression){
        LOG.entering(LOG.getName(),"importDisassembly");
        DisassemblyManager.exportDisassembly(tiles, graphicsFilePath, compression);
        LOG.exiting(LOG.getName(),"importDisassembly");        
    }   
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength, int compression){
        LOG.entering(LOG.getName(),"importOriginalRom");
        paletteManager.importRom(romFilePath, paletteOffset, paletteLength);
        Color[] palette = paletteManager.getPalette();
        palette[0] = new Color(255, 255, 255, 0);
        tiles = RomManager.importRom(romFilePath, graphicsOffset, graphicsLength, compression, palette);
        LOG.exiting(LOG.getName(),"importOriginalRom");
    }
    
    public void exportRom(String originalRomFilePath, String graphicsOffset, int compression){
        LOG.entering(LOG.getName(),"exportOriginalRom");
        RomManager.exportRom(tiles, originalRomFilePath, graphicsOffset, compression);
        LOG.exiting(LOG.getName(),"exportOriginalRom");        
    }      
    
    public int importPng(String filepath){
        LOG.entering(LOG.getName(),"importPng");
        tiles = PngManager.importPng(filepath);
        paletteManager.setPalette(tiles[0].getPalette());
        int tileWidth = PngManager.getImportedPngTileWidth();
        LOG.exiting(LOG.getName(),"importPng");
        return tileWidth;
    }
    
    public void exportPng(String filepath, String tilesPerRow){
        LOG.entering(LOG.getName(),"exportPng");
        PngManager.exportPng(tiles, filepath, tilesPerRow);
        LOG.exiting(LOG.getName(),"exportPng");       
    }

}
