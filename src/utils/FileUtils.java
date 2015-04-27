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
import entity.LearningPattern;
import entity.MigrationPattern;
import entity.StrategyPattern;
import entity.World;

public class FileUtils {
	/** 删除该文件夹即文件夹下所有文件 */
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
		try {
			FileUtils.deleteFile(filePath);
			for (Integer i : Snapshoot.keySet()) {
				File f = getFile(filePath + "\\turn_" + i + ".jpg");
				ImageIO.write((RenderedImage) Snapshoot.get(i), "jpg", f);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param filename
	 *            String The system-dependent filename
	 * @param str
	 *            要输出到文件的内容
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

	/** 获取该文件对象，如果该文件对象不存在，则创建它 */
	public static File getFile(String fileName) {
		File f = new File(fileName);
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		return f;
	}



	
}
