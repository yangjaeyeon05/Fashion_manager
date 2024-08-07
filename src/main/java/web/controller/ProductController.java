package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.ProductDto;
import web.service.ProductService;
//08.07

import java.util.ArrayList;

@RestController
@RequestMapping("/product")
public class ProductController {
    //08.07
    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public boolean productAdd(@RequestBody ProductDto productDto){
        System.out.println("ProductController.productAdd");
        System.out.println("productDto = " + productDto);
        return productService.productAdd(productDto);
    }
    //08.07 컬러테이블 출력
    @GetMapping("/color")
    public ArrayList<ProductDto> productColor(){
        System.out.println("ProductController.productDto");
        return productService.productColor();
    }
    //08.07 카테고리테이블 출력
    @GetMapping("/category")
    public ArrayList<ProductDto> productCategory(){
        System.out.println("ProductController.productCategory");
        return productService.productCategory();
    }
}
