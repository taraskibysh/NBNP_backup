package com.nbnp.trip_to_go.service;

import com.nbnp.trip_to_go.model.FolderType;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public String upload(MultipartFile file, FolderType folderType);

    public Boolean delete(String fileName );

    public String getFileURL(String fileName);

}
