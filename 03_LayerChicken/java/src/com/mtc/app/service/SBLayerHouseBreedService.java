package com.mtc.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mtc.entity.app.SBLayerHouseBreed;
import com.mtc.mapper.app.SBLayerHouseBreedMapper;
import com.mtc.mapper.app.SBLayerHouseBreedMapperCustom;

@Service
public class SBLayerHouseBreedService {
    @Autowired
    private SBLayerHouseBreedMapper tSBLayerHouseBreedMapper;

    @Autowired
    private SBLayerHouseBreedMapperCustom tSBLayerHouseBreedMapperCustom;

     public void insert(SBLayerHouseBreed  tSBLayerHouseBreed){
         tSBLayerHouseBreedMapper.insert(  tSBLayerHouseBreed);
     }

     public SBLayerHouseBreed selectByPrimaryKey(int id){
         return tSBLayerHouseBreedMapper.selectByPrimaryKey(id);
     }

    public SBLayerHouseBreed selectByFarmIdAndHouseId(int farmId, int houseId, int farmBreedId){
        return tSBLayerHouseBreedMapperCustom.selectByHouseIdAndFarmId(farmId, houseId, farmBreedId);
    }

    public int updateByPrimaryKey(SBLayerHouseBreed record){
        return tSBLayerHouseBreedMapper.updateByPrimaryKey(record);
    }
}
