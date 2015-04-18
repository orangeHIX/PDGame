package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import utils.FileUtils;

public class FileReorganize {

	public static class Data {
		float ALLPD;
		float BCH_Dr0;
		float BSH_DG0;
		float DRG_DgDr;

		@Override
		public String toString() {
			return "Data [ALLPD=" + ALLPD + ", BCH_Dr0=" + BCH_Dr0
					+ ", BSH_DG0=" + BSH_DG0 + ", DRG_DgDr=" + DRG_DgDr + "]";
		}
	}

	public static void reorganize(String filepath) {
		File f = new File(filepath);
		ArrayList<Data> dataSet = new ArrayList<>();
		for (File file1 : f.listFiles()) {
			if (file1.isDirectory() && file1.getName().endsWith("$") &&
					 file1.getName().contains("data4")) {
				System.out.println(file1.getName());

				//int i = 0;
				for (File file2 : file1.listFiles()) {
					if (file2.getName()
							.endsWith("_CooperationLevelSummary.txt")) {
						dataSet.add(readData(file2));
					}
				}
				writeFile(file1.getAbsolutePath().concat("\\summary.txt"),
						dataSet);
				dataSet.clear();
			}
		}
	}

	public static void writeFile(String filename, List<Data> dataSet) {
		File f = FileUtils.getFile(filename);
		try (PrintWriter pw = new PrintWriter(f)) {
			pw.print("从上到下依次是：BCH（Dr=0）、BSH（Dg=0）、DRG（Dr=Dg）、ALLPD\r\n");
			for (int i = 0; i < dataSet.size(); i++) {
				pw.print("qi=0." + i);
				pw.print("\t\t");
			}
			pw.print("\r\n");
			for (Data data : dataSet) {
				pw.print(data.BCH_Dr0);
				pw.print("\t");
			}
			pw.print("\r\n");
			for (Data data : dataSet) {
				pw.print(data.BSH_DG0);
				pw.print("\t");
			}
			pw.print("\r\n");
			for (Data data : dataSet) {
				pw.print(data.DRG_DgDr);
				pw.print("\t");
			}
			pw.print("\r\n");
			for (Data data : dataSet) {
				pw.print(data.ALLPD);
				pw.print("\t");
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Data readData(File f) {
		if (f.exists() && f.isFile()) {
			Data d = new Data();
			try (Scanner sc = new Scanner(f)) {
				d.ALLPD = Float.parseFloat(sc.nextLine().replace("ALLPD:", ""));
				d.BCH_Dr0 = Float.parseFloat(sc.nextLine().replace("BCH:", ""));
				d.BSH_DG0 = Float.parseFloat(sc.nextLine().replace("BSH:", ""));
				d.DRG_DgDr = Float
						.parseFloat(sc.nextLine().replace("DRG:", ""));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return d;
		}
		return null;
	}

	public static void main(String[] args) {
		reorganize("F:\\毕设任务");
	}
}
