package com.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.po.Emp;

@Service("IEmpDao")
public interface IEmpMapper {
    /**����**/
	public int save(Emp emp);
    /**�޸�**/
	public int update(Emp emp);
	/**ɾ��**/
	public int delById(Integer eid);
	/**��ѯ����**/
	public Emp findById(Integer eid);
	/**��ҳ��ѯ**/
	public List<Emp> findPageAll(@Param("page") int page, @Param("rows") int rows);
	/**��ѯ�ܼ�¼��**/
	public int findMaxRows();
	/***��ȡԱ���������***/
	public int findMaxEid();
	
}
