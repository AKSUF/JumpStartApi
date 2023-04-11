package com.storehouse.com;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.storehouse.com.config.AppConstants;
import com.storehouse.com.entity.Role;
import com.storehouse.com.repository.RoleRepository;

@SpringBootApplication
public class StorehouseApplication  implements CommandLineRunner{
	@Autowired
	private RoleRepository roleRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(StorehouseApplication.class, args);
	}
	// model mapper
		@Bean
		public ModelMapper modelMapper() {
			return new ModelMapper();
		}
		// setting the roles on run
		@Override
		public void run(String... args) throws Exception {

			try {

				// admin role
				Role role_admin = new Role();
				role_admin.setRole_id(AppConstants.ROLE_ADMIN.longValue());
				role_admin.setRole_name("ROLE_ADMIN");

				// user role
				Role role_custmer = new Role();
				role_custmer.setRole_id(AppConstants.ROLE_CUSTOMER.longValue());
				role_custmer.setRole_name("ROLE_CUSTOMER");

				// partner role
				Role role_producer = new Role();
				role_producer.setRole_id(AppConstants.ROLE_PRODUCER.longValue());
				role_producer.setRole_name("ROLE_PRODUCER");

				// rider role
				Role role_rider = new Role();
				role_rider.setRole_id(AppConstants.ROLE_RIDER.longValue());
				role_rider.setRole_name("ROLE_RIDER");

				// volunteer role
				Role role_storemanager = new Role();
				role_storemanager.setRole_id(AppConstants.ROLE_STOREMANAGER.longValue());
				role_storemanager.setRole_name("ROLE_STOREMANAGER");

				
				Role role_manager=new Role();
				role_manager.setRole_id(AppConstants.ROLE_MANAGER.longValue());
				role_manager.setRole_name("ROLE_MANAGER");
				
				
//				// career giver role
//				Role role_careergiver = new Role();
//				role_careergiver.setRole_id(AppConstants.ROLE_CARERGIVER.longValue());
//				role_careergiver.setRole_name("ROLE_CAREGIVER");

				List<Role> roles = Arrays.asList(role_admin, role_custmer, role_producer, role_rider, role_storemanager,role_manager
						);

				this.roleRepository.saveAll(roles);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
