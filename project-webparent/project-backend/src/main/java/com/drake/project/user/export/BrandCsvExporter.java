package com.drake.project.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.drake.common.entity.Brand;
import com.drake.project.AbstractExporter;



public class BrandCsvExporter extends AbstractExporter {
public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader(response, "text/csv", ".csv", "brands_");
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), 
				CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"Brand ID", "Name", "Categories"};
		String[] fieldMapping = {"id", "name", "categories"};
		csvWriter.writeHeader(csvHeader);
		
		for (Brand brand : listBrands) {
			csvWriter.write(brand, fieldMapping);
		}
		
		csvWriter.close();
	}
}
