package ua.oneman.footballmanagerbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.oneman.footballmanagerbackend.dto.req.TeamReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.admin.AdminTeamRespDTO;
import ua.oneman.footballmanagerbackend.dto.resp.open.PublicTeamRespDTO;
import ua.oneman.footballmanagerbackend.dto.resp.secured.PrivateTeamRespDTO;
import ua.oneman.footballmanagerbackend.model.Team;

@Mapper(componentModel = "spring")
public interface TeamMapper {



    PublicTeamRespDTO toPublicDTO(Team team);
    PrivateTeamRespDTO toPrivateDTO(Team team);
    AdminTeamRespDTO toAdminDTO(Team team);



    //  TeamReqDTO -> Team
    @Mapping(target = "owner", ignore = true) // Owner додається в сервісі
    Team toEntity(TeamReqDTO dto);

}