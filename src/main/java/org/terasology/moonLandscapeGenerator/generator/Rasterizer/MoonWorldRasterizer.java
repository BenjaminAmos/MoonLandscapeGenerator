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
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.utilities.random.FastRandom;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

/*
* This is the rasterizer for the MoonWorld generator
*/
public class MoonWorldRasterizer implements WorldRasterizer {
    private Block stone;
    private Block ironOre;
    private Block copperOre;
    private Block diamondOre;
    private Block water;
    private FastRandom chance;

    @Override
    public void initialize() {
        BlockManager blockManager = CoreRegistry.get(BlockManager.class);

        stone = blockManager.getBlock("Core:Stone");
        ironOre = blockManager.getBlock("Core:IronOre");
        copperOre = blockManager.getBlock("Core:CopperOre");
        diamondOre = blockManager.getBlock("Core:DiamondOre");
        water = blockManager.getBlock("Core:Water");

        chance = new FastRandom(547846885);
    }


    /*
    * This is where the blocks are placed
    */
    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        for (Vector3i position : chunkRegion.getRegion()) {
            float surfaceHeight = surfaceHeightFacet.getWorld(position.x, position.z);

            if (position.y < surfaceHeight - 1 && chance.nextBoolean()) {
                chunk.setBlock(ChunkMath.calcBlockPos(position), copperOre);
            } else {
                if (position.y < surfaceHeight - 2 && chance.nextBoolean() && chance.nextBoolean()) {
                    chunk.setBlock(ChunkMath.calcBlockPos(position), ironOre);
                } else {
                    if (position.y < surfaceHeight - 3 && chance.nextBoolean() & chance.nextBoolean() && chance.nextBoolean()) {
                        /** <!--chunk.setBlock(ChunkMath.calcBlockPos(position), water);--> */
                    } else {
                        if (position.y < surfaceHeight - 4 && chance.nextBoolean() && chance.nextBoolean() && chance.nextBoolean() && chance.nextBoolean()) {
                            chunk.setBlock(ChunkMath.calcBlockPos(position), diamondOre);
                        } else {
                            if (position.y < surfaceHeight) {
                                chunk.setBlock(ChunkMath.calcBlockPos(position), stone);
                            }
                        }
                    }
                }
            }
        }
    }
}
