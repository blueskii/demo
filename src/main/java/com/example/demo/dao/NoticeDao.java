package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Notice;

@Mapper
public interface NoticeDao {

	int insert(Notice notice);

	List<Notice> findAll();

	Notice findById(Long id);

	boolean existsByTitleExcludingId(@Param("title") String title, @Param("id") Long id);

	int update(Notice notice);

	int deleteById(Long id);
}
