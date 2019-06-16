/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.layout.DefaultLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class GifManager {
    
    private static int importedGifTileWidth = 32;

    public static int getImportedGifTileWidth() {
        return importedGifTileWidth;
    }

    public static void setImportedGifTileWidth(int importedGifTileWidth) {
        GifManager.importedGifTileWidth = importedGifTileWidth;
    }

    private static final Logger LOG = Logger.getLogger(GifManager.class.getName());
    
    public static Tile[] importGif(String filepath){
        LOG.entering(LOG.getName(),"importGif");
        Tile[] tiles = null;
        try{
            Path path = Paths.get(filepath);
            BufferedImage img = ImageIO.read(path.toFile());
            ColorModel cm = img.getColorModel();
            if(!(cm instanceof IndexColorModel)){
                LOG.warning("GIF FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
            }else{
                IndexColorModel icm = (IndexColorModel) cm;
                Color[] palette = buildColors(icm);
                Graphics g = img.getGraphics();
                
                int imageWidth = img.getWidth();
                int imageHeight = img.getHeight();
                if(imageWidth%8!=0 || imageHeight%8!=0){
                    LOG.warning("GIF FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
                }else{
                    importedGifTileWidth = imageWidth/8;
                    tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
                    int tileId = 0;
                    for(int y=0;y<imageHeight;y+=8){
                        for(int x=0;x<imageWidth;x+=8){
                            LOG.fine("Building tile from coordinates "+x+":"+y);
                            Tile tile = new Tile();
                            tile.setId(tileId);
                            tile.setPalette(palette);
                            for(int j=0;j<8;j++){
                                for(int i=0;i<8;i++){
                                    Color color = new Color(img.getRGB(x+i,y+j));
                                    for(int c=0;c<16;c++){
                                        if(color.equals(palette[c])){
                                            tile.setPixel(i, j, c);
                                            break;
                                        }
                                    }
                                }
                            }
                            LOG.finest(tile.toString());
                            tiles[tileId] = tile;   
                            tileId++;
                        }
                    }
                }
                

                
                
            }
        }catch(Exception e){
             LOG.throwing(LOG.getName(), "importGif", e);
        }        
        LOG.exiting(LOG.getName(),"importGif");        
        return tiles;                
    }
    
    private static Color[] buildColors(IndexColorModel icm){
        Color[] colors = new Color[16];
        if(icm.getMapSize()>16){
            LOG.warning("com.sfc.sf2.graphics.io.GifManager.buildColors() - GIF FORMAT HAS MORE THAN 16 INDEXED COLORS : "+icm.getMapSize());
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
    
    public static void exportGif(Tile[] tiles, String filepath, String tilesPerRow){
        try {
            LOG.entering(LOG.getName(),"exportGif");
            int imageTileWidth = Integer.parseInt(tilesPerRow,10);
            BufferedImage image = new DefaultLayout().buildImage(tiles, imageTileWidth);
            File outputfile = new File(filepath);
            LOG.fine("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "gif", outputfile);
            LOG.exiting(LOG.getName(),"exportGif");
        } catch (Exception ex) {
            LOG.throwing(LOG.getName(),"exportGif", ex);
        }
        
                
    }
    
}
