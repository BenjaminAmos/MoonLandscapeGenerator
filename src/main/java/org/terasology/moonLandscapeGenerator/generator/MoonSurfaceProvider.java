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

import org.terasology.math.geom.*;
import org.terasology.utilities.procedural.*;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

/**
* This controls the height for the moon terrain using noise
*/
@Produces(SurfaceHeightFacet.class)
public class MoonSurfaceProvider implements FacetProvider {

    /**
    * This is for generating noise
    */
    private Noise surfaceNoise;

    /**
     * This is for generating noise
     */
    private Noise craterNoise;

    /**
    * This sets the seed for the terrain
     */
    @Override
    public void setSeed(long seed) {
        surfaceNoise = new SubSampledNoise(new PerlinNoise(seed), new Vector2f(0.01f, 0.01f), 1);
        craterNoise = new SubSampledNoise(new SimplexNoise((int)seed), new Vector2f(0.01f, 0.01f), 1);
    }

    /**
    * This creates the terrain heights
     */
    @Override
    public void process(GeneratingRegion region) {
        Border3D border = region.getBorderForFacet(SurfaceHeightFacet.class);
        SurfaceHeightFacet facet = new SurfaceHeightFacet(region.getRegion(), border);

        Rect2i processRegion = facet.getWorldRegion();
        for (BaseVector2i position : processRegion.contents()) {
            float noiseResult = surfaceNoise.noise(position.x(), position.y());
            float craterResult = craterNoise.noise(position.x(), position.y());

            facet.setWorld(position, noiseResult * 20);
        }

        region.setRegionFacet(SurfaceHeightFacet.class, facet);
    }
}
