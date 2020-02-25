package sample.first.Factory;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import sample.first.Entites.Bonus;
import sample.first.Entites.Boss;
import sample.first.Entites.Enemy;
import sample.first.EntityType;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class EnnemyFactory implements EntityFactory {

    @Spawns("Ennemy")
    public Entity newEnnemy(SpawnData data) {
        ArrayList l = FXGL.getGameState().getObject("spawners");
        Point2D point = (Point2D) l.get(FXGL.random(0, l.size() - 1));
        double x = point.getX();
        double y = point.getY();
        int rank = FXGL.random(1, 17);
        String sprite = "Enemy/Enemy " + rank + "-1.png";
        PhysicsComponent pc = new PhysicsComponent();
        pc.setBodyType(BodyType.DYNAMIC);
        return new EntityBuilder().from(data)
                .type(EntityType.ENNEMY)
                .at(x, y)
                .bbox(new HitBox(BoundingShape.box(32, 32)))
                .with(new CollidableComponent(true))
                .with(pc)
                .with(new Enemy(sprite, rank))
                .build();
    }

    @Spawns("Arrow")
    public Entity newArrow(SpawnData data) {
        javafx.geometry.Point2D dir = data.get("dir");
        double angle = data.get("angle");
        String sprite = data.get("sprite");
        EntityType type = sprite.equals("Arrow.png") ? EntityType.ARROW : EntityType.FIREBALL;
        Entity en = new EntityBuilder()
                .from(data)
                .with(new CollidableComponent(true))
                .viewWithBBox(sprite)
                .type(type)
                .with(new ProjectileComponent(dir, 400))
                .build();
        en.rotateBy(angle);
        return en;
    }

    @Spawns("Boss")
    public Entity newBoss(SpawnData data) {
        PhysicsComponent pc = new PhysicsComponent();
        pc.setBodyType(BodyType.DYNAMIC);
        return new EntityBuilder().from(data)
                .type(EntityType.BOSS)
                .at(130, 190)
                .bbox(new HitBox(BoundingShape.box(60, 60)))
                .with(new CollidableComponent(true))
                .with(pc)
                .with(new Boss())
                .build();
    }

    @Spawns("Bonus")
    public Entity newBonus(SpawnData data) {
        ArrayList l = FXGL.getGameState().getObject("spawners");
        Point2D point = (Point2D) l.get(FXGL.random(0, l.size() - 1));
        double x = point.getX();
        double y = point.getY();
        return new EntityBuilder()
                .at(x, y)
                .with(new Bonus())
                .bbox(new HitBox(BoundingShape.box(32, 32)))
                .with(new CollidableComponent(true))
                .type(EntityType.BONUS)
                .build();
    }
}
