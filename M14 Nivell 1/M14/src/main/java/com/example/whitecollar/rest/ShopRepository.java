package com.example.whitecollar.rest;

//Spring Data JPA provides a collection of repository interfaces that
//help reducing boilerplate code required to implement the data access 
//layer for various databases
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whitecollar.models.Shop;

public interface ShopRepository extends JpaRepository<Shop, Integer>{
}