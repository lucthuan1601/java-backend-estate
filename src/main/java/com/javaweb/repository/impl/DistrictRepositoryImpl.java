package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.utils.ConnectionJDBCUtil;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository{
	
	@Override
	public List<DistrictEntity> getValueDistrict(Long id) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(" SELECT d.id, d.code, d.name FROM district d where 1 = 1 ").append(" AND d.id = " + id + " ");
		 
        List<DistrictEntity> result = new ArrayList<>();
        
        try (Connection conn = ConnectionJDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString());) {
            
            while(rs.next()) {
            	DistrictEntity districtEntity = new DistrictEntity();
            
//            	districtEntity.setDistrictid(rs.getLong("id"));
            	districtEntity.setName(rs.getString("name"));
            	districtEntity.setCode(rs.getString("code"));;
            	
                result.add(districtEntity);
            }
       
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("connected database failed...");
        }
        
        return result;
	}
}
