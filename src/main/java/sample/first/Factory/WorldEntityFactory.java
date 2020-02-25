package sample.first.Factory;


import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import sample.first.EntityType;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public class WorldEntityFactory implements EntityFactory {
    @Spawns("Limit")
    public Entity newLimit(SpawnData data) {
        PhysicsComponent pc = FXGL.getGameState().getObject("pc");
        return new EntityBuilder()
                .from(data)
                .at(data.getX(), data.getY())
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .type(EntityType.DECOR)
                .build();
    }

    @Spawns("Spawners")
    public Entity newSpawners(SpawnData data) {
        ArrayList<Point2D> spawners = FXGL.getGameState().getObject("spawners");
        spawners.add(new Point2D.Double(data.getX(), data.getX()));
        FXGL.getGameState().setValue("spawners", spawners);
        return new Entity();
    }


}
