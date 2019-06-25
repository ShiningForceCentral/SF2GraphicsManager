/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.layout;

import com.sfc.sf2.graphics.Tile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class DefaultLayout extends JPanel {
    
    private static final int DEFAULT_TILES_PER_ROW = 32;
    
    private int tilesPerRow = DEFAULT_TILES_PER_ROW;
    private int displaySize = 1;
    private Tile[] tiles;
    
    BufferedImage currentImage;
    private boolean redraw = true;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage(){
        if(redraw){
            currentImage = buildImage(this.tiles,this.tilesPerRow);
            setSize(currentImage.getWidth(), currentImage.getHeight());
        }
        return currentImage;
    }
    
    public BufferedImage buildImage(Tile[] tiles, int tilesPerRow){
        int imageHeight = (tiles.length/tilesPerRow)*8;
        if(tiles.length%tilesPerRow!=0){
            imageHeight+=8;
        }
        if(redraw){
            IndexColorModel icm = buildIndexColorModel(tiles[0].getPalette());
            currentImage = new BufferedImage(tilesPerRow*8, imageHeight , BufferedImage.TYPE_BYTE_INDEXED, icm);
            Graphics graphics = currentImage.getGraphics();
            int i=0;
            int j=0;
            while(i*tilesPerRow+j<tiles.length){
                while(j<tilesPerRow && i*tilesPerRow+j<tiles.length){
                    graphics.drawImage(tiles[i*tilesPerRow+j].getImage(), j*8, i*8, null);
                    j++;
                }
                j=0;
                i++;
            }
            redraw = false;
        }
        currentImage = resize(currentImage);
        return currentImage;
    }
    
    private static IndexColorModel buildIndexColorModel(Color[] colors){
        byte[] reds = new byte[16];
        byte[] greens = new byte[16];
        byte[] blues = new byte[16];
        byte[] alphas = new byte[16];
        for(int i=0;i<16;i++){
            reds[i] = (byte)colors[i].getRed();
            greens[i] = (byte)colors[i].getGreen();
            blues[i] = (byte)colors[i].getBlue();
            alphas[i] = (byte)0xFF;
        }
        IndexColorModel icm = new IndexColorModel(4,16,reds,greens,blues,alphas);       
        return icm;
    } 
    
    public void resize(int size){
        this.displaySize = size;
        currentImage = resize(currentImage);
        this.redraw = true;
    }
    
    private BufferedImage resize(BufferedImage image){
        BufferedImage newImage = new BufferedImage(image.getWidth()*displaySize, image.getHeight()*displaySize, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)image.getColorModel());
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth()*displaySize, image.getHeight()*displaySize, null);
        g.dispose();
        return newImage;
    }  
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
    
        public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
        redraw = true;
    }
    
    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
        redraw = true;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
        redraw = true;
    }
    
    
}
