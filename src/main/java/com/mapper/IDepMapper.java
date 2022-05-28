package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.po.Dep;

@Service("IDepDao")
public interface IDepMapper {
	/**²éÑ¯ËùÓĞ**/
	public List<Dep> findAll();

}
