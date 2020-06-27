package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.LineNotExistsException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
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


    public List<Line> getAll() throws RecordNotExistsException {
        List<Line> lines = lineRepository.findAll();
        return Optional.ofNullable(lines).orElseThrow(() -> new RecordNotExistsException());
    }

    public Line getLineByNumber(String num) throws LineNotExistsException {
        Line line = lineRepository.findByLinenumber(num);
        return Optional.ofNullable(line).orElseThrow(() -> new LineNotExistsException());
    }
    public List<Line> getLinesByUserDNI(String dni) throws RecordNotExistsException {
        List<Line> lines = lineRepository.getLinesByUserDNI(dni);
        return Optional.ofNullable(lines).orElseThrow(() -> new RecordNotExistsException());
    }

    public List<Line> getLinesByUser(User user) throws RecordNotExistsException {
        List<Line> lines = lineRepository.findAllByUser(user);
        return Optional.ofNullable(lines).orElseThrow(() -> new RecordNotExistsException());
    }

    public Integer createLine(Integer idUser,Integer idType) throws LineNotExistsException {
        Integer id=lineRepository.createLine(idUser,idType);
        return Optional.ofNullable(id).orElseThrow(() -> new LineNotExistsException());
    }

    public Integer disabledLine(Integer id) throws LineNotExistsException {
        Integer idLine=lineRepository.disabledLine(id);
        return Optional.ofNullable(idLine).orElseThrow(() -> new LineNotExistsException());
    }

}
