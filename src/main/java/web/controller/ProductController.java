package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.ProductDto;
import web.model.dto.ProductSearchDto;
import web.service.ProductService;
//08.07

import java.util.ArrayList;

@RestController
@RequestMapping("/product")
public class ProductController {
    //08.07
    @Autowired
    ProductService productService;

    //08.08 상품등록
    @PostMapping("/add")
    public boolean productAdd(ProductDto productDto){
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
    //08.12 상품목록 출력
    @GetMapping("/getall")
    public ArrayList<ProductDto> productGetAll(ProductSearchDto productSearchDto){
        System.out.println("ProductController.productGetAll");
        System.out.println("productSearchDto = " + productSearchDto);
        return productService.productGetAll(productSearchDto);
    }
    //08.16 상품수정
    @PutMapping("/edit")
    public boolean productEdit(ProductDto productDto){
        System.out.println("ProductController.productEdit");
        System.out.println("productDto = " + productDto);
        return productService.productEdit(productDto);
    }
    //08.16 상품 개별출력
    @GetMapping("/getone")
    public ProductDto productGetOne(int prodDetailcode){
        System.out.println("ProductController.productDto");
        System.out.println("prodDetailcode = " + prodDetailcode);
        return productService.productGetOne(prodDetailcode);
    }
    //08.16 상품 삭제
    @DeleteMapping("/delete")
    public boolean productDelete(int prodDetailcode){
        System.out.println("ProductController.productDelete");
        System.out.println("prodDetailcode = " + prodDetailcode);
        return productService.productDelete(prodDetailcode);
    }
}
