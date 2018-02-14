package com.azilen.birt.utiil;

import java.io.File;
import java.util.ArrayList;

public class FileListUtil {

	public static ArrayList<String> listFilesForFolder(String birtReportPath) {
		
		File folder = new File(birtReportPath);
		
		ArrayList<String> list = new ArrayList<>();
		if(folder!=null){
			for (File fileEntry : folder.listFiles()) {
				if (fileEntry.getName().endsWith((".rptdesign"))) {
					list.add(fileEntry.getName());
				}
			}
		}
		return list;
	}	
}

