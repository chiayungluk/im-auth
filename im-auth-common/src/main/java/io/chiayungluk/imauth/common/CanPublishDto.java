package io.chiayungluk.imauth.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class CanPublishDto {
    private String senderUsername;
    private String senderUserId;

    private String receiverUsername;
    private String receiveUserId;
}
