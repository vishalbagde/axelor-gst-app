package com.axelor.gst.db.web;

import java.io.File;

import com.axelor.app.AppSettings;

public class GstReportController {
	
	public String getAttachmentPath(String test)
	{
		String attachmentPath =AppSettings.get().getPath("file.upload.dir","");
		attachmentPath= attachmentPath.endsWith(File.separator)? attachmentPath:attachmentPath+File.separator;
		return  attachmentPath;
	}

}
