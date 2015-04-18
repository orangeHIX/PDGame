package graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Individual;
import entity.World;

public class Painter {

	public static Image getImage(int width, int height, World world) {

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g2 = (Graphics2D) bi.getGraphics();

		g2.setBackground(Color.WHITE);
		g2.clearRect(0, 0, width, height);
		
		int widPerCell, heiPerCell;
		int L = world.getLength();
		widPerCell = (int) (width / L);
		heiPerCell = (int) (height / L);
		Individual in;

		for (int i = 0; i < L; i++) {
			for (int j = 0; j < L; j++) {
				if ((in = world.getIndividual(i, j)) != null) {
					g2.setColor(new Color(in.getStrategy(), 0, (1 - in
							.getStrategy())));
				} else {
					g2.setColor(new Color(0, 0, 0));
				}
				g2.fillRect(j * widPerCell, i * heiPerCell, widPerCell,
						heiPerCell);
			}
		}
		return bi;
	}

	public static void main(String[] args) {
		// int width = 100;
		// int height = 100;
		// String s = "ÄãºÃ";
		//
		// File file = new File("c:/image.jpg");
		//
		// Font font = new Font("Serif", Font.BOLD, 10);
		// BufferedImage bi = new BufferedImage(width, height,
		// BufferedImage.TYPE_INT_RGB);
		//
		// Graphics2D g2 = (Graphics2D)bi.getGraphics();
		// g2.setColor(Color.WHITE);
		// g2.fillRect(0, 0,width, height);
		// //g2.setBackground(Color.WHITE);
		// //g2.clearRect(0, 0, width, height);
		// g2.setPaint(Color.RED);
		//
		// FontRenderContext context = g2.getFontRenderContext();
		// Rectangle2D bounds = font.getStringBounds(s, context);
		// double x = (width - bounds.getWidth()) / 2;
		// double y = (height - bounds.getHeight()) / 2;
		// double ascent = -bounds.getY();
		// double baseY = y + ascent;
		//
		// g2.drawString(s, (int)x, (int)baseY);
		//
		// try {
		// ImageIO.write(bi, "jpg", file);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
