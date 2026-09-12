package com.javaweb.repository.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.repository.entity.RentTypeEntity;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void joinTable(BuildingSearchBuilder buildingSearchBuilder, StringBuilder sql) {
        Long staffId = buildingSearchBuilder.getStaffId();
        if (staffId != null) {
            sql.append(" INNER JOIN b.users staff ");
        }
        
        List<String> typeCode = buildingSearchBuilder.getTypeCode();
        if (typeCode != null && !typeCode.isEmpty()) {
            sql.append(" INNER JOIN b.rentTypes rt ");
        }

        Long areaFrom = buildingSearchBuilder.getAreaFrom();
        Long areaTo = buildingSearchBuilder.getAreaTo();
        if (areaTo != null || areaFrom != null) {
            sql.append(" INNER JOIN b.rentAreas ra ");
        }
    }

    public void queryNormal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
        try {
            Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
            for (Field item : fields) {
                item.setAccessible(true);
                String fieldName = item.getName();

                if (!fieldName.equals("staffId") && !fieldName.equals("typeCode")
                        && !fieldName.startsWith("area")
                        && !fieldName.startsWith("rentPrice")
                        && !fieldName.startsWith("districtId")){

                    Object fieldValue = item.get(buildingSearchBuilder);

                    if (fieldValue != null) {
                        String value = fieldValue.toString().trim();

                        if (StringUtil.checkString(value)) {
                            if (NumberUtil.isNumber(value)) {
                                where.append(" AND b.").append(fieldName).append(" = ").append(value);
                            } else {
                                String safeValue = value.replace("'", "''");
                                where.append(" AND b.").append(fieldName).append(" LIKE '%").append(safeValue).append("%' ");
                            }
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void querySpecial(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
        Long staffId = buildingSearchBuilder.getStaffId();
        if (staffId != null) {
            where.append(" AND staff.id = ").append(staffId);
        }

        Long districtId = buildingSearchBuilder.getDistrictId();
        if (districtId != null) {
            where.append(" AND b.district.id = ").append(districtId);
        }
        
        Long rentAreaTo = buildingSearchBuilder.getAreaTo();
        Long rentAreaFrom = buildingSearchBuilder.getAreaFrom();
        if (rentAreaTo != null || rentAreaFrom != null) {
            if (rentAreaFrom != null) {
                where.append(" AND ra.value >= ").append(rentAreaFrom);
            }
            if (rentAreaTo != null) {
                where.append(" AND ra.value <= ").append(rentAreaTo);
            }
        }

        Long rentPriceTo = buildingSearchBuilder.getRentPriceTo();
        Long rentPriceFrom = buildingSearchBuilder.getRentPriceFrom();
        if (rentPriceTo != null || rentPriceFrom != null) {
            if (rentPriceFrom != null) {
                where.append(" AND b.rentprice >= ").append(rentPriceFrom);
            }
            if (rentPriceTo != null) {
                where.append(" AND b.rentprice <= ").append(rentPriceTo);
            }
        }

        List<String> typeCode = buildingSearchBuilder.getTypeCode();
        if (typeCode != null && !typeCode.isEmpty()) {
            String sqlTypes = typeCode.stream()
                                      .map(code -> "'" + code + "'")
                                      .collect(Collectors.joining(","));
            where.append(" AND rt.code IN(").append(sqlTypes).append(") ");
        }
    }

    @Override
    public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT b FROM BuildingEntity b ");
        
        joinTable(buildingSearchBuilder, sql);
        
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
        queryNormal(buildingSearchBuilder, where);
        querySpecial(buildingSearchBuilder, where);
        
        sql.append(where);

        Query query = entityManager.createQuery(sql.toString(), BuildingEntity.class);
        
        return query.getResultList();
    }

	@Override
	@Transactional
	public void createBuilding(BuildingRequestDTO buildingRequestDTO) {
		// TODO Auto-generated method stub
		BuildingEntity buildingEntity = new BuildingEntity();
		
		buildingEntity.setId(buildingRequestDTO.getId());
		buildingEntity.setName(buildingRequestDTO.getName());
	    buildingEntity.setStreet(buildingRequestDTO.getStreet());
	    buildingEntity.setWard(buildingRequestDTO.getWard());
	    buildingEntity.setStructure(buildingRequestDTO.getStructure());
	    buildingEntity.setNumberofbasement(buildingRequestDTO.getNumberOfBasement());
	    buildingEntity.setFloorarea(buildingRequestDTO.getFloorArea());
	    buildingEntity.setDirection(buildingRequestDTO.getDirection());
	    buildingEntity.setLevel(buildingRequestDTO.getLevel());
	    buildingEntity.setRentprice(buildingRequestDTO.getRentPrice());
	    buildingEntity.setRentpricedescription(buildingRequestDTO.getRentPriceDescription());
	    buildingEntity.setServicefee(buildingRequestDTO.getServiceFee());
	    buildingEntity.setCarfee(buildingRequestDTO.getCarFee());
	    buildingEntity.setMotorbikefee(buildingRequestDTO.getMotorbikeFee());
	    buildingEntity.setOvertimefee(buildingRequestDTO.getOvertimeFee());
	    buildingEntity.setWaterfee(buildingRequestDTO.getWaterFee());
	    buildingEntity.setElectricityfee(buildingRequestDTO.getElectricityFee());
	    buildingEntity.setDeposit(buildingRequestDTO.getDeposit());
	    buildingEntity.setPayment(buildingRequestDTO.getPayment());
	    buildingEntity.setRenttime(buildingRequestDTO.getRentTime());
	    buildingEntity.setDecorationtime(buildingRequestDTO.getDecorationTime());
	    buildingEntity.setBrokeragefee(buildingRequestDTO.getBrokerageFee());
	    buildingEntity.setNote(buildingRequestDTO.getNote());
	    buildingEntity.setLinkofbuilding(buildingRequestDTO.getLinkOfBuilding());
	    buildingEntity.setMap(buildingRequestDTO.getMap());
	    buildingEntity.setImage(buildingRequestDTO.getImage());
	    buildingEntity.setManagername(buildingRequestDTO.getManagerName());
	    buildingEntity.setManagerphonenumber(buildingRequestDTO.getManagerPhoneNumber());

	 // 2. Set Quan hệ District (Quận) qua EntityManager
        if (buildingRequestDTO.getDistrictId() != null) {
            DistrictEntity districtEntity = entityManager.find(DistrictEntity.class, buildingRequestDTO.getDistrictId());
            buildingEntity.setDistrict(districtEntity);
        }

        // 3. Xử lý chuỗi RentArea (VD: "100, 200, 300" -> List<RentAreaEntity>)
        if (buildingRequestDTO.getRentArea() != null && !buildingRequestDTO.getRentArea().trim().isEmpty()) {
            List<RentAreaEntity> rentAreaEntities = new ArrayList<>();
            String[] areas = buildingRequestDTO.getRentArea().split(",");
            for (String area : areas) {
                if (!area.trim().isEmpty()) {
                    RentAreaEntity rentAreaEntity = new RentAreaEntity();
                    rentAreaEntity.setValue(Integer.parseInt(area.trim()));
                    rentAreaEntity.setBuilding(buildingEntity);
                    rentAreaEntities.add(rentAreaEntity);
                }
            }
            buildingEntity.setRentAreas(rentAreaEntities);
        }

        // 4. Xử lý danh sách RentType (Loại tòa nhà) qua EntityManager
        if (buildingRequestDTO.getTypeCode() != null && !buildingRequestDTO.getTypeCode().isEmpty()) {
            List<RentTypeEntity> rentTypeEntities = entityManager.createQuery(
                    "SELECT r FROM RentTypeEntity r WHERE r.code IN :codes", RentTypeEntity.class)
                    .setParameter("codes", buildingRequestDTO.getTypeCode())
                    .getResultList();
            buildingEntity.setRentTypes(rentTypeEntities);
        }

        // 5. Lưu xuống Database thông qua EntityManager
        if (buildingEntity.getId() == null) {
            entityManager.persist(buildingEntity);
        } else {
            entityManager.merge(buildingEntity);
        }
        
       
}

	@Override
	public void updateBuilding(BuildingRequestDTO buildingRequestDTO) {
		// TODO Auto-generated method stub
		// 1. Kiểm tra ID tồn tại
	    if (buildingRequestDTO.getId() == null) {
	        return;
	    }

	    // 2. Tìm Entity hiện tại trong Database
	    BuildingEntity buildingEntity = entityManager.find(BuildingEntity.class, buildingRequestDTO.getId());
	    if (buildingEntity == null) {
	        return; // Hoặc ném Exception: throw new NotFoundException("Building not found");
	    }

	    // 3. Cập nhật các trường thông tin cơ bản
	    buildingEntity.setName(buildingRequestDTO.getName());
	    buildingEntity.setStreet(buildingRequestDTO.getStreet());
	    buildingEntity.setWard(buildingRequestDTO.getWard());
	    buildingEntity.setStructure(buildingRequestDTO.getStructure());
	    buildingEntity.setNumberofbasement(buildingRequestDTO.getNumberOfBasement());
	    buildingEntity.setFloorarea(buildingRequestDTO.getFloorArea());
	    buildingEntity.setDirection(buildingRequestDTO.getDirection());
	    buildingEntity.setLevel(buildingRequestDTO.getLevel());
	    buildingEntity.setRentprice(buildingRequestDTO.getRentPrice());
	    buildingEntity.setRentpricedescription(buildingRequestDTO.getRentPriceDescription());
	    buildingEntity.setServicefee(buildingRequestDTO.getServiceFee());
	    buildingEntity.setCarfee(buildingRequestDTO.getCarFee());
	    buildingEntity.setMotorbikefee(buildingRequestDTO.getMotorbikeFee());
	    buildingEntity.setOvertimefee(buildingRequestDTO.getOvertimeFee());
	    buildingEntity.setWaterfee(buildingRequestDTO.getWaterFee());
	    buildingEntity.setElectricityfee(buildingRequestDTO.getElectricityFee());
	    buildingEntity.setDeposit(buildingRequestDTO.getDeposit());
	    buildingEntity.setPayment(buildingRequestDTO.getPayment());
	    buildingEntity.setRenttime(buildingRequestDTO.getRentTime());
	    buildingEntity.setDecorationtime(buildingRequestDTO.getDecorationTime());
	    buildingEntity.setBrokeragefee(buildingRequestDTO.getBrokerageFee());
	    buildingEntity.setNote(buildingRequestDTO.getNote());
	    buildingEntity.setLinkofbuilding(buildingRequestDTO.getLinkOfBuilding());
	    buildingEntity.setMap(buildingRequestDTO.getMap());
	    buildingEntity.setImage(buildingRequestDTO.getImage());
	    buildingEntity.setManagername(buildingRequestDTO.getManagerName());
	    buildingEntity.setManagerphonenumber(buildingRequestDTO.getManagerPhoneNumber());

	    // 4. Cập nhật Quận (District)
	    if (buildingRequestDTO.getDistrictId() != null) {
	        DistrictEntity districtEntity = entityManager.find(DistrictEntity.class, buildingRequestDTO.getDistrictId());
	        buildingEntity.setDistrict(districtEntity);
	    } else {
	        buildingEntity.setDistrict(null);
	    }

	    // 5. Cập nhật Diện tích thuê (RentArea) - Xóa danh sách cũ & Thêm danh sách mới
	    if (buildingEntity.getRentAreas() != null) {
	        // Xóa liên kết cũ trong bảng rentarea
	        entityManager.createQuery("DELETE FROM RentAreaEntity r WHERE r.building.id = :buildingId")
	                     .setParameter("buildingId", buildingEntity.getId())
	                     .executeUpdate();
	        buildingEntity.getRentAreas().clear();
	    } else {
	        buildingEntity.setRentAreas(new ArrayList<>());
	    }

	    if (buildingRequestDTO.getRentArea() != null && !buildingRequestDTO.getRentArea().trim().isEmpty()) {
	        String[] areas = buildingRequestDTO.getRentArea().split(",");
	        for (String area : areas) {
	            if (!area.trim().isEmpty()) {
	                RentAreaEntity rentAreaEntity = new RentAreaEntity();
	                rentAreaEntity.setValue(Integer.parseInt(area.trim()));
	                rentAreaEntity.setBuilding(buildingEntity);
	                buildingEntity.getRentAreas().add(rentAreaEntity);
	            }
	        }
	    }

	    // 6. Cập nhật Loại tòa nhà (RentType)
	    if (buildingRequestDTO.getTypeCode() != null && !buildingRequestDTO.getTypeCode().isEmpty()) {
	        List<RentTypeEntity> rentTypeEntities = entityManager.createQuery(
	                "SELECT r FROM RentTypeEntity r WHERE r.code IN :codes", RentTypeEntity.class)
	                .setParameter("codes", buildingRequestDTO.getTypeCode())
	                .getResultList();
	        buildingEntity.setRentTypes(rentTypeEntities);
	    } else {
	        buildingEntity.getRentTypes().clear();
	    }

	    // 7. Lưu thay đổi
	    // Lưu ý: Nhờ tính chất Persistence Context của JPA, Entity sau khi sửa đổi trong hàm @Transactional 
	    // sẽ tự động được flush/commit xuống DB mà không nhất thiết phải gọi merge().
	    entityManager.merge(buildingEntity);
		
	}
	
	@Override
	public void deleteBuilding(Long id) {
		if (id == null) return;

	    // 1. Tìm tòa nhà trong Database
	    BuildingEntity buildingEntity = entityManager.find(BuildingEntity.class, id);
	    if (buildingEntity == null) {
	        return; // Hoặc ném Exception: throw new NotFoundException("Tòa nhà không tồn tại!");
	    }

	    // 2. Xóa các diện tích thuê (RentArea) liên quan trước
	    entityManager.createQuery("DELETE FROM RentAreaEntity r WHERE r.building.id = :buildingId")
	                 .setParameter("buildingId", id)
	                 .executeUpdate();

	    // 3. Xóa liên kết Loại tòa nhà (RentType) trong bảng trung gian (nếu dùng @ManyToMany)
	    if (buildingEntity.getRentTypes() != null) {
	        buildingEntity.getRentTypes().clear();
	    }

	    // 4. Thực hiện xóa Tòa nhà
	    entityManager.remove(buildingEntity);
	}
}