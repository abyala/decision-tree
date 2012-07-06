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

import java.lang.reflect.Method;

/**
 * User: abyala
 * Date: 6/26/12
 */
public abstract class AbstractResultAttribute implements ResultAttribute {
    protected final String name;
    protected final Method setter;

    public AbstractResultAttribute(final String name, final Method setter) {
        this.name = name;
        this.setter = setter;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Method getSetter() {
        return setter;
    }
}
