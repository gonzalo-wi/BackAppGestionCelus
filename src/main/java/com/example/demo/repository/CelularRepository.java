package com.example.demo.repository;

import com.example.demo.model.Celular;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelularRepository extends JpaRepository<Celular, Integer> {
}
