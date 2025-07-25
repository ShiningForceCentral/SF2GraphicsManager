/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.palette.Palette;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class RawImageManager {
    
    public static final int FILE_FORMAT_PNG = 0;
    public static final int FILE_FORMAT_GIF = 1;
    
    private static int importedImageTileWidth = 32;

    public static int getImportedImageTileWidth() {
        return importedImageTileWidth;
    }

    public static void setImportedImageTileWidth(int importedImageTileWidth) {
        RawImageManager.importedImageTileWidth = importedImageTileWidth;
    }

    private static final Logger LOG = Logger.getLogger(RawImageManager.class.getName());
    
    public static Tile[] importImage(String filepath) {
        LOG.entering(LOG.getName(),"importImage");
        Tile[] tiles = null;
        try{
            Path path = Paths.get(filepath);
            BufferedImage img = ImageIO.read(path.toFile());
            ColorModel cm = img.getColorModel();
            if(!(cm instanceof IndexColorModel)){
                LOG.warning("IMAGE FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
            }else{
                IndexColorModel icm = (IndexColorModel)cm;
                String filename = path.getFileName().toString();
                filename = filename.substring(0, filename.lastIndexOf("."));
                Palette palette = new Palette(filename, Palette.fromICM(icm));
                WritableRaster raster = img.getRaster();
                
                int imageWidth = img.getWidth();
                int imageHeight = img.getHeight();
                if(imageWidth%8!=0 || imageHeight%8!=0){
                    LOG.warning("IMAGE FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
                }else{
                    importedImageTileWidth = imageWidth/8;
                    tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
                    int tileId = 0;
                    int[] pixels = new int[64];
                    for(int t = 0; t < tiles.length; t++) {
                        int x = t%importedImageTileWidth*8;
                        int y = t/importedImageTileWidth*8;
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
             LOG.throwing(LOG.getName(), "importImage", e);
        }        
        LOG.exiting(LOG.getName(),"importImage");        
        return tiles;                
    }
    
    public static void exportImage(Tile[] tiles, String filepath, int tilesPerRow, int fileFormat){
        try {
            LOG.entering(LOG.getName(),"exportImage");
            int imageHeight = (tiles.length/tilesPerRow)*8;
            if(tiles.length%tilesPerRow!=0){
                imageHeight+=8;
            }
            int imageWidth = tilesPerRow*8;
            BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_BINARY, tiles[0].getIcm());
            WritableRaster raster = image.getRaster();
            
            int[] pixels = new int[64];
            for(int t = 0; t < tiles.length; t++) {
                if (tiles[t] != null) {
                    for(int j=0;j<8;j++){
                        for(int i=0;i<8;i++){
                            pixels[i+j*8] = tiles[t].getPixels()[i][j];
                        }
                    }
                    int x = t%tilesPerRow*8;
                    int y = t/tilesPerRow*8;
                    raster.setPixels(x, y, 8, 8, pixels);
                }
            }
            exportImage(image, filepath, tilesPerRow, fileFormat);
        } catch (Exception ex) {
            LOG.throwing(LOG.getName(),"exportImage", ex);
        }
    }
    
    public static void exportImage(BufferedImage image, String filepath, int tilesPerRow, int fileFormat){
        try {
            LOG.entering(LOG.getName(),"exportImage");
            File outputfile = new File(filepath);
            LOG.fine("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, GetFileExtensionString(fileFormat), outputfile);
            LOG.exiting(LOG.getName(),"exportImage");
        } catch (Exception ex) {
            LOG.throwing(LOG.getName(),"exportImage", ex);
        }
    }
    
    public static String GetFileExtensionString(int fileFormat) {
        switch (fileFormat) {
            case FILE_FORMAT_PNG:
                return "png";
            case FILE_FORMAT_GIF:
                return "gif";
            default:
                LOG.throwing(LOG.getName(),"UNKNOWN FILE FORMAT", new IOException());
                return "bin";
        }
    }
}
