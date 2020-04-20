/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.options;

import io.micronaut.starter.feature.Feature;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public enum Language {
    JAVA,
    GROOVY,
    KOTLIN;

    public static Language infer(List<Feature> features) {
        return features.stream()
                .map(Feature::getRequiredLanguage)
                .filter(Optional::isPresent)
                .findFirst()
                .map(Optional::get)
                .orElse(null);
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    @Nonnull
    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

}
