package com.my.attendance.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.my.attendance.dao.AttendanceRepository;
import com.my.attendance.dto.AttendanceDTO;
import com.my.attendance.entity.AttendanceEntity;
import com.my.car.repository.CarRepository;
import com.my.car.service.CarMapper;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.member.entity.MemberEntity;
import com.my.member.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
	
	@Autowired
	private AttendanceRepository repository;
	
	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private AttendanceMapper model;

	@Override
	public List<AttendanceEntity> findAll() throws FindException {
		
		return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		
	} // findAll

	@Override
	public Page<AttendanceDTO> findAllByMemberId(String memberId, Pageable pageable) throws FindException {
	    log.warn("1. findByMemberId의 memberid ===> {} ", memberId);
	    
	    Page<AttendanceEntity> entityList= repository.findAllByMemberId(memberId, pageable);
	    model = new AttendanceMapper();
		return entityList.map(model::VoToDTO);

	} // findAllByMemberId

	@Override
	public void createAttendance(AttendanceDTO dto) throws AddException {

		// dto 객체로 들어온 것을 entiiy로 변환
		AttendanceEntity entity = model.DtoToVo(dto);

		log.warn("Service entity ===> {}", entity.getMemberId());
		
		// currentTime에 현재시간 대입
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        
        log.warn("현재시간: " + currentTime);
//        log.warn("아이디 1 : " + dto.getMemberId() );
        log.warn("아이디 2 : " + entity.getMemberId().getId() );
        
//        String mId = entity.getMemberId().getId();
//        
//        Optional<AttendanceEntity> existingAttendance = repository.findByMemberId(mId);
//        
//	      if (!existingAttendance.isPresent()) {
//	  	  
//	  	  log.warn("existingAttendance date : {}", existingAttendance.get());
//	  	  
//	  	  entity = existingAttendance.get();
//	  	  String existingDate = entity.getAttendanceDate();
//	  	 
//	  	  
//	    	if (currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) == existingDate) {
//	    		log.warn("이미 출석했습니다");
//	    		return;
//			
//		  	} else {
//		  		
//		        LocalTime onTimeStart = AttendanceTime.ON_TIME.getTime();
//		        LocalTime lateTimeEnd = AttendanceTime.LATE_TIME.getTime();
//		        LocalTime absenceTimeEnd = AttendanceTime.ABSENCE_TIME.getTime(); // 12시 이후
//		        
//		        if (currentTime.isBefore(onTimeStart)) {	// 9시까지 출근
//		      	  entity.setAttendanceDate(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//		      	  entity.setStartTime(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//		      	  entity.setStatus(AttendanceStatus.ON.getStatus());
//		      	  log.error("time ${}", entity.getStartTime());
//		        } else if (currentTime.isBefore(lateTimeEnd)) { // 9시부터 12시까지
//		      	  entity.setAttendanceDate(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//		      	  entity.setStartTime(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//		      	  entity.setStatus(AttendanceStatus.LATE.getStatus());
//		        } else if (currentTime.isAfter(absenceTimeEnd)) { // 12시 이후
//		      	  entity.setAttendanceDate(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//		      	  entity.setStartTime(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//		      	  entity.setStatus(AttendanceStatus.ABSENCE.getStatus());
//		        } // if-else
//		        
//		        repository.save(entity);
//		  		
//		  	} // inner if-else
//	  	  
//	    }

	      // ==
	      LocalTime onTimeStart = AttendanceTime.ON_TIME.getTime();
	      LocalTime lateTimeEnd = AttendanceTime.LATE_TIME.getTime();
	      LocalTime absenceTimeEnd = AttendanceTime.ABSENCE_TIME.getTime(); // 12시 이후
	      
	      if (currentTime.isBefore(onTimeStart)) {	// 9시까지 출근
	    	  entity.setAttendanceDate(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	    	  entity.setStartTime(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	    	  entity.setStatus(AttendanceStatus.ON.getStatus());
	    	  log.error("time ${}", entity.getStartTime());
	      } else if (currentTime.isBefore(lateTimeEnd)) { // 9시부터 12시까지
	    	  entity.setAttendanceDate(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	    	  entity.setStartTime(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	    	  entity.setStatus(AttendanceStatus.LATE.getStatus());
	      } else if (currentTime.isAfter(absenceTimeEnd)) { // 12시 이후
	    	  entity.setAttendanceDate(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	    	  entity.setStartTime(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	    	  entity.setStatus(AttendanceStatus.ABSENCE.getStatus());
	      } // if-else
	      
	      repository.save(entity);

	} // create


	@Override		// modify로 수정
	public void modifyAttendance(AttendanceDTO dto) throws ModifyException {

	    // dto 객체로 들어온 것을 entity로 변환
	    AttendanceEntity entity = model.DtoToVo(dto);

	    MemberEntity memberId = entity.getMemberId();

	    // repository에서 memberId를 사용하여 해당 엔터티를 찾음
	    AttendanceEntity existingEntity = repository.findByMemberId(memberId);

	    if (existingEntity != null) {
	    	
	        LocalTime currentTime = LocalTime.now();

	        existingEntity.setEndTime(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	        existingEntity.setStatus(AttendanceStatus.OFF.getStatus());

	        repository.save(existingEntity);
	        
	    } else {	
	        // 예외 처리 - 해당 memberId를 찾을 수 없는 경우
	        throw new ModifyException("Attendance for memberId " + memberId + " not found.");
	    }
	    
	} // end class

	
} // end class
