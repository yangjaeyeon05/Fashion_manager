package web.model.dao;

import org.springframework.stereotype.Component;
import web.model.dto.ReplyDto;
import web.model.dto.VendorDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class VendorDao extends Dao{

    // 1. 거래처목록출력
    public List<VendorDto> vendorallread(String searchKey , String searchKeyword){
        List<VendorDto> list = new ArrayList<>();
        try {
            String sql = "select * from vendor";
            if(searchKeyword == ""){

            }else {
                sql += " where " + searchKey + " like '%" + searchKeyword + "%'";
            }
            System.out.println(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                VendorDto vendorDto = VendorDto.builder()
                        .vendorcode(rs.getInt("vendorcode"))
                        .vname(rs.getString("vname"))
                        .vcontact(rs.getString("vcontact"))
                        .vaddress(rs.getString("vaddress"))
                        .vdate(rs.getString("vdate"))
                        .build();
                list.add(vendorDto);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return list;
    }   // vendorallread() end

    // 2. 거래처등록
    public boolean vendorAdd(VendorDto vendorDto){
        System.out.println("VendorDao.vendorAdd");
        System.out.println("vendorDto = " + vendorDto);
        try{
            String sql = "insert into vendor(vname , vcontact , vaddress) values(? , ? , ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1 , vendorDto.getVname());
            ps.setString(2 , vendorDto.getVcontact());
            ps.setString(3 , vendorDto.getVaddress());
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // vendorAdd() end

    // 3. 거래처삭제
    public boolean vendorDelete(int vendorcode){
        System.out.println("VendorDao.vendorDelete");
        System.out.println("vendorcode = " + vendorcode);
        try{
            String sql = "delete from vendor where vendorcode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,vendorcode);
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // vendorDelete() end

    // 4. 수정 전 정보 가져오기
    public VendorDto infoRead(int vendorcode){
        System.out.println("VendorDao.infoRead");
        System.out.println("vendorcode = " + vendorcode);
        VendorDto vendorDto = new VendorDto();
        try{
            String sql = "select * from vendor where vendorcode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1 , vendorcode);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                vendorDto = VendorDto.builder()
                        .vname(rs.getString("vname"))
                        .vcontact(rs.getString("vcontact"))
                        .vaddress(rs.getString("vaddress"))
                        .build();
                return vendorDto;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }   // infoRead() end

    // 5. 거래처정보수정
    public Boolean infoUpdate(VendorDto vendorDto){
        System.out.println("VendorDao.infoUpdate");
        System.out.println("vendorDto = " + vendorDto);
        try{
            String sql = "update vendor set vcontact = ? , vaddress = ? where vendorcode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, vendorDto.getVcontact());
            ps.setString(2 , vendorDto.getVaddress());
            ps.setInt(3 , vendorDto.getVendorcode());
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // infoUpdate() end

}   // class end
