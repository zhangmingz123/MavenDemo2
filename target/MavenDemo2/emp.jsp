<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css"/>
<script type="text/javascript" src="easyui/jquery-1.9.1.js"></script>
<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
$(function(){
	$('#win').window('close');  // close a window  
	//获取后台准备的添加页面值部门和福利
	$.getJSON("doint_Emp.do",function(map){
		var lsdep=map.lsdep;
		var lswf=map.lswf;
		//福利复选框
		for(var i=0;i<lswf.length;i++){
			var wf=lswf[i];
			$("#wf").append("<input type='checkbox' id='wids' name='wids' value='"+wf.wid+"'>"+wf.wname);
		}
		//部门下拉列表
		$('#depid').combobox({    
			data:lsdep,    
		    valueField:'depid',    
		    textField:'depname',
		    value:1,
		    panelHeight:80 
		});
	});
});
/**************员工列表begin***************/
$(function(){
	$('#dg').datagrid({    
	    url:'findPageAll_Emp.do',
	    pagination:'true',
	    striped:'true',
	    singleSelect:'true',
	    pageNumber:1,
	    pageSize:5,
	    pageList:[1,5,10,15,20],
	    columns:[[    
	        {field:'eid',title:'编号',align:'center',width:50},
	        {field:'ename',title:'姓名',align:'center',width:100},
	        {field:'sex',title:'性别',align:'center',width:50},
	        {field:'address',title:'地址',align:'center',width:100},
	        {field:'sdate',title:'生日',align:'center',width:100},
	        {field:'photo',title:'照片',align:'center',width:100,
	        	formatter:function(value,row,index){
					return '<img src=uppic/'+row.photo+' width=40 height=50  >';
				}  
	        },
	        {field:'depname',title:'部门',align:'center',width:80},
	        {field:'opt',title:'操作',align:'center',width:180,
	         formatter:function(value,row,index){
					var bt1='<input type="button" value="删除" onclick="dodelById('+row.eid+')">';
					var bt2='<input type="button" value="编辑" onclick="findById('+row.eid+')">';
					var bt3='<input type="button" value="详细" onclick="findDetail('+row.eid+')">';
					return bt1+'&nbsp;'+bt2+'&nbsp;'+bt3;
				}  
	        }
	    ]]      
	});  
});
/**************员工列表begin***************/
/**************保存与修改begin***************/
$(function(){
	//保存
	$("#btsave").click(function(){
		$.messager.progress();	// 显示进度条
		$('#ffemp').form('submit', {
			url: 'save_Emp.do',
			onSubmit: function(){
				var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
				}
				return isValid;	// 返回false终止表单提交
			},
			success: function(code){
			    if(code=='1'){
			    	$.messager.progress('close');	// 如果提交成功则隐藏进度条
			    	$.messager.alert('提示','添加成功');	
			    	$('#dg').datagrid('reload');    // 重新载入当前页面数据  
			    }else{
			    	$.messager.alert('提示','添加失败');
			    }
			}
		});
	});
	//修改
	//保存
	$("#btupdate").click(function(){
		$.messager.progress();	// 显示进度条
		$('#ffemp').form('submit', {
			url: 'update_Emp.do',
			onSubmit: function(){
				var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
				}
				return isValid;	// 返回false终止表单提交
			},
			success: function(code){
			    if(code=='1'){
			    	$.messager.progress('close');	// 如果提交成功则隐藏进度条
			    	$.messager.alert('提示','修改成功');	
			    	$('#dg').datagrid('reload');    // 重新载入当前页面数据  
			    }else{
			    	$.messager.alert('提示','修改失败');
			    }
			}
		});
	});
});
/**************保存与修改end***************/
/**************删除和查找begin****************/
function dodelById(eid){
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
	    if (r){    
	       $.get("delById_Emp.do?eid="+eid,function(code){
	    	   if(code=='1'){
			    	$.messager.alert('提示','删除成功');	
			    	$('#dg').datagrid('reload');    // 重新载入当前页面数据  
			    }else{
			    	$.messager.alert('提示','删除失败');
			    }
	       });    
	    }    
	});  

}
function findById(eid){
	$.getJSON("findById_Emp.do?eid="+eid,function(emp){
		//删除原有复选框的选中（防止上一次残留）
		$(":checkbox[name='wids']").each(function(){
			$(this).prop("checked",false);	//设置选中状态为false
		});
		//处理表单
		$('#ffemp').form('load',{
			eid:emp.eid,
			ename:emp.ename,
			sex:emp.sex,
			address:emp.address,
			sdate:emp.sdate,
			depid:emp.depid,
			emoney:emp.emoney
		});
		//处理图片
        $("#myphoto").attr('src','uppic/'+emp.photo);
		//处理复选框
		var wids=emp.wids;
		$(":checkbox[name='wids']").each(function(){
			for(var i=0;i<wids.length;i++){
				if($(this).val()==wids[i]){
					$(this).prop("checked",true);	//设置选中状态为false
				}
			}
			
		});

	});
}
function findDetail(eid){
	
	$.getJSON("findById_Emp.do?eid="+eid,function(emp){
		$("#enameText").html(emp.ename);
		$("#sexText").html(emp.sex);
		$("#addressText").html(emp.address);
		$("#sdateText").html(emp.sdate);
		$("#photoText").html(emp.photo);
		$("#depnameText").html(emp.depname);
		$("#emoneyText").html(emp.emoney);
		$("#dtmyphoto").attr('src','uppic/'+emp.photo);
		var lswf=emp.lswf;
		var wnames=[];
		for(var i=0;i<lswf.length;i++){
			var wf=lswf[i];
			wnames.push(wf.wname);
		}
		var strwname=wnames.join(",");
		$("#wfText").html(strwname);
	});
	$('#win').window('open');  // open a window    
}

