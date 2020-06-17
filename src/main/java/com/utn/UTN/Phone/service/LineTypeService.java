package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.LineNotExistsException;
import com.utn.UTN.Phone.exceptions.LineTypeNotExistsException;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.LineType;
import com.utn.UTN.Phone.repository.LineRepository;
import com.utn.UTN.Phone.repository.LineTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LineTypeService {

    private LineTypeRepository lineTypeRepository;

    @Autowired
    public LineTypeService(LineTypeRepository lineTypeRepository) {
        this.lineTypeRepository = lineTypeRepository;
    }

    public LineType getByType(String type) throws LineTypeNotExistsException {
        LineType lineType = lineTypeRepository.findByType(type);
        return Optional.ofNullable(lineType).orElseThrow(() -> new LineTypeNotExistsException());
    }
}
