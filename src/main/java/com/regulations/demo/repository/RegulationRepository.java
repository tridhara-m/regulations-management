package com.regulations.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.regulations.demo.entity.Regulation;


public interface RegulationRepository extends JpaRepository<Regulation, String>{

}
