package com.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.po.Emp;
import com.po.Salary;

@Service("ISalaryDao")
public interface ISalaryMapper {
    /**����**/
	public int save(Salary sa);
	/**����Ա������޸�н��**/
	public int update(Salary sa);
	/**����Ա�����ɾ��н��**/
	public int delByEid(Integer eid);
	/**����Ա����Ų�ѯ����**/
	public Salary findByEid(Integer eid);
	
	
}
