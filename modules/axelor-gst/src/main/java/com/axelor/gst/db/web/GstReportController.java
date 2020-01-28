package com.axelor.gst.db.web;

import com.axelor.app.AppSettings;
import java.io.File;

public class GstReportController {

  public String getAttachmentPath(String test) {
    String attachmentPath = AppSettings.get().getPath("file.upload.dir", "");
    attachmentPath =
        attachmentPath.endsWith(File.separator) ? attachmentPath : attachmentPath + File.separator;
    return attachmentPath;
  }
}
