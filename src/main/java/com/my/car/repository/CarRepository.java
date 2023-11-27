package com.my.car.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.car.entity.CarEntity;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, String> {
	List<CarEntity> findAllByOrderByStatusAsc();
}
