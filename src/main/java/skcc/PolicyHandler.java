package skcc;

import skcc.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyHandler{

    @Autowired
    HospitalRepository hospitalRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequested_PCntChange(@Payload Requested requested){

        if(requested.isMe()){
            System.out.println("##### 검진 요청으로 인한 인원 변화 : " + requested.toJson());
            List<Hospital> list = hospitalRepository.findByHospitalId(requested.getHospitalId());
            // 병원아이디 + 날짜
            for(Hospital temp : list){
                if(temp.getChkDate().equals(requested.getChkDate())){
                    temp.setPCnt(temp.getPCnt()-1);
                    hospitalRepository.save(temp);
                }
            }
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceled_PCntChange(@Payload Canceled canceled){

        if(canceled.isMe()){
            System.out.println("##### 검진 취소 요청으로 인한 인원 변화 : " + canceled.toJson());
            List<Hospital> list = hospitalRepository.findByHospitalId(canceled.getHospitalId());

            for(Hospital temp : list){
                // 병원아이디 + 날짜
                if(temp.getChkDate().equals(canceled.getChkDate())){
                    temp.setPCnt(temp.getPCnt()+1);
                    hospitalRepository.save(temp);
                }
            }
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverScreeningChanged_PCntChange(@Payload ScreeningChanged screeningChanged){

        if(screeningChanged.isMe()){
            // 1. 날짜만 변경
            //    기존 날짜 +1 , 요청날짜 -1 필요
            // 2. 병원을 변경....생각하지말자
        }
    }

}
