package com.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.po.Emp;
import com.po.EmpWelfare;
import com.po.Welfare;

@Service("IEmpWelfareDao")
public interface IEmpWelfareMapper {
    /**根据员工增加福利**/
	public int save(EmpWelfare ewf);
	/**根据员工编号删除福利数据**/
	public int delByEid(Integer eid);
	/**根据员工编号查询该员工对应的所有福利**/
	public List<Welfare> findById(Integer eid);
	
	
}
