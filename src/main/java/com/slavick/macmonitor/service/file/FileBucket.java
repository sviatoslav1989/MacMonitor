
package com.slavick.macmonitor.service.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by apanasyonok on 13.12.2016.
 */
public class FileBucket {

    MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
