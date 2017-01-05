/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.moonLandscapeGenerator.system;

import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.CharacterMovementComponent;
import org.terasology.logic.characters.CharacterImpulseEvent;
import org.terasology.math.geom.Vector3f;
import org.terasology.moonLandscapeGenerator.component.LowGravityComponent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.moonLandscapeGenerator.data.EnableLowGravity;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;

/*
* This system simulates a "low gravity" environment
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class LowGravitySystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    @In
    private EntityManager entityManager;
    private boolean systemEnabled;

    /**
    * This is for setting the initial variables
    */
    @Override
    public void initialise() {
        try {
            systemEnabled = CoreRegistry.get(EnableLowGravity.class).enable;
        } catch (NullPointerException ex) {
            systemEnabled = false;
        }
    }

    /**
     * Update function for the Component System, which is called each
     * time the engine is updated.
     *
     * @param delta The time (in seconds) since the last engine update.
     */
    @Override
    public void update(float delta) {
        if (!systemEnabled) {
            return;
        }

        for (EntityRef entity : entityManager.getEntitiesWith(LowGravityComponent.class)) {
            float yVelocity = entity.getComponent(CharacterMovementComponent.class).getVelocity().y;

            if (yVelocity > 0) {
                continue;
            }

            entity.send(new CharacterImpulseEvent(new Vector3f(0, (yVelocity * -1) - 2, 0)));
        }
    }

    /**
    * This is for handling the disposal of variables
     */
    @Override
    public void shutdown() {
        try {
            systemEnabled = CoreRegistry.get(EnableLowGravity.class).enable;
            CoreRegistry.put(EnableLowGravity.class, new EnableLowGravity(false));
        } catch (NullPointerException ex) {
            systemEnabled = false;
        }
    }

}
