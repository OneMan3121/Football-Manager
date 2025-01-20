package ua.oneman.footballmanagerbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.oneman.footballmanagerbackend.dto.req.PlayerReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PlayerRespDTO;
import ua.oneman.footballmanagerbackend.model.Player;

@Mapper(componentModel = "spring", uses = {TeamMapper.class})
public interface PlayerMapper {

    // Мапимо DTO на сутність
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Player toEntity(PlayerReqDTO dto);

    @Mapping(target = "teamName", source = "team.name")
    PlayerRespDTO toDTO(Player player);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(@MappingTarget Player existingPlayer, PlayerReqDTO dto);
}