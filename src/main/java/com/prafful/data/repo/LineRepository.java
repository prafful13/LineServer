package com.prafful.data.repo;

import com.prafful.data.model.Line;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends CrudRepository<Line, String> {
}