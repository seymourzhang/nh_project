package com.mtc.mapper.app;

import com.mtc.entity.app.SBLayerHouseBreed;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Seymour on 2017/3/20.
 */
public interface SBLayerHouseBreedMapperCustom {

    SBLayerHouseBreed selectByHouseIdAndFarmId(@Param("farmId") Integer farmId, @Param("houseId") Integer houseId, @Param("farmBreedId") Integer farmBreedId);
}
