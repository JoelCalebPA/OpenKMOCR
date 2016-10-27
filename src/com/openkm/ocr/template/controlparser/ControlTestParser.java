/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2015 Paco Avila & Josep Llort
 *
 * No bytes were intentionally harmed during the development of this application.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.openkm.ocr.template.controlparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.dao.bean.OCRTemplateControlField;
import com.openkm.ocr.template.OCRParserEmptyValueException;
import com.openkm.ocr.template.OCRTemplateControlParser;
import com.openkm.ocr.template.OCRTemplateException;

/**
 * ControlTestParser
 *
 * @author jllort
 */
@PluginImplementation
public class ControlTestParser implements OCRTemplateControlParser {
	private static Logger log = LoggerFactory.getLogger(ControlTestParser.class);
	
	@Override
	public boolean parse(OCRTemplateControlField otf, String text) throws OCRTemplateException, OCRParserEmptyValueException {
		log.info("extracted text\n" + text);
		
		if (text == null || text.equals("")) {
			return false;
		} else {
			if (otf.getPattern() == null || otf.getPattern().equals("")) {
				return text.toLowerCase().contains(otf.getValue()); // normalize to lowercase
			} else {
				Pattern pattern = Pattern.compile("(" + otf.getPattern() + ")", Pattern.UNICODE_CASE);
				Matcher matcher = pattern.matcher(text);
				if (matcher.find() && matcher.groupCount() == 1) {
					if (otf.getValue().equals("")) {
						return true;
					} else {
						return otf.getValue().equals(matcher.group());
					}
				} else {
					return false;
				}
			}
		}
	}

	@Override
	public String getName() {
		return "Control test";
	}

	@Override
	public boolean isPatternRequired() {
		return false;
	}

	@Override
	public String info() {
		StringBuffer sb = new StringBuffer();
		sb.append("Empty pattern case: true if extracted text contains field value.\n");
		sb.append("Pattern with value field empty: true if pattern found.\n");
		sb.append("Pattern with value field: true if pattern found equals value.\n");
		return null;
	}
}