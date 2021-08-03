package com.i2dsp.maintenance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.i2dsp.maintenance.mapper")
@EnableAsync
@EnableTransactionManagement(proxyTargetClass = true)
public class I2dspMaintenanceRecordApplication {

	public static void main(String[] args) {
		SpringApplication.run(I2dspMaintenanceRecordApplication.class, args);
	}

}
