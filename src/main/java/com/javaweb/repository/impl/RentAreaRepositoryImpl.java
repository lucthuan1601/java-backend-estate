package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.utils.ConnectionJDBCUtil;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository {

	@Override
	public List<RentAreaEntity> getValueByBuildingId(Long id) {
	    String sql = "SELECT * FROM rentarea ra WHERE ra.buildingid = ?";
	    List<RentAreaEntity> result = new ArrayList<>();

	    try (Connection conn = ConnectionJDBCUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setLong(1, id);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                RentAreaEntity entity = new RentAreaEntity();
	                entity.setId(rs.getLong("id"));
	               // entity.setValue((rs.getLong("value"));
	                entity.setBuildingid(rs.getLong("buildingid"));
	                result.add(entity);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return result;
	}
}