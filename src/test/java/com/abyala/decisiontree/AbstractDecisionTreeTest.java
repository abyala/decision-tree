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

package com.abyala.decisiontree;

import com.abyala.decisiontree.exceptions.DecisionTreeParserException;
import org.junit.Before;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

/**
 * User: abyala
 * Date: 6/20/12
 */
public abstract class AbstractDecisionTreeTest {
    protected DecisionTree tree;

    @Before
    public void init() throws DecisionTreeParserException {
        this.tree = parseDecisionTreeAtFileLocation(getFileLocation());
    }

    protected DecisionTree parseDecisionTreeAtFileLocation(final String fileLocation) throws DecisionTreeParserException {
        if (fileLocation == null) return null;

        try {
            final InputStream xmlInputStream = new BufferedInputStream(new FileInputStream(fileLocation));
            try {
                final DecisionTreeParser parser = new SimpleDecisionTreeParser();
                return parser.parse(xmlInputStream);
            } finally {
                xmlInputStream.close();
            }
        } catch (IOException e) {
            fail("Cannot load XML file");
            return null;
        }
    }

    protected abstract String getFileLocation();
}
