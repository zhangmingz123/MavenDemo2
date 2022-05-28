package com.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.po.Emp;
import com.po.PageBean;

public interface IEmpService {
	/**增加**/
	public boolean save(Emp emp);
    /**修改**/
	public boolean update(Emp emp);
	/**删除**/
	public boolean delById(Integer eid);
	/**查询单个**/
	public Emp findById(Integer eid);
	/**分页查询**/
	public List<Emp> findPageAll(PageBean pb);
	/**查询总记录数**/
	public int findMaxRows();
}
