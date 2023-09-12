package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Categorie;
import com.mycompany.myapp.domain.Gestion;
import com.mycompany.myapp.domain.Immobilisation;
import com.mycompany.myapp.service.dto.CategorieDTO;
import com.mycompany.myapp.service.dto.GestionDTO;
import com.mycompany.myapp.service.dto.ImmobilisationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Immobilisation} and its DTO {@link ImmobilisationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImmobilisationMapper extends EntityMapper<ImmobilisationDTO, Immobilisation> {
    @Mapping(target = "categorie", source = "categorie", qualifiedByName = "categorieId")
    @Mapping(target = "gestion", source = "gestion", qualifiedByName = "gestionId")
    ImmobilisationDTO toDto(Immobilisation s);

    @Named("categorieId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategorieDTO toDtoCategorieId(Categorie categorie);

    @Named("gestionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GestionDTO toDtoGestionId(Gestion gestion);
}
