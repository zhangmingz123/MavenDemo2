package com.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.po.Emp;
import com.po.EmpWelfare;
import com.po.Welfare;

@Service("IEmpWelfareDao")
public interface IEmpWelfareMapper {
    /**����Ա�����Ӹ���**/
	public int save(EmpWelfare ewf);
	/**����Ա�����ɾ����������**/
	public int delByEid(Integer eid);
	/**����Ա����Ų�ѯ��Ա����Ӧ�����и���**/
	public List<Welfare> findById(Integer eid);
	
	
}
