package web.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.model.dto.ProductDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class ProdcutDao extends  Dao {

    public boolean productAdd(ProductDto productDto){
        System.out.println("ProdcutDao.productAdd");
        System.out.println("productDto = " + productDto);

        try{
            // 1. Product 레코드 생성
            String sql = "insert into product(prodname, prodprice, prodgender, proddesc) values(?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS );
            ps.setString(1,productDto.getProdName());
            ps.setInt(2,productDto.getProdPrice());
            ps.setString(3,productDto.getProdGender());
            ps.setString(4,productDto.getProdDesc());
            int count = ps.executeUpdate();
            if( count == 1 ){
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int prodcode = rs.getInt( 1 );
                System.out.println("prodcode = " + prodcode);
                // 2. Product
                String sql2="insert into productdetail(prodcode,prodcatecode,colorcode,prodsize,prodfilename,proddate) values(?,?,?,?,?,?);";
                PreparedStatement ps2= conn.prepareStatement(sql2);
                ps2.setInt(1,prodcode);
                ps2.setInt(2,productDto.getProdCatecode());
                ps2.setInt(3,productDto.getColorCode());
                ps2.setString(4,productDto.getProdSize());
                ps2.setString(5,productDto.getProdFilename());
                ps2.setString(6,productDto.getProdDate());
                int count2=ps2.executeUpdate();
                if (count2==1){
                    return true;
                }

            }



        } catch (Exception e){System.out.println("productdao" + e);}

        return false;
    }
}
