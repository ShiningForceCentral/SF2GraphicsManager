/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compressed.BasicGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.BasicGraphicsEncoder;
import com.sfc.sf2.graphics.uncompressed.UncompressedGraphicsDecoder;
import com.sfc.sf2.graphics.uncompressed.UncompressedGraphicsEncoder;
import java.awt.Color;
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

    private static final Logger LOG = Logger.getLogger(DisassemblyManager.class.getName());
    
    public static Tile[] importDisassembly(String filePath, Color[] palette, int compression){
        LOG.entering(LOG.getName(),"importDisassembly");
        Tile[] tiles = DisassemblyManager.parseGraphics(filePath, palette, compression);        
        LOG.exiting(LOG.getName(),"importDisassembly");
        return tiles;
    }
    
    public static void exportDisassembly(Tile[] tiles, String filePath, int compression){
        LOG.entering(LOG.getName(),"exportDisassembly");
        DisassemblyManager.produceGraphics(tiles, compression);
        DisassemblyManager.writeFiles(filePath, compression);
        LOG.exiting(LOG.getName(),"exportDisassembly");        
    }    
    
    private static Tile[] parseGraphics(String filePath, Color[] palette, int compression){
        LOG.entering(LOG.getName(),"parseGraphics");
        Tile[] tiles = null;       
        try{
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            switch(compression){
                case GraphicsManager.COMPRESSION_NONE:
                    tiles = UncompressedGraphicsDecoder.decodeUncompressedGraphics(data, palette);
                    break;
                case GraphicsManager.COMPRESSION_BASIC:
                    tiles = BasicGraphicsDecoder.decodeBasicGraphics(data, palette);
                    break;
                
            }
        }catch(Exception e){
             LOG.throwing(LOG.getName(),"parseGraphics", e);
        } 
        LOG.exiting(LOG.getName(),"parseGraphics");
        return tiles;
    }

    private static void produceGraphics(Tile[] tiles, int compression) {
        LOG.entering(LOG.getName(),"produceGraphics");
        switch(compression){
            case GraphicsManager.COMPRESSION_NONE:
                UncompressedGraphicsEncoder.produceGraphics(tiles);
                break;
            case GraphicsManager.COMPRESSION_BASIC:
                BasicGraphicsEncoder.produceGraphics(tiles);
                break;
        } 
        LOG.exiting(LOG.getName(),"produceGraphics");
    }    
  
    private static void writeFiles(String filePath, int compression){
        try {
            LOG.entering(LOG.getName(),"writeFiles");
            Path graphicsFilePath = Paths.get(filePath);
            byte[] newGraphicsFileBytes = null;
            switch(compression){
                case GraphicsManager.COMPRESSION_NONE:
                    newGraphicsFileBytes = UncompressedGraphicsEncoder.getNewGraphicsFileBytes(); 
                    break;
                case GraphicsManager.COMPRESSION_BASIC:
                    newGraphicsFileBytes = BasicGraphicsEncoder.getNewGraphicsFileBytes(); 
                    break;
            }
            Files.write(graphicsFilePath,newGraphicsFileBytes);
            LOG.info(newGraphicsFileBytes.length + " bytes into " + graphicsFilePath);
            LOG.entering(LOG.getName(),"writeFiles");
        } catch (IOException ex) {
            LOG.throwing(LOG.getName(),"writeFiles", ex);
        }
    }    

    
}
