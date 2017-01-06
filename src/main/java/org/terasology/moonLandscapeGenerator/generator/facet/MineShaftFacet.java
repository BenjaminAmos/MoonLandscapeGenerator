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

import org.terasology.math.Region3i;
import org.terasology.moonLandscapeGenerator.generator.data.MineShaft;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.facets.base.SparseObjectFacet3D;

public class MineShaftFacet extends SparseObjectFacet3D<MineShaft> {
    public MineShaftFacet(Region3i targetRegion, Border3D border) {
        super(targetRegion, border);
    }
}
