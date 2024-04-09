package com.his.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.his.entity.CoNotice;

public interface CoNoticeRepository extends JpaRepository<CoNotice, Integer> {
	CoNotice findByAppNumber(Integer appNumber);
}
