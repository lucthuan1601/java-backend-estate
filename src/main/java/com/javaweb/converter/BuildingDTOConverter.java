//package com.javaweb.converter;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.javaweb.model.BuildingDTO;
//import com.javaweb.repository.DistrictRepository;
//import com.javaweb.repository.RentAreaRepository;
//import com.javaweb.repository.entity.BuildingEntity;
//import com.javaweb.repository.entity.DistrictEntity;
//import com.javaweb.repository.entity.RentAreaEntity;
//
//@Component
//public class BuildingDTOConverter {
//	@Autowired
//	private DistrictRepository districtRepository;
//	
//	@Autowired
//	private RentAreaRepository rentAreaRepository;
//	
//	@Autowired
//	private ModelMapper modelMapper;
//	
//	public BuildingDTO toBuildingDTO (BuildingEntity item) {
//		BuildingDTO building = modelMapper.map(item, BuildingDTO.class);
//		
////		building.setId(item.getId());
////		building.setName(item.getName());
////		building.setDistrictId(item.getDistrictid());
////		building.setStreet(item.getStreet());
////		building.setWard(item.getWard());
//		
//		String districtName = "";
//        if (item.getDistrictId() != null) {
//            List<DistrictEntity> districts = districtRepository.getValueDistrict(item.getDistrictId());
//            if (!districts.isEmpty()) {
//                districtName = districts.get(0).getName(); // Lấy tên quận từ phần tử đầu tiên
//            }
//        }
//		building.setAddress(item.getStreet() + ", " + item.getWard() + "," + districtName);
//		
//		List<RentAreaEntity> rentAreaEntities = rentAreaRepository.getValueByBuildingId(item.getId());
//		String rentResult = rentAreaEntities.stream().map(it->it.getValue().toString()).collect(Collectors.joining(","));
//		building.setRentArea(rentResult);
//
////		building.setNumberOfBasement(item.getNumberofbasement());
////		building.setFloorArea(item.getFloorarea());
////		building.setRentPrice(item.getRentprice());
////		building.setServiceFee(item.getServicefee());
////		building.setBrokerageFee(item.getBrokeragefee());
////		building.setManagerName(item.getManagername());
////		building.setManagerPhoneNumber(item.getManagerphonenumber());
////		building.setEmptyArea(item.getEmptyArea());
//		
//		return building;
//	}
//}




package com.javaweb.converter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;

@Component
public class BuildingDTOConverter {
//	@Autowired
//	private DistrictRepository districtRepository;
//	
//	@Autowired
//	private RentAreaRepository rentAreaRepository;
//	
	@Autowired
	private ModelMapper modelMapper;
	
	public BuildingDTO toBuildingDTO (BuildingEntity item) {
		BuildingDTO building = modelMapper.map(item, BuildingDTO.class);

		building.setAddress(item.getStreet() + ", " + item.getWard() + ", " + item.getDistrict().getName());
		List<RentAreaEntity> rentAreas = item.getRentAreas();
		String rentResult = rentAreas.stream().map(it->it.getValue().toString()).collect(Collectors.joining(","));
		building.setRentArea(rentResult);
		
		return building;
	}
}