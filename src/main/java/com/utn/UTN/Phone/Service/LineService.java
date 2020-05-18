package com.utn.UTN.Phone.Service;

import com.utn.UTN.Phone.Model.Line;
import com.utn.UTN.Phone.Repository.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineService {

    private LineRepository lineRepository;

    @Autowired
    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }


    public void addLine(Line line) { lineRepository.save(line);}

    public List<Line> getAll() {
        return  lineRepository.findAll();
    }

}
