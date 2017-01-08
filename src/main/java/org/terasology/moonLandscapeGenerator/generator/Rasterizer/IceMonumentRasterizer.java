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
package org.terasology.moonLandscapeGenerator.generator.Rasterizer;

import org.terasology.math.ChunkMath;
import org.terasology.math.Region3i;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.BaseVector3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.moonLandscapeGenerator.generator.data.IceMonument;
import org.terasology.moonLandscapeGenerator.generator.data.MineShaft;
import org.terasology.moonLandscapeGenerator.generator.facet.IceMonumentFacet;
import org.terasology.moonLandscapeGenerator.generator.facet.MineShaftFacet;
import org.terasology.registry.CoreRegistry;
import org.terasology.utilities.random.FastRandom;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

import java.util.Map;

/*
* This is the rasterizer for the MoonWorld generator
*/
public class IceMonumentRasterizer implements WorldRasterizer {
    private Block ice;
    private Block gold;
    private FastRandom chance;

    @Override
    public void initialize() {
        ice = CoreRegistry.get(BlockManager.class).getBlock("Core:Ice");
        gold = CoreRegistry.get(BlockManager.class).getBlock("Core:GoldOre");

        chance = new FastRandom(547846885);
    }


    /*
    * This is where the blocks are placed
    */
    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        IceMonumentFacet monumentFacet = chunkRegion.getFacet(IceMonumentFacet.class);

        for (Map.Entry<BaseVector3i, IceMonument> entry : monumentFacet.getWorldEntries().entrySet()) {
            // there should be a mine shaft here
            // create a couple 3d regions to help iterate through the cube shape, inside and out
            Vector3i centerPosition = new Vector3i(entry.getKey());
            int extent = entry.getValue().getExtent();
            centerPosition.add(0, extent, 0);
            Region3i walls = Region3i.createFromCenterExtents(centerPosition, extent);

            // loop through each of the positions in the cube, ignoring the is
            for (Vector3i newBlockPosition : walls) {
                if (chunkRegion.getRegion().encompasses(newBlockPosition) && newBlockPosition.x == centerPosition.x && newBlockPosition.z == centerPosition.z && newBlockPosition.y == centerPosition.y) {
                    chunk.setBlock(ChunkMath.calcBlockPos(newBlockPosition), gold);
                    continue;
                }

                if (chunkRegion.getRegion().encompasses(newBlockPosition)) {
                    chunk.setBlock(ChunkMath.calcBlockPos(newBlockPosition), ice);
                }
            }
        }
    }
}
