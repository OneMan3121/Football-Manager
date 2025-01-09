package ua.oneman.footballmanagerbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.oneman.footballmanagerbackend.dto.req.PlayerReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PlayerRespDTO;
import ua.oneman.footballmanagerbackend.model.Player;

@Mapper(componentModel = "spring", uses = {TeamMapper.class})
public interface PlayerMapper {

    Player toEntity(PlayerReqDTO dto);

    @Mapping(target = "teamName", source = "team.name")
    PlayerRespDTO toDTO(Player player);
}