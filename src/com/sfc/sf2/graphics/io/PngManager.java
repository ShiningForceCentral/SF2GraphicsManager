/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.graphics.Tile;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class PngManager {
    
    private static int importedPngTileWidth = 32;

    public static int getImportedPngTileWidth() {
        return importedPngTileWidth;
    }

    public static void setImportedPngTileWidth(int importedPngTileWidth) {
        PngManager.importedPngTileWidth = importedPngTileWidth;
    }

    private static final Logger LOG = Logger.getLogger(PngManager.class.getName());
    
    public static Tile[] importPng(String filepath) {
        LOG.entering(LOG.getName(),"importPng");
        Tile[] tiles = null;
        try{
            Path path = Paths.get(filepath);
            BufferedImage img = ImageIO.read(path.toFile());
            ColorModel cm = img.getColorModel();
            if(!(cm instanceof IndexColorModel)){
                LOG.warning("PNG FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
            }else{
                IndexColorModel icm = (IndexColorModel)cm;
                Color[] palette = buildColors(icm);
                WritableRaster raster = img.getRaster();
                
                int imageWidth = img.getWidth();
                int imageHeight = img.getHeight();
                if(imageWidth%8!=0 || imageHeight%8!=0){
                    LOG.warning("PNG FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
                }else{
                    importedPngTileWidth = imageWidth/8;
                    tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
                    int tileId = 0;
                    int[] pixels = new int[64];
                    for(int t = 0; t < tiles.length; t++) {
                        int x = t%importedPngTileWidth*8;
                        int y = t/importedPngTileWidth*8;
                        LOG.fine("Building tile from coordinates "+x+":"+y);
                        Tile tile = new Tile();
                        tile.setId(tileId);
                        tile.setPalette(palette);
                        raster.getPixels(x, y, 8, 8, pixels);
                        for(int j=0;j<8;j++){
                            for(int i=0;i<8;i++){
                                tile.setPixel(i, j, pixels[i+j*8]);
                            }
                        }
                        LOG.finest(tile.toString());
                        tiles[tileId] = tile;   
                        tileId++;
                    }
                }
            }
        }catch(Exception e){
             LOG.throwing(LOG.getName(), "importPng", e);
        }        
        LOG.exiting(LOG.getName(),"importPng");        
        return tiles;                
    }
    
    private static Color[] buildColors(IndexColorModel icm){
        Color[] colors = new Color[16];
        if(icm.getMapSize()>16){
            LOG.warning("com.sfc.sf2.graphics.io.PngManager.buildColors() - PNG FORMAT HAS MORE THAN 16 INDEXED COLORS : "+icm.getMapSize());
        }
        byte[] reds = new byte[icm.getMapSize()];
        byte[] greens = new byte[icm.getMapSize()];
        byte[] blues = new byte[icm.getMapSize()];
        icm.getReds(reds);
        icm.getGreens(greens);
        icm.getBlues(blues);
        for(int i=0;i<16;i++){
            colors[i] = new Color((int)(reds[i]&0xff),(int)(greens[i]&0xff),(int)(blues[i]&0xff));
        }
        return colors;
    }
    
    public static void exportPng(Tile[] tiles, String filepath, int tilesPerRow){
        try {
            LOG.entering(LOG.getName(),"exportPng");
            int imageWidth = (tiles.length%tilesPerRow)*8;
            int imageHeight = (tiles.length/tilesPerRow+1)*8;
            BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_INDEXED, tiles[0].getIcm());
            WritableRaster raster = image.getRaster();
            
            int[] pixels = new int[64];
            for(int t = 0; t < tiles.length; t++) {
                for(int j=0;j<8;j++){
                    for(int i=0;i<8;i++){
                        pixels[i+j*8] = tiles[i].getPixels()[i][j];
                    }
                }
                int x = t%tilesPerRow*8;
                int y = t/tilesPerRow*8;
                raster.setPixels(x, y, 8, 8, pixels);
            }
            exportPng(image, filepath, tilesPerRow);
        } catch (Exception ex) {
            LOG.throwing(LOG.getName(),"exportPng", ex);
        }
    }
    
    public static void exportPng(BufferedImage image, String filepath, int tilesPerRow){
        try {
            LOG.entering(LOG.getName(),"exportPng");
            File outputfile = new File(filepath);
            LOG.fine("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "png", outputfile);
            LOG.exiting(LOG.getName(),"exportPng");
        } catch (Exception ex) {
            LOG.throwing(LOG.getName(),"exportPng", ex);
        }
    }
}
