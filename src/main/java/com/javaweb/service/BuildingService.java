package com.javaweb.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaweb.model.BuildingDTO;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.repository.entity.BuildingEntity;

public interface BuildingService {
	List<BuildingDTO> findAll( Map<String,Object> params,List<String> typeCode);
	void saveBuilding(BuildingRequestDTO buildingRequestDTO);
	void deleteBuilding(Long id);
	void deleteBuildings(List<Long> ids);
	void updateBuilding(Long id, BuildingRequestDTO buildingRequestDTO);
}
