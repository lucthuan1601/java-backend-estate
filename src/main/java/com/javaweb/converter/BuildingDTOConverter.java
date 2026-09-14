package com.javaweb.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.model.BuildingDTO;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentTypeRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.repository.entity.RentTypeEntity;
import com.javaweb.utils.StringUtil;

@Component
public class BuildingDTOConverter {	
	@Autowired
	private ModelMapper modelMapper;
	
    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private RentTypeRepository rentTypeRepository;
    //Chuyển đổi Entity thành DTO
	public BuildingDTO toBuildingDTO (BuildingEntity item) {
		BuildingDTO building = modelMapper.map(item, BuildingDTO.class);
		
		building.setAddress(item.getStreet() + ", " + item.getWard() + ", " + item.getDistrict().getName());
		List<RentAreaEntity> rentAreas = item.getRentAreas();
		String rentResult = rentAreas.stream().map(it->it.getValue().toString()).collect(Collectors.joining(","));
		building.setRentArea(rentResult);
		return building;
	}
	
	
	
	public BuildingEntity toBuildingEntity (BuildingRequestDTO buildingRequestDTO) {
		BuildingEntity buildingEntity = modelMapper.map(buildingRequestDTO, BuildingEntity.class);
		
		if(buildingRequestDTO.getDistrictId() != null) {
			DistrictEntity district = districtRepository.findById(buildingRequestDTO.getDistrictId()).orElse(null);
			buildingEntity.setDistrict(district);
		}
		// 3. Xử lý tách chuỗi RentArea (VD: "100, 200" -> List<RentAreaEntity>)
        if (StringUtil.checkString(buildingRequestDTO.getRentArea())) {
            List<RentAreaEntity> rentAreas = new ArrayList<>();
            String[] areas = buildingRequestDTO.getRentArea().split(",");
            for (String area : areas) {
                if (StringUtil.checkString(area)) {
                    RentAreaEntity rentAreaEntity = new RentAreaEntity();
                    rentAreaEntity.setValue(Integer.parseInt(area.trim()));
                    rentAreaEntity.setBuilding(buildingEntity); // Gán quan hệ 2 chiều
                    rentAreas.add(rentAreaEntity);
                }
            }
            buildingEntity.setRentAreas(rentAreas);
        }

        // 4. Xử lý Loại tòa nhà RentType (VD: ["TANG_TRET", "NGUYEN_CAN"] -> List<RentTypeEntity>)
        if (buildingRequestDTO.getTypeCode() != null && !buildingRequestDTO.getTypeCode().isEmpty()) {
            List<RentTypeEntity> rentTypes = rentTypeRepository.findByCodeIn(buildingRequestDTO.getTypeCode());
            buildingEntity.setRentTypes(rentTypes);
        }

        return buildingEntity;
	}
	
	// Bổ sung phương thức này vào class BuildingDTOConverter
	public BuildingEntity toBuildingEntityForUpdate(BuildingRequestDTO buildingRequestDTO, BuildingEntity buildingEntity) {
	    // 1. Map các trường dữ liệu cơ bản (ghi đè lên entity cũ)
		
		modelMapper.typeMap(BuildingRequestDTO.class, BuildingEntity.class)
		.addMappings(mapper->mapper.skip(BuildingEntity::setId));
		
	    modelMapper.map(buildingRequestDTO, buildingEntity);

	    // 2. Cập nhật District
	    if (buildingRequestDTO.getDistrictId() != null) {
	        DistrictEntity district = districtRepository.findById(buildingRequestDTO.getDistrictId()).orElse(null);
	        buildingEntity.setDistrict(district);
	    }

	    // 3. Cập nhật RentArea (Xóa các diện tích cũ và ghi đè danh sách mới)
	    if (StringUtil.checkString(buildingRequestDTO.getRentArea())) {
	        // Nhờ orphanRemoval = true, clear() sẽ tự động dọn sạch các bản ghi rentarea cũ trong DB
	        buildingEntity.getRentAreas().clear();

	        String[] areas = buildingRequestDTO.getRentArea().split(",");
	        for (String area : areas) {
	            if (StringUtil.checkString(area)) {
	                RentAreaEntity rentAreaEntity = new RentAreaEntity();
	                rentAreaEntity.setValue(Integer.parseInt(area.trim()));
	                rentAreaEntity.setBuilding(buildingEntity);
	                buildingEntity.getRentAreas().add(rentAreaEntity);
	            }
	        }
	    }

	    // 4. Cập nhật RentType (Cập nhật bảng trung gian)
	    if (buildingRequestDTO.getTypeCode() != null && !buildingRequestDTO.getTypeCode().isEmpty()) {
	        List<RentTypeEntity> rentTypes = rentTypeRepository.findByCodeIn(buildingRequestDTO.getTypeCode());
	        buildingEntity.setRentTypes(rentTypes);
	    }

	    return buildingEntity;
	}
}