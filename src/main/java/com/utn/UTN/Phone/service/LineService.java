package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.dto.LineDto;
import com.utn.UTN.Phone.exceptions.LineNotExistsException;
import com.utn.UTN.Phone.exceptions.NoDataFound;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.proyection.LineProyection;
import com.utn.UTN.Phone.repository.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LineService {

    private LineRepository lineRepository;

    @Autowired
    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }


    public void addLine(Line line) { lineRepository.save(line);}

    public List<Line> getAll() throws RecordNotExistsException {
        List<Line> lines = lineRepository.findAll();
        return Optional.ofNullable(lines).orElseThrow(() -> new RecordNotExistsException());
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Line> getLinesByUser(Integer id) throws RecordNotExistsException {
        List<Line> lines = lineRepository.getLinesByUser(id);
        return Optional.ofNullable(lines).orElseThrow(() -> new RecordNotExistsException());
    }

    public Line getLineByNumber(String num) throws LineNotExistsException {
        Line line = lineRepository.getLineByNumber(num);
        return Optional.ofNullable(line).orElseThrow(() -> new LineNotExistsException());
    }
    public List<Line> getLinesByUserDNI(String dni) throws RecordNotExistsException {
        List<Line> lines = lineRepository.getLinesByUserDNI(dni);
        return Optional.ofNullable(lines).orElseThrow(() -> new RecordNotExistsException());
    }

    public String createLine(String dni) {
        return lineRepository.createLine(dni);
    }

    public Boolean deleteLine(String num) {return lineRepository.deleteLine(num);}
}
