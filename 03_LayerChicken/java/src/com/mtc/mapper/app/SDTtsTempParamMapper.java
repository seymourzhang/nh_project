package com.mtc.mapper.app;

import com.mtc.entity.app.SDTtsTempParam;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SDTtsTempParamMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_d_tts_temp_param
     *
     * @mbggenerated Mon Jul 04 11:20:00 CST 2016
     */
    int deleteByPrimaryKey(@Param("tempId") String tempId, @Param("paramName") String paramName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_d_tts_temp_param
     *
     * @mbggenerated Mon Jul 04 11:20:00 CST 2016
     */
    int insert(SDTtsTempParam record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_d_tts_temp_param
     *
     * @mbggenerated Mon Jul 04 11:20:00 CST 2016
     */
    SDTtsTempParam selectByPrimaryKey(@Param("tempId") String tempId, @Param("paramName") String paramName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_d_tts_temp_param
     *
     * @mbggenerated Mon Jul 04 11:20:00 CST 2016
     */
    List<SDTtsTempParam> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_d_tts_temp_param
     *
     * @mbggenerated Mon Jul 04 11:20:00 CST 2016
     */
    int updateByPrimaryKey(SDTtsTempParam record);
}