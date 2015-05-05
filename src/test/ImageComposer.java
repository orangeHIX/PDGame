package test;

import graphic.NamedImage;
import graphic.Painter;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import utils.FileUtils;

public class ImageComposer {

	private static ArrayList<NamedImage> readImages(String path) {
		return readImages(new File(path));
	}

	private static ArrayList<NamedImage> readImages(File f) {

		if (!f.isDirectory()) {
			return null;
		}

		BufferedImage bi = null;
		ArrayList<NamedImage> imageList = new ArrayList<>();

		if (f != null && f.isDirectory()) {
			File[] fs = f.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					if (name.startsWith("turn_") && name.endsWith(".jpg")) {
						return true;
					}
					return false;
				}
			});

			try {
				for (File imagef : fs) {
					bi = ImageIO.read(imagef);
					if (bi != null) {
						imageList.add(new NamedImage(imagef.getName(), bi));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return imageList;
	}

	public static void printPicture(final Image image) {
//		BufferedImage bi = null;
//		final ArrayList<graphic.NamedImage> imageList = // readImages("C:\\Users\\hyx\\Desktop\\补充微观数据\\shuju");
//		readImages("C:\\Users\\hyx\\Desktop\\补充微观数据\\shuju\\"
//				+ "max_payoff_learning_$_optimistic_migrate_$_continuous_strategy_$_pi=0.50_$_qi=0.00\\"
//				+ "gr=(1.00,-0.10,1.90,0.00)_$_d0=0.50");

		// 创建frame
		JFrame frame = new JFrame() {
			public static final int MarginWith = 40;

			@Override
			public void paint(Graphics g) {
				g.clearRect(0, 0, this.getWidth(), this.getHeight());
				g.drawImage(image, MarginWith,
						MarginWith, null);
			}
		};
		// 调整frame的大小和初始位置
		frame.setSize(1000, 880);
		frame.setLocation(100, 100);
		// 增加窗口监听事件，使用内部类方法，并用监听器的默认适配器
		frame.addWindowListener(new WindowAdapter() {

			// 重写窗口关闭事件
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}

		});
		frame.setTitle("test");
		// 显示窗体
		frame.setVisible(true);
	}

	public static void main(String[] args) {
//		printPicture(6,2);
		ArrayList<graphic.NamedImage> imageList;
		int column = 6;
		int zoom = 2;
		Image image = null;
		File[] fs1 = new File("C:\\Users\\hyx\\Desktop\\补充微观数据\\shuju")
				.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						// TODO Auto-generated method stub
						return name.contains("_$_");
					}
				});
		for (File f1 : fs1) {
			if (f1.isDirectory()) {
				File[] fs2 = f1.listFiles();
				for (File f2 : fs2) {
					imageList = readImages(f2);
					image = Painter.composing(imageList, column,
							(int) (120 * zoom), (int) (120 * zoom),
							(int) (5 * zoom), (int) (15 * zoom),
							(int) (3 * zoom), (int) (15 * zoom));
					if (image != null) {
						FileUtils.outputImageToFile((RenderedImage) image,
								"png", new File(f2.getAbsolutePath() + "\\"
										+ f1.getName() + ".png"));
						printPicture(image);

					}
				}
			}
		}
		// final ArrayList<graphic.NamedImage> imageList =
		// readImages("C:\\Users\\hyx\\Desktop\\补充微观数据\\shuju");

	}
}
