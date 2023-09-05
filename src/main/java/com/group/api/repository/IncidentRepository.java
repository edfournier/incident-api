package com.group.api.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import com.group.api.model.Incident;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class IncidentRepository {
    
    private JdbcTemplate template;
    private RowMapper<Incident> mapper;

    public IncidentRepository(JdbcTemplate template, RowMapper<Incident> mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public Incident getIncident(String date) {
        try {
            String sql = "SELECT date, log FROM incidents WHERE date = ?";
            return template.queryForObject(sql, mapper, date); 
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void addIncident(String date, String[] log) {
        String sql = "INSERT INTO incidents VALUES (?, ?)";
        template.update(sql, date, log);
    }
    
    public boolean existsIncident(String date) {
        String sql = "SELECT date, log FROM incidents WHERE date = ?";
        List<String> list = template.query(sql, (rs, rowNum) -> {
            return rs.getString("date");
        }, date);
        return list.size() == 1;
    }
}
