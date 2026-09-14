package com.javaweb.repository.custom;

import java.util.List;

import com.javaweb.repository.entity.RentTypeEntity;

public interface RentTypeRepositoryCustom {
	List<RentTypeEntity> findByCodeIn(List<String> codes);
}
