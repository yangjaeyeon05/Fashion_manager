package web.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.UUID;
@Service
public class FileService {
    String uploadPath="C:\\Users\\tj-bu-703-021\\Desktop\\Fashion_manager\\src\\main\\resources\\static\\upload\\";
    //08.07 첨부파일
    public String fileUpload(MultipartFile multipartFile){
        System.out.println(multipartFile.getContentType());
        System.out.println(multipartFile.getName());
        System.out.println(multipartFile.getSize());
        System.out.println(multipartFile.isEmpty());

        String uuid= UUID.randomUUID().toString();
        System.out.println("uuid = " + uuid);

        String filename=multipartFile.getOriginalFilename();
        filename=uuid+"_"+filename.replaceAll("_","-");
        System.out.println("filename = " + filename);

        String filePath=uploadPath+filename;

        File file=new File(filePath);
        try {
            multipartFile.transferTo(file);
        }catch (Exception e){System.out.println("e = " + e); return null;}
        return filename;
    }

    @Autowired
    private HttpServletRequest request;
    @Autowired private HttpServletResponse response;
    //08.07
    public void fileDownload(String filename) {
        System.out.println("FileService.fileDownload");
        System.out.println("filename = " + filename);
        String downloadPath=uploadPath+filename;
        File file=new File(downloadPath);
        if (!file.exists()){return;}
        try {
            //====================파일을 바이트 배열로 읽어오기
            BufferedInputStream fin = new BufferedInputStream(new FileInputStream(downloadPath));
            long fileSize=file.length();
            byte[] bytes=new byte[(int)fileSize];
            fin.read(bytes);
            fin.close();
            System.out.println(Arrays.toString(bytes));
            //====================읽어온 바이트배열을 HTTP 바이트 형식으로 응답하기

            //BufferedOutputStream fout=new BufferedOutputStream(response.getOutputStream());
            ServletOutputStream fout=response.getOutputStream();
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(filename.split("_")[1],"UTF-8"));
            fout.write(bytes);
            fout.close();
        }catch (Exception e){System.out.println("e = " + e);}
    }
}
