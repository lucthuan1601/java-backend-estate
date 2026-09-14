package com.javaweb.repository.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.custom.BuildingRepositoryCustom;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.StringUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepositoryCustom
{

	@PersistenceContext
    private EntityManager entityManager;
	
	
	// 1. Hàm xử lý JOIN và các điều kiện phức tạp/đặc biệt
    private void queryJoinAndSpecial(BuildingSearchBuilder builder, Root<BuildingEntity> building, 
                                     CriteriaBuilder cb, List<Predicate> predicates) {
        // Join Staff
        if (builder.getStaffId() != null) {
            Join<Object, Object> users = building.join("users", JoinType.INNER);
            predicates.add(cb.equal(users.get("id"), builder.getStaffId()));
        }

        // Join RentType
        if (builder.getTypeCode() != null && !builder.getTypeCode().isEmpty()) {
            Join<Object, Object> rentTypes = building.join("rentTypes", JoinType.INNER);
            predicates.add(rentTypes.get("code").in(builder.getTypeCode()));
        }

        // Join RentArea
        if (builder.getAreaFrom() != null || builder.getAreaTo() != null) {
            Join<Object, Object> rentAreas = building.join("rentAreas", JoinType.INNER);
            if (builder.getAreaFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(rentAreas.get("value"), builder.getAreaFrom()));
            }
            if (builder.getAreaTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(rentAreas.get("value"), builder.getAreaTo()));
            }
        }

        // District
        if (builder.getDistrictId() != null) {
            predicates.add(cb.equal(building.get("district").get("id"), builder.getDistrictId()));
        }

        // Rent Price Range
        if (builder.getRentPriceFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(building.get("rentprice"), builder.getRentPriceFrom()));
        }
        if (builder.getRentPriceTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(building.get("rentprice"), builder.getRentPriceTo()));
        }
    }
	
    private void queryNormal(BuildingSearchBuilder builder, Root<BuildingEntity> building, 
            CriteriaBuilder cb, List<Predicate> predicates) {
    	if (StringUtil.checkString(builder.getName())) {
    		predicates.add(cb.like(building.get("name"), "%" + builder.getName().trim() + "%"));
    	}
    	if (StringUtil.checkString(builder.getStreet())) {
    		predicates.add(cb.like(building.get("street"), "%" + builder.getStreet().trim() + "%"));
    	}
    	if (StringUtil.checkString(builder.getWard())) {
    		predicates.add(cb.like(building.get("ward"), "%" + builder.getWard().trim() + "%"));
    	}
    	if (builder.getNumberOfBasement() != null) {
    		predicates.add(cb.equal(building.get("numberofbasement"), builder.getNumberOfBasement()));
    	}
    	if (builder.getFloorArea() != null) {
    			predicates.add(cb.equal(building.get("floorarea"), builder.getFloorArea()));
    	}
    	if (StringUtil.checkString(builder.getDirection())) {
    		predicates.add(cb.like(building.get("direction"), builder.getDirection().trim()));
    	}
    	if (StringUtil.checkString(builder.getLevel())) {
    		predicates.add(cb.like(building.get("level"), builder.getLevel().trim()));
    	}
    	if (StringUtil.checkString(builder.getManagerName())) {
    		predicates.add(cb.like(building.get("managername"), "%" + builder.getManagerName().trim() + "%"));
    	}
    	if (StringUtil.checkString(builder.getManagerPhoneNumber())) {
    		predicates.add(cb.like(building.get("managerphonenumber"), "%" + builder.getManagerPhoneNumber().trim() + "%"));
    	}
    }
	
    @Override
    public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder){
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();//Công cụ tạo các điều kiện(equal,like...)
    	// Khởi tạo CriteriaQuery: Khung của câu truy vấn, khai báo kiểu dữ liệu trả về là BuildingEntity
    	CriteriaQuery query = cb.createQuery(BuildingEntity.class);
    	// Định nghĩa bảng gốc (Root) để bắt đầu truy vấn: Tương đương "FROM building_entity"
    	Root<BuildingEntity> building = query.from(BuildingEntity.class);
        query.distinct(true);
     // Tạo danh sách chứa các điều kiện lọc (Predicates): Mặc định ban đầu là danh sách rỗng
        
    	List<Predicate> predicates = new ArrayList<>();
    	// Gọi các hàm phụ để xử lý từng nhóm điều kiện
        queryJoinAndSpecial(buildingSearchBuilder, building, cb, predicates);
        queryNormal(buildingSearchBuilder, building, cb, predicates);

        // Gom điều kiện và thực thi
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
	
}