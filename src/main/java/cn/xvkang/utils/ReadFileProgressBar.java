package cn.xvkang.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReadFileProgressBar {
	public void readFile() {
		File f1 = null;
		BufferedReader br = null;
		try {
			f1 = new File("/home/wu/test/zipfile/42_10001_2016124_SALE.DAT");
			long totalLength = f1.length();
			double lengthPerPercent = 100.0 / totalLength;
			long readLength = 0;
			br = new BufferedReader(new FileReader(f1));
			String s = null;
			while ((s = br.readLine()) != null) {
				readLength += s.length();
				System.out.println((int) Math.round(lengthPerPercent * readLength));
			}
			System.out.println(totalLength);
			System.out.println(readLength);
		} catch (Exception e) {
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}// void readFile() End

	public void lineNumberReader() {
		File f1 = null;
		LineNumberReader lineNumberReader = null;
		BufferedReader br = null;
		try {
			f1 = new File("/home/wu/test/zipfile/42_10001_2016124_SALE.DAT");
			lineNumberReader = new LineNumberReader(new FileReader(f1));
			lineNumberReader.skip(Long.MAX_VALUE);
			long lineNumbers = lineNumberReader.getLineNumber();
			System.out.println(lineNumbers);
			br = new BufferedReader(new FileReader(f1));
			String s = null;
			long l = 0;
			long c = 0;
			while ((s = br.readLine()) != null) {
				s.length();
				l++;
				c++;
				if (c % 200000 == 0)
					System.out.println((double) l / lineNumbers);

			}
			System.out.println((double) l / lineNumbers);
		} catch (Exception e) {
		} finally {
			if (lineNumberReader != null) {
				try {
					lineNumberReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void zipFileLineNumberReader() throws FileNotFoundException {
		Path sourcePath = Paths.get("/home/wu/test/zipfile/42_10001_2016124_SALE.DAT.ZIP");
		ZipInputStream zis = null;
		ZipEntry zipEntry = null;
		BufferedReader br = null;
		LineNumberReader lineNumberReader = null;
		long lineNumbers = 0;
		if (Files.exists(sourcePath) && Files.isRegularFile(sourcePath)) {
			try {
				zis = new ZipInputStream(Files.newInputStream(sourcePath));
				while ((zipEntry = zis.getNextEntry()) != null) {
					if (!zipEntry.isDirectory()) {
						br = new BufferedReader(new InputStreamReader(zis));
						lineNumberReader = new LineNumberReader(br);
						lineNumberReader.skip(Long.MAX_VALUE);
						lineNumbers += lineNumberReader.getLineNumber();
					}
				}
			} catch (Exception e) {
			}
		}
		System.out.println(lineNumbers);
		if (Files.exists(sourcePath) && Files.isRegularFile(sourcePath)) {
			try {
				zis = new ZipInputStream(Files.newInputStream(sourcePath));
				while ((zipEntry = zis.getNextEntry()) != null) {
					if (!zipEntry.isDirectory()) {
						br = new BufferedReader(new InputStreamReader(zis));
						String s = null;
						long l = 0;
						long c = 0;
						while ((s = br.readLine()) != null) {
							s.length();
							l++;
							c++;
							if (c % 200000 == 0)
								System.out.println((double) l / lineNumbers);
						}
						System.out.println((double) l / lineNumbers);
					}
				}
			} catch (Exception e) {

			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		// new ReadFileProgressBar().readFile();
		// new ReadFileProgressBar().lineNumberReader();
		//new ReadFileProgressBar().zipFileLineNumberReader();
		
	}//main End
}
