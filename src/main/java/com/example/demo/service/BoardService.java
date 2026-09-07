package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.BoardDao;
import com.example.demo.entity.BoardPost;
import com.example.demo.exception.BoardNotFoundException;

@Service
@Transactional(readOnly = true)
public class BoardService {

	private final BoardDao boardDao;

	public BoardService(BoardDao boardDao) {
		this.boardDao = boardDao;
	}

	@Transactional
	public BoardPost create(BoardPost boardPost) {
		boardDao.insert(boardPost);
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
		boardPost.setId(id);
		if (boardDao.update(boardPost) == 0) {
			throw new BoardNotFoundException(id);
		}
		return getById(id);
	}

	@Transactional
	public void delete(Long id) {
		if (boardDao.deleteById(id) == 0) {
			throw new BoardNotFoundException(id);
		}
	}
}
