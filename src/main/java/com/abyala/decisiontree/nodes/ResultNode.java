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

package com.abyala.decisiontree.nodes;

import com.abyala.decisiontree.exceptions.DecisionTreeParserException;
import com.abyala.decisiontree.results.ResultAttribute;
import com.abyala.decisiontree.results.ResultSpec;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: abyala
 * Date: 6/26/12
 */
public class ResultNode {
    private final ResultSpec spec;
    private final Map<String, String> resultFields = new HashMap<String, String>();

    private ResultNode(final ResultSpec spec, final Map<String, String> resultFields) {
        this.spec = spec;
        this.resultFields.putAll(resultFields);
    }

    public Object createResult() {
        try {
            final Object result = spec.getResultClass().newInstance();

            for (ResultAttribute attribute : spec.getAllAttributes()) {
                final String rawValue = resultFields.get(attribute.getName());
                final Object valueToSet = attribute.transformValueForSetter(rawValue);
                attribute.getSetter().invoke(result, valueToSet);
            }

            return result;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Builder {
        private final ResultSpec spec;
        private final Map<String, String> resultFields = new HashMap<String, String>();

        public Builder(final ResultSpec spec) {
            this.spec = spec;
        }

        public Builder addAttribute(final String key, final String value) throws DecisionTreeParserException {
            spec.validateAttribute(key, value);
            this.resultFields.put(key, value);
            return this;
        }

        public ResultNode build() {
            return new ResultNode(spec, resultFields);
        }
    }
}
