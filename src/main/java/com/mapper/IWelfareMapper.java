package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.po.Welfare;

@Service("IWelfareDao")
public interface IWelfareMapper {
	/**��ѯ����**/
	public List<Welfare> findAll();

}
