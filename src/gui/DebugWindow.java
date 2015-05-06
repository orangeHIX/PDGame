package gui;

import java.awt.Graphics;

import javax.swing.JFrame;

import entity.World;
import graphic.Painter;

public class DebugWindow extends JFrame {
	
	public static final int MarginWith = 40;
	World world;
	public DebugWindow(World world){
		super();
		this.world = world;
	}
	
	/**绘图，红色方块代表合作者，蓝色背叛者，紫色采取居中策略的人，黑色空位*/
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(Painter.getPDGameImage(getWidth()-MarginWith*2, getHeight()-MarginWith*2, world), MarginWith, MarginWith, null);
	}
	 
}
