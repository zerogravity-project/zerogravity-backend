package com.zerogravity.myapp.emotion.typehandler;

import com.zerogravity.myapp.emotion.dto.EmotionRecord;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MyBatis TypeHandler for EmotionRecord.Type enum
 * Converts between database string values (lowercase: daily/moment)
 * and Java enum constants (uppercase: DAILY/MOMENT)
 */
@MappedTypes(EmotionRecord.Type.class)
public class EmotionRecordTypeHandler extends BaseTypeHandler<EmotionRecord.Type> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EmotionRecord.Type parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public EmotionRecord.Type getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : EmotionRecord.Type.fromValue(value);
    }

    @Override
    public EmotionRecord.Type getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : EmotionRecord.Type.fromValue(value);
    }

    @Override
    public EmotionRecord.Type getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : EmotionRecord.Type.fromValue(value);
    }
}
