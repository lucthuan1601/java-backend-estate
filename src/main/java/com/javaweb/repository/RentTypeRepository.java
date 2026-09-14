package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.repository.custom.RentAreaRepositoryCustom;
import com.javaweb.repository.custom.RentTypeRepositoryCustom;
import com.javaweb.repository.entity.RentTypeEntity;

public interface RentTypeRepository extends JpaRepository<RentTypeEntity,Long>,RentTypeRepositoryCustom{

}
