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
import com.abyala.decisiontree.nodes.Node;
import com.abyala.decisiontree.nodes.NodeBuilder;
import com.abyala.decisiontree.nodes.ResultNode;
import com.abyala.decisiontree.results.*;
import com.abyala.decisiontree.types.BooleanInputType;
import com.abyala.decisiontree.types.InputType;
import com.abyala.decisiontree.types.IntegerInputType;
import com.abyala.decisiontree.types.StringInputType;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.*;

/**
 * User: abyala
 * Date: 6/14/12
 */
public class SimpleDecisionTreeParser implements DecisionTreeParser {

    protected DecisionTree parseDocument(final Document doc) throws DecisionTreeParserException {
        final Element rootElement = doc.getRootElement();
        final Map<String, InputType> types = parseInputTypes(rootElement.getChild("input-types"));
        final ResultSpec resultSpec = parseResultClass(rootElement.getChild("result-type"));
        final Node rootNode = parseTable(rootElement.getChild("tree"), types, resultSpec);
        return new DecisionTree(rootNode, types.values());
    }

    private ResultSpec parseResultClass(final Element resultElement) throws DecisionTreeParserException {
        if (resultElement == null) throw new DecisionTreeParserException("Missing result-type element");

        final String className = resultElement.getAttributeValue("class");
        final Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new DecisionTreeParserException("Result class not found: " + className);
        }

