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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
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
        int imageHeight = (tiles.length/tilesPerRow)*Tile.PIXEL_HEIGHT;
        if(tiles.length%tilesPerRow!=0){
            imageHeight+=Tile.PIXEL_HEIGHT;
        }
        int imageWidth = tilesPerRow*Tile.PIXEL_WIDTH;
        if(redraw){
            IndexColorModel icm = buildIndexColorModel(tiles[0].getPalette());
            currentImage = new BufferedImage(imageWidth*displaySize, imageHeight*displaySize, BufferedImage.TYPE_BYTE_INDEXED, icm);
            int i=0;
            int j=0;
            while(i*tilesPerRow+j<tiles.length){
                while(j<tilesPerRow && i*tilesPerRow+j<tiles.length){
                    drawIndexedColorPixels(currentImage, tiles[i*tilesPerRow+j].getPixels(), j*Tile.PIXEL_WIDTH*displaySize,  i*Tile.PIXEL_HEIGHT*displaySize, displaySize);
                    j++;
                }
                j=0;
                i++;
            }
            redraw = false;
        }
        return currentImage;
    }
    
    public void drawIndexedColorPixels(BufferedImage image, int[][] pixels, int x, int y, int size){
        byte[] data = ((DataBufferByte)(image.getRaster().getDataBuffer())).getData();
        int width = image.getWidth();
        for(int i=0;i<pixels.length;i++){
            for(int j=0;j<pixels[i].length;j++){
                for(int ys=0;ys<size;ys++){
                    for(int xs=0;xs<size;xs++){
                        data[(y+j*size+ys)*width+x+i*size+xs] = (byte)(pixels[i][j]);
                    }
                }
            }
        }
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
        alphas[0] = 0;
        IndexColorModel icm = new IndexColorModel(4,16,reds,greens,blues,alphas);       
        return icm;
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
