package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import web.model.dao.ProdcutDao;
import web.model.dto.ProductDto;

import java.util.ArrayList;

@Service
public class ProductService {
    @Autowired ProdcutDao prodcutDao;

    public boolean productAdd(ProductDto productDto){
        System.out.println("ProductService.productAdd");
        System.out.println("productDto = " + productDto);
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
}
