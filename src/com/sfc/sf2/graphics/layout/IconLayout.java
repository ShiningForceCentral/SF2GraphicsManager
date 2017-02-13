/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.graphics.layout;

import com.sfc.sf2.graphics.Tile;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class IconLayout extends JPanel {
    
    private Tile[] tiles;

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage(){
        BufferedImage image = new BufferedImage(16, (tiles.length/2)*8, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        for(int i=0;i<tiles.length/6;i++){
            graphics.drawImage(tiles[i*6].getImage(), 0, i*8*3, null);
            graphics.drawImage(tiles[i*6+1].getImage(), 8, i*8*3, null);
            graphics.drawImage(tiles[i*6+2].getImage(), 0, (i*8*3)+8, null);
            graphics.drawImage(tiles[i*6+3].getImage(), 8, (i*8*3)+8, null);
            graphics.drawImage(tiles[i*6+4].getImage(), 0, (i*8*3)+16, null);
            graphics.drawImage(tiles[i*6+5].getImage(), 8, (i*8*3)+16, null);
        }
        return image;
    }
    
}
