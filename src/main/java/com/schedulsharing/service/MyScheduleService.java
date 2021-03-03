package com.schedulsharing.service;

import com.schedulsharing.dto.MySchedule.*;
import com.schedulsharing.dto.resource.MyScheduleResource;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.entity.schedule.MySchedule;
import com.schedulsharing.excpetion.InvalidGrantException;
import com.schedulsharing.excpetion.MyScheduleNotFoundException;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.repository.MyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyScheduleService {
    private final MyScheduleRepository myScheduleRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public EntityModel<MyScheduleCreateResponse> create(MyScheduleCreateRequest myScheduleCreateRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();

        MySchedule mySchedule = MySchedule.createMySchedule(myScheduleCreateRequest, member);
        MySchedule savedMySchedule = myScheduleRepository.save(mySchedule);
        MyScheduleCreateResponse myScheduleCreateResponse = modelMapper.map(savedMySchedule, MyScheduleCreateResponse.class);

        return MyScheduleResource.createMyScheduleLink(myScheduleCreateResponse);
    }

    @Transactional(readOnly = true)
    public EntityModel<MyScheduleResponse> getMySchedule(Long myScheduleId, String email) {
        Member member = memberRepository.findByEmail(email).get();
        MySchedule mySchedule = mySchedulefindById(myScheduleId);
        MyScheduleResponse myScheduleResponse = modelMapper.map(mySchedule, MyScheduleResponse.class);
        return MyScheduleResource.getMyScheduleLink(myScheduleResponse);
    }

    public EntityModel<MyScheduleUpdateResponse> update(Long myScheduleId, MyScheduleUpdateRequest updateRequest) {
        MySchedule mySchedule = mySchedulefindById(myScheduleId);
        mySchedule.update(updateRequest);
        MyScheduleUpdateResponse myScheduleUpdateResponse = modelMapper.map(mySchedule, MyScheduleUpdateResponse.class);
        return MyScheduleResource.updateMyScheduleLink(myScheduleUpdateResponse);
    }

    private MySchedule mySchedulefindById(Long myScheduleId) {
        Optional<MySchedule> optionalMySchedule = myScheduleRepository.findById(myScheduleId);
        if (optionalMySchedule.isEmpty()) {
            throw new MyScheduleNotFoundException("내 스케줄이 존재하지 않습니다.");
        }
        return optionalMySchedule.get();
    }
}
