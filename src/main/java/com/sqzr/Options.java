/*
 * Copyright 2015 Kantega AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sqzr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 *
 */
public class Options {


    private static Options instance;

    private final FixInsecureDeserialization fixInsecureDeserialization;
    private final List<FixInsecureDeserializationTransformer> fixInsecureDeserializationTransformers;


    public Options(ClassLoader classLoader) {

        FixInsecureDeserialization fixInsecureDeserialization = null;

        ServiceLoader<FixInsecureDeserialization> fixInsecureDeserializations = ServiceLoader.load(FixInsecureDeserialization.class, classLoader);
        Iterator<FixInsecureDeserialization> iterator = fixInsecureDeserializations.iterator();

        if(iterator.hasNext()) {
            fixInsecureDeserialization = iterator.next();
        }
        if(iterator.hasNext()) {
            throw new IllegalStateException("Classpath has more than one implementation of " + FixInsecureDeserialization.class.getName());
        }

        if(fixInsecureDeserialization == null) {
            fixInsecureDeserialization = new DefaultFixInsecureDeserialization();
        }

        this.fixInsecureDeserialization = fixInsecureDeserialization;

        this.fixInsecureDeserializationTransformers = new ArrayList<FixInsecureDeserializationTransformer>();

        for(FixInsecureDeserializationTransformer trans : ServiceLoader.load(FixInsecureDeserializationTransformer.class, classLoader)) {
            fixInsecureDeserializationTransformers.add(trans);
        }
    }

    public static Options getInstance() {
        return instance;
    }

    public FixInsecureDeserialization getFixInsecureDeserialization() {
        return fixInsecureDeserialization;
    }

    public static Options makeInstance(ClassLoader classLoader) {
        return instance = new Options(classLoader);
    }

    public List<FixInsecureDeserializationTransformer> getFixInsecureDeserializationTransformers() {
        return fixInsecureDeserializationTransformers;
    }
}
