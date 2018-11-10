package com.timing.filter.xmlfile;

import java.io.File;
import java.io.FileFilter;

/**
 * 文件过滤器
 * @author 清明
 *
 */
public class MyFileFiltter implements FileFilter{

	private String extension;
	
	public MyFileFiltter(String extention) {
		this.extension = extention;
	}
	
	@Override
	public boolean accept(File file) {
		String fileName = file.getName().toLowerCase();
		return file.isFile()&&(fileName.indexOf(extension)>0);
	}

}
