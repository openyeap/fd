/*
 * The MIT License
 *
 * Copyright 2013-2016 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ltd.fdsa.fdsql.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.el.parser.TokenMgrError;
import org.assertj.core.util.Strings;

import ltd.fdsa.fdsql.parser.ast.ComparisonOperator;
import ltd.fdsa.fdsql.parser.ast.Node;
import ltd.fdsa.fdsql.parser.ast.NodesFactory;
import ltd.fdsa.fdsql.parser.ast.RSQLOperators;

/**
 * Parser of the RDML
 *
 * <p>
 * RDML is a RESTful data manipulation language
 * </p>
 * version: 1.0 select=alias:field,alias:field
 * query=field.opt.value;field.opt.value,(field.opt.value;field.opt.value)
 * order=field.desc,field.asc page=0-n size=1-N
 */
public final class RDMLParser {

	public String parseSelect(String input) {
		if (Strings.isNullOrEmpty(input)) {
			return "select *";
		}
		String[] segments = input.split(",");
		List<String> list = new ArrayList<String>(segments.length);
		for (String segment : segments) {
			String[] fields = segment.split(":");
			if (fields.length == 2) {
				list.add(fields[1] + " as " + fields[0]);
			} else {
				list.add(segment);
			}
		}
		return "select \n" + String.join(",\n", list);
	}

	public String parseQuery(String input) {
		if (Strings.isNullOrEmpty(input)) {
			return "";
		}
		String sql = input.replace(",", " or ").replace(";", " and ").replace(".", " ");
		return "where " + sql;
	}

	public String parseOrder(String input) {
		if (Strings.isNullOrEmpty(input)) {
			return "";
		}
		String sql = input.replace(".", " ");
		return "order by " + sql;

	}

	public String parsePage(int page, int size) {
		if (page < 0 || size < 1) {
			return "";
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" limit ");
		sql.append(size);
		sql.append(" offset ");
		sql.append(page * size);
		return sql.toString();
	}
}
