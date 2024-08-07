package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.FileService;

@RestController
public class FileController {
    //08.07 첨부파일
    @Autowired
    private FileService fileService;

    @GetMapping("/file/download")
    public void fileDownload(String filename){
        System.out.println("FileController.fileDownload");
        System.out.println("filename = " + filename);
        fileService.fileDownload(filename);
    }
}
