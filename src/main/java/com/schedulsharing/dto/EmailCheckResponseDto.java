package com.schedulsharing.dto;

import com.schedulsharing.controller.MemberController;
import lombok.Data;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Data
public class EmailCheckResponseDto {
    private boolean duplicate;
    private String message;

    public EmailCheckResponseDto(boolean duplicate, String message) {
        this.duplicate = duplicate;
        this.message = message;
    }

    public EntityModel<EmailCheckResponseDto> createSelfProfileLink(){
        WebMvcLinkBuilder selfLinkBuilder = linkTo(MemberController.class).slash("checkEmail");

        List<Link> links = Arrays.asList(
                selfLinkBuilder.withSelfRel(),
                new Link("/docs/index.html#resources-member-checkEmail").withRel("profile")
        );
        return EntityModel.of(this,links);
    }
}
