package com.schedulsharing.service;

import com.schedulsharing.dto.yearMonth.YearMonthRequest;
import com.schedulsharing.dto.MySchedule.*;
import com.schedulsharing.dto.resource.MyScheduleResource;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.entity.schedule.MySchedule;
import com.schedulsharing.excpetion.MyScheduleNotFoundException;
import com.schedulsharing.excpetion.common.InvalidGrantException;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.repository.myschedule.MyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<MyScheduleResponse>> getMyScheduleList(YearMonthRequest yearMonthRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();
        List<MySchedule> myScheduleList = myScheduleRepository.findAllByEmail(member.getEmail(), yearMonthRequest);
        for (MySchedule mySchedule : myScheduleList) {
            if (!member.getEmail().equals(mySchedule.getMember().getEmail())) {
                throw new InvalidGrantException("조회 권한이 없습니다.");
            }
        }
        List<MyScheduleResponse> myScheduleResponseList = myScheduleList.stream()
                .map(mySchedule -> modelMapper.map(mySchedule, MyScheduleResponse.class))
                .collect(Collectors.toList());
        return MyScheduleResource.getMyScheduleListLink(myScheduleResponseList, member.getEmail());
    }

    public EntityModel<MyScheduleUpdateResponse> update(Long myScheduleId, MyScheduleUpdateRequest updateRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();
        MySchedule mySchedule = mySchedulefindById(myScheduleId);
        if (!member.equals(mySchedule.getMember())) {
            throw new InvalidGrantException("수정 권한이 없습니다.");
        }
        mySchedule.update(updateRequest);
        MyScheduleUpdateResponse myScheduleUpdateResponse = modelMapper.map(mySchedule, MyScheduleUpdateResponse.class);
        return MyScheduleResource.updateMyScheduleLink(myScheduleUpdateResponse);
    }

    public EntityModel<MyScheduleDeleteResponse> delete(Long myScheduleId, String email) {
        Member member = memberRepository.findByEmail(email).get();
        MySchedule mySchedule = mySchedulefindById(myScheduleId);
        if (!member.equals(mySchedule.getMember())) {
            throw new InvalidGrantException("삭제 권한이 없습니다.");
        }
        myScheduleRepository.deleteById(myScheduleId);
        MyScheduleDeleteResponse myScheduleDeleteResponse = MyScheduleDeleteResponse.builder()
                .message("나의 스케줄을 삭제하였습니다.")
                .success(true)
                .build();
        return MyScheduleResource.deleteMyScheduleLink(myScheduleId, myScheduleDeleteResponse);
    }

    private MySchedule mySchedulefindById(Long myScheduleId) {
        Optional<MySchedule> optionalMySchedule = myScheduleRepository.findById(myScheduleId);
        if (optionalMySchedule.isEmpty()) {
            throw new MyScheduleNotFoundException("내 스케줄이 존재하지 않습니다.");
        }
        return optionalMySchedule.get();
    }
}
