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

import com.abyala.decisiontree.ResultField;

/**
 * User: abyala
 * Date: 7/3/12
 */
public class DefaultTestResult {
    private String string;
    private Boolean bool;
    private String memo;

    public String getString() {
        return string;
    }

    @ResultField(name = "string")
    public void setString(final String string) {
        this.string = string;
    }

    public Boolean getBool() {
        return bool;
    }

    @ResultField(name = "bool")
    public void setBool(final Boolean bool) {
        this.bool = bool;
    }

    public String getMemo() {
        return memo;
    }

    @ResultField(name = "memo")
    public void setMemo(final String memo) {
        this.memo = memo;
    }
}
