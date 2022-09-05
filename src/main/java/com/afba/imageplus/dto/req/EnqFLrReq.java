package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnqFLrReq {

    String userId;
    @NotBlank(message = "QueueID should not be empty.")
    String queueId;
    @Size(min=15,max = 15,message = "queue priority value must be 15 characters")
    String combination;
}
