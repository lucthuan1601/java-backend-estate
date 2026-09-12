package com.javaweb.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.repository.entity.BuildingEntity;

public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {

	List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder);
	
	void createBuilding (BuildingRequestDTO buldingRequestDTO);
	
	void updateBuilding(BuildingRequestDTO buildingRequestDTO);
	
	
	
	
	void deleteBuilding(Long id);
}
