package com.example.demo.service;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.BoardDao;
import com.example.demo.entity.BoardPost;
import com.example.demo.exception.BoardNotFoundException;
import com.example.demo.exception.DuplicateBoardTitleException;

@Service
@Transactional(readOnly = true)
public class BoardService {

	private final BoardDao boardDao;

	public BoardService(BoardDao boardDao) {
		this.boardDao = boardDao;
	}

	@Transactional
	public BoardPost create(BoardPost boardPost) {
		ensureUniqueTitle(boardPost.getTitle(), null);
		try {
			boardDao.insert(boardPost);
		} catch (DuplicateKeyException exception) {
			throw new DuplicateBoardTitleException();
		}
		return getById(boardPost.getId());
	}

	public List<BoardPost> getAll() {
		return boardDao.findAll();
	}

	public BoardPost getById(Long id) {
		BoardPost boardPost = boardDao.findById(id);
		if (boardPost == null) {
			throw new BoardNotFoundException(id);
		}
		return boardPost;
	}

	@Transactional
	public BoardPost update(Long id, BoardPost boardPost) {
		ensureUniqueTitle(boardPost.getTitle(), id);
		boardPost.setId(id);
		try {
			if (boardDao.update(boardPost) == 0) {
				throw new BoardNotFoundException(id);
			}
		} catch (DuplicateKeyException exception) {
			throw new DuplicateBoardTitleException();
		}
		return getById(id);
	}

	@Transactional
	public void delete(Long id) {
		if (boardDao.deleteById(id) == 0) {
			throw new BoardNotFoundException(id);
		}
	}

	private void ensureUniqueTitle(String title, Long id) {
		if (boardDao.existsByTitleExcludingId(title, id)) {
			throw new DuplicateBoardTitleException();
		}
	}
}