        final ResultSpec.Builder builder = new ResultSpec.Builder(clazz);
        parseResultAttributes(builder, resultElement, clazz);
        return builder.build();
    }

    private Map<String, Method> parseAnnotatedMethods(final Class clazz) {
        final Map<String, Method> methodMap = new HashMap<String, Method>();

        for (Method method : clazz.getMethods()) {
            final ResultField annotation = method.getAnnotation(ResultField.class);
            if (annotation != null) {
                methodMap.put(annotation.name(), method);
            }
        }

        return methodMap;
    }

    private void parseResultAttributes(final ResultSpec.Builder builder, final Element element, final Class resultClass) throws DecisionTreeParserException {
        final Map<String, Method> annotatedResultMethods = parseAnnotatedMethods(resultClass);

        for (Element child : element.getChildren()) {
            final String attributeName = child.getAttributeValue("name");
            final Method method = annotatedResultMethods.get(attributeName);
            if (method == null)
                throw new DecisionTreeParserException("Result string attribute " + attributeName + " not defined on result class");

            final ResultAttribute attribute = parseResultAttribute(child, method);
            builder.addAttribute(attribute);
        }
    }

    private ResultAttribute parseResultAttribute(final Element element, final Method method) throws DecisionTreeParserException {
        final String elementType = element.getName();
        final String attributeName = element.getAttributeValue("name");
        if ("string-attribute".equals(elementType)) {
            return parseResultStringAttribute(element, attributeName, method);
        } else if ("boolean-attribute".equals(elementType)) {
            return parseResultBooleanAttribute(element, attributeName, method);
        } else if ("text-attribute".equals(elementType)) {
            return parseResultTextAttribute(element, attributeName, method);
        } else {
            throw new DecisionTreeParserException("Unknown result attribute type: " + elementType);
        }
    }

    private ResultAttribute parseResultTextAttribute(final Element element, final String name, final Method method) {
        final TextResultAttribute.Builder builder = new TextResultAttribute.Builder(name, method);
        builder.setDefaultValue(element.getAttributeValue("default"));
        return builder.build();
    }

    private ResultAttribute parseResultBooleanAttribute(final Element element, final String name, final Method method) throws DecisionTreeParserException {
        final BooleanResultAttribute.Builder builder = new BooleanResultAttribute.Builder(name, method);
        final Attribute defaultAttribute = element.getAttribute("default");
        if (defaultAttribute != null) {
            final String value = defaultAttribute.getValue();
            if ("true".equals(value)) {
                builder.setDefaultValue(true);
            } else if ("false".equals(value)) {
                builder.setDefaultValue(false);
            } else {
                throw new DecisionTreeParserException("Invalid default value \"" + value + "\" for boolean result attribute \"" + name + "\"");
            }
        }

        return builder.build();
    }

    private StringResultAttribute parseResultStringAttribute(final Element element, final String name, final Method method) throws DecisionTreeParserException {
        final StringResultAttribute.Builder builder = new StringResultAttribute.Builder(name, method);
        for (Element value : element.getChildren("value")) {
            final String text = value.getTextNormalize();
            builder.addEnumValue(text);
            if ("true".equals(value.getAttributeValue("default"))) {
                builder.setDefaultValue(text);
            }
        }

        return builder.build();
    }

    private Node parseTable(final Element tableElement, final Map<String, InputType> types, final ResultSpec resultSpec) throws DecisionTreeParserException {
        if (tableElement == null) {
            throw new DecisionTreeParserException("Invalid XML: No element named \"tree\" found");
        }

        final Node rootNode = parseInputs("/", tableElement.getChildren(), types, resultSpec);
        rootNode.validate();
        return rootNode;
    }

    private Node parseInputs(final String parentPath, final List<Element> inputs, final Map<String, InputType> types, final ResultSpec resultSpec) throws DecisionTreeParserException {
        final String inputName = getInputName(parentPath, inputs);
        final String nodePath = parentPath + inputName;
        final InputType inputType = getInputType(inputName, types);
        final NodeBuilder builder = inputType.createNodeBuilder();

        for (Element input : inputs) {
            final String value = input.getAttributeValue("value");
            final String refId = input.getAttributeValue("refid");
            final String childPath = nodePath + "=" + value;
            final List<Element> children = input.getChildren();

            if (refId != null) {
                if (!children.isEmpty()) {
                    throw new DecisionTreeParserException("Node at path " + childPath + " may not have both a refid and child elements.");
                } else {
                    builder.addReferenceMapping(value, refId);
                }
            } else if (children.isEmpty()) {
                throw new DecisionTreeParserException("Node at path " + childPath + " must have a result, child inputs, or a refid");
            } else if (children.size() == 1 && "result".equals(children.get(0).getName())) {
                builder.addResultMapping(value, parseResult(children.get(0), types, resultSpec));
            } else {
                builder.addNodeMapping(value, parseInputs(childPath + "/", children, types, resultSpec));
            }
        }

        return builder.build();
    }

    /**
     * Creates a result specification for this node. Does not create the actual result object,
     * since there's no guarantee that that object will be immutable.
     */
    private ResultNode parseResult(final Element element, final Map<String, InputType> types, final ResultSpec resultSpec) throws DecisionTreeParserException {
        final ResultNode.Builder builder = new ResultNode.Builder(resultSpec);
        for (final Attribute attribute : element.getAttributes()) {
            final String name = attribute.getName();
            final String value = attribute.getValue();
            builder.addAttribute(name, value);
        }

        return builder.build();
    }

    private InputType getInputType(final String name, final Map<String, InputType> types) throws DecisionTreeParserException {
        final InputType inputType = types.get(name);
        if (inputType == null) {
            throw new DecisionTreeParserException("Undefined input name: \"" + name + "\"");
        }

        return inputType;
    }

    private String getInputName(final String path, final List<Element> children) throws DecisionTreeParserException {
        if (children.isEmpty()) throw new DecisionTreeParserException("Node at path " + path + " has no children");

        final Set<String> inputTypes = new HashSet<String>();
        for (Element element : children) {
            inputTypes.add(element.getName());
        }

        if (inputTypes.size() != 1) {
            throw new DecisionTreeParserException("Node at path " + path + " must have only one child type.");
        } else {
            return inputTypes.iterator().next();
        }
    }

    @Override
    public DecisionTree parse(final Reader inputReader) throws DecisionTreeParserException {
        try {
            final Document doc = new SAXBuilder().build(inputReader);
            return parseDocument(doc);
        } catch (JDOMException e) {
            throw new DecisionTreeParserException(e);
        } catch (IOException e) {
            throw new DecisionTreeParserException(e);
        }
    }

    @Override
    public DecisionTree parse(final InputStream inputStream) throws DecisionTreeParserException {
        try {
            final Document doc = new SAXBuilder().build(inputStream);
            return parseDocument(doc);
        } catch (JDOMException e) {
            throw new DecisionTreeParserException(e);
        } catch (IOException e) {
            throw new DecisionTreeParserException(e);
        }
    }

    int parseMinInt(final String min, final String fieldName) throws DecisionTreeParserException {
        if ("unbounded".equals(min)) return Integer.MIN_VALUE;
        try {
            return Integer.parseInt(min);
        } catch (NumberFormatException e) {
            throw new DecisionTreeParserException("Invalid min value for field " + fieldName + ". Value = \"" + min + "\"");
        }
    }

    int parseMaxInt(final String max, final String fieldName) throws DecisionTreeParserException {
        if ("unbounded".equals(max)) return Integer.MAX_VALUE;
        try {
            return Integer.parseInt(max);
        } catch (NumberFormatException e) {
            throw new DecisionTreeParserException("Invalid max value for field " + fieldName + ". Value = \"" + max + "\"");
        }
    }

    protected Map<String, InputType> parseInputTypes(final Element child) throws DecisionTreeParserException {
        final Map<String, InputType> types = new HashMap<String, InputType>(child.getChildren().size());

        for (Element typeElement : child.getChildren()) {
            final InputType type = parseInputType(typeElement);

            if ("result".equals(type.getName())) {
                throw new DecisionTreeParserException("Invalid configuration: No input-type may be named \"result\" since it is() a reserved keyword");
            }

            types.put(type.getName(), type);
        }

        return types;
    }

    private InputType parseInputType(final Element typeElement) throws DecisionTreeParserException {
        final String typeName = typeElement.getName();
        if ("string-type".equals(typeName)) {
            return parseStringInputType(typeElement);
        } else if ("integer-type".equals(typeName)) {
            return parseIntegerInputType(typeElement);
        } else if ("boolean-type".equals(typeName)) {
            return parseBooleanInputType(typeElement);
        } else {
            throw new DecisionTreeParserException("Unknown input-type: " + typeName);
        }
    }

    InputType parseBooleanInputType(final Element typeElement) {
        final String name = typeElement.getAttributeValue("name");
        final BooleanInputType.Builder builder = new BooleanInputType.Builder(name);
        return builder.build();
    }

    private InputType parseIntegerInputType(final Element typeElement) throws DecisionTreeParserException {
        final String name = typeElement.getAttributeValue("name");
        final IntegerInputType.Builder builder = new IntegerInputType.Builder(name);
        builder.setMinValue(parseMinInt(typeElement.getAttributeValue("min"), "integer-type " + name));
        builder.setMaxValue(parseMaxInt(typeElement.getAttributeValue("max"), "integer-type " + name));
        return builder.build();
    }

    InputType parseStringInputType(final Element typeElement) throws DecisionTreeParserException {
        final String name = typeElement.getAttributeValue("name");
        final StringInputType.Builder builder = new StringInputType.Builder(name);
        boolean hasDefaultValue = false;
        for (Element child : typeElement.getChildren("value")) {
            final boolean isDefault = "true".equals(child.getAttributeValue("default"));
            if (isDefault && hasDefaultValue) {
                throw new DecisionTreeParserException("Input-type \"" + name + "\" may not have more than one default type.");
            }
            builder.addEnumValue(child.getTextNormalize(), isDefault);
            hasDefaultValue = isDefault;
        }
        return builder.build();
    }
}
