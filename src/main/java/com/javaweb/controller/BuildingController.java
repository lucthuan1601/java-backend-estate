package com.javaweb.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingDTO;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.service.BuildingService;

@RestController
@RequestMapping(value = "/api/building")
public class BuildingController {
	@Autowired
	private BuildingService buildingService;
	
    @GetMapping
    public List<BuildingDTO> getBuilding(@RequestParam Map<String,Object> params,
    									 @RequestParam( name = "typeCode",required = false) List<String> typeCode ) {
    	params.remove("typeCode");
    	List<BuildingDTO> result = buildingService.findAll(params,typeCode);
    	
        return result;
    }
    @PostMapping
    public ResponseEntity <Void> addBuilding (@RequestBody BuildingRequestDTO buildingRequestDTO){
    	buildingService.saveBuilding(buildingRequestDTO);
    	return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity <Void> deleteBuilding (@PathVariable Long id) {
    	buildingService.deleteBuilding(id);
    	return ResponseEntity.noContent().build();
    }
    @DeleteMapping
    public ResponseEntity <Void> deleteBuildings (@RequestBody List<Long> ids) {
    	buildingService.deleteBuildings(ids);
    	return ResponseEntity.noContent().build();
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateBuilding (@PathVariable Long id,@RequestBody  BuildingRequestDTO buildingRequestDTO){
    	buildingService.updateBuilding(id, buildingRequestDTO);
    	return ResponseEntity.noContent().build();
    }
	
}
