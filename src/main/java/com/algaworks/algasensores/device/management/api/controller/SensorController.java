package com.algaworks.algasensores.device.management.api.controller;

import com.algaworks.algasensores.device.management.api.model.SensorInput;
import com.algaworks.algasensores.device.management.api.model.SensorOutput;
import com.algaworks.algasensores.device.management.common.IDGenerator;
import com.algaworks.algasensores.device.management.domain.model.Sensor;
import com.algaworks.algasensores.device.management.domain.model.SensorId;
import com.algaworks.algasensores.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository repository;

    @PutMapping("/{sensorId}/enable")
    public SensorOutput enableSensor(@PathVariable final TSID sensorId) {

        SensorId id = new SensorId(sensorId);

        Sensor sensor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sensor.setEnabled(true);
        sensor = repository.saveAndFlush(sensor);

        return convertToSensor(sensor);

    }

    @DeleteMapping("/{sensorId}/enable")
    public SensorOutput disableSensor(@PathVariable final TSID sensorId) {
        SensorId id = new SensorId(sensorId);

        Sensor sensor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sensor.setEnabled(false);
        sensor = repository.saveAndFlush(sensor);

        return convertToSensor(sensor);
    }


    @DeleteMapping("/{sensorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final TSID sensorId) {
        SensorId id = new SensorId(sensorId);

        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        repository.deleteById(id);
    }

    @PutMapping("/{sensorId}")
    @ResponseStatus(HttpStatus.OK)
    public SensorOutput update(@PathVariable final TSID sensorId, @RequestBody SensorInput sensorInput) {

        SensorId id = new SensorId(sensorId);

        Sensor sensor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sensor.setName(sensorInput.getName());
        sensor.setIp(sensorInput.getIp());
        sensor.setLocation(sensorInput.getLocation());
        sensor.setProtocol(sensorInput.getProtocol());
        sensor.setModel(sensorInput.getModel());

        sensor = repository.saveAndFlush(sensor);

        return convertToSensor(sensor);
    }

    @GetMapping
    public Page<SensorOutput> getSensors(@PageableDefault Pageable pageable) {

        Page<Sensor> sensors = repository.findAll(pageable);
        return sensors.map(this::convertToSensor);

    }

    @GetMapping("/{sensorId}")
    public SensorOutput getSensor(@PathVariable("sensorId") TSID sensorId) {
        return repository.findById(new SensorId(sensorId))
                .map(this::convertToSensor)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput sensorInput) {

        Sensor sensor = Sensor.builder()
                .id(new SensorId(IDGenerator.generate()))
                .name(sensorInput.getName())
                .ip(sensorInput.getIp())
                .location(sensorInput.getLocation())
                .protocol(sensorInput.getProtocol())
                .model(sensorInput.getModel())
                .enabled(false)
                .build();

        sensor = repository.saveAndFlush(sensor);

        return convertToSensor(sensor);

    }

    private SensorOutput convertToSensor(Sensor sensor) {
        return SensorOutput.builder()
                .id(sensor.getId().getValue())
                .name(sensor.getName())
                .ip(sensor.getIp())
                .location(sensor.getLocation())
                .protocol(sensor.getProtocol())
                .model(sensor.getModel())
                .enabled(sensor.getEnabled())
                .build();
    }

}
