package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.dto.CallDto;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.repository.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CallService {

    private CallRepository callRepository;

    @Autowired
    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    public Call addCall(Line origin,Line dest ,Time duration, Timestamp dateTime) {
        Call call= new Call();
        call.setOriginCall(origin);
        call.setDestinationCall(dest);
        call.setDurationtime(duration);
        call.setCreateAt(dateTime);
        return callRepository.save(call);}

    public List<Call> getCallsByLine(Line line) throws RecordNotExistsException {
        List<Call> lines = callRepository.findAllByOriginCall(line);
        return Optional.ofNullable(lines).orElseThrow(() -> new RecordNotExistsException());
    }

    public List<Call> getCallsByDate(Line line, Date fromDate, Date toDate) throws RecordNotExistsException {
        List<Call> lines = callRepository.getCallsByDate(line.getId(), fromDate, toDate);
        return Optional.ofNullable(lines).orElseThrow(() -> new RecordNotExistsException());
    }

    public Call getCallSmall() throws RecordNotExistsException {
        Call call=callRepository.getCallSmall();
        return Optional.ofNullable(call).orElseThrow(() -> new RecordNotExistsException());
    }

}
