package co.develhope.fileuploaddownload1.services;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;


    public String upload(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString() + "." + extension; //bisogna rinominare il file per evitare problemi dati da casi di omonimia
        File finalFolder = new File(fileRepositoryFolder);
        if (!finalFolder.exists()) throw new IOException("Final folder does not exist");
        if (!finalFolder.isDirectory()) throw new IOException("Final folder is not a directory.");

        File finalDestination = new File(fileRepositoryFolder + "\\" + newFileName);
        if(finalDestination.exists()) throw new IOException("Conflict: file already exist.");

        file.transferTo(finalDestination);
        return newFileName + " uploaded in: " + finalFolder;
    }

    public byte[] download(String fileName) throws IOException {
        File fileToDownload = new File(fileRepositoryFolder + "\\" + fileName);
        if (!fileToDownload.exists()) throw new IOException("File does not exist.");
        return IOUtils.toByteArray(new FileInputStream(fileToDownload));

    }
}