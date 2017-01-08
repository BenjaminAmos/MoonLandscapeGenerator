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
import org.terasology.math.geom.*;
import org.terasology.moonLandscapeGenerator.generator.data.IceMonument;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(IceMonumentFacet.class)
@Requires(@Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(sides = 4)))
public class IceMonumentProvider implements FacetProvider {
    private Noise noise;
    private boolean monumentPlaced;

    /**
     * @param seed the seed value (typically used for random number generators)
     */
    @Override
    public void setSeed(long seed) {
        noise = new SimplexNoise(seed);
    }

    @Override
    public void process(GeneratingRegion region) {
        Border3D border = region.getBorderForFacet(IceMonumentFacet.class).extendBy(0, 8, 8);
        IceMonumentFacet facet = new IceMonumentFacet(region.getRegion(), border);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        Rect2i worldRegion = surfaceHeightFacet.getWorldRegion();

        if (!monumentPlaced) {
            for (int wz = worldRegion.minY(); wz <= worldRegion.maxY(); wz++) {
                for (int wx = worldRegion.minX(); wx <= worldRegion.maxX(); wx++) {
                    int surfaceHeight = TeraMath.floorToInt(surfaceHeightFacet.getWorld(wx, wz));

                    // check if height is within this region
                    if (surfaceHeight >= facet.getWorldRegion().minY() && surfaceHeight <= facet.getWorldRegion().maxY()) {

                        // TODO: check for overlap
                        if ((surfaceHeight) > facet.getWorldRegion().minY()
                                && noise.noise(wx, wz) > 0.99) {
                            facet.setWorld(wx, surfaceHeight, wz, new IceMonument());
                            monumentPlaced = true;
                        }
                    }
                }
            }
        }

        region.setRegionFacet(IceMonumentFacet.class, facet);
    }
}
