package com.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.po.Emp;
import com.po.Salary;

@Service("ISalaryDao")
public interface ISalaryMapper {
    /**增加**/
	public int save(Salary sa);
	/**根据员工编号修改薪资**/
	public int update(Salary sa);
	/**根据员工编号删除薪资**/
	public int delByEid(Integer eid);
	/**根据员工编号查询单个**/
	public Salary findByEid(Integer eid);
	
	
}
