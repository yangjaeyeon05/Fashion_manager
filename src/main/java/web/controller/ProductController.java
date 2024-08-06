package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.model.dto.ProductDto;
import web.sevice.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public boolean productAdd(@RequestBody ProductDto productDto){
        System.out.println("ProductController.productAdd");
        System.out.println("productDto = " + productDto);
        return productService.productAdd(productDto);
    }
}
