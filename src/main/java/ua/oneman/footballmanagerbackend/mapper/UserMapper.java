package ua.oneman.footballmanagerbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ua.oneman.footballmanagerbackend.dto.req.UserUpdateReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PrivateUserRespDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PublicUserRespDTO;
import ua.oneman.footballmanagerbackend.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    PrivateUserRespDTO toPrivateDTO(User user);

    PublicUserRespDTO toPublicDTO(User user);

    void updateEntity(@MappingTarget User user, UserUpdateReqDTO userUpdateReqDTO);
}