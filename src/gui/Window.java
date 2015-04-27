package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entity.Individual;
import entity.World;
import graphic.Painter;

public class Window extends JFrame {
	
	public static final int MarginWith = 40;
	World world;
	public Window(World world){
		super();
		this.world = world;
	}
	
	/**绘图，红色方块代表合作者，蓝色背叛者，紫色采取居中策略的人，黑色空位*/
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(Painter.getPDGameImage(getWidth()-MarginWith*2, getHeight()-MarginWith*2, world), MarginWith, MarginWith, null);
//		//setSize(500, 500
//		int width, height;
//		int widPerCell, heiPerCell;
//		int L = world.getLength();
//		width = (int)( getSize().getWidth()*(L-1)/L);
//		height = (int)( getSize().getHeight()*(L-1)/L);
//		widPerCell = (int) (width/L);
//		heiPerCell = (int) (height/L);
//		Individual in;
//		
//		for(int i = 0; i < L; i++){
//			for(int j = 0; j < L; j++){
//				if( (in = world.getIndividual(i, j)) != null ){
//					g.setColor(new Color(in.getStrategy(),0,(1-in.getStrategy())));
//				}else{
//					g.setColor(new Color(0, 0, 0));
//				}
//				g.fillRect( 20 + j*widPerCell, 40 + i * heiPerCell, widPerCell, heiPerCell);
//			}
//		}
////		g.fillRect(90, 70, 50, 50);
////		g.drawRect(50, 50, 50, 50);
	}
	 
}
