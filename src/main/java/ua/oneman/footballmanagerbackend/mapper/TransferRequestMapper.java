package ua.oneman.footballmanagerbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.oneman.footballmanagerbackend.dto.resp.TransferRequestRespDTO;
import ua.oneman.footballmanagerbackend.model.TransferRequest;

@Mapper(componentModel = "spring")
public interface TransferRequestMapper {

    /**
     * Мапінг TransferRequest на TransferRequestRespDTO.
     * @param transferRequest Модель TransferRequest.
     * @return Підготовлене DTO TransferRequestRespDTO.
     */
    @Mapping(source = "player.id", target = "playerId")
    @Mapping(source = "fromTeam.id", target = "fromTeamId")
    @Mapping(source = "toTeam.id", target = "toTeamId")
    @Mapping(source = "buyer.username", target = "buyerUsername")
    @Mapping(source = "seller.username", target = "sellerUsername")
    TransferRequestRespDTO toDto(TransferRequest transferRequest);
}