package utils;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import javax.imageio.ImageIO;

import rule.GamblingRule;
import rule.LearningPattern;
import rule.MigrationPattern;
import rule.StrategyPattern;
import entity.World;

public class FileUtils {
	/** ɾ�����ļ��м��ļ����������ļ� */
	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			File[] ff = file.listFiles();
			for (int i = 0; i < ff.length; i++) {
				deleteFile(ff[i].getPath());
			}
		}
		file.delete();
	}

	public static void outputSnapshootToFile(String filePath,
			Map<Integer, Image> Snapshoot) {

		FileUtils.deleteFile(filePath);
		for (Integer i : Snapshoot.keySet()) {
			File f = getFile(filePath + "\\turn_" + i + ".jpg");
			outputImageToFile((RenderedImage) Snapshoot.get(i), "jpg", f);
		}

	}

	public static void outputImageToFile(RenderedImage image,
			String formatName, File output) {
		try {
			ImageIO.write((RenderedImage) image, "jpg", output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param filename
	 *            String The system-dependent filename
	 * @param str
	 *            Ҫ������ļ�������
	 */
	public static void outputTofile(String fileName, String str) {
		File f = getFile(fileName);
		try (FileWriter w = new FileWriter(f.getAbsolutePath())) {
			w.write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** ��ȡ���ļ�����������ļ����󲻴��ڣ��򴴽��� */
	public static File getFile(String fileName) {
		File f = new File(fileName);
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		return f;
	}

}
