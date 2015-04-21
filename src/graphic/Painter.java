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

	public static final Color CColor = Color.YELLOW;
	public static final Color MColor = Color.RED;
	public static final Color DColor = Color.BLUE;

	public static Color getGradient(Color cfrom, Color cto, float f) {
		if (f >= 0 && f <= 1.0f) {
			int r = (int) (cfrom.getRed() + (cto.getRed() - cfrom.getRed()) * f);
			int g = (int) (cfrom.getGreen() + (cto.getGreen() - cfrom.getGreen()) * f);
			int b = (int) (cfrom.getBlue() + (cto.getBlue() - cfrom.getBlue()) * f);
			return new Color(r, g, b);
		} else {
			return null;
		}
	}

	public static Color getGradient(Color cfrom,  Color cmid, Color cto, float f) {
		if(f >= 0 && f < .5f){
			return getGradient(cfrom, cmid, f*2);
		}else if( f <= 1.0f){
			return getGradient(cmid, cto, (f - 0.5f) *2);
		}else{
			return null;
		}
	}
	
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
					g2.setColor(getGradient(DColor, MColor,CColor, in.getStrategy()));
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
