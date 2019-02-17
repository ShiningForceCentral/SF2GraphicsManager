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
import com.sfc.sf2.graphics.compressed.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.StackGraphicsEncoder;
import com.sfc.sf2.graphics.uncompressed.UncompressedGraphicsDecoder;
import com.sfc.sf2.graphics.uncompressed.UncompressedGraphicsEncoder;
import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
                case GraphicsManager.COMPRESSION_STACK:
                    tiles = new StackGraphicsDecoder().decodeStackGraphics(data, palette);
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
            case GraphicsManager.COMPRESSION_STACK:
                StackGraphicsEncoder.produceGraphics(tiles);
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
                case GraphicsManager.COMPRESSION_STACK:
                    newGraphicsFileBytes = StackGraphicsEncoder.getNewGraphicsFileBytes(); 
                    break;
            }
            Files.write(graphicsFilePath,newGraphicsFileBytes);
            LOG.info(newGraphicsFileBytes.length + " bytes into " + graphicsFilePath);
            LOG.exiting(LOG.getName(),"writeFiles");
        } catch (IOException ex) {
            LOG.throwing(LOG.getName(),"writeFiles", ex);
        }
    }    

    public static Tile[] importDisassemblyWithLayout(String baseTilesetPath, Color[][] palettes, String tileset1FilePath, String tileset1Offset, String tileset2FilePath, String tileset2Offset, int compression, String layoutPath){
        LOG.entering(LOG.getName(),"importDisassemblyWithLayout");
        Tile[] baseTiles = DisassemblyManager.parseGraphics(baseTilesetPath, palettes[0], GraphicsManager.COMPRESSION_STACK); 
        Tile[] tileset1 = DisassemblyManager.parseGraphics(tileset1FilePath, palettes[0], compression); 
        Tile[] tileset2 = DisassemblyManager.parseGraphics(tileset2FilePath, palettes[0], compression); 
        Tile[] vRamTiles = new Tile[0x800];
        Tile[] tiles = null;
        int t1offset = Integer.valueOf(tileset1Offset, 16) / 0x20;
        int t2offset = Integer.valueOf(tileset2Offset, 16) / 0x20;
        System.arraycopy(baseTiles, 0, vRamTiles, 0, baseTiles.length);
        System.arraycopy(tileset1, 0, vRamTiles, t1offset, tileset1.length);
        System.arraycopy(tileset2, 0, vRamTiles, t2offset, tileset2.length);
        try {        
            Path path = Paths.get(layoutPath);
            byte[] data = Files.readAllBytes(path);
            Tile[] layoutTiles = new Tile[data.length/2];
            for(int i=0;i<layoutTiles.length;i++){
                int layoutValue = getWord(data,i*2);
                int priority = (layoutValue&0x8000)>>15;
                int palette = (layoutValue&0x6000)>>13;
                int vFlip = (layoutValue&0x1000)>>12;
                int hFlip = (layoutValue&0x0800)>>11;
                int tileId = (layoutValue&0x7FF);
                if(tileId>=0&&tileId<vRamTiles.length){
                    Tile outputTile = vRamTiles[tileId];
                    if(outputTile!=null&&palette!=0){
                        outputTile = Tile.paletteSwap(outputTile,palettes[palette]);
                    }
                    if(outputTile!=null&&vFlip!=0){
                        outputTile = Tile.vFlip(outputTile);
                    }
                    if(outputTile!=null&&hFlip!=0){
                        outputTile = Tile.hFlip(outputTile);
                    }
                    layoutTiles[i] = outputTile;
                }
                if(layoutTiles[i]==null){
                    LOG.fine("Layout tile "+i+" : wrong tile id "+tileId);
                    layoutTiles[i] = baseTiles[0];
                }
            }
            tiles = layoutTiles;
        } catch (IOException ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.exiting(LOG.getName(),"importDisassemblyWithLayout");
        return tiles;
    }
    
    private static short getWord(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor+1]);
        bb.put(data[cursor]);
        short s = bb.getShort(0);
        return s;
    }      
}
