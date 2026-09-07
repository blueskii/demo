package com.example.demo.service;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.NoticeDao;
import com.example.demo.entity.Notice;
import com.example.demo.exception.DuplicateNoticeTitleException;
import com.example.demo.exception.NoticeNotFoundException;

@Service
@Transactional(readOnly = true)
public class NoticeService {

	private final NoticeDao noticeDao;

	public NoticeService(NoticeDao noticeDao) {
		this.noticeDao = noticeDao;
	}

	@Transactional
	public Notice create(Notice notice) {
		ensureUniqueTitle(notice.getTitle(), null);
		try {
			noticeDao.insert(notice);
		} catch (DuplicateKeyException exception) {
			throw new DuplicateNoticeTitleException();
		}
		return getById(notice.getId());
	}

	public List<Notice> getAll() {
		return noticeDao.findAll();
	}

	public Notice getById(Long id) {
		Notice notice = noticeDao.findById(id);
		if (notice == null) {
			throw new NoticeNotFoundException(id);
		}
		return notice;
	}

	@Transactional
	public Notice update(Long id, Notice notice) {
		ensureUniqueTitle(notice.getTitle(), id);
		notice.setId(id);
		try {
			if (noticeDao.update(notice) == 0) {
				throw new NoticeNotFoundException(id);
			}
		} catch (DuplicateKeyException exception) {
			throw new DuplicateNoticeTitleException();
		}
		return getById(id);
	}

	@Transactional
	public void delete(Long id) {
		if (noticeDao.deleteById(id) == 0) {
			throw new NoticeNotFoundException(id);
		}
	}

	private void ensureUniqueTitle(String title, Long id) {
		if (noticeDao.existsByTitleExcludingId(title, id)) {
			throw new DuplicateNoticeTitleException();
		}
	}
}
