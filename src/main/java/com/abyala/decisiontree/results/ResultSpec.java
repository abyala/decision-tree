/*
 * Copyright (c) 2012 Andrew Byala
 *
 * This file is part of DecisionTree.
 *
 * DecisionTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DecisionTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DecisionTree.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.abyala.decisiontree.results;

import com.abyala.decisiontree.exceptions.DecisionTreeParserException;

import java.util.*;

/**
 * User: abyala
 * Date: 6/25/12
 */
public class ResultSpec {
    private final Class clazz;
    private final Map<String, ResultAttribute> attributeMap;

    private ResultSpec(final Class clazz, final Map<String, ResultAttribute> attributeMap) {
        this.clazz = clazz;
        this.attributeMap = Collections.unmodifiableMap(new HashMap<String, ResultAttribute>(attributeMap));
    }

    public Class getResultClass() {
        return clazz;
    }

    public void validateAttribute(final String key, final String value) throws DecisionTreeParserException {
        final ResultAttribute attribute = attributeMap.get(key);
        if (attribute == null) {
            throw new DecisionTreeParserException("Result spec has no mapping for attribute \"" + key + "\"");
        }

        attribute.validateValue(value);
    }

    public Set<ResultAttribute> getAllAttributes() {
        return new HashSet<ResultAttribute>(attributeMap.values());
    }

    public static class Builder {
        private final Class clazz;
        private final Map<String, ResultAttribute> attributeMap = new HashMap<String, ResultAttribute>();

        public Builder(final Class clazz) {
            this.clazz = clazz;
        }

        public Builder addAttribute(final ResultAttribute attribute) {
            attributeMap.put(attribute.getName(), attribute);
            return this;
        }

        public ResultSpec build() {
            return new ResultSpec(clazz, attributeMap);
        }
    }
}
