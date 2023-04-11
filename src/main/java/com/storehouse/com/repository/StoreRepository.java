package com.storehouse.com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.storehouse.com.entity.Store;
import com.storehouse.com.entity.StoreProduct;
import com.storehouse.com.entity.User;

public interface StoreRepository extends JpaRepository<Store,Long> {

	@Query("SELECT s FROM Store s WHERE s.storeManager.id = :storeManagerId")
	Store findStoreByStoreManagerId(@Param("storeManagerId") Long storeManagerId);

	//Store findByStoreManager(User user);

	void save(StoreProduct storeProduct);

	Store findByStoreManager(User user);



	List<Store> findAllByStoreManager(User user);









	//List<Store> findByStoreManager(User user);

	//Optional<Store> findByStoreManagerId(Long storemangerId);






	



	


}
