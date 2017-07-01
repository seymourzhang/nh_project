package com.nh.ifarm.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.nh.ifarm.system.service.SysDictService;
import com.nh.ifarm.util.common.Page;
import com.nh.ifarm.util.common.PageData;
import com.nh.ifarm.util.dao.impl.DaoSupport;

@Service
public class SysDictServiceImpl implements SysDictService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public void save(PageData pd) throws Exception {
		
		dao.save("SysDictMapper.save", pd);
	}

	@Override
	public void delete(PageData pd) throws Exception {
		
		dao.delete("SysDictMapper.delete", pd);
	}

	@Override
	public void edit(PageData pd) throws Exception {
		
		dao.update("SysDictMapper.edit", pd);
	}

	@Override
	public List<PageData> sysDictlistPage(Page page) throws Exception {
		
		return (List<PageData>) dao.findForList("SysDictMapper.sysDictlistPage", page);
	}

	@Override
	public PageData findDictInfo(PageData pd) throws Exception {
		
		return (PageData) dao.findForObject("SysDictMapper.findDictInfo", pd);
	}

//	@Override
//	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
//		// TODO Auto-generated method stub
//
//	}

}
