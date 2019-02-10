package ru.otus.akn.project.db.api;

import ru.otus.akn.project.db.entity.PositionEntity;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

@Local
public interface PositionsDAO {

    PositionEntity getPositionEntity(@NotNull String positionName);

    PositionEntity getPositionEntity(@NotNull Long positionId);

    List<PositionEntity> getAllPositionEntities();

    void deleteAllPositionsEntities();

    void saveAllPositions(@NotNull List<PositionEntity> positionEntities);
}
