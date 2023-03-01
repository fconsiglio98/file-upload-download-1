package co.develhope.fileuploaddownload1.controllers;

import co.develhope.fileuploaddownload1.services.FileStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public List<String> uploadMultiple(@RequestParam MultipartFile[] files) throws IOException {
        List<String> filesNames = new ArrayList<>();
        for (MultipartFile file: files) {
            String fileName = fileStorageService.upload(file);
            filesNames.add(fileName);
        }
        return filesNames;
    }

    @GetMapping("/download")
    public @ResponseBody byte[] download(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        System.out.println("Downloading " + fileName);
        String extension = FilenameUtils.getExtension(fileName);
        switch (extension) {
            case "jpg", "jpeg" -> response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            case "png" -> response.setContentType(MediaType.IMAGE_PNG_VALUE);
            case "gif" -> response.setContentType(MediaType.IMAGE_GIF_VALUE);
            default -> throw new IOException("Unknown file extension.");
        }
        response.setHeader("Content-Disposition", "attachment; filename = \""+fileName+"\"");
        return fileStorageService.download(fileName);
    }

}

