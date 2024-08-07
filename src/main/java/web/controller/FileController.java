package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.FileService;

@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @GetMapping("/file/download")
    public void fileDownload(String filename){
        fileService.fileDownload(filename);
    }
}
