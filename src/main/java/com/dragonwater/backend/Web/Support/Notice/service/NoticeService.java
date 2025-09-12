package com.dragonwater.backend.Web.Support.Notice.service;

import com.dragonwater.backend.Web.Support.Notice.domain.Notices;
import com.dragonwater.backend.Web.Support.Notice.dto.AdminNoticeAddReqDto;
import com.dragonwater.backend.Web.Support.Notice.dto.AdminNoticeResDto;
import com.dragonwater.backend.Web.Support.Notice.dto.NoticeReqDto;
import com.dragonwater.backend.Web.Support.Notice.dto.NoticeResDto;
import java.util.Queue;

public interface NoticeService {

    /**
     * 모든 공지사항 가져오는 메소드
     * @return
     */
    Queue<AdminNoticeResDto> getAdminAllNotices();


    /**
     * 모든 공지사항을 가져오는 메소드 - 위의 메소드와 차이가 뭔지 모르겠다 .... ㅠㅠㅠ
     * @return
     */
    Queue<NoticeResDto> getAllNotices();

    /**
     * 공지사항을 등록하는 메소드
     * @param dto 공지사항 내용에 대한 DTO
     * @return
     */
    Notices addNotices(AdminNoticeAddReqDto dto);

    /**
     * 공지사항을 수정하는 메소드
     * @param dto 공지사항 수정 내용에 대한 DTO
     * @param id 등록을 했었던 공지사항에 대한 id 식별 값
     * @return
     */
    Notices editNotices(AdminNoticeAddReqDto dto, Long id);

    /**
     * 공지사항을 삭제하는 메소드
     * @param id 공지사항을 식별하는 id 식별 값
     * @return
     */
    boolean deleteNotice(Long id);

    /**
     * 조회수를 올리는 메소드
     * @param id 공지사항을 식별하는 id 식별 값
     */
    void addView(Long id);

    /**
     * 공지사항의 상세 정보 가져오기
     * @param id 공지사항을 식별하는 id값
     * @return
     */
    NoticeReqDto getDetailNotice(Long id);

    /**
     * 공지사항 객체를 가져오는 메소드
     * @param id 공지사항을 식별하는 id값
     * @return
     */
    Notices getNoticeById(Long id);

}
