package com.my.notification.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.my.member.entity.MemberEntity;
import com.my.notification.dto.NotificationDTO;
import com.my.notification.dto.NotificationDTO.Response;
import com.my.notification.entity.NotificationEntity;

public interface NotificationService {

	// 구독
	public SseEmitter subscribe(String userName, String lastEventId);
	
	// 수신자에게 메시지 보내기
	public void send(MemberEntity id, NotificationEntity.NotificationType notificationType, String Content);
	
	// 알림 조회
	@Query("SELECT n FROM NotificationEntity n WHERE n.memberEntity.id = :receiverId")
	public List<Response> findAllByMemberId(String MemberId);
	
	// 미팅 참여자 알림 조회
	
}