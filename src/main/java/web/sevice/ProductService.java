package web.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.dao.ProdcutDao;
import web.model.dto.ProductDto;

@Service
public class ProductService {
    @Autowired ProdcutDao prodcutDao;

    public boolean productAdd(ProductDto productDto){
        System.out.println("ProductService.productAdd");
        System.out.println("productDto = " + productDto);
        return prodcutDao.productAdd(productDto);
    }
}
