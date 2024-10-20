package com.cpt.payments.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ValidatorRulesDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<String> getAllRuleNames() {
        String sql = "SELECT rule_name FROM validations.validator_rules";

        // Execute the query and map results to a list of strings
        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(), (rs, rowNum) -> rs.getString("rule_name"));
    }
}

