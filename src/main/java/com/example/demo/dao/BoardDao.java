package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.BoardPost;

@Mapper
public interface BoardDao {

	int insert(BoardPost boardPost);

	List<BoardPost> findAll();

	BoardPost findById(Long id);

	int update(BoardPost boardPost);

	int deleteById(Long id);
}
