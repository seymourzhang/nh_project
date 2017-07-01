/**
 *
 * MTC-上海农汇信息科技有限公司
 * Copyright © 2015 农汇 版权所有
 */
package com.mtc.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mtc.entity.app.SBBbfar;
import com.mtc.mapper.app.SBBbfarMapper;

/**
 * @ClassName: SBBbfarSercice
 * @Description: 
 * @Date 2016-1-6 下午2:43:02
 * @Author Shao Yao Yu
 * 
 */
@Service
public class SBBbfarSercice {

	@Autowired
	private SBBbfarMapper tSBBbfarMapper;
	
	public int insertSbbbfar(SBBbfar record){
		return tSBBbfarMapper.insert(record);
	}
	
}
