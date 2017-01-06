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
package org.terasology.moonLandscapeGenerator.generator.facet;

import org.terasology.math.TeraMath;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.moonLandscapeGenerator.generator.data.MineShaft;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.random.FastRandom;
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(MineShaftFacet.class)
@Requires(@Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(sides = 4)))
public class MineShaftProvider implements FacetProvider {
    private Noise noise;
    private FastRandom random;

    @Override
    public void setSeed(long seed) {
        noise = new SimplexNoise(seed);
        random = new FastRandom(seed);
    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border = region.getBorderForFacet(MineShaftFacet.class).extendBy(0, 8, 4);
        MineShaftFacet facet = new MineShaftFacet(region.getRegion(), border);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        Rect2i worldRegion = surfaceHeightFacet.getWorldRegion();

        for (int wz = worldRegion.minY(); wz <= worldRegion.maxY(); wz++) {
            for (int wx = worldRegion.minX(); wx <= worldRegion.maxX(); wx++) {
                int surfaceHeight = TeraMath.floorToInt(surfaceHeightFacet.getWorld(wx, wz));

                // check if height is within this region
                if (surfaceHeight >= facet.getWorldRegion().minY()
                        && surfaceHeight <= facet.getWorldRegion().maxY()) {

                    boolean placeMineShaft = false;
                    boolean previousValue = false;

                    for (int i = 0; i < 20; i++) {
                        placeMineShaft = random.nextBoolean();
                        previousValue = placeMineShaft;

                        if (previousValue && placeMineShaft) {
                            placeMineShaft = true;
                        }
                        else {
                            placeMineShaft = false;
                        }
                    }

                    // TODO: check for overlap
                    if ((surfaceHeight - 6) > facet.getWorldRegion().minY()
                            && noise.noise(wx, wz) > 0.99
                            && placeMineShaft) {
                        facet.setWorld(wx, surfaceHeight - 6, wz, new MineShaft());
                    }
                }
            }
        }

        region.setRegionFacet(MineShaftFacet.class, facet);
    }
}
