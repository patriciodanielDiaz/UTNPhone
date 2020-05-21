package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.NoDataFound;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.repository.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Line> getLinesByUser(Integer id) throws NoDataFound {
        List<Line> lines = lineRepository.getLinesByUser(id);
        return Optional.ofNullable(lines).orElseThrow(() -> new NoDataFound());
    }
}
