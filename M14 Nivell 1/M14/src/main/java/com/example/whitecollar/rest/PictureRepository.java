package com.example.whitecollar.rest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.whitecollar.models.Picture;

import javax.transaction.Transactional;
//Spring Data JPA provides a collection of repository interfaces that
//help reducing boilerplate code required to implement the data access 
//layer for various databases
public interface PictureRepository extends JpaRepository<Picture, Integer>{
    Page<Picture> findByShopId(Integer shopId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Picture p WHERE p.shop.id = ?1")
    void deleteByShopId(Integer shopId);
}