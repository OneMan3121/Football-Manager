package ua.oneman.footballmanagerbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.oneman.footballmanagerbackend.dto.req.UserUpdateReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.secured.PrivateUserRespDTO;
import ua.oneman.footballmanagerbackend.dto.resp.open.PublicUserRespDTO;
import ua.oneman.footballmanagerbackend.dto.resp.open.PublicTeamRespDTO;
import ua.oneman.footballmanagerbackend.model.User;
import ua.oneman.footballmanagerbackend.model.Team;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TeamMapper.class})
public interface UserMapper {

    PrivateUserRespDTO toPrivateDTO(User user);

    // Якщо колекції маплити слід вручну
    @Mapping(source = "teams", target = "teams")
    PublicUserRespDTO toPublicDTO(User user);

    void updateEntity(@MappingTarget User user, UserUpdateReqDTO userUpdateReqDTO);

    // Явний мапінг у разі потреби
    List<PublicTeamRespDTO> mapTeams(List<Team> teams);
}