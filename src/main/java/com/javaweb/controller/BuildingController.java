package com.javaweb.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingDTO;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.service.BuildingService;

@RestController
public class BuildingController {
	@Autowired
	private BuildingService buildingService;
	
    @GetMapping(value = "/api/building")
    public List<BuildingDTO> getBuilding(@RequestParam Map<String,Object> params,
    									 @RequestParam( name = "typeCode",required = false) List<String> typeCode ) {
    	params.remove("typeCode");
    	List<BuildingDTO> result = buildingService.findAll(params,typeCode);
    	
        return result;
    }
    @PostMapping(value = "/api/building")
    public ResponseEntity <Void> addBuilding (@RequestBody BuildingRequestDTO buildingRequestDTO){
    	buildingService.saveBuilding(buildingRequestDTO);
    	return ResponseEntity.noContent().build();
    }


	
//    
//    @GetMapping(value = "/api/building/{id}")
//    public BuildingDTO getBuildingById(@PathVariable Long id) {
//    	BuildingDTO result = new BuildingDTO();
//    	BuildingEntity buildingEntity = buildingRepository.getOne(id);
//    	return result;
//    }
//    
//    @PutMapping(value = "/api/building")
//    public void updateBuilding (@RequestBody BuildingRequestDTO buldingRequestDTO ) {
//    	buildingService.updateBuilding(buldingRequestDTO);
//    	System.out.println("ok");
//    }
//    @DeleteMapping(value = "/api/building/{id}")
//    public void deleteBuilding(@PathVariable Long id) {
//    	buildingService.deleteBuilding(id);
//    	System.out.println("ok");
//    }
}
