package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.converter.BuildingDTOConverter;
import com.javaweb.converter.BuildingSearchBuilderConverter;
import com.javaweb.model.BuildingDTO;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.service.BuildingService;

@Service
@Transactional
public class BuildingServiceImpl implements BuildingService {
	@Autowired
	private BuildingRepository buildingRepository;
		
	@Autowired
	private BuildingDTOConverter buildingDTOConverter;
	
	@Autowired
	private BuildingSearchBuilderConverter buildingSearchBuilderConverter;
	
	
	//Tìm kiếm tòa nhà theo 16 field
	public List<BuildingDTO> findAll(Map<String,Object> params, List<String> typeCode) {
		// TODO Auto-generated method stub
		//tại sao sử dụng builder
		BuildingSearchBuilder buildingSearchBuilder = buildingSearchBuilderConverter.toBuildingSearchBuilder(params, typeCode);
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(buildingSearchBuilder);
		
		List<BuildingDTO> result = new ArrayList<>();

		for (BuildingEntity item : buildingEntities) {
			BuildingDTO building = buildingDTOConverter.toBuildingDTO(item);
			result.add(building);
		}
		return result;
		
	}


	//Thêm thông tin tòa nhà mới
	@Override
	public void saveBuilding(BuildingRequestDTO buildingRequestDTO) {
		// TODO Auto-generated method stub
		BuildingEntity buildingEntity = buildingDTOConverter.toBuildingEntity(buildingRequestDTO);
		buildingRepository.save(buildingEntity);
	}
	//Xóa 1 tòa nhà
	@Override
	public void deleteBuilding (Long id) {
		BuildingEntity buildingEntity = buildingRepository.findById(id).orElseThrow(()-> new RuntimeException("Tòa nhà không tồn tại với Id:"+id));
		buildingRepository.delete(buildingEntity);
	}


	@Override
	public void deleteBuildings(List<Long> ids) {
		// TODO Auto-generated method stub
		List<BuildingEntity> buildingEntities = buildingRepository.findAllById(ids);
		if(buildingEntities.isEmpty()) {
			throw new RuntimeException("Không tìm thấy tòa nhà nào để xóa");
		}
		buildingRepository.deleteAll(buildingEntities);
		
	}


	@Override
	public void updateBuilding(Long id, BuildingRequestDTO buildingRequestDTO) {
		// TODO Auto-generated method stub
		BuildingEntity buildingEntity = buildingRepository.findById(id).orElseThrow(()->new RuntimeException("Tòa nhà không tồn tại với Id:"+id));
		buildingEntity = buildingDTOConverter.toBuildingEntityForUpdate(buildingRequestDTO, buildingEntity);
		buildingRepository.save(buildingEntity);
	}
	


	
	
	







	
}