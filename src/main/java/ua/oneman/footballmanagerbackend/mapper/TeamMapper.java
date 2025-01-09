package ua.oneman.footballmanagerbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.oneman.footballmanagerbackend.dto.req.TeamReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.TeamRespDTO;
import ua.oneman.footballmanagerbackend.model.Team;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    // Перетворення TeamReqDTO -> Team
    @Mapping(target = "owner", ignore = true) // Owner додається в сервісі
    Team toEntity(TeamReqDTO dto);

    // Перетворення Team -> TeamRespDTO
    @Mapping(target = "ownerUsername", source = "owner.username")
    TeamRespDTO toDTO(Team team);
}