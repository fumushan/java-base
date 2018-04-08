package com.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.base.mapper.BaseMapper;

public class BaseService<T> {

	@Autowired
	private BaseMapper<T> baseMapper;

	public int save(T t) {
		return baseMapper.insert(t);
	}
	
	public void deleteById(String key) {
		baseMapper.deleteByPrimaryKey(key);
	}
	
	public T selectOne(String key) {
		return baseMapper.selectByPrimaryKey(key);
	}
	
	public List<T> selectList(T t) {
		return baseMapper.selectByExample(t);
	}

}
