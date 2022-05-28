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
		//���Ա����Ϣ
		int code=daoService.getEmpMapper().save(emp);
		if(code>0){
		//��Ϊeid�����ݿ������ģ���ֻ�ܻ�ȡ�ղ���ӵ�Ա�����
		Integer eid=daoService.getEmpMapper().findMaxEid();
		//��Ӹ�Ա����н��
		Salary sa=new Salary(eid,emp.getEmoney());
		daoService.getSalaryMapper().save(sa);
		//��Ӹ�Ա���ĸ���
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
		//�޸�Ա����Ϣ
		 int code=daoService.getEmpMapper().update(emp);
		 if(code>0){
		     //�޸ĸ�Ա����н��
			   //1)��ȡԭ��н��
			   Salary oldsa=daoService.getSalaryMapper().findByEid(emp.getEid());
			   //2)����н��
			   if(oldsa!=null && oldsa.getEmoney()!=null){
				   //ԭ����н�ʣ���֤н��������
				   if(emp.getEmoney()>oldsa.getEmoney()){
					   Salary sa=new Salary(emp.getEid(),emp.getEmoney());
					   daoService.getSalaryMapper().update(sa);
				   }
			   }else{
				   //ԭ��û��н��
				   Salary sa=new Salary(emp.getEid(),emp.getEmoney());
				   daoService.getSalaryMapper().save(sa);
			   }
				
				//�޸ĸ�Ա���ĸ���
				  //1.��ȡԭ�и���
			      List<Welfare> lswf=daoService.getEmpWelfareMapper().findById(emp.getEid());
			      if(lswf!=null && lswf.size()>0){
			    	  //ԭ���и������޸ģ�
			    	    //��ɾ
			    	   daoService.getEmpWelfareMapper().delByEid(emp.getEid());
			    	   
			      }
			    	 //ԭ��û�и�������ӣ�
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
		//��ɾ�ӱ���ɾ����
		//ɾ��н��
		daoService.getSalaryMapper().delByEid(eid);
		//ɾ������
		daoService.getEmpWelfareMapper().delByEid(eid);
		//ɾ��Ա��
		int code= daoService.getEmpMapper().delById(eid);
		if(code>0){
			return true;
		}
		return false;
	}

	@Override
	public Emp findById(Integer eid) {
		//��ȡԱ������
		Emp emp=daoService.getEmpMapper().findById(eid);
		System.out.println("BBB:"+emp.toString());
		//��ȡн��
		Salary sa=daoService.getSalaryMapper().findByEid(eid);
		if(sa!=null&&sa.getEmoney()!=null){
			emp.setEmoney(sa.getEmoney());
		}
		//��ȡ������Ա����������id���飬�������ϣ�
		List<Welfare> lswf=daoService.getEmpWelfareMapper().findById(eid);
		if(lswf!=null && lswf.size()>0){
			//������������
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
