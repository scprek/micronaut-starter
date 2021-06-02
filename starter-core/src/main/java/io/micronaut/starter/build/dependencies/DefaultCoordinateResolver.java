/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.build.dependencies;

import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.Optional;

@Singleton
@Primary
public class DefaultCoordinateResolver implements CoordinateResolver {

    private final CoordinateResolver[] coordinateResolvers;

    public DefaultCoordinateResolver(CoordinateResolver[] coordinateResolvers) {
        this.coordinateResolvers = coordinateResolvers;
    }

    @NonNull
    public Optional<Coordinate> resolve(@NonNull String artifactId) {
        return Arrays.stream(coordinateResolvers)
                .map(resolver -> resolver.resolve(artifactId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}