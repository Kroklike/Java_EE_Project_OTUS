package ru.otus.akn.project.ejb.api.stateless;

import ru.otus.akn.project.db.entity.PositionEntity;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.util.List;

@Remote
public interface PositionsService {

    PositionEntity getPositionEntity(@NotNull String positionName);

    List<PositionEntity> getAllPositionEntities();

    void deleteAllPositionsEntities();

    void saveAllPositions(@NotNull List<PositionEntity> positionEntities);

}
