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
package org.terasology.moonLandscapeGenerator.generator;

import org.terasology.core.world.generator.facetProviders.SeaLevelProvider;
import org.terasology.engine.SimpleUri;
import org.terasology.moonLandscapeGenerator.data.EnableLowGravity;
import org.terasology.moonLandscapeGenerator.generator.Rasterizer.IceMonumentRasterizer;
import org.terasology.moonLandscapeGenerator.generator.Rasterizer.MineShaftRasterizer;
import org.terasology.moonLandscapeGenerator.generator.Rasterizer.MoonWorldRasterizer;
import org.terasology.moonLandscapeGenerator.generator.facet.IceMonumentProvider;
import org.terasology.moonLandscapeGenerator.generator.facet.MineShaftProvider;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

/**
* This is a generator for a moon world
*/
@RegisterWorldGenerator(displayName = "Moon World", id = "MoonWorld", description = "This generator generates a moon world.")
public class MoonWorldGenerator extends BaseFacetedWorldGenerator {
    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public MoonWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    /**
    * This creates the world
    */
    @Override
    protected WorldBuilder createWorld() {
        //Tell the world to use low gravity
        CoreRegistry.put(EnableLowGravity.class, new EnableLowGravity(true));

        //Create a new world and adds the required provider and rasterizer classes
        return new WorldBuilder(worldGeneratorPluginLibrary)
                .addProvider(new MoonSurfaceProvider())
                .addProvider(new SeaLevelProvider(0))
                .addProvider(new MineShaftProvider())
                .addProvider(new IceMonumentProvider())
                .addRasterizer(new MoonWorldRasterizer())
                .addRasterizer(new MineShaftRasterizer())
                .addRasterizer(new IceMonumentRasterizer());
    }
}
