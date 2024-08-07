package web.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.model.dto.ProductDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Component
public class ProdcutDao extends  Dao {

    public boolean productAdd(ProductDto productDto){
        System.out.println("ProdcutDao.productAdd");
        System.out.println("productDto = " + productDto);
        //08.07
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

    //08.07 컬러테이블 출력
    public ArrayList<ProductDto> productColor(){
        System.out.println("ProdcutDao.productColor");
        ArrayList<ProductDto> color=new ArrayList<>();
        try {
            String sql="select * from color;";
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                ProductDto productDto= ProductDto.builder()
                        .colorCode(rs.getInt("colorcode"))
                        .colorName(rs.getString("colorname"))
                        .build();
                color.add(productDto);
            }
            System.out.println(color);
        }catch (Exception e){
            System.out.println("productdao" + e);
        }
        return color;
    }

    //08.07 카테고리테이블 출력
    public ArrayList<ProductDto> productCategory(){
        System.out.println("ProdcutDao.productCategory");
        ArrayList<ProductDto> category=new ArrayList<>();
        try {
            String sql="select * from productcategory;";
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                ProductDto productDto= ProductDto.builder()
                        .prodCatecode(rs.getInt("prodcatecode"))
                        .prodCatename(rs.getString("prodcatename"))
                        .build();
                category.add(productDto);
            }
            System.out.println(category);
        }catch (Exception e){
            System.out.println("productdao" + e);
        }
        return category;
    }

}
