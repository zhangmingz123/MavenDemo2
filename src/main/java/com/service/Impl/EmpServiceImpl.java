package com.service.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.po.Emp;
import com.po.EmpWelfare;
import com.po.PageBean;
import com.po.Salary;
import com.po.Welfare;
import com.service.IEmpService;
import com.util.DaoServiceUtil;
@Service("EmpServiceImpl")
@Transactional
public class EmpServiceImpl implements IEmpService {
    @Resource(name="DaoServiceUtil")
    private DaoServiceUtil daoService;
    
	public DaoServiceUtil getDaoService() {
		return daoService;
	}

	public void setDaoService(DaoServiceUtil daoService) {
		this.daoService = daoService;
	}

	@Override
	public boolean save(Emp emp) {
		//添加员工信息
		int code=daoService.getEmpMapper().save(emp);
		if(code>0){
		//因为eid是数据库自增的，我只能获取刚才添加的员工编号
		Integer eid=daoService.getEmpMapper().findMaxEid();
		//添加该员工的薪资
		Salary sa=new Salary(eid,emp.getEmoney());
		daoService.getSalaryMapper().save(sa);
		//添加该员工的福利
		String[] wids=emp.getWids();
		if(wids!=null && wids.length>0){
			for(int i=0;i<wids.length;i++){
				EmpWelfare ewf=new EmpWelfare(eid,Integer.parseInt(wids[i]));
				daoService.getEmpWelfareMapper().save(ewf);
			}
		}
		return true;
		}
		return false;
	}

	@Override
	public boolean update(Emp emp) {
		//修改员工信息
		 int code=daoService.getEmpMapper().update(emp);
		 if(code>0){
		     //修改该员工的薪资
			   //1)获取原有薪资
			   Salary oldsa=daoService.getSalaryMapper().findByEid(emp.getEid());
			   //2)处理薪资
			   if(oldsa!=null && oldsa.getEmoney()!=null){
				   //原来有薪资（保证薪资增长）
				   if(emp.getEmoney()>oldsa.getEmoney()){
					   Salary sa=new Salary(emp.getEid(),emp.getEmoney());
					   daoService.getSalaryMapper().update(sa);
				   }
			   }else{
				   //原来没有薪资
				   Salary sa=new Salary(emp.getEid(),emp.getEmoney());
				   daoService.getSalaryMapper().save(sa);
			   }
				
				//修改该员工的福利
				  //1.获取原有福利
			      List<Welfare> lswf=daoService.getEmpWelfareMapper().findById(emp.getEid());
			      if(lswf!=null && lswf.size()>0){
			    	  //原来有福利（修改）
			    	    //先删
			    	   daoService.getEmpWelfareMapper().delByEid(emp.getEid());
			    	   
			      }
			    	 //原来没有福利（添加）
			    	  String[] wids=emp.getWids();
			  		if(wids!=null && wids.length>0){
			  			for(int i=0;i<wids.length;i++){
			  				EmpWelfare ewf=new EmpWelfare(emp.getEid(),Integer.parseInt(wids[i]));
			  				daoService.getEmpWelfareMapper().save(ewf);
			  			}
			  		}
				return true;
				}
		return false;
	}

	@Override
	public boolean delById(Integer eid) {
		//先删从表再删主表
		//删除薪资
		daoService.getSalaryMapper().delByEid(eid);
		//删除福利
		daoService.getEmpWelfareMapper().delByEid(eid);
		//删除员工
		int code= daoService.getEmpMapper().delById(eid);
		if(code>0){
			return true;
		}
		return false;
	}

	@Override
	public Emp findById(Integer eid) {
		//获取员工对象
		Emp emp=daoService.getEmpMapper().findById(eid);
		System.out.println("BBB:"+emp.toString());
		//获取薪资
		Salary sa=daoService.getSalaryMapper().findByEid(eid);
		if(sa!=null&&sa.getEmoney()!=null){
			emp.setEmoney(sa.getEmoney());
		}
		//获取福利（员工福利表福利id数组，福利集合）
		List<Welfare> lswf=daoService.getEmpWelfareMapper().findById(eid);
		if(lswf!=null && lswf.size()>0){
			//创建福利数组
			String[] wids=new String[lswf.size()];
			for(int i=0;i<lswf.size();i++){
				Welfare wf=lswf.get(i);
				wids[i]=wf.getWid().toString();
			}
			emp.setWids(wids);
		}else{
			lswf=new ArrayList<Welfare>();
		}
		System.out.println(lswf.size());
		emp.setLswf(lswf);
		return emp;
	}

	@Override
	public List<Emp> findPageAll(PageBean pb) {
		if(pb!=null){
		List<Emp> emplist=daoService.getEmpMapper().findPageAll(pb.getPage(), pb.getRows());	
		return emplist;
		}
		return null;
	}

	@Override
	public int findMaxRows() {
		// TODO Auto-generated method stub
		return daoService.getEmpMapper().findMaxRows();
	}

}
