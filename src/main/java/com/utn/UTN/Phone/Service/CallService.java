package com.utn.UTN.Phone.Service;

import com.utn.UTN.Phone.Repository.CallRespository;
import org.springframework.stereotype.Service;

@Service
public class CallService {

    private CallRespository callRespository;

    public CallService(CallRespository callRespository)
    {
        this.callRespository = callRespository;
    }
}
