package web.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.model.dto.ProductDto;
import web.model.dto.ProductSearchDto;

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

    //08.13 상품 전체출력
    public ArrayList<ProductDto> productGetAll(ProductSearchDto productSearchDto){
        System.out.println("ProdcutDao.productGetAll");
        ArrayList<ProductDto> list=new ArrayList<>(); // productDto 타입 어레이 리스트 생성
        try{
            // 4개 테이블 조인
            String sql="select * from productdetail a inner join product b on a.prodcode=b.prodcode inner join productcategory c on a.prodcatecode=c.prodcatecode inner join color d on a.colorcode=d.colorcode";
            // 상품 등록일 조건
            if (productSearchDto.getStartDate().isEmpty()){ // 시작하는 날짜 없으면 검색X
            }else{
                sql+=" where a.proddate between "+"'"+productSearchDto.getStartDate()+"'"+" and "+"'"+productSearchDto.getEndDate()+"'";
            }
            // 상품 가격 조건
            if (productSearchDto.getMinPrice()==0&&productSearchDto.getMaxPrice()==0){}//최소가격,최대가격 둘다 공백일 때
            else {
                if (productSearchDto.getStartDate().isEmpty()){// 날짜 검색 x면 where 아니면 and
                    sql+=" where ";
                }
                else {
                    sql+=" and ";
                }
                sql+=" prodprice between "+productSearchDto.getMinPrice()+" and "+productSearchDto.getMaxPrice();
            }
            // 카테고리 조건
            if (productSearchDto.getProdCatecode()==0){}//모든 카테고리
            else {
                if (productSearchDto.getMinPrice()==0&&productSearchDto.getMaxPrice()==0&&productSearchDto.getStartDate().isEmpty()){
                    sql+=" where ";
                }
                else {
                    sql+=" and ";
                }
                sql+=" a.prodcatecode= "+productSearchDto.getProdCatecode();
            }
            //색상 조건
            if (productSearchDto.getColorCode()==0){}//모든 색상
            else {
                if (productSearchDto.getMinPrice()==0&&productSearchDto.getMaxPrice()==0&&productSearchDto.getStartDate().isEmpty()&&productSearchDto.getProdCatecode()==0){
                    sql+=" where ";
                }
                else {
                    sql+=" and ";
                }
                sql+=" a.colorcode= "+productSearchDto.getColorCode();
            }
            //사이즈 조건
            if (productSearchDto.getProdSize().isEmpty()){}//모든 사이즈
            else {
                if (productSearchDto.getMinPrice()==0&&productSearchDto.getMaxPrice()==0&&productSearchDto.getStartDate().isEmpty()&&productSearchDto.getProdCatecode()==0&&productSearchDto.getColorCode()==0){
                    sql+=" where ";
                }
                else {
                    sql+=" and ";
                }
                sql+=" prodsize= "+"'"+productSearchDto.getProdSize()+"'";
            }
            //성별 조건
            if (productSearchDto.getProdGender().isEmpty()){}//모든 성별
            else {
                if (productSearchDto.getMinPrice()==0&&productSearchDto.getMaxPrice()==0&&productSearchDto.getStartDate().isEmpty()&&productSearchDto.getProdCatecode()==0&&productSearchDto.getColorCode()==0&&productSearchDto.getProdSize().isEmpty()){
                    sql+=" where ";
                }
                else {
                    sql+=" and ";
                }
                sql+=" prodgender= "+"'"+productSearchDto.getProdGender()+"'";
            }
            //상품명 조건
            if(productSearchDto.getSearchKeyword().isEmpty()){}//상품명 조건으로 검색x
            else {
                if (productSearchDto.getMinPrice()==0&&productSearchDto.getMaxPrice()==0&&productSearchDto.getStartDate().isEmpty()&&productSearchDto.getProdCatecode()==0&&productSearchDto.getColorCode()==0&&productSearchDto.getProdSize().isEmpty()&&productSearchDto.getProdGender().isEmpty()){
                    sql+=" where ";
                }
                else {
                    sql+=" and ";
                }
                sql+=" prodname "+" like '%" + productSearchDto.getSearchKeyword() + "%'";//검색할 단어가 들어간 상품 검색
            }
            PreparedStatement ps=conn.prepareStatement(sql);
            System.out.println(" sql  = " +  sql );
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                ProductDto productDto=ProductDto.builder()
                        .prodDetailcode(rs.getInt("proddetailcode"))
                        .colorName(rs.getString("colorname"))
                        .prodDate(rs.getString("proddate"))
                        .prodGender(rs.getString("prodgender"))
                        .prodFilename(rs.getString("prodfilename"))
                        .prodName(rs.getString("prodname"))
                        .prodCatename(rs.getString("prodcatename"))
                        .prodSize(rs.getString("prodsize"))
                        .prodPrice(rs.getInt("prodprice"))
                        .build();
                list.add(productDto);
            }

        }catch (Exception e){System.out.println("e = " + e);}
        System.out.println("list = " + list);
        return list;
    }

}
