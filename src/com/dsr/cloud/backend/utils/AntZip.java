package com.dsr.cloud.backend.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * 压缩解压ZIP文件
 * 
 * @author Administrator
 * 
 */
public class AntZip {
	private ZipFile zipFile;
	private static int bufSize; // size of bytes
	private byte[] buf;
	private int readedBytes;

	/**
	 * 
	 * @param bufSize
	 *            缓存大小
	 */
	public AntZip(int bufSize) {
		this.bufSize = bufSize;
		this.buf = new byte[this.bufSize];
	}

	public AntZip() {
		this(1024);
	}

	/**
	 * 生存目录
	 * 
	 * @param directory
	 *            解压文件存放目录
	 * @param subDirectory
	 *            子目录（没有时可传入空字符串）
	 */
	private void createDirectory(String directory, String subDirectory) {
		String dir[];
		File fl = new File(directory);
		try {
			if (subDirectory == "" && fl.exists() != true)
				fl.mkdir();
			else if (subDirectory != "") {
				dir = subDirectory.replace('\\', '/').split("/");
				for (int i = 0; i < dir.length; i++) {
					File subFile = new File(directory + File.separator + dir[i]);
					if (subFile.exists() == false)
						subFile.mkdir();
					directory += File.separator + dir[i];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压指定的ZIP文件
	 * 
	 * @param unZipFileName
	 *            文件名字符串（包含路径）
	 * @param outputDirectory
	 *            解压后存放目录
	 */
	public void unZip(String unZipFileName, String outputDirectory,String encode) {
		FileOutputStream fileOut;
		// File file;
		InputStream inputStream;

		try {
			createDirectory(outputDirectory, "");
	/*		if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
				this.zipFile = new ZipFile(unZipFileName, "GBK");
			} else if (System.getProperty("os.name").toLowerCase()
					.indexOf("linux") >= 0) {
				this.zipFile = new ZipFile(unZipFileName, "UTF-8");
			}*/
			this.zipFile = new ZipFile(unZipFileName, encode);
			for (Enumeration entries = this.zipFile.getEntries(); entries
					.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) {
					// 是目录，则创建之
					String name = entry.getName().substring(0,
							entry.getName().length() - 1);
					File f = new File(outputDirectory + File.separator + name);
					f.mkdir();
					// file.mkdirs();
				} else {
					// 是文件
					String fileName = entry.getName().replace('\\', '/');
					if (fileName.indexOf("/") != -1) {
						createDirectory(outputDirectory, fileName.substring(0,
								fileName.lastIndexOf("/")));
						fileName = fileName.substring(
								fileName.lastIndexOf("/") + 1,
								fileName.length());
					}
					File f = new File(outputDirectory + File.separator
							+ entry.getName());
					f.createNewFile();
					inputStream = zipFile.getInputStream(entry);
					fileOut = new FileOutputStream(f);
					while ((this.readedBytes = inputStream.read(this.buf)) > 0) {
						fileOut.write(this.buf, 0, this.readedBytes);
					}
					fileOut.close();
					inputStream.close();
				}
			}
			this.zipFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压指定ZIP文件
	 * 
	 * @param unZipFile
	 *            需要解压的ZIP文件
	 */
	public void unZip(File unZipFile) {
		String outputDirectory = new String("D:/ip/"); // 解压后存放目录
		unZip(unZipFile.toString(), outputDirectory,"gb2312");
	}
}