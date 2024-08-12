package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import web.model.dao.ProdcutDao;
import web.model.dto.ProductDto;
import web.model.dto.ProductSearchDto;

import java.util.ArrayList;

@Service
public class ProductService {
    @Autowired ProdcutDao prodcutDao;
    @Autowired FileService fileService;

    //08.08 상품등록
    public boolean productAdd(ProductDto productDto){
        System.out.println("ProductService.productAdd");
        System.out.println("productDto = " + productDto);
        if (!productDto.getUploadFile().isEmpty()) {
            String uploadFileName = fileService.fileUpload(productDto.getUploadFile());
            if (uploadFileName == null) return false;
            productDto.setProdFilename(uploadFileName);
        }
        return prodcutDao.productAdd(productDto);
    }
    //08.07 컬러테이블 출력
    public ArrayList<ProductDto> productColor(){
        System.out.println("ProductService.productColor");
        return prodcutDao.productColor();
    }
    //08.07 카테고리테이블 출력
    public ArrayList<ProductDto> productCategory(){
        System.out.println("ProductController.productCategory");
        return prodcutDao.productCategory();
    }
    //08.12 상품 전체출력
    public ArrayList<ProductDto> productGetAll(ProductSearchDto productSearchDto){
        System.out.println("ProductService.productGetAll");
        return prodcutDao.productGetAll(productSearchDto);
    }
}
