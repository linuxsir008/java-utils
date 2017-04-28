package cn.xvkang.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.lingala.zip4j.exception.ZipException;

/**
 * 对zip数据文件进行操作工具类
 * 
 * @author wu
 *
 */
public class ZipFileUtil {
	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

	static {
		getAllFileType(); // 初始化文件类型信息
	}

	/**
	 * Created on 2013-1-21
	 * <p>
	 * Discription:[getAllFileType,常见文件头信息]
	 * 
	 * @author:shaochangfu
	 */
	private static void getAllFileType() {
		FILE_TYPE_MAP.put("jpg", "FFD8FF"); // JPEG (jpg)
		FILE_TYPE_MAP.put("png", "89504E47");// PNG (png)
		FILE_TYPE_MAP.put("gif", "47494638");// GIF (gif)
		FILE_TYPE_MAP.put("bmp", "424D"); // Windows Bitmap (bmp)
		FILE_TYPE_MAP.put("zip", "504B0304");// zip 压缩文件
	}

	public final static boolean isValidZipFile(String filePath) {
		try {
			boolean isValidZipFile = true;
			File f3 = new File(filePath);
			boolean isZipFile = false;
			if ((!f3.isDirectory()) && getFileByFile(f3) != null && getFileByFile(f3).equals("zip")) {
				isZipFile = true;
			}
			if (isZipFile) {
				net.lingala.zip4j.core.ZipFile zf = null;
				try {
					zf = new net.lingala.zip4j.core.ZipFile(filePath);
				} catch (ZipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!zf.isValidZipFile()) {
					isValidZipFile = false;
				}
			} else {
				isValidZipFile = false;
			}

			return isValidZipFile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public final static boolean isValidZipFile(File file) {
		boolean isValidZipFile = true;
		boolean isZipFile = false;
		if ((!file.isDirectory()) && getFileByFile(file) != null && getFileByFile(file).equals("zip")) {
			isZipFile = true;
		}
		if (isZipFile) {

			net.lingala.zip4j.core.ZipFile zf = null;
			try {
				zf = new net.lingala.zip4j.core.ZipFile(file);
			} catch (ZipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!zf.isValidZipFile()) {
				isValidZipFile = false;
			}
		} else {
			isValidZipFile = false;

		}
		return isValidZipFile;
	}

	/**
	 * Created on 2013-1-21
	 * <p>
	 * Discription:[getImageFileType,获取图片文件实际类型,若不是图片则返回null]
	 * </p>
	 * 
	 * @param File
	 * @return fileType
	 * @author:shaochangfu
	 */
	public final static String getImageFileType(File f) {
		if (isImage(f)) {
			try {
				ImageInputStream iis = ImageIO.createImageInputStream(f);
				Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
				if (!iter.hasNext()) {
					return null;
				}
				ImageReader reader = iter.next();
				iis.close();
				return reader.getFormatName();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * Created on 2013-1-21
	 * <p>
	 * Discription:[getFileByFile,获取文件类型,包括图片,若格式不是已配置的,则返回null]
	 * </p>
	 * 
	 * @param file
	 * @return fileType
	 * @author:shaochangfu
	 */
	public final static String getFileByFile(File file) {
		String filetype = null;
		byte[] b = new byte[50];
		try {
			InputStream is = new FileInputStream(file);
			is.read(b);
			filetype = getFileTypeByStream(b);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filetype;
	}

	/**
	 * Created on 2013-1-21
	 * <p>
	 * Discription:[getFileTypeByStream]
	 * </p>
	 * 
	 * @param b
	 * @return fileType
	 * @author:shaochangfu
	 */
	public final static String getFileTypeByStream(byte[] b) {
		String filetypeHex = String.valueOf(getFileHexString(b));
		Iterator<Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
		while (entryiterator.hasNext()) {
			Entry<String, String> entry = entryiterator.next();
			String fileTypeHexValue = entry.getValue();
			if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Created on 2013-1-21
	 * <p>
	 * Discription:[isImage,判断文件是否为图片]
	 * </p>
	 * 
	 * @param file
	 * @return true 是 | false 否
	 * @author:shaochangfu
	 */
	public static final boolean isImage(File file) {
		boolean flag = false;
		try {
			BufferedImage bufreader = ImageIO.read(file);
			int width = bufreader.getWidth();
			int height = bufreader.getHeight();
			if (width == 0 || height == 0) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (IOException e) {
			flag = false;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * Created on 2013-1-21
	 * <p>
	 * Discription:[getFileHexString]
	 * </p>
	 * 
	 * @param b
	 * @return fileTypeHex
	 * @author:shaochangfu
	 */
	public final static String getFileHexString(byte[] b) {
		StringBuilder stringBuilder = new StringBuilder();
		if (b == null || b.length <= 0) {
			return null;
		}
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 根据数据文件名称，生成压缩文件
	 * @param datFileName 数据文件名称
	 * @param datZipFileName 压缩文件名称
	 */
	public final static void makeZipFile(String datFileName, String datZipFileName) {
		FileInputStream fin = null;
		BufferedInputStream bi = null;
		ZipOutputStream zout = null;
		BufferedOutputStream bo = null;
		try {
			File datFile = new File(datFileName);
			fin = new FileInputStream(datFile);
			bi = new BufferedInputStream(fin);
			
			zout = new ZipOutputStream(new FileOutputStream(datZipFileName));
			bo = new BufferedOutputStream(zout);  
			if (datFile.isFile()) {
				String path = datFileName.substring(0, datFileName.lastIndexOf(File.separator));
				zout.putNextEntry(new ZipEntry(path));
				int c = -1;
				while ((c = bi.read()) != -1) {
					bo.write(c);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != bi) bi.close();
				if (null != fin) fin.close();
				if (null != bo) bo.close();
				if (null != zout) zout.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	//只压缩一个文件
	public final static void makeZipFile2(String sourceFilePathString,String targetFilePathString){
		Path sourceFilePath=Paths.get(sourceFilePathString);
		Path targetFilePath=Paths.get(targetFilePathString);
		InputStream is=null;
		ZipOutputStream zipOutputStream=null;
		if(Files.exists(sourceFilePath)&&Files.isRegularFile(sourceFilePath)){
			try {
				is=Files.newInputStream(sourceFilePath);
				zipOutputStream=new ZipOutputStream(Files.newOutputStream(targetFilePath));
				ZipEntry zipEntry=new ZipEntry(sourceFilePath.getFileName().toString());
				zipOutputStream.putNextEntry(zipEntry);
				Files.copy(sourceFilePath, zipOutputStream);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					zipOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}


	/**
	 * 统计zip文件中 所有zipEntry 所有行数
	 */
	public static Integer count(String zipFilePath) {
		Integer result = 0;
		Path sourcePath = Paths.get(zipFilePath);
		ZipInputStream zis = null;
		ZipEntry zipEntry = null;
		BufferedReader br = null;
		LineNumberReader lineNumberReader = null;
		if (Files.exists(sourcePath) && Files.isRegularFile(sourcePath)) {
			try {
				zis = new ZipInputStream(Files.newInputStream(sourcePath));
				while ((zipEntry = zis.getNextEntry()) != null) {
					if (!zipEntry.isDirectory()) {
						br = new BufferedReader(new InputStreamReader(zis));
						lineNumberReader = new LineNumberReader(br);
						lineNumberReader.skip(Long.MAX_VALUE);
						result += lineNumberReader.getLineNumber();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (zis != null) {
					try {
						zis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		//System.out.println(ZipFileUtil.count("/home/wu/test/zipfile/a.zip"));
		
	}
}
