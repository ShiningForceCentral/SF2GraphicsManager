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
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class PngManager {
    
    private static String CHARACTER_FILENAME = "symbolXX.png";
    
    public static Tile[] importPng(String filepath){
        System.out.println("com.sfc.sf2.graphics.io.PngManager.importPng() - Importing PNG files ...");
        Tile[] tiles = null;
        try{
            Path path = Paths.get(filepath);
            BufferedImage img = ImageIO.read(path.toFile());
            ColorModel cm = img.getColorModel();
            if(!(cm instanceof IndexColorModel)){
                System.out.println("PNG FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
            }else{
                IndexColorModel icm = (IndexColorModel) cm;
                Color[] palette = buildColors(icm);
                Graphics g = img.getGraphics();
                
                int imageWidth = img.getWidth();
                int imageHeight = img.getHeight();
                if(imageWidth%8!=0 || imageHeight%8!=0){
                    System.out.println("PNG FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
                }else{
                    tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
                    int tileId = 0;
                    for(int y=0;y<imageHeight;y+=8){
                        for(int x=0;x<imageWidth;x+=8){
                            System.out.println("Building tile from coordinates "+x+":"+y);
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
                            System.out.println(tile);
                            tiles[tileId] = tile;   
                            tileId++;
                        }
                    }
                }
                

                
                
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.graphics.io.PngManager.importPng() - Error while parsing PNG data : "+e);
             e.printStackTrace();
        }        
        System.out.println("com.sfc.sf2.graphics.io.PngManager.importPng() - PNG files imported.");        
        return tiles;                
    }
    
    private static Color[] buildColors(IndexColorModel icm){
        Color[] colors = new Color[16];
        if(icm.getMapSize()>16){
            System.out.println("com.sfc.sf2.graphics.io.PngManager.buildColors() - PNG FORMAT HAS MORE THAN 16 INDEXED COLORS : "+icm.getMapSize());
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
    
    public static void exportPng(Tile[] tiles, String filepath, String tilesPerRow){
        try {
            System.out.println("com.sfc.sf2.graphics.io.PngManager.exportPng() - Exporting PNG file ...");
            int imageTileWidth = Integer.parseInt(tilesPerRow,10);
            BufferedImage image = DefaultLayout.buildImage(tiles, imageTileWidth);
            File outputfile = new File(filepath);
            System.out.println("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "png", outputfile);
            System.out.println("com.sfc.sf2.graphics.io.PngManager.exportPng() - PNG file exported.");
        } catch (Exception ex) {
            Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }
    
}
