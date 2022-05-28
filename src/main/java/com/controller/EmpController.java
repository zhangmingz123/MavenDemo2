package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.po.Dep;
import com.po.Emp;
import com.po.PageBean;
import com.po.Welfare;
import com.util.AJAxUtil;
import com.util.BizServiceUtil;

@Controller
public class EmpController {
  @Resource(name="BizServiceUtil")
  private BizServiceUtil bizService;

public BizServiceUtil getBizService() {
	return bizService;
}

public void setBizService(BizServiceUtil bizService) {
	this.bizService = bizService;
}
@RequestMapping(value="save_Emp.do")
public String save(HttpServletRequest request,HttpServletResponse response,Emp emp){
	System.out.println("��ӷ���:"+emp.toString());
	String realpath=request.getRealPath("/");
	/*************�ļ��ϴ�begin*****************/
	//��ȡ�ϴ���Ƭ����
	MultipartFile multipartFile=emp.getPic();
	if(multipartFile!=null && !multipartFile.isEmpty()){
		//��ȡ�ϴ��ļ�����
		String fname=multipartFile.getOriginalFilename();
		//����
		if(fname.lastIndexOf(".")!=-1){
			//��ȡ��׺
			String ext=fname.substring(fname.lastIndexOf("."));
			//�����ϴ���ʽ
			if(ext.equalsIgnoreCase(".jpg")){
				String newfname=new Date().getTime()+ext;
				//�����ļ����󣬽��ϴ����ֽ��ļ�д�뵽ָ���ļ�
				File dostFile=new File(realpath+"/uppic/"+newfname);
				//�ϴ�
				try {
					FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostFile);
					emp.setPhoto(newfname);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/*************�ļ��ϴ�end*****************/
	boolean flag=bizService.getEmpService().save(emp);
	if(flag){
		AJAxUtil.printString(response, 1+"");
	}else{
		AJAxUtil.printString(response, 0+"");
	}	
	return null;
}
@RequestMapping(value="update_Emp.do")
public String update(HttpServletRequest request,HttpServletResponse response,Emp emp){
	System.out.println("�޸ķ���:"+emp.toString());
	String realpath=request.getRealPath("/");
	//��ȡԭ����Ƭ
	String oldphoto=bizService.getEmpService().findById(emp.getEid()).getPhoto();
	if(emp.getPic()!=null){
		/*************�ļ��ϴ�begin*****************/
		//��ȡ�ϴ���Ƭ����
		MultipartFile multipartFile=emp.getPic();
		if(multipartFile!=null && !multipartFile.isEmpty()){
			//��ȡ�ϴ��ļ�����
			String fname=multipartFile.getOriginalFilename();
			//����
			if(fname.lastIndexOf(".")!=-1){
				//��ȡ��׺
				String ext=fname.substring(fname.lastIndexOf("."));
				//�����ϴ���ʽ
				if(ext.equalsIgnoreCase(".jpg")){
					String newfname=new Date().getTime()+ext;
					//�����ļ����󣬽��ϴ����ֽ��ļ�д�뵽ָ���ļ�
					File dostFile=new File(realpath+"/uppic/"+newfname);
					//�ϴ�
					try {
						FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostFile);
						emp.setPhoto(newfname);
						//ɾ��ԭ��
						File oldfile=new File(realpath+"/uppic/"+oldphoto);
						if(oldfile.exists()&& !oldphoto.equalsIgnoreCase("default.jpg")){
							oldfile.delete();//ɾ��
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		/*************�ļ��ϴ�end*****************/
	}else{//�޸�ʱ���ϴ�
		emp.setPhoto(oldphoto);
	}
	
	boolean flag=bizService.getEmpService().update(emp);
	if(flag){
		AJAxUtil.printString(response, 1+"");
	}else{
		AJAxUtil.printString(response, 0+"");
	}	
	return null;
}
@RequestMapping(value="delById_Emp.do")
public String delById(HttpServletRequest request,HttpServletResponse response,Integer eid){
	System.out.println("ɾ����"+eid);
	String realpath=request.getRealPath("/");
	//��ȡԭ����Ƭ
	String oldphoto=bizService.getEmpService().findById(eid).getPhoto();
	System.out.println("aaa:"+oldphoto);
	boolean flag=bizService.getEmpService().delById(eid);
	if(flag){
		//ɾ��ԭ��
		File oldfile=new File(realpath+"/uppic/"+oldphoto);
		if(oldfile.exists()&& !oldphoto.equalsIgnoreCase("default.jpg")){
			oldfile.delete();//ɾ��
		}
		AJAxUtil.printString(response, 1+"");
	}else{
		AJAxUtil.printString(response, 0+"");
	}	
	return null;
}
@RequestMapping(value="findById_Emp.do")
public String findById(HttpServletRequest request,HttpServletResponse response,Integer eid){
	System.out.println("��ѯ������"+eid);
	Emp oldemp=bizService.getEmpService().findById(eid);
	System.out.println("oldemp:"+oldemp.toString());
	//����תjson	
	PropertyFilter propertyFilter=AJAxUtil.filterProperts("birthday","pic");
	String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter, SerializerFeature.DisableCircularReferenceDetect);
    AJAxUtil.printString(response, jsonstr);
	return null;
}
@RequestMapping(value="findPageAll_Emp.do")
public String findPageAll(HttpServletRequest request,HttpServletResponse response,Integer page,Integer rows){
	System.out.println("��ҳ��ѯ����:"+page+"===="+rows);
	Map<String, Object> map=new HashMap<String,Object>();
	PageBean pb=new PageBean();
	page=page==null||page<1?pb.getPage():page;
	rows=rows==null||rows<1?pb.getRows():rows;
	if(rows>10)rows=10;
	pb.setPage(page);
	pb.setRows(rows);
	List<Emp> lsemp=bizService.getEmpService().findPageAll(pb);
	int maxRows=bizService.getEmpService().findMaxRows();
	map.put("page", page);
	map.put("rows", lsemp);
	map.put("total", maxRows);
    //����תjson	
	PropertyFilter propertyFilter=AJAxUtil.filterProperts("birthday","pic");
	String jsonstr=JSONObject.toJSONString(map,propertyFilter, SerializerFeature.DisableCircularReferenceDetect);
    AJAxUtil.printString(response, jsonstr);
	return null;
}
@RequestMapping(value="doint_Emp.do")
public String doint(HttpServletRequest request,HttpServletResponse response){
	System.out.println("���ҳ��׼������");
	Map<String, Object> map=new HashMap<String,Object>();
	List<Dep> lsdep=bizService.getDepService().findAll();
	List<Welfare> lswf=bizService.getWelfareService().findAll();
    map.put("lsdep",lsdep);
    map.put("lswf", lswf);
	//����תjson	
	PropertyFilter propertyFilter=AJAxUtil.filterProperts("birthday","pic");
	String jsonstr=JSONObject.toJSONString(map,propertyFilter, SerializerFeature.DisableCircularReferenceDetect);
    AJAxUtil.printString(response, jsonstr);
	return null;
}
}
