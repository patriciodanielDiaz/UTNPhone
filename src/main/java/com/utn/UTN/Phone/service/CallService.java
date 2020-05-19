package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.repository.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallService {

    private CallRepository callRepository;

    @Autowired
    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }



    //----------------------------------------------------------------------------
    public void addCall(Call call) {
        callRepository.save(call);
    }

    public List<Call> getAll() {
        return  callRepository.findAll();
    }
}
