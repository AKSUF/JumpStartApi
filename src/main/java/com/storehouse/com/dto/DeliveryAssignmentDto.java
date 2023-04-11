package com.storehouse.com.dto;

import com.storehouse.com.entity.Delivery;
import com.storehouse.com.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DeliveryAssignmentDto {
	private Long deliveryassignId;
	private UserDto deliveryman;
	private UserDto assignedBy;
	private DeliveryDto delivery;
}
