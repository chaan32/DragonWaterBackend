package com.dragonwater.backend.Web.Support.Notice.service;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.DeleteFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.EditFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.NoticeFailedUploadException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.NoticeNotFoundException;
import com.dragonwater.backend.Web.Support.Notice.domain.Notices;
import com.dragonwater.backend.Web.Support.Notice.dto.AdminNoticeAddReqDto;
import com.dragonwater.backend.Web.Support.Notice.dto.AdminNoticeResDto;
import com.dragonwater.backend.Web.Support.Notice.dto.NoticeReqDto;
import com.dragonwater.backend.Web.Support.Notice.dto.NoticeResDto;
import com.dragonwater.backend.Web.Support.Notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;

    public Queue<AdminNoticeResDto> getAdminAllNotices() {
        List<Notices> all = noticeRepository.findAll();
        return all.stream()
                .map(AdminNoticeResDto::of)
                .collect(Collectors.toCollection(LinkedList::new));
    }
    public Queue<NoticeResDto> getAllNotices() {
        List<Notices> all = noticeRepository.findAll();
        return all.stream()
                .map(NoticeResDto::of)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional
    public Notices addNotices(AdminNoticeAddReqDto dto) {
        try {
            Notices notice = Notices.of(dto);
            return noticeRepository.save(notice);
        } catch (Exception e) {
            throw new NoticeFailedUploadException();
        }
    }

    @Transactional
    public Notices editNotices(AdminNoticeAddReqDto dto, Long id) {
        Notices notices = noticeRepository.findById(id).orElseThrow(
                () -> new NoticeNotFoundException()
        );
        try {
            notices.edit(dto);
            return notices;
        } catch (Exception e) {
            throw new EditFailedException();
        }
    }

    @Transactional
    public boolean deleteNotice(Long id) {
        try {
            noticeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DeleteFailedException();
        }
    }

    @Transactional
    public void addView(Long id) {
        Notices notices = getNoticeById(id);
        notices.addView();
    }


    public NoticeReqDto getDetailNotice(Long id) {
        Notices notices = getNoticeById(id);
        return NoticeReqDto.of(notices);
    }

    public Notices getNoticeById(Long id) {
        return noticeRepository.findById(id).orElseThrow(()->new NoticeNotFoundException());
    }
}
