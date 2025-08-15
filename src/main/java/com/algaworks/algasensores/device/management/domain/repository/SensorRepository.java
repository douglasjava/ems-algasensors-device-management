package com.algaworks.algasensores.device.management.domain.repository;

import com.algaworks.algasensores.device.management.domain.model.Sensor;
import com.algaworks.algasensores.device.management.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, SensorId> {

}
