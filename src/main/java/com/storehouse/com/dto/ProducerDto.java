package com.storehouse.com.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProducerDto {
	private Long producerId;
	private String producerName;
	private String producerLocation;
	private UserDto userDto;
}
