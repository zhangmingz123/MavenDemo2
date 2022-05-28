package com.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.po.Welfare;
import com.service.IWelfareService;
import com.util.DaoServiceUtil;
@Service("WelfareServiceImpl")
@Transactional
public class WelfareServiceImpl implements IWelfareService {
	@Resource(name="DaoServiceUtil")
    private DaoServiceUtil daoService;
    
	public DaoServiceUtil getDaoService() {
		return daoService;
	}

	public void setDaoService(DaoServiceUtil daoService) {
		this.daoService = daoService;
	}

	@Override
	public List<Welfare> findAll() {
		// TODO Auto-generated method stub
		return daoService.getWelfareMapper().findAll();
	}

}
