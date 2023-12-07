package com.my.car.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.my.car.dto.CarDTO;
import com.my.car.dto.CarRentDTO;
import com.my.car.entity.CarEntity;
import com.my.car.entity.CarRentEntity;
import com.my.car.repository.CarRentRepository;
import com.my.car.repository.CarRepository;
import com.my.member.entity.MemberEntity;
import com.my.notification.entity.NotificationEntity;
import com.my.notification.service.NotificationServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CarServiceImpl implements CarService {
	private CarRepository cr;
	private CarRentRepository crr;
	
	// 찬석
	private NotificationServiceImpl notify;
	
	@Autowired
	public CarServiceImpl(CarRepository cr, CarRentRepository crr) {
		this.cr = cr;
		this.crr = crr;
	}
	
	// 찬석
	@Autowired
	public void NotificationServiceImpl(NotificationServiceImpl notify) {
	    this.notify = notify; // NotificationServiceImpl 주입
	}
	
	@Transactional
	public void modifyCarStatus() {
		LocalDate today = LocalDate.now();
		
//		String todaystring = "2023-12-01";
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		LocalDate today = LocalDate.parse(todaystring, formatter);
//		System.out.println("*************service: "+today);
		
		cr.saveEndCarStatus(today);
	}
	
	//*************** 차량 목록 **************************
	
	@Override
	public Page<CarDTO> findAllCarList(Pageable pageable, String startDate, String endDate) {
		Page<CarEntity> entityList = cr.findAllCarList(pageable, startDate, endDate);
		CarMapper cm = new CarMapper();
		return entityList.map(cm::entityToDto);
	}
	
//	@Override
//	public Page<CarDTO> findAllCarByDateSelect(Pageable pageable){
//		Page<CarEntity> entityList = cr.findAllCarList(pageable);
//		CarMapper cm = new CarMapper();
//		return entityList.map(cm::entityToDto);
//	}
	
	@Override
	public void createCarRent(CarRentDTO carRent) {
		CarRentMapper crm = new CarRentMapper();
		CarRentEntity entity = crm.dtoToEntity(carRent);
		System.out.println("entity carId   "+entity.getCar().getId());
		crr.save(entity);
		
		// 찬석
//		String memberEntity = entity.getMember().getId();
		MemberEntity memberEntity = entity.getMember();
		log.warn("차량예약 id : {}", memberEntity.getId());
		
//		Optional<CarEntity> optC = cr.findById(carRent.getCar().getId());
//		CarEntity carEntity = optC.get();
//		carEntity.modifyCarStatus((long)1);
//		cr.save(carEntity);
		
		log.warn("여기까진 오나..?");
		
	    // 찬석
	    notify.send(memberEntity, NotificationEntity.NotificationType.CAR, "차량이 등록 되었습니다.");

	}
	
	//****************** 차량 대여 목록 **************************
	
	
	@Override
	public Page<CarRentDTO> findAllMyCarRent(Pageable pageable, String memberId) {
		Page<CarRentEntity> entityList = crr.findAllByMemberIdOrderByReqDateDesc(pageable, memberId);
		CarRentMapper crm = new CarRentMapper();
		return entityList.map(crm::entityToDto);
	}
	
	@Override
	public void removeByIdCarRent(Long id) {
		crr.deleteById(id);
	}
	
	
	//***************** 차량 관리 메인 ************************
	
	@Override
	public List<CarDTO> findAllCarManage(){
		List<CarEntity> entityList = cr.findAll();
		List<CarDTO> dtoList = new ArrayList<>();
		for(CarEntity entity : entityList) {			
			CarMapper cm = new CarMapper();
			CarDTO dto = cm.entityToDto(entity);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	//***************** 차량 관리 승인 **************************
	
	@Override
	public Page<CarRentDTO> findAllApprove(Pageable pageable){
		Page<CarRentEntity> entityList = crr.findAllByStatusOrderByReqDate(pageable, (long)0);
		CarRentMapper crm = new CarRentMapper();
		return entityList.map(crm::entityToDto);
	}
	
	@Override
	public void modifyCarRentStatus(Long id, Long status) {
		Optional<CarRentEntity> optC = crr.findById(id);
		CarRentEntity carRentEntity = optC.get();
		carRentEntity.modifyCarRentStatus(status);
		crr.save(carRentEntity);
	}
	
	@Override
	public void saveCarRentReject(CarRentDTO carRent, Long status) {
		Optional<CarRentEntity> optC = crr.findById(carRent.getId());
		CarRentEntity carRentEntity = optC.get();
		carRentEntity.modifyCarRentStatus(status);
		carRentEntity.modifyCarRentReject(carRent.getReject());
		crr.save(carRentEntity);
		
	}
	
	//***************** 차량 관리 대여 목록 *********************
	
	@Override
	public Page<CarRentDTO> findAllRentList(Pageable pageable) {
		Page<CarRentEntity> entityList = crr.findAllRentList(pageable);
		CarRentMapper crm = new CarRentMapper();
		return entityList.map(crm::entityToDto);
	}
	
	//***************** 차량 관리 대여 미반납 *********************
	
	@Override
	public Page<CarRentDTO> findAllNoReturnList(Pageable pageable){
		Page<CarRentEntity> entityList = crr.findAllNoReturnList(pageable);
		CarRentMapper crm = new CarRentMapper();
		return entityList.map(crm::entityToDto);
	}
	
	//***************** 차량 관리 모든 예약 내역 *********************
	
	@Override
	public Page<CarRentDTO> findAllRentListAll(Pageable pageable){
		Page<CarRentEntity> entityList = crr.findAllByOrderByReqDateDesc(pageable);
		CarRentMapper crm = new CarRentMapper();
		return entityList.map(crm::entityToDto);
	}
}
