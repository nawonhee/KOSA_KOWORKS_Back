package com.my.stuff.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.member.entity.MemberEntity;
import com.my.stuff.entity.StuffEntity;
import com.my.stuff.entity.StuffReqEntity;

@Repository
public interface StuffReqRepository extends JpaRepository<StuffReqEntity, Long> {
	
	/**
	 * stuff_req 테이블에서 사용자가 작성한 비품요청 행을 조회하여 반환한다
	 * @param member
	 * @return List<StuffReqEntity>
	 */
	List<StuffReqEntity> findByMember(MemberEntity member);
	
	/**
	 * stuff_req 테이블에서 사용자가 작성한 비품 요청 행 중 stuffId에 인자로 넘겨진 문자열이 포함된 행들을 조회하여 반환한다 
	 * @param MemberEntity, StuffEntity
	 * @return <StuffReqEntity>
	 */
	List<StuffReqEntity> findByMemberAndStuffLike(MemberEntity member, StuffEntity stuff);
	
	/**
	 * stuff_req 테이블에서 사용자가 작성한 비품 요청 행 중 stuffId에 압력한 문자열이포함되는 경우, status값이 일치라는 경우를 반환한다
	 * @param MemberEntity, StuffEntity status
	 * @return <StuffReqEntity>
	 */
	List<StuffReqEntity> findByMemberAndStatusAndStuffLike(MemberEntity member, Long status, StuffEntity stuff);
	
	/**
	 * stuff_req 테이블에서 사용자가 작성한 비품 요청 행 중 stuffId에 압력한 문자열이포함되는 경우, status값이 일치라는 경우를 반환한다
	 * @param MemberEntity, StuffEntity status
	 * @return <StuffReqEntity>
	 */
	List<StuffReqEntity> findByMemberAndStatusBetweenAndStuffLike(MemberEntity member, Long startS, Long endS, StuffEntity stuff);
	
//	/**
//	 * stuff_req 테이블에서 사용자가 작성한 비품 요청 행 중 stuffId에 압력한 문자열이포함되는 경우, status값이 일치라는 경우, 입력한 날짜 범위에 해당하는 경우를 반환한다
//	 * @param MemberEntity, StuffEntity status
//	 * @return <StuffReqEntity>
//	 */
//	List<StuffReqEntity> findByMemberAndStatusAndStuffLikeAndDateBetween(MemberEntity member, Long status, StuffEntity stuff, Date startDate, Date endDate);

}
