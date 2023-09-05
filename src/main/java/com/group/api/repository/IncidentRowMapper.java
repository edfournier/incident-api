package com.group.api.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.group.api.model.Incident;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Maps ResponseSet rs from query to an Incident object. 
 */
@Component
public class IncidentRowMapper implements RowMapper<Incident> {

    @Override
    public Incident mapRow(ResultSet rs, int rowNum) throws SQLException {
        Incident incident = new Incident();
        incident.setDate(rs.getString("date"));
        incident.setLog((String[])rs.getArray("log").getArray());

        return incident;
    }
}
