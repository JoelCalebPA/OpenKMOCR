package com.openkm.ocr.template.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openkm.dao.bean.OCRTemplateField;
import com.openkm.ocr.template.OCRParserEmptyValueException;
import com.openkm.ocr.template.OCRTemplateException;
import com.openkm.ocr.template.OCRTemplateParser;

/**
 * TestParser
 * 
 */
@PluginImplementation
public class TestParser implements OCRTemplateParser {
	private static Logger log = LoggerFactory.getLogger(TestParser.class);

	@Override
	public Object parse(OCRTemplateField otf, String text) throws OCRTemplateException, OCRParserEmptyValueException {
		log.info("extracted text\n" + text);
		
		if (text == null || text.equals("")) {
			throw new OCRParserEmptyValueException("Empty value");
		}

		if (otf.getPattern() == null || otf.getPattern().equals("")) {
			return text != null ? text.trim() : null;
		} else {
			Pattern pattern = Pattern.compile("(" + otf.getPattern() + ")", Pattern.UNICODE_CASE);
			Matcher matcher = pattern.matcher(text);

			if (matcher.find() && matcher.groupCount() == 1) {
				return matcher.group();
			} else {
				throw new OCRTemplateException("Bad format, parse exception");
			}
		}
	}

	@Override
	public String getName() {
		return "Test parser";
	}

	@Override
	public boolean isPatternRequired() {
		return false;
	}
}