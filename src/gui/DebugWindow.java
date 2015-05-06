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
	
	/**��ͼ����ɫ�����������ߣ���ɫ�����ߣ���ɫ��ȡ���в��Ե��ˣ���ɫ��λ*/
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(Painter.getPDGameImage(getWidth()-MarginWith*2, getHeight()-MarginWith*2, world), MarginWith, MarginWith, null);
	}
	 
}
