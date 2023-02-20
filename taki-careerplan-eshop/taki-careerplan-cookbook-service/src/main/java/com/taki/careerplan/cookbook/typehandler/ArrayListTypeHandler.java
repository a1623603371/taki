package com.taki.careerplan.cookbook.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taki.careerplan.domain.dto.Food;
import com.taki.careerplan.domain.dto.StepDetail;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName ArrayListTypeHandler
 * @Description TODO
 * @Author Long
 * @Date 2023/2/20 18:50
 * @Version 1.0
 */
@MappedTypes(value = {StepDetail.class, Food.class})
@MappedJdbcTypes(value = {JdbcType.VARCHAR},includeNullJdbcType = true)
public class ArrayListTypeHandler<T extends Object> extends BaseTypeHandler<List<T>> {

    private static  final ObjectMapper MAPPER = new ObjectMapper();

    private Class<List<T>> clazz;

    public ArrayListTypeHandler(Class<List<T>>  clazz) {

        if (clazz == null ){
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<T> ts, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,toJson(ts));
    }



    @Override
    public List<T> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return toObject(resultSet.getString(s),List.class);
    }



    @Override
    public List<T> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return toObject(resultSet.getString(i),List.class);
    }

    @Override
    public List<T> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return toObject(callableStatement.getString(i),List.class);
    }

    private List<T> toObject(String content, Class<List> listClass) {
        if (StringUtils.hasLength(content)){

            try {
                return (List<T>) MAPPER.readValue(content,listClass);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        return  null;

    }
    private String toJson(List<T> list) {
        try {
            return MAPPER.writeValueAsString(list);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
