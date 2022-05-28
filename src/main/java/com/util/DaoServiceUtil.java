package com.util;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mapper.*;

@Service("DaoServiceUtil")
public class DaoServiceUtil {
	@Resource(name="IDepDao")
	private IDepMapper depMapper;
	@Resource(name="IEmpDao")
	private IEmpMapper empMapper;
	@Resource(name="IEmpWelfareDao")
	private IEmpWelfareMapper empWelfareMapper;
	@Resource(name="ISalaryDao")
	private ISalaryMapper salaryMapper;
	@Resource(name="IWelfareDao")
	private IWelfareMapper welfareMapper;
	public IDepMapper getDepMapper() {
		return depMapper;
	}
	public void setDepMapper(IDepMapper depMapper) {
		this.depMapper = depMapper;
	}
	public IEmpMapper getEmpMapper() {
		return empMapper;
	}
	public void setEmpMapper(IEmpMapper empMapper) {
		this.empMapper = empMapper;
	}
	public IEmpWelfareMapper getEmpWelfareMapper() {
		return empWelfareMapper;
	}
	public void setEmpWelfareMapper(IEmpWelfareMapper empWelfareMapper) {
		this.empWelfareMapper = empWelfareMapper;
	}
	public ISalaryMapper getSalaryMapper() {
		return salaryMapper;
	}
	public void setSalaryMapper(ISalaryMapper salaryMapper) {
		this.salaryMapper = salaryMapper;
	}
	public IWelfareMapper getWelfareMapper() {
		return welfareMapper;
	}
	public void setWelfareMapper(IWelfareMapper welfareMapper) {
		this.welfareMapper = welfareMapper;
	}
	

}
