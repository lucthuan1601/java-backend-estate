package com.javaweb.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaweb.model.BuildingDTO;
import com.javaweb.model.BuildingRequestDTO;

public interface BuildingService {
	List<BuildingDTO> findAll( Map<String,Object> params,List<String> typeCode);
	void saveBuilding(BuildingRequestDTO buildingRequestDTO);
}