/*************删除和查找end*****************/
</script>
</head>
<body>
<p align="center">员工列表</p>
<hr />
<table id="dg"></table>
<!-- 添加修改表单 -->
<p>
<form action="" method="post" enctype="multipart/form-data" name="ffemp" id="ffemp">
	  <table width="550" border="1" align="center" cellpadding="1" cellspacing="0">
	    <tr>
	      <td colspan="3" align="center" bgcolor="#99FFCC">员工管理</td>
        </tr>
	    <tr>
	      <td width="104">姓名</td>
	      <td width="303">
          <input type="text" name="ename"  id="ename" class="easyui-validatebox" data-options="required:true,missingMessage:'员工名称'"/>
          <input type="hidden" name="eid"  id="eid"/></td>
	      <td width="129" rowspan="7"><img id="myphoto" src="uppic/default.jpg" width="129" height="150" /></td>
        </tr>
	    <tr>
	      <td>性别</td>
	      <td>
	      <input name="sex" type="radio" id="radio" value="男" checked="checked" />男
          <input type="radio" name="sex" id="radio2" value="女" />女
          </td>
        </tr>
	    <tr>
	      <td>地址</td>
	      <td><input type="text" name="address" id="address"/></td>
        </tr>
	    <tr>
	      <td>生日</td>
	      <td><input name="sdate" type="date" id="sdate" value="1990-01-01" /></td>
        </tr>
	    <tr>
	      <td>照片选择</td>
	      <td>
          <input type="file" name="pic" id="pic" /></td>
        </tr>
	    <tr>
	      <td>部门</td>
	      <td>
          <input type="text" name="depid" id="depid" />
          </td>
        </tr>
	    <tr>
	      <td>薪资</td>
	      <td>
          <input name="emoney" type="text" id="emoney" value="2000"/></td>
        </tr>
	    <tr>
	      <td>福利</td>
	      <td colspan="2"><span id="wf"></span></td>
        </tr>
	    <tr>
	      <td colspan="3" align="center" bgcolor="#99FFCC">
	      <input type="button" name="btsave" id="btsave" value="保存" />
          <input type="button" name="btupdate" id="btupdate" value="修改" />
          <input type="button" name="btreset" id="btreset" value="取消" /></td>
        </tr>
      </table>
</form>
</p>
<div id="win" class="easyui-window" title="员工详细" style="width:600px;height:400px"   
        data-options="iconCls:'icon-save',modal:true">   
       <table width="550" border="1" align="center" cellpadding="1" cellspacing="0">
	    <tr>
	      <td colspan="3" align="center" bgcolor="#99FFCC">员工信息</td>
        </tr>
	    <tr>
	      <td width="104">姓名</td>
	      <td width="303">
          <span id="enameText"></span>
          <input type="hidden" name="eid"  id="eid"/></td>
	      <td width="129" rowspan="7"><img id="dtmyphoto" src="uppic/default.jpg" width="129" height="150" /></td>
        </tr>
	    <tr>
	      <td>性别</td>
	      <td>
	      <span id="sexText"></span>
          </td>
        </tr>
	    <tr>
	      <td>地址</td>
	      <td><span id="addressText"></span></td>
        </tr>
	    <tr>
	      <td>生日</td>
	      <td><span id="sdateText"></span></td>
        </tr>
	    <tr>
	      <td>照片选择</td>
	      <td>
          <span id="photoText"></span></td>
        </tr>
	    <tr>
	      <td>部门</td>
	      <td>
          <span id="depnameText"></span>
          </td>
        </tr>
	    <tr>
	      <td>薪资</td>
	      <td>
          <span id="emoneyText"></span></td>
        </tr>
	    <tr>
	      <td>福利</td>
	      <td colspan="2"><span id="wfText"></span></td>
        </tr>
      </table> 
</div>  

</body>
</html>