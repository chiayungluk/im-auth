package io.chiayungluk.imauth.client;

import io.chiayungluk.imauth.common.CanConnectDto;
import io.chiayungluk.imauth.common.CanPublishDto;
import io.chiayungluk.imauth.common.CanSubscribeDto;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * For authentication
 */
public class ImAuthClient {
    private static final String TOKEN_PREFIX_BEARER = "Bearer ";

    private String host;

    private final WebClient client;


    public ImAuthClient(@NonNull String host) {
        client = WebClient.builder()
                .baseUrl(host)
                .build();
    }

    /**
     * Intended to be called before connecting
     *
     * @param token JWT token
     * @return
     */
    public Mono<Boolean> canConnect(@NonNull String username, @NonNull String userId, @NonNull String token) {
        CanConnectDto canConnectDto = new CanConnectDto();
        canConnectDto.setUsername(username);
        canConnectDto.setUserId(userId);
        return client.post().uri("/canConnect")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(canConnectDto)
                .accept(MediaType.APPLICATION_JSON)
                .header(TOKEN_PREFIX_BEARER + token)
                .exchangeToMono(response -> Mono.just(response.statusCode().is2xxSuccessful()));
    }

    /**
     * Intended to be called before subscribe
     *
     * @param username
     * @param userId
     * @param groupId
     * @return
     */
    public Mono<Boolean> canSubscribe(@NonNull String username, @NonNull String userId, @NonNull String groupId) {
        CanSubscribeDto canSubscribeDto = new CanSubscribeDto();
        canSubscribeDto.setUsername(username);
        canSubscribeDto.setUserId(userId);
        canSubscribeDto.setGroupId(groupId);
        return client.post().uri("/canSubscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(canSubscribeDto)
                .exchangeToMono(response -> Mono.just(response.statusCode().is2xxSuccessful()));
    }

    /**
     * Intended to be called before publish
     *
     * @param senderUsername
     * @param senderUserId
     * @param receiverUsername
     * @param receiverUserId
     * @return
     */
    public Mono<Boolean> canPublish(@NonNull String senderUsername, @NonNull String senderUserId,
                                    @NonNull String receiverUsername, @NonNull String receiverUserId) {
        if (senderUsername.equals(receiverUsername) && senderUserId.equals(receiverUserId)) {
            return Mono.just(true);
        }
        CanPublishDto canPublishDto = new CanPublishDto();
        canPublishDto.setSenderUsername(senderUsername);
        canPublishDto.setSenderUserId(senderUserId);
        canPublishDto.setReceiverUsername(receiverUsername);
        canPublishDto.setReceiveUserId(receiverUserId);
        return client.post().uri("/canPublish")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(canPublishDto)
                .exchangeToMono(response -> Mono.just(response.statusCode().is2xxSuccessful()));
    }
}
