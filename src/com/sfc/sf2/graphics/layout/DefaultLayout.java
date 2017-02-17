/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.layout;

import com.sfc.sf2.graphics.Tile;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class DefaultLayout extends JPanel {
    
    private static int DEFAULT_TILES_PER_ROW = 32;
    
    private int tilesPerRow = DEFAULT_TILES_PER_ROW;
    private Tile[] tiles;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage(){
        
        int imageHeight = (tiles.length/tilesPerRow)*8;
        if(tiles.length%tilesPerRow!=0){
            imageHeight+=8;
        }
        BufferedImage image = new BufferedImage(tilesPerRow*8, imageHeight , BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
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
        setSize(image.getWidth(), image.getHeight());
        return image;
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
    }
    
    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
    }
    
}
