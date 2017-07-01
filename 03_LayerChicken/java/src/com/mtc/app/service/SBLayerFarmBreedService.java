package com.mtc.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mtc.entity.app.SBLayerFarmBreed;
import com.mtc.mapper.app.SBLayerFarmBreedMapper;

import java.util.List;

@Service
public class SBLayerFarmBreedService {
	@Autowired
	private SBLayerFarmBreedMapper tSBLayerFarmBreedMapper;

	public void insert(SBLayerFarmBreed tSBLayerFarmBreed){
		  tSBLayerFarmBreedMapper.insert(tSBLayerFarmBreed);
	}

	public SBLayerFarmBreed selectByPrimaryKey(int id) {
		return tSBLayerFarmBreedMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKey(SBLayerFarmBreed record) {
		return tSBLayerFarmBreedMapper.updateByPrimaryKey(record);
	}
}
