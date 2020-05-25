package com.utn.UTN.Phone.Service;

import com.utn.UTN.Phone.Model.LineType;
import com.utn.UTN.Phone.Repository.LineTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineTypeService {

    private LineTypeRepository lineTypeRepository;
    @Autowired

    public LineTypeService(LineTypeRepository lineTypeRepository)
    {
        this.lineTypeRepository = lineTypeRepository;
    }
    public List<LineType> getAll()
    {
        return  lineTypeRepository.findAll();
    }

    public LineType getLineTypeById(Integer id) {
        return lineTypeRepository.findById(id).get();
    }

    public void saveOrUpdate(LineType lineType) {
        lineTypeRepository.save(lineType);
    }

    public void delete(Integer id) {
        lineTypeRepository.deleteById(id);
    }
}
