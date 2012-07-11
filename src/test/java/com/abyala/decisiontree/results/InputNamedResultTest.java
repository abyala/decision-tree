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

import com.abyala.decisiontree.AbstractDecisionTreeTest;
import com.abyala.decisiontree.exceptions.DecisionTreeParserException;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: abyala
 * Date: 6/20/12
 */
public class InputNamedResultTest extends AbstractDecisionTreeTest {

    @Override
    protected String getFileLocation() {
        return null;
    }

    @Test
    public void testBadInputName() throws DecisionTreeParserException {
        try {
            parseDecisionTreeAtFileLocation("src" + File.separator + "test" + File.separator + "resources" + File.separator + "testInputNamedResult.xml");
            fail("Should have rejected an input named \"result\"");
        } catch (DecisionTreeParserException e) {
            assertThat(e.getMessage(), equalTo("Invalid configuration: No input-type may be named \"result\" since it is() a reserved keyword"));
        }
    }
}
